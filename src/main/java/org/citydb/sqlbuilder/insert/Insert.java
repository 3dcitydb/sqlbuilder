/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2026
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

package org.citydb.sqlbuilder.insert;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.Expressions;
import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.CommonTableExpression;
import org.citydb.sqlbuilder.query.QueryStatement;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Insert implements SqlObject {
    private final List<CommonTableExpression> with;
    private final List<String> columns;
    private final List<List<Expression>> values;
    private boolean withRecursive;
    private Table table;
    private QueryStatement<?> query;

    private Insert() {
        with = new ArrayList<>();
        columns = new ArrayList<>();
        values = new ArrayList<>();
    }

    private Insert(Insert other) {
        with = new ArrayList<>(other.with);
        columns = new ArrayList<>(other.columns);
        values = new ArrayList<>();
        other.values.forEach(row -> values.add(new ArrayList<>(row)));
        withRecursive = other.withRecursive;
        table = other.table;
        query = other.query;
    }

    public static Insert newInstance() {
        return new Insert();
    }

    public static Insert into(Table table) {
        return new Insert().table(table);
    }

    public static Insert of(Insert other) {
        return new Insert(other);
    }

    public Optional<Table> getTable() {
        return Optional.ofNullable(table);
    }

    public Insert table(Table table) {
        this.table = table;
        return this;
    }

    public List<CommonTableExpression> getWith() {
        return with;
    }

    public boolean isWithRecursive() {
        return withRecursive;
    }

    public Insert with(String name, QueryStatement<?> statement) {
        return with(CommonTableExpression.of(name, statement));
    }

    public Insert with(CommonTableExpression... ctes) {
        if (ctes != null) {
            with.addAll(Arrays.asList(ctes));
        }

        return this;
    }

    public Insert withRecursive(String name, QueryStatement<?> statement) {
        return withRecursive(CommonTableExpression.of(name, statement));
    }

    public Insert withRecursive(CommonTableExpression... ctes) {
        if (ctes != null) {
            with.addAll(Arrays.asList(ctes));
            withRecursive = true;
        }

        return this;
    }

    public Insert removeWith() {
        with.clear();
        withRecursive = false;
        return this;
    }

    public List<String> getColumns() {
        return columns;
    }

    public Insert columns(String... columns) {
        if (columns != null) {
            this.columns.addAll(Arrays.asList(columns));
        }

        return this;
    }

    public Insert columns(Column... columns) {
        if (columns != null) {
            Arrays.stream(columns).map(Column::getName).forEach(this.columns::add);
        }

        return this;
    }

    public Insert removeColumns() {
        columns.clear();
        return this;
    }

    public List<List<Expression>> getValues() {
        return values;
    }

    public Insert values(Object... values) {
        if (values != null && values.length > 0) {
            // values replace a previously set query
            query = null;
            this.values.add(Arrays.stream(values)
                    .map(Expressions::as)
                    .toList());
        }

        return this;
    }

    public Insert removeValues() {
        values.clear();
        return this;
    }

    public Optional<QueryStatement<?>> getQuery() {
        return Optional.ofNullable(query);
    }

    public Insert query(QueryStatement<?> query) {
        if (query != null) {
            // a query replaces previously added values
            values.clear();
        }

        this.query = query;
        return this;
    }

    public Insert removeQuery() {
        query = null;
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
}
