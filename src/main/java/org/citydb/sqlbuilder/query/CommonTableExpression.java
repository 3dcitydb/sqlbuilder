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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.SQLObject;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class CommonTableExpression implements SQLObject {
    private final String name;
    private final QueryStatement<?> queryStatement;
    private final List<String> columns;

    private CommonTableExpression(String name, QueryStatement<?> queryStatement, List<String> columns) {
        this.name = Objects.requireNonNull(name, "The name must not be null.");
        this.queryStatement = Objects.requireNonNull(queryStatement, "The query statement must not be null.");
        this.columns = columns;
    }

    public static CommonTableExpression of(String name, QueryStatement<?> queryStatement, String... columns) {
        return of(name, queryStatement, columns != null ?
                new ArrayList<>(Arrays.asList(columns)) :
                null);
    }

    public static CommonTableExpression of(String name, QueryStatement<?> queryStatement, List<String> columns) {
        return new CommonTableExpression(name, queryStatement, columns);
    }

    public String getName() {
        return name;
    }

    public QueryStatement<?> getQueryStatement() {
        return queryStatement;
    }

    public List<String> getColumns() {
        return columns != null ? columns : Collections.emptyList();
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        queryStatement.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        queryStatement.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(name);
        if (columns != null && !columns.isEmpty()) {
            builder.append(" (")
                    .append(String.join(", ", columns))
                    .append(")");
        }

        builder.append(builder.keyword(" as "))
                .append(queryStatement);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
