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
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class BinaryLogicalOperator implements LogicalOperator {
    private final List<Predicate> operands;
    private final LogicalOperatorType type;

    private BinaryLogicalOperator(LogicalOperatorType type, List<Predicate> operands) {
        this.operands = Objects.requireNonNull(operands, "The operands list must not be null.");
        this.type = Objects.requireNonNull(type, "The logical operation type list must not be null.");

        if (operands.isEmpty()) {
            throw new IllegalArgumentException("The operands list must not be empty.");
        } else if (!LogicalOperatorType.BINARY_OPERATORS.contains(type)) {
            throw new IllegalArgumentException("The comparison type '" + type + "' is not binary.");
        }
    }

    public static BinaryLogicalOperator of(LogicalOperatorType type, List<Predicate> operands) {
        return new BinaryLogicalOperator(type, operands);
    }

    public static BinaryLogicalOperator of(LogicalOperatorType type, Predicate... operands) {
        return new BinaryLogicalOperator(type, operands != null ? Arrays.asList(operands) : null);
    }

    public static BinaryLogicalOperator of(Predicate leftOperand, LogicalOperatorType type, Predicate rightOperand) {
        List<Predicate> operands = new ArrayList<>();
        operands.add(Objects.requireNonNull(leftOperand, "The left operand must not be null."));
        operands.add(Objects.requireNonNull(rightOperand, "The right operand must not be null."));
        return new BinaryLogicalOperator(type, operands);
    }

    public List<Predicate> getOperands() {
        return operands;
    }

    public BinaryLogicalOperator operand(Predicate operand) {
        if (operand != null) {
            operands.add(operand);
        }

        return this;
    }

    @Override
    public LogicalOperatorType getType() {
        return type;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        operands.forEach(operand -> operand.buildInvolvedTables(tables));
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        operands.forEach(operand -> operand.buildInvolvedPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        if (operands.size() > 1) {
            builder.openParenthesis()
                    .append(operands, " ", true, type.toSQL(builder) + " ")
                    .closeParenthesis();
        } else {
            builder.append(operands.get(0));
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
