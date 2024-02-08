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

package org.citydb.sqlbuilder.update;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.Statement;
import org.citydb.sqlbuilder.literal.Literals;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.operator.LogicalOperator;
import org.citydb.sqlbuilder.query.CommonTableExpression;
import org.citydb.sqlbuilder.query.QueryStatement;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Update implements Statement {
    private final List<CommonTableExpression> with;
    private final List<UpdateValue> set;
    private final List<LogicalOperator> where;
    private boolean withRecursive;
    private Table table;

    private Update() {
        with = new ArrayList<>();
        set = new ArrayList<>();
        where = new ArrayList<>();
    }

    private Update(Update other) {
        with = new ArrayList<>(other.with);
        set = new ArrayList<>(other.set);
        where = new ArrayList<>(other.where);
        withRecursive = other.withRecursive;
        table = other.table;
    }

    public static Update newInstance() {
        return new Update();
    }

    public static Update of(Update other) {
        return new Update(other);
    }

    public Table getTable() {
        return table;
    }

    public Update table(Table table) {
        this.table = table;
        return this;
    }

    public List<CommonTableExpression> getWith() {
        return with;
    }

    public Update with(String name, QueryStatement<?> statement) {
        return with(CommonTableExpression.of(name, statement));
    }

    public Update with(CommonTableExpression... ctes) {
        with.addAll(Arrays.asList(ctes));
        return this;
    }

    public Update withRecursive(String name, QueryStatement<?> statement) {
        return withRecursive(CommonTableExpression.of(name, statement));
    }

    public Update withRecursive(CommonTableExpression... ctes) {
        with.addAll(Arrays.asList(ctes));
        withRecursive = true;
        return this;
    }

    public List<UpdateValue> getSet() {
        return set;
    }

    public Update set(UpdateValue... values) {
        set.addAll(Arrays.asList(values));
        return this;
    }

    public UpdateValueBuilder set(Column column) {
        return new UpdateValueBuilder(column);
    }

    public List<LogicalOperator> getWhere() {
        return where;
    }

    public Update where(LogicalOperator... operators) {
        where.addAll(Arrays.asList(operators));
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

        if (table != null) {
            table.getPlaceHolders(placeHolders);
        }

        set.forEach(value -> value.getPlaceHolders(placeHolders));
        where.forEach(operator -> operator.getPlaceHolders(placeHolders));
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

        builder.append(builder.keyword("update "));
        builder.appendln()
                .indent(table != null ? table : Table.of("null"))
                .append(" ");

        if (!set.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("set "))
                    .indentln(set, ", ");
        }

        if (!where.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("where "))
                    .indentln(where, " ", builder.keyword("and "));
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }

    public class UpdateValueBuilder {
        private final Column column;

        private UpdateValueBuilder(Column column) {
            this.column = column;
        }

        public Update value(Object value) {
            set.add(UpdateValue.of(column, value instanceof Expression expression ?
                    expression :
                    Literals.of(value)));
            return Update.this;
        }
    }
}
