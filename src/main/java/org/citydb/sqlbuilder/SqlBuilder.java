/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2024
 * virtualcitysystems GmbH, Germany
 * https://vc.systems/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citydb.sqlbuilder;

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.function.Cast;
import org.citydb.sqlbuilder.function.Function;
import org.citydb.sqlbuilder.function.WindowFunction;
import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.literal.*;
import org.citydb.sqlbuilder.operation.*;
import org.citydb.sqlbuilder.query.*;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.schema.WildcardColumn;
import org.citydb.sqlbuilder.update.Update;
import org.citydb.sqlbuilder.update.UpdateValue;
import org.citydb.sqlbuilder.util.AliasGenerator;
import org.citydb.sqlbuilder.util.DefaultAliasGenerator;
import org.citydb.sqlbuilder.util.PlaceholderBuilder;
import org.citydb.sqlbuilder.util.PlainText;

import java.util.*;
import java.util.stream.Collectors;

public class SqlBuilder {

    private SqlBuilder() {
    }

    public static SqlBuilder newInstance() {
        return new SqlBuilder();
    }

    public String build(SqlObject object) {
        return build(object, SqlBuildOptions.defaults());
    }

    public String build(SqlObject object, SqlBuildOptions options) {
        Processor processor = new Processor(options);
        if (object instanceof Select select) {
            processor.build(select);
        } else if (object instanceof SetOperator operator) {
            processor.build(operator);
        } else {
            object.accept(processor);
        }

        return processor.builder.toString();
    }

    private static class Processor implements SqlVisitor {
        private final StringBuilder builder = new StringBuilder();
        private final SqlBuildOptions options;
        private final AliasGenerator aliasGenerator;
        private final PlaceholderBuilder placeholderBuilder;
        private final Map<Table, String> tableAliases;
        private int level;

        Processor(SqlBuildOptions options) {
            this.options = options != null ? options : SqlBuildOptions.defaults();
            this.aliasGenerator = this.options.getAliasGenerator().orElse(DefaultAliasGenerator.newInstance());
            this.placeholderBuilder = this.options.getPlaceholderBuilder().orElse(null);
            tableAliases = new IdentityHashMap<>();
        }

        Processor(Processor other) {
            options = other.options;
            aliasGenerator = other.aliasGenerator;
            placeholderBuilder = other.placeholderBuilder;
            tableAliases = other.tableAliases;
            level = other.level;
        }

        @Override
        public void visit(ArithmeticOperation operation) {
            builder.append("(");
            operation.getLeftOperand().accept(this);
            builder.append(" ")
                    .append(keyword(operation.getOperator()))
                    .append(" ");
            operation.getRightOperand().accept(this);
            builder.append(")");
        }

        @Override
        public void visit(Between between) {
            between.getOperand().accept(this);
            builder.append(" ")
                    .append(keyword(between.getOperator()))
                    .append(" ");
            between.getLowerBound().accept(this);
            builder.append(keyword(" and "));
            between.getUpperBound().accept(this);
        }

        @Override
        public void visit(BinaryComparisonOperation operation) {
            operation.getLeftOperand().accept(this);
            builder.append(" ")
                    .append(keyword(operation.getOperator()))
                    .append(" ");
            operation.getRightOperand().accept(this);
        }

        @Override
        public void visit(BinaryLogicalOperation operation) {
            if (operation.getOperands().size() > 1) {
                builder.append("(");
                newlineAndIndent(() -> build(operation.getOperands(), " ", keyword(operation.getOperator()) + " "));
                newlineAndAppend(")");
            } else {
                operation.getOperands().get(0).accept(this);
            }
        }

        @Override
        public void visit(BooleanLiteral literal) {
            literal.getValue().ifPresentOrElse(value ->
                            builder.append(keyword(value ? "true" : "false")),
                    () -> build(literal));
        }

        @Override
        public void visit(Case expression) {
            builder.append(keyword("case "));
            expression.getConditions().forEach((when, then) ->
                    newlineAndIndent(() -> {
                        builder.append(keyword("when "));
                        when.accept(this);
                        builder.append(keyword(" then "));
                        then.accept(this);
                        builder.append(" ");
                    }));

            newlineAndIndent(() -> expression.getElse().ifPresent(otherwise -> {
                builder.append(keyword("else "));
                otherwise.accept(this);
                builder.append(" ");
            }));

            newlineAndAppend(keyword("end"));
        }

