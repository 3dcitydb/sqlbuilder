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

import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.select.Select;
import org.citydb.sqlbuilder.select.operator.set.SetOperator;

import java.util.Objects;

public final class Table {
    private String name;
    private String alias;
    private String schema;
    private SubQueryExpression queryExpression;

    public Table(String name, String schema, AliasGenerator aliasGenerator) {
        this.name = name;
        this.schema = schema;

        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The table name shall neither be null nor empty.");

        alias = aliasGenerator.nextAlias();
    }

    public Table(String name, AliasGenerator aliasGenerator) {
        this(name, null, aliasGenerator);
    }

    public Table(Select select, AliasGenerator aliasGenerator) {
        this("(" + select + ")", aliasGenerator);
        queryExpression = select;
    }

    public Table(SetOperator setOperator, AliasGenerator aliasGenerator) {
        this("(" + setOperator + ")", aliasGenerator);
        queryExpression = setOperator;
    }

    public Table(String name, String schema) {
        this(name, schema, GlobalAliasGenerator.getInstance());
    }

    public Table(String name) {
        this(name, GlobalAliasGenerator.getInstance());
    }

    public Table(Select select) {
        this(select, GlobalAliasGenerator.getInstance());
    }

    public Table(SetOperator setOperator) {
        this(setOperator, GlobalAliasGenerator.getInstance());
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isSetQueryExpression() {
        return queryExpression != null;
    }

    public SubQueryExpression getQueryExpression() {
        return queryExpression;
    }

    public Column getColumn(String columnName) {
        return new Column(this, columnName);
    }

    public Column getColumn(String columnName, String asName) {
        return new Column(this, columnName, asName);
    }

    public Column[] getColumns(String... columnNames) {
        Objects.requireNonNull(columnNames, "The column names array must not be null.");
        Column[] columns = new Column[columnNames.length];
        for (int i = 0; i < columnNames.length; i++)
            columns[i] = getColumn(columnNames[i]);

        return columns;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        if (schema != null && schema.length() > 0)
            tmp.append(schema).append(".");

        return tmp.append(name).append(" ").append(alias).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Table) {
            Table other = (Table) obj;
            return (schema == null ? other.schema == null : schema.equalsIgnoreCase(other.schema))
                    && name.equalsIgnoreCase(other.name)
                    && alias.equals(other.alias);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        if (schema != null)
            hash = hash * 31 + schema.toUpperCase().hashCode();

        hash = hash * 31 + name.toUpperCase().hashCode();
        hash = hash * 31 + alias.hashCode();

        return hash;
    }
}
