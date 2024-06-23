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

package org.citydb.sqlbuilder.util;

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.operation.BooleanExpression;
import org.citydb.sqlbuilder.query.QueryExpression;
import org.citydb.sqlbuilder.query.Selection;
import org.citydb.sqlbuilder.schema.ColumnExpression;

import java.util.*;

public class PlainText implements BooleanExpression, ColumnExpression, QueryExpression, Selection<PlainText> {
    private final String sql;
    private final List<SqlObject> tokens;
    private String alias;

    private PlainText(String sql, List<SqlObject> tokens) {
        this.sql = Objects.requireNonNull(sql, "The plain SQL text must not be null.");
        this.tokens = tokens;
    }

    public static PlainText of(String sql) {
        return new PlainText(sql, null);
    }

    public static PlainText of(String sql, SqlObject... tokens) {
        return new PlainText(sql, tokens != null ? Arrays.asList(tokens) : null);
    }

    public String getSql() {
        return sql;
    }

    public List<SqlObject> getTokens() {
        return tokens != null ? tokens : Collections.emptyList();
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public PlainText as(String alias) {
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
}