        @Override
        public void visit(Cast cast) {
            builder.append(keyword("cast"))
                    .append(" (");
            cast.getExpression().accept(this);
            builder.append(keyword(" as "))
                    .append(cast.getTargetType())
                    .append(")");
        }

        @Override
        public void visit(Collate collate) {
            collate.getExpression().accept(this);
            builder.append(" ")
                    .append(keyword(collate.getOperator()))
                    .append(" ")
                    .append(collate.getCollation());
        }

        @Override
        public void visit(Column column) {
            builder.append(getOrCreateAlias(column.getTable()))
                    .append(".")
                    .append(identifier(column.getName()));
        }

        @Override
        public void visit(CommonTableExpression expression) {
            builder.append(expression.getName());
            if (!expression.getColumns().isEmpty()) {
                builder.append(" (")
                        .append(expression.getColumns().stream()
                                .map(this::identifier)
                                .collect(Collectors.joining(", ")))
                        .append(") ");
            }

            builder.append(keyword(" as "));
            expression.getQueryExpression().accept(this);
        }

        @Override
        public void visit(DateLiteral literal) {
            literal.getValue().ifPresentOrElse(value ->
                            builder.append(options.isUseJdbcEscapeNotation() ?
                                    "{d '" + value + "'}" :
                                    "'" + value + "'"),
                    () -> build(literal));
        }

        @Override
        public void visit(DoubleLiteral literal) {
            build(literal);
        }

        @Override
        public void visit(Exists exists) {
            builder.append(keyword(exists.getOperator()))
                    .append(" ");
            exists.getOperand().accept(this);
        }

        @Override
        public void visit(Frame frame) {
            builder.append(keyword(frame.getUnits()))
                    .append(" ");
            frame.getEnd().ifPresent(end -> builder.append(keyword("between ")));
            frame.getStartExpression().ifPresent(expression -> {
                expression.accept(this);
                builder.append(" ");
            });

            builder.append(keyword(frame.getStart()));
            frame.getEnd().ifPresent(end -> {
                builder.append(keyword(" and "));
                frame.getEndExpression().ifPresent(expression -> {
                    expression.accept(this);
                    builder.append(expression);
                    builder.append(" ");
                });
                builder.append(keyword(end));
            });
        }

        @Override
        public void visit(Function function) {
            function.getSchema().ifPresent(schema ->
                    builder.append(schema)
                            .append("."));
            builder.append(keyword(function.getName()))
                    .append("(");

            if (!function.getQualifiers().isEmpty()) {
                builder.append(function.getQualifiers().stream()
                                .map(this::keyword)
                                .collect(Collectors.joining(" ")))
                        .append(" ");
            }

            build(function.getArguments(), ", ", false);
            builder.append(")");
        }

        @Override
        public void visit(In in) {
            in.getLeftOperand().accept(this);
            builder.append(" ")
                    .append(keyword(in.getOperator()))
                    .append(" (");
            in.getRightOperand().accept(this);
            builder.append(")");
        }

        @Override
        public void visit(IntegerLiteral literal) {
            build(literal);
        }

        @Override
        public void visit(IsNull isNull) {
            isNull.getOperand().accept(this);
            builder.append(" ")
                    .append(keyword(isNull.getOperator()));
        }

        @Override
        public void visit(Join join) {
            builder.append(keyword(join.getType()))
                    .append(" ");
            join.getTable().accept(this);
            if (!join.getConditions().isEmpty()) {
                builder.append(keyword(" on "));
                Operators.and(join.getConditions()).reduce().accept(this);
            }
        }

        @Override
        public void visit(Like like) {
            like.getOperand().accept(this);
            builder.append(" ")
                    .append(keyword(like.getOperator()))
                    .append(" ");
            like.getPattern().accept(this);
            like.getEscapeCharacter().ifPresent(escapeCharacter -> {
                if (options.isUseJdbcEscapeNotation()) {
                    builder.append(" {escape ")
                            .append(escapeCharacter)
                            .append("}");
                } else {
                    builder.append(keyword(" escape "))
                            .append(escapeCharacter);
                }
            });
        }

