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

import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.join.Joins;
import org.citydb.sqlbuilder.operation.BinaryComparisonOperation;
import org.citydb.sqlbuilder.operation.BooleanExpression;
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
    private final List<Table> from;
    private final List<Join> joins;
    private final List<BooleanExpression> where;
    private boolean withRecursive;
    private boolean distinct;
    private String alias;

    private Select() {
        hints = new ArrayList<>();
        with = new ArrayList<>();
        select = new ArrayList<>();
        from = new ArrayList<>();
        joins = new ArrayList<>();
        where = new ArrayList<>();
    }

    private Select(Select other) {
        super(other);
        hints = new ArrayList<>(other.hints);
        with = new ArrayList<>(other.with);
        select = new ArrayList<>(other.select);
        from = new ArrayList<>(other.from);
        joins = new ArrayList<>(other.joins);
        where = new ArrayList<>(other.where);
        withRecursive = other.withRecursive;
        distinct = other.distinct;
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

    public Select removeHints() {
        hints.clear();
        return this;
    }

    public List<CommonTableExpression> getWith() {
        return with;
    }

    public boolean isWithRecursive() {
        return withRecursive;
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

    public Select removeWith() {
        with.clear();
        return this;
    }

    public List<Selection<?>> getSelect() {
        return select;
    }

    public Select select(Selection<?>... selections) {
        return selections != null ? select(Arrays.asList(selections)) : this;
    }

    public Select select(List<? extends Selection<?>> selections) {
        if (selections != null && !selections.isEmpty()) {
            select.addAll(selections);
        }

        return this;
    }

    public Select removeSelect() {
        select.clear();
        return this;
    }

    public List<Table> getFrom() {
        return from;
    }

    public Select fromLateral(Select select) {
        return from(Table.lateral(select));
    }

    public Select from(Table... from) {
        if (from != null) {
            this.from.addAll(Arrays.asList(from));
        }

        return this;
    }

    public Select removeFrom() {
        from.clear();
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

    public JoinBuilder join(Table table, String type) {
        return new JoinBuilder(table, type);
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

    public Select crossJoin(Table table) {
        return join(Joins.cross(table));
    }

    public Select removeJoins() {
        joins.clear();
        return this;
    }

    public List<BooleanExpression> getWhere() {
        return where;
    }

    public Select where(BooleanExpression... operators) {
        if (operators != null) {
            where.addAll(Arrays.asList(operators));
        }

        return this;
    }

    public Select removeWhere() {
        where.clear();
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
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
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

        public Select on(BinaryComparisonOperation operator) {
            if (operator.getLeftOperand() instanceof Column left
                    && operator.getRightOperand() instanceof Column right) {
                if (left.getTable() == table) {
                    joins.add(Join.of(type, left, operator.getOperator(), right));
                } else if (right.getTable() == table) {
                    joins.add(Join.of(type, right, operator.getOperator(), left));
                }
            }

            return Select.this;
        }
    }
}
