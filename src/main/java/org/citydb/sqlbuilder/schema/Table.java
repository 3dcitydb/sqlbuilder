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

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.CommonTableExpression;
import org.citydb.sqlbuilder.query.QueryExpression;
import org.citydb.sqlbuilder.query.Select;
import org.citydb.sqlbuilder.query.SetOperator;

import java.util.*;
import java.util.stream.Collectors;

public class Table implements SqlObject {
    private final String name;
    private final String schema;
    private final QueryExpression queryExpression;
    private final boolean isLateral;
    private String alias;

    private Table(String name, String schema, QueryExpression queryExpression, boolean isLateral) {
        this.name = Objects.requireNonNull(name, "The table name must not be null.");
        this.schema = schema;
        this.queryExpression = queryExpression;
        this.isLateral = isLateral;
    }

    public static Table of(String name, String schema) {
        return new Table(name, schema, null, false);
    }

    public static Table of(String name) {
        return new Table(name, null, null, false);
    }

    public static Table of(Select select) {
        return new Table("", null, select, false);
    }

    public static Table of(SetOperator setOperator) {
        return new Table("", null, setOperator, false);
    }

    public static Table of(CommonTableExpression cte) {
        return new Table(cte.getName(), null, null, false);
    }

    public static Table lateral(Select select) {
        return new Table("", null, select, true);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getSchema() {
        return Optional.ofNullable(schema);
    }

    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    public Table alias(String alias) {
        this.alias = alias;
        return this;
    }

    public Optional<QueryExpression> getQueryExpression() {
        return Optional.ofNullable(queryExpression);
    }

    public boolean isLateral() {
        return isLateral;
    }

    public Column column(String name) {
        return Column.of(this, name);
    }

    public Column column(String name, String alias) {
        return Column.of(this, name, alias);
    }

    public List<Column> columns(String... columns) {
        return columns != null ?
                columns(Arrays.asList(columns)) :
                Collections.emptyList();
    }

    public List<Column> columns(List<String> columns) {
        return columns != null ?
                columns.stream()
                        .map(name -> Column.of(this, name))
                        .collect(Collectors.toList()) :
                Collections.emptyList();
    }

    public List<Column> columns(Map<String, String> columns) {
        return columns != null ?
                columns.entrySet().stream()
                        .map(e -> Column.of(this, e.getKey(), e.getValue()))
                        .collect(Collectors.toList()) :
                Collections.emptyList();
    }

    public WildcardColumn wildcard() {
        return WildcardColumn.of(this);
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