        @Override
        public void visit(LiteralList literalList) {
            build(literalList.getLiterals(), ", ", false);
        }

        @Override
        public void visit(Not not) {
            builder.append(keyword(not.getOperator()))
                    .append(" ");
            not.getOperand().accept(this);
        }

        @Override
        public void visit(NullLiteral literal) {
            build(literal);
        }

        @Override
        public void visit(OrderBy orderBy) {
            orderBy.getSortExpression().accept(this);
            if (!OrderBy.ASCENDING.equalsIgnoreCase(orderBy.getSortOrder())) {
                builder.append(" ")
                        .append(keyword(orderBy.getSortOrder()));
            }

            orderBy.getNullOrder().ifPresent(nullOrder ->
                    builder.append(" ")
                            .append(keyword(nullOrder)));
        }

        @Override
        public void visit(Placeholder placeholder) {
            builder.append(placeholderBuilder != null ?
                    placeholderBuilder.build(placeholder, options) :
                    "?");
        }

        @Override
        public void visit(PlainText plainText) {
            String sql = plainText.getSql();
            for (Object token : plainText.getTokens()) {
                String replacement = token instanceof SqlObject sqlObject ?
                        toSql(sqlObject) :
                        String.valueOf(token);

                sql = sql.replaceFirst("\\{}", replacement);
            }

            builder.append(sql);
        }

        @Override
        public void visit(Select select) {
            builder.append("(");
            newlineAndIndent(() -> build(select));
            newlineAndAppend(")");
        }

        @Override
        public void visit(SetOperator operator) {
            builder.append("(");
            newlineAndIndent(() -> build(operator));
            newlineAndAppend(")");
        }

        @Override
        public void visit(SubQueryOperator operator) {
            builder.append(keyword(operator.getOperator()))
                    .append(" (");
            operator.getOperand().accept(this);
            builder.append(")");
        }

        @Override
        public void visit(StringLiteral literal) {
            literal.getValue().ifPresentOrElse(value ->
                            builder.append("'")
                                    .append(value.replace("'", "''"))
                                    .append("'"),
                    () -> build(literal));
        }

        @Override
        public void visit(Table table) {
            if (table.isLateral()) {
                builder.append(keyword("lateral "));
            }

            table.getQueryExpression().ifPresentOrElse(expression -> expression.accept(this),
                    () -> {
                        table.getSchema().ifPresent(schema ->
                                builder.append(schema)
                                        .append("."));
                        builder.append(identifier(table.getName()));
                    });
            builder.append(" ")
                    .append(getOrCreateAlias(table));
        }

        @Override
        public void visit(TimestampLiteral literal) {
            literal.getValue().ifPresentOrElse(value ->
                            builder.append(options.isUseJdbcEscapeNotation() ?
                                    "{ts '" + value + "'}" :
                                    "'" + value + "'"),
                    () -> build(literal));
        }

        @Override
        public void visit(Update update) {
            if (!update.getWith().isEmpty()) {
                builder.append(keyword("with "));
                if (update.isWithRecursive()) {
                    builder.append(keyword("recursive "));
                }

                build(update.getWith(), ", ");
                newline();
            }

            builder.append(keyword("update "));
            newlineAndIndent(() -> builder.append(update.getTable().orElse(Table.of("null")))
                    .append(" "));

            if (!update.getSet().isEmpty()) {
                newlineAndAppend(keyword("set "));
                newlineAndIndent(() -> build(update.getSet(), ", "));
            }

            if (!update.getWhere().isEmpty()) {
                BinaryLogicalOperation where = Operators.and(update.getWhere()).reduce();
                newlineAndAppend(keyword("where "));
                newlineAndIndent(() -> build(where.getOperands(), " ", keyword(where.getOperator()) + " "));
            }
        }

        @Override
        public void visit(UpdateValue value) {
            builder.append(identifier(value.getColumn().getName()))
                    .append(" = ");
            value.getValue().accept(this);
        }

