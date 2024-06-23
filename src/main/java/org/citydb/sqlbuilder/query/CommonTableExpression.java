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

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;

import java.util.*;

public class CommonTableExpression implements SqlObject {
    private final String name;
    private final QueryExpression expression;
    private final List<String> columns;

    private CommonTableExpression(String name, QueryExpression expression, List<String> columns) {
        this.name = Objects.requireNonNull(name, "The name must not be null.");
        this.expression = Objects.requireNonNull(expression, "The query expression must not be null.");
        this.columns = columns;
    }

    public static CommonTableExpression of(String name, QueryExpression expression, String... columns) {
        return of(name, expression, columns != null ? new ArrayList<>(Arrays.asList(columns)) : null);
    }

    public static CommonTableExpression of(String name, QueryExpression expression, List<String> columns) {
        return new CommonTableExpression(name, expression, columns);
    }

    public String getName() {
        return name;
    }

    public QueryExpression getQueryExpression() {
        return expression;
    }

    public List<String> getColumns() {
        return columns != null ? columns : Collections.emptyList();
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
