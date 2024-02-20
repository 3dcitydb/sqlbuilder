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

package org.citydb.sqlbuilder.query;

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.Buildable;
import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.join.Joins;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.operation.BinaryLogicalOperation;
import org.citydb.sqlbuilder.operation.ComparisonOperation;
import org.citydb.sqlbuilder.operation.LogicalExpression;
import org.citydb.sqlbuilder.operation.Operators;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Select extends QueryStatement<Select> implements Selection<Select> {
    private final List<String> hints;
    private final List<CommonTableExpression> with;
    private final List<Selection<?>> select;
    private final List<Join> joins;
    private final List<LogicalExpression> where;
    private boolean withRecursive;
    private boolean distinct;
    private Table from;
    private String alias;

    private Select() {
        hints = new ArrayList<>();
        with = new ArrayList<>();
        select = new ArrayList<>();
        joins = new ArrayList<>();
        where = new ArrayList<>();
    }

    private Select(Select other) {
        super(other);
        hints = new ArrayList<>(other.hints);
        with = new ArrayList<>(other.with);
        select = new ArrayList<>(other.select);
        joins = new ArrayList<>(other.joins);
        where = new ArrayList<>(other.where);
        withRecursive = other.withRecursive;
        distinct = other.distinct;
        from = other.from;
        alias = null;
    }

    public static Select newInstance() {
        return new Select();
    }

    public static Select of(Select other) {
        return new Select(other);
    }

    public boolean isDistinct() {
        return distinct;
    }

    public Select distinct() {
        return distinct(true);
    }

    public Select distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public List<String> getHints() {
        return hints;
    }

    public Select hint(String... hints) {
        if (hints != null) {
            this.hints.addAll(Arrays.asList(hints));
        }

        return this;
    }

    public List<CommonTableExpression> getWith() {
        return with;
    }

    public Select with(String name, QueryStatement<?> statement) {
        return with(CommonTableExpression.of(name, statement));
    }

    public Select with(CommonTableExpression... ctes) {
        if (ctes != null) {
            with.addAll(Arrays.asList(ctes));
        }

        return this;
    }

    public Select withRecursive(String name, QueryStatement<?> statement) {
        return withRecursive(CommonTableExpression.of(name, statement));
    }

    public Select withRecursive(CommonTableExpression... ctes) {
        if (ctes != null) {
            with.addAll(Arrays.asList(ctes));
            withRecursive = true;
        }

        return this;
    }

    public List<Selection<?>> getSelect() {
        return select;
    }

    public Select select(Selection<?>... selections) {
        if (selections != null) {
            select.addAll(Arrays.asList(selections));
        }

        return this;
    }

    public Table getFrom() {
        return from;
    }

    public Select from(Table from) {
        this.from = from;
        return this;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public Select join(Join... joins) {
        if (joins != null) {
            this.joins.addAll(Arrays.asList(joins));
        }

        return this;
    }

    public JoinBuilder join(Table table) {
        return join(table, Joins.INNER_JOIN);
    }

    public JoinBuilder leftJoin(Table table) {
        return join(table, Joins.LEFT_JOIN);
    }

    public JoinBuilder rightJoin(Table table) {
        return join(table, Joins.RIGHT_JOIN);
    }

    public JoinBuilder fullJoin(Table table) {
        return join(table, Joins.FULL_JOIN);
    }

    public JoinBuilder join(Table table, String type) {
        return new JoinBuilder(table, type);
    }

    public List<LogicalExpression> getWhere() {
        return where;
    }

    public Select where(LogicalExpression... operators) {
        if (operators != null) {
            where.addAll(Arrays.asList(operators));
        }

        return this;
    }

    public SetOperator union(Select other) {
        return Sets.union(this, other);
    }

    public SetOperator unionAll(Select other) {
        return Sets.unionAll(this, other);
    }

    public SetOperator intersect(Select other) {
        return Sets.intersect(this, other);
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Select as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public List<PlaceHolder> getPlaceHolders() {
        List<PlaceHolder> placeHolders = new ArrayList<>();
        getPlaceHolders(placeHolders);
        return placeHolders;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        with.forEach(cte -> cte.getPlaceHolders(placeHolders));
        select.forEach(projection -> projection.getPlaceHolders(placeHolders));

        if (from != null) {
            from.getPlaceHolders(placeHolders);
        }

        joins.forEach(join -> join.getPlaceHolders(placeHolders));
        where.forEach(operator -> operator.getPlaceHolders(placeHolders));
        groupBy.forEach(groupBy -> groupBy.getPlaceHolders(placeHolders));
        having.forEach(having -> having.getPlaceHolders(placeHolders));
        orderBy.forEach(orderBy -> orderBy.getPlaceHolders(placeHolders));
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        if (!with.isEmpty()) {
            builder.append(builder.keyword("with "));
            if (withRecursive) {
                builder.append(builder.keyword("recursive "));
            }

            builder.append(with, ", ")
                    .appendln(" ");
        }

        builder.append(builder.keyword("select "));

        if (!hints.isEmpty()) {
            builder.append("/*+ " + String.join(" ", hints) + " */ ");
        }

        if (distinct) {
            builder.append(builder.keyword("distinct "));
        }

        if (!select.isEmpty()) {
            builder.appendln()
                    .indentln(select.stream()
                            .map(projection -> (Buildable) projection::buildSelection)
                            .toList(), ", ");
        } else {
            builder.appendln()
                    .indent(Column.WILDCARD + " ");
        }

        builder.appendln()
                .appendln(builder.keyword("from "))
                .indent(from != null ? from : Table.of("null"))
                .append(" ");

        if (!joins.isEmpty()) {
            builder.appendln()
                    .indentln(joins, " ");
        }

        if (where != null) {
            BinaryLogicalOperation where = Operators.and(this.where).reduce();
            builder.appendln()
                    .appendln(builder.keyword("where "))
                    .indentln(where.getOperands(), " ", builder.keyword(where.getOperator()) + " ");
        }

        super.buildSql(builder);
    }

    @Override
    public String toString() {
        return toSql();
    }

    @Override
    protected Select self() {
        return this;
    }

    public class JoinBuilder {
        private final Table table;
        private final String type;

        private JoinBuilder(Table table, String type) {
            this.table = table;
            this.type = type;
        }

        public Select on(ComparisonOperation operator) {
            if (operator.getLeftOperand() instanceof Column left
                    && operator.getRightOperand() instanceof Column right
                    && (left.getTable() == table
                    || right.getTable() == table)) {
                joins.add(Join.of(type, right, operator.getOperator(), left));
            }

            return Select.this;
        }
    }
}