        @Override
        public void visit(WildcardColumn column) {
            builder.append(column.getTable()
                    .map(table -> getOrCreateAlias(table) + ".*")
                    .orElse("*"));
        }

        @Override
        public void visit(Window window) {
            builder.append("(");
            if (window.isReferenceOnly()) {
                window.getReference().ifPresent(builder::append);
            } else if (!window.isEmpty()) {
                window.getReference().ifPresent(reference -> builder.append(reference)
                        .append(" "));

                if (!window.getPartitionBy().isEmpty()) {
                    newlineAndIndent(() -> {
                        builder.append(keyword("partition by "));
                        build(window.getPartitionBy(), ", ");
                    });
                }

                if (!window.getOrderBy().isEmpty()) {
                    newlineAndIndent(() -> {
                        builder.append(keyword("order by "));
                        build(window.getOrderBy(), ", ");
                    });
                }

                window.getFrame().ifPresent(frame ->
                        newlineAndIndent(() -> {
                            frame.accept(this);
                            builder.append(" ");
                        }));

                newline();
            }

            builder.append(")");
        }

        @Override
        public void visit(WindowFunction function) {
            function.getFunction().accept(this);
            builder.append(keyword(" over "));

            if (function.getWindow().isReferenceOnly()) {
                function.getWindow().getReference().ifPresent(builder::append);
            } else {
                function.getWindow().accept(this);
            }
        }

        private void build(Literal<?> literal) {
            builder.append(literal.getValue()
                    .map(Object::toString)
                    .orElse("null"));
        }

        private void build(Select select) {
            if (!select.getWith().isEmpty()) {
                builder.append(keyword("with "));
                if (select.isWithRecursive()) {
                    builder.append(keyword("recursive "));
                }

                build(select.getWith(), ", ");
                newline();
            }

            builder.append(keyword("select "));
            if (!select.getHints().isEmpty()) {
                builder.append("/*+ ")
                        .append(String.join(" ", select.getHints()))
                        .append(" */ ");
            }

            newlineAndIndent(() -> {
                if (select.isDistinct()) {
                    builder.append(keyword("distinct "));
                }

                if (!select.getSelect().isEmpty()) {
                    build(select.getSelect().stream()
                            .map(selection -> (ObjectBuilder) () -> build(selection))
                            .toList(), ", ");
                } else {
                    builder.append(Column.WILDCARD)
                            .append(" ");
                }
            });

            if (!select.getFrom().isEmpty()) {
                newlineAndAppend(keyword("from "));
                newlineAndIndent(() -> {
                    build(select.getFrom(), ", ");
                    if (!select.getJoins().isEmpty()) {
                        newline();
                        build(select.getJoins(), " ");
                    }
                });
            }

            if (!select.getWhere().isEmpty()) {
                BinaryLogicalOperation where = Operators.and(select.getWhere()).reduce();
                newlineAndAppend(keyword("where "));
                newlineAndIndent(() -> build(where.getOperands(), " ", keyword(where.getOperator()) + " "));
            }

            build((QueryStatement<?>) select);
        }

        private void build(QueryStatement<?> statement) {
            if (!statement.getGroupBy().isEmpty()) {
                newlineAndAppend(keyword("group by "));
                newlineAndIndent(() -> build(statement.getGroupBy(), ", "));
            }

            if (!statement.getHaving().isEmpty()) {
                newlineAndAppend(keyword("having "));
                newlineAndIndent(() -> build(statement.getHaving(), ", "));
            }

            if (!statement.getWindow().isEmpty()) {
                newlineAndAppend(keyword("window "));
                newlineAndIndent(() ->
                        build(statement.getWindow(), ", ", (window, i) ->
                                window.getOrCreateName(aliasGenerator) + keyword(" as ")));
            }

            if (!statement.getOrderBy().isEmpty()) {
                newlineAndAppend(keyword("order by "));
                newlineAndIndent(() -> build(statement.getOrderBy(), ", "));
            }

            statement.getOffset().ifPresent(offset -> {
                newlineAndAppend(keyword("offset "));
                offset.accept(this);
                builder.append(keyword(" rows "));
            });

            statement.getFetch().ifPresent(fetch -> {
                if (statement.getOffset().isEmpty()) {
                    newline();
                }

                builder.append(keyword("fetch "))
                        .append(keyword(statement.getOffset().map(offset -> "next ").orElse("first ")));
                fetch.accept(this);
                builder.append(keyword(" rows only "));
            });
        }

