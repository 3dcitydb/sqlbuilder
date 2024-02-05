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
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.query.CommonTableExpression;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class Update implements Statement {
    private final List<CommonTableExpression> with;
    private final List<UpdateValue> set;
    private final List<Predicate> where;
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

    public Update with(CommonTableExpression... ctes) {
        with.addAll(Arrays.asList(ctes));
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

    public List<Predicate> getWhere() {
        return where;
    }

    public Update where(Predicate... predicates) {
        where.addAll(Arrays.asList(predicates));
        return this;
    }

    @Override
    public Set<Table> getInvolvedTables() {
        Set<Table> tables = new LinkedHashSet<>();
        if (table != null) {
            table.buildInvolvedTables(tables);
        }

        set.forEach(value -> value.buildInvolvedTables(tables));
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
        tables.removeAll(getInvolvedTables());
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        with.forEach(cte -> cte.buildInvolvedPlaceHolders(placeHolders));

        if (table != null) {
            table.buildInvolvedPlaceHolders(placeHolders);
        }

        set.forEach(value -> value.buildInvolvedPlaceHolders(placeHolders));
        where.forEach(predicate -> predicate.buildInvolvedPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        if (!with.isEmpty()) {
            builder.append(builder.keyword("with "))
                    .append(with, ", ")
                    .append(" ");
        }

        builder.append(builder.keyword("update "));

        Set<Table> tables = getInvolvedTables();
        if (!tables.isEmpty()) {
            builder.newlineAndIncreaseIndent()
                    .append(tables.iterator().next())
                    .append(" ")
                    .decreaseIndent();
        }

        if (!set.isEmpty()) {
            builder.newline()
                    .append(builder.keyword("set "))
                    .newline()
                    .indentAndAppend(set, ", ", true);
        }

        if (!where.isEmpty()) {
            builder.newline()
                    .append(builder.keyword("where "))
                    .newline()
                    .indentAndAppend(where, " ", true, builder.keyword("and "));
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
