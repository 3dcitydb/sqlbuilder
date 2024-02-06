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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.Statement;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.query.QueryExpression;
import org.citydb.sqlbuilder.schema.ColumnExpression;
import org.citydb.sqlbuilder.schema.Projection;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class PlainSQLText implements Expression, ColumnExpression, Projection<PlainSQLText>, QueryExpression, Statement {
    private final String sql;
    private String alias;

    private PlainSQLText(String sql) {
        this.sql = Objects.requireNonNull(sql, "The plain SQL text must not be null.");
    }

    public static PlainSQLText of(String sql) {
        return new PlainSQLText(sql);
    }

    @Override
    public Set<Table> getInvolvedTables() {
        return Collections.emptySet();
    }

    @Override
    public List<PlaceHolder> getInvolvedPlaceHolders() {
        return Collections.emptyList();
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public PlainSQLText as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void buildSQL(SQLBuilder builder, boolean withAlias) {
        builder.append(sql);
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