        private void build(Selection<?> selection) {
            selection.accept(this);
            selection.getAlias().ifPresent(alias ->
                    builder.append(keyword(" as "))
                            .append(alias));
        }

        private void build(SetOperator operator) {
            for (Iterator<Select> iterator = operator.getOperands().iterator(); iterator.hasNext(); ) {
                iterator.next().accept(this);
                if (iterator.hasNext()) {
                    builder.append(" ");
                    newlineAndAppend(keyword(operator.getType()))
                            .append(" ");
                    newline();
                }
            }

            build((QueryStatement<?>) operator);
        }

        private void build(Collection<?> builders, String delimiter) {
            build(builders, delimiter, true);
        }

        private void build(Collection<?> builders, String delimiter, boolean newline) {
            build(builders, delimiter, (object, i) -> null, newline);
        }

        private void build(Collection<?> builders, String delimiter, String prefix) {
            build(builders, delimiter, (object, i) -> i > 0 ? prefix : null);
        }

        private <T> void build(Collection<T> builders, String delimiter, PrefixBuilder<T, Integer, String> prefixBuilder) {
            build(builders, delimiter, prefixBuilder, true);
        }

        private <T> void build(Collection<T> builders, String delimiter, PrefixBuilder<T, Integer, String> prefixBuilder, boolean newline) {
            Iterator<T> iterator = builders.iterator();
            int counter = 0;

            while (iterator.hasNext()) {
                T object = iterator.next();
                String prefix = prefixBuilder.build(object, counter++);
                if (prefix != null) {
                    builder.append(prefix);
                }

                if (object instanceof SqlObject sqlObject) {
                    sqlObject.accept(this);
                } else if (object instanceof ObjectBuilder objectBuilder) {
                    objectBuilder.build();
                }

                if (iterator.hasNext()) {
                    builder.append(delimiter);
                    if (newline) {
                        newline();
                    }
                }
            }

            if (newline) {
                builder.append(" ");
            }
        }

        private String keyword(String keyword) {
            if (keyword != null) {
                return options.getKeywordCase() == SqlBuildOptions.KeywordCase.UPPERCASE ?
                        keyword.toUpperCase(Locale.ROOT) :
                        keyword.toLowerCase(Locale.ROOT);
            } else {
                return "";
            }
        }

        private String identifier(String identifier) {
            if (identifier != null) {
                identifier = switch (options.getIdentifierCase()) {
                    case UPPERCASE -> identifier.toUpperCase(Locale.ROOT);
                    case LOWERCASE -> identifier.toLowerCase(Locale.ROOT);
                    case UNCHANGED -> identifier;
                };

                return options.isSetIdentifierDelimiter() ?
                        options.isSetIdentifierDelimiter() + identifier + options.isSetIdentifierDelimiter() :
                        identifier;
            } else {
                return "";
            }
        }

        private void newline() {
            if (options.isSetIndent()) {
                builder.append(options.getNewline());
                builder.append(options.getIndent().repeat(level));
            }
        }

        private StringBuilder newlineAndAppend(String text) {
            newline();
            return builder.append(text);
        }

        private void newlineAndIndent(Runnable action) {
            level++;
            newline();
            action.run();
            level--;
        }

        private String getOrCreateAlias(Table table) {
            return table.getAlias().orElse(tableAliases.computeIfAbsent(table, k -> aliasGenerator.next()));
        }

        private String toSql(SqlObject sqlObject) {
            Processor processor = new Processor(this);
            sqlObject.accept(processor);
            return processor.builder.toString();
        }
    }

    @FunctionalInterface
    private interface ObjectBuilder {
        void build();
    }

    @FunctionalInterface
    private interface PrefixBuilder<T, Integer, String> {
        String build(T object, Integer index);
    }
}
