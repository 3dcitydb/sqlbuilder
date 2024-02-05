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

package org.citydb.sqlbuilder.schema;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.query.OrderBy;
import org.citydb.sqlbuilder.query.SortOrder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Column implements ColumnExpression, Projection<Column> {
    public static final WildcardColumn WILDCARD = WildcardColumn.newInstance();

    private final Table table;
    private final String name;
    private String alias;

    private Column(Table table, String name, String alias) {
        this.table = Objects.requireNonNull(table, "The table must not be null.");
        this.name = Objects.requireNonNull(name, "The column name must not be null.");
        this.alias = alias;
    }

    public static Column of(Table table, String name, String alias) {
        return new Column(table, name, alias);
    }

    public static Column of(Table table, String name) {
        return new Column(table, name, null);
    }

    public Table getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Column as(String alias) {
        this.alias = alias;
        return this;
    }

    public OrderBy asc() {
        return OrderBy.of(this, SortOrder.ASCENDING);
    }

    public OrderBy desc() {
        return OrderBy.of(this, SortOrder.DESCENDING);
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        table.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        table.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder, boolean withAlias) {
        builder.append(table.getAlias() + "." + builder.identifier(name));
        if (withAlias && alias != null) {
            builder.append(builder.keyword(" as ") + alias);
        }
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        buildSQL(builder, false);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
