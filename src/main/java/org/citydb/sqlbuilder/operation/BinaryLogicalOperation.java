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

package org.citydb.sqlbuilder.operation;

import org.citydb.sqlbuilder.common.SqlVisitor;

import java.util.*;

public class BinaryLogicalOperation implements LogicalOperation {
    private final List<BooleanExpression> operands;
    private final String operator;
    private String alias;

    private BinaryLogicalOperation(String operator, List<? extends BooleanExpression> operands) {
        this.operands = new ArrayList<>(Objects.requireNonNull(operands, "The operands list must not be null."));
        this.operator = Objects.requireNonNull(operator, "The operator type must not be null.");

        if (operands.isEmpty()) {
            throw new IllegalArgumentException("The operands list must not be empty.");
        } else if (!Operators.AND.equalsIgnoreCase(operator) && !Operators.OR.equalsIgnoreCase(operator)) {
            throw new IllegalArgumentException("The operator '" + operator + "' is not a supported binary operator.");
        }
    }

    public static BinaryLogicalOperation of(String operator, List<? extends BooleanExpression> operands) {
        return new BinaryLogicalOperation(operator, operands);
    }

    public static BinaryLogicalOperation of(String operator, BooleanExpression... operands) {
        return new BinaryLogicalOperation(operator, operands != null ? Arrays.asList(operands) : null);
    }

    public static BinaryLogicalOperation of(BooleanExpression leftOperand, String operator, BooleanExpression rightOperand) {
        List<BooleanExpression> operands = new ArrayList<>();
        operands.add(Objects.requireNonNull(leftOperand, "The left operand must not be null."));
        operands.add(Objects.requireNonNull(rightOperand, "The right operand must not be null."));
        return new BinaryLogicalOperation(operator, operands);
    }

    public List<BooleanExpression> getOperands() {
        return operands;
    }

    public BinaryLogicalOperation reduce() {
        BinaryLogicalOperation reduced = this;
        while (reduced.operands.size() == 1
                && reduced.getOperands().get(0) instanceof BinaryLogicalOperation operation) {
            reduced = operation;
        }

        return reduced;
    }

    public BinaryLogicalOperation add(BooleanExpression... operands) {
        return operands != null ? add(Arrays.asList(operands)) : this;
    }

    public BinaryLogicalOperation add(List<? extends BooleanExpression> operands) {
        if (operands != null && !operands.isEmpty()) {
            operands.stream()
                    .filter(Objects::nonNull)
                    .forEach(this.operands::add);
        }

        return this;
    }

    BinaryLogicalOperation fluentAnd(BooleanExpression operand) {
        if (operand != null) {
            if (hasOperator(Operators.OR)) {
                int index = operands.size() - 1;
                operands.set(index, Operators.and(operands.get(index), operand));
            } else {
                operands.add(operand);
            }
        }

        return this;
    }

    BinaryLogicalOperation fluentOr(BooleanExpression operand) {
        if (operand != null) {
            if (hasOperator(Operators.AND)) {
                return Operators.or(this, operand);
            } else {
                operands.add(operand);
            }
        }

        return this;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public BinaryLogicalOperation as(String alias) {
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
