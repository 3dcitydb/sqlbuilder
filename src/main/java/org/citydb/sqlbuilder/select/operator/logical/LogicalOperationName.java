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

package org.citydb.sqlbuilder.select.operator.logical;

import org.citydb.sqlbuilder.select.operator.OperationName;

import java.util.EnumSet;

public enum LogicalOperationName implements OperationName {
    AND("and"),
    OR("or"),
    NOT("not");

    public static final EnumSet<LogicalOperationName> BINARY_OPERATIONS = EnumSet.of(AND, OR);
    public static final EnumSet<LogicalOperationName> UNARY_OPERATIONS = EnumSet.of(NOT);

    final String symbol;

    LogicalOperationName(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
