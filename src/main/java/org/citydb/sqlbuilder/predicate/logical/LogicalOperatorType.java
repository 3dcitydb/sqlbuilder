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

package org.citydb.sqlbuilder.predicate.logical;

import org.citydb.sqlbuilder.SQLBuilder;

import java.util.EnumSet;

public enum LogicalOperatorType {
    AND("and"),
    OR("or"),
    NOT("not"),
    LIKE("like"),
    NOT_LIKE("not like"),
    BETWEEN("between"),
    NOT_BETWEEN("not between"),
    IN("in"),
    NOT_IN("not in"),
    IS_NULL("is null"),
    IS_NOT_NULL("is not null"),
    EXISTS("exists"),
    NOT_EXISTS("not exists"),
    ALL("all"),
    ANY("any"),
    SOME("some");

    public static final EnumSet<LogicalOperatorType> BINARY_OPERATORS = EnumSet.of(AND, OR);
    public static final EnumSet<LogicalOperatorType> UNARY_OPERATORS = EnumSet.of(
            IS_NULL, IS_NOT_NULL, EXISTS, NOT_EXISTS, ALL, ANY, SOME);
    private final String sql;

    LogicalOperatorType(String sql) {
        this.sql = sql;
    }

    public String toSQL(SQLBuilder builder) {
        return builder.keyword(sql);
    }

    @Override
    public String toString() {
        return sql;
    }
}
