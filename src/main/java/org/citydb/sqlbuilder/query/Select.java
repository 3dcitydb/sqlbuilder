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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Buildable;
import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.join.JoinType;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperator;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Projection;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class Select extends QueryStatement<Select> {
    private final List<String> hints;
    private final List<CommonTableExpression> with;
    private final List<Projection<?>> select;
    private final List<Join> joins;
    private final List<Predicate> where;
    private boolean withRecursive;
    private boolean distinct;
    private Table from;

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
        this.hints.addAll(Arrays.asList(hints));
        return this;
    }

    public List<CommonTableExpression> getWith() {
        return with;
    }

    public Select with(String name, QueryStatement<?> statement) {
        return with(CommonTableExpression.of(name, statement));
    }

    public Select with(CommonTableExpression... ctes) {
        with.addAll(Arrays.asList(ctes));
        return this;
    }

    public Select withRecursive(String name, QueryStatement<?> statement) {
        return withRecursive(CommonTableExpression.of(name, statement));
    }

    public Select withRecursive(CommonTableExpression... ctes) {
        with.addAll(Arrays.asList(ctes));
        withRecursive = true;
        return this;
    }

    public List<Projection<?>> getSelect() {
        return select;
    }

    public Select select(Projection<?>... projections) {
        select.addAll(Arrays.asList(projections));
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
        this.joins.addAll(Arrays.asList(joins));
        return this;
    }

    public JoinBuilder join(Table table) {
        return new JoinBuilder(table);
    }

    public List<Predicate> getWhere() {
        return where;
    }

    public Select where(Predicate... predicates) {
        where.addAll(Arrays.asList(predicates));
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
    public Set<Table> getInvolvedTables() {
        Set<Table> tables = new LinkedHashSet<>();
        if (from != null) {
            from.buildInvolvedTables(tables);
        }

        select.forEach(projection -> projection.buildInvolvedTables(tables));
        joins.forEach(join -> join.buildInvolvedTables(tables));
        return tables;
    }

    @Override
    public List<PlaceHolder> getInvolvedPlaceHolders() {
        List<PlaceHolder> placeHolders = new ArrayList<>();
        buildInvolvedPlaceHolders(placeHolders);
        return placeHolders;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        where.forEach(predicate -> predicate.buildInvolvedTables(tables));
        groupBy.forEach(groupBy -> groupBy.buildInvolvedTables(tables));
        having.forEach(having -> having.buildInvolvedTables(tables));
        orderBy.forEach(orderBy -> orderBy.buildInvolvedTables(tables));
        tables.removeAll(getInvolvedTables());
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        with.forEach(cte -> cte.buildInvolvedPlaceHolders(placeHolders));
        select.forEach(projection -> projection.buildInvolvedPlaceHolders(placeHolders));

        if (from != null) {
            from.buildInvolvedPlaceHolders(placeHolders);
        }

        joins.forEach(join -> join.buildInvolvedPlaceHolders(placeHolders));
        where.forEach(predicate -> predicate.buildInvolvedPlaceHolders(placeHolders));
        groupBy.forEach(groupBy -> groupBy.buildInvolvedPlaceHolders(placeHolders));
        having.forEach(having -> having.buildInvolvedPlaceHolders(placeHolders));
        orderBy.forEach(orderBy -> orderBy.buildInvolvedPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
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
                            .map(projection -> (Buildable) b -> projection.buildSQL(b, true))
                            .toList(), ", ");
        }

        Set<Table> from = getInvolvedTables();
        if (!from.isEmpty()) {
            if (!joins.isEmpty()) {
                Set<Table> joinTables = new HashSet<>();
                joins.forEach(join -> join.buildInvolvedTables(joinTables));
                from.removeAll(joinTables);
                if (from.isEmpty()) {
                    from.add(joins.get(0).getFromColumn().getTable());
                }
            }

            builder.appendln()
                    .appendln(builder.keyword("from "))
                    .indentln(from, ", ");

            if (!joins.isEmpty()) {
                builder.appendln()
                        .indentln(joins, " ");
            }
        }

        if (!where.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("where "))
                    .indentln(where, " ", builder.keyword("and "));
        }

        super.buildSQL(builder);
    }

    @Override
    public String toString() {
        return toSQL();
    }

    @Override
    protected Select self() {
        return this;
    }

    public class JoinBuilder {
        private final Table table;

        private JoinBuilder(Table table) {
            this.table = table;
        }

        public Select on(ComparisonOperator operator) {
            return build(JoinType.INNER_JOIN, operator);
        }

        public Select inner(ComparisonOperator operator) {
            return build(JoinType.INNER_JOIN, operator);
        }

        public Select left(ComparisonOperator operator) {
            return build(JoinType.LEFT_JOIN, operator);
        }

        public Select right(ComparisonOperator operator) {
            return build(JoinType.RIGHT_JOIN, operator);
        }

        public Select full(ComparisonOperator operator) {
            return build(JoinType.FULL_JOIN, operator);
        }

        private Select build(JoinType type, ComparisonOperator operator) {
            if (operator.getLeftOperand() instanceof Column toColumn
                    && operator.getRightOperand() instanceof Column fromColumn
                    && (toColumn.getTable() == table
                    || fromColumn.getTable() == table)) {
                joins.add(Join.of(type, toColumn, operator.getType(), fromColumn));
            }

            return Select.this;
        }
    }
}
