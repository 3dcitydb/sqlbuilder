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

import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.OrderBy;
import org.citydb.sqlbuilder.query.Selection;

import java.util.Objects;
import java.util.Optional;

public class Column implements ColumnExpression, Selection<Column> {
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

    public OrderBy orderBy() {
        return asc();
    }

    public OrderBy asc() {
        return OrderBy.of(this, OrderBy.ASCENDING);
    }

    public OrderBy desc() {
        return OrderBy.of(this, OrderBy.DESCENDING);
    }

    public OrderBy orderBy(String sortOrder) {
        return OrderBy.of(this, sortOrder);
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
