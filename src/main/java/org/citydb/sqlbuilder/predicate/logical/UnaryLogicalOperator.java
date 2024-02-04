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
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UnaryLogicalOperator implements LogicalOperator {
    private final Expression operand;
    private final LogicalOperatorType type;

    private UnaryLogicalOperator(Expression operand, LogicalOperatorType type) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.type = Objects.requireNonNull(type, "The comparison type must not be null.");
        if (!LogicalOperatorType.UNARY_OPERATORS.contains(type)) {
            throw new IllegalArgumentException("The comparison type '" + type + "' is not unary.");
        }
    }

    public static UnaryLogicalOperator of(Expression operand, LogicalOperatorType type) {
        return new UnaryLogicalOperator(operand, type);
    }

    public Expression getOperand() {
        return operand;
    }

    @Override
    public LogicalOperatorType getType() {
        return type;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        operand.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        operand.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        boolean prefixNotation = switch (type) {
            case EXISTS, NOT_EXISTS, ALL, ANY, SOME -> true;
            default -> false;
        };

        if (prefixNotation) {
            builder.append(getType().toSQL(builder) + " ")
                    .append(operand);
        } else {
            builder.append(operand)
                    .append(" " + getType().toSQL(builder));
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
