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

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.*;

public class BinaryLogicalOperator implements LogicalExpression {
    private final List<LogicalExpression> operands;
    private final String type;
    private String alias;

    private BinaryLogicalOperator(String type, List<LogicalExpression> operands) {
        this.operands = Objects.requireNonNull(operands, "The operands list must not be null.");
        this.type = Objects.requireNonNull(type, "The operator type must not be null.");

        if (operands.isEmpty()) {
            throw new IllegalArgumentException("The operands list must not be empty.");
        } else if (!Operators.AND.equalsIgnoreCase(type) && !Operators.OR.equalsIgnoreCase(type)) {
            throw new IllegalArgumentException("The operator '" + type + "' is not a supported binary operator.");
        }
    }

    public static BinaryLogicalOperator of(String type, List<LogicalExpression> operands) {
        return new BinaryLogicalOperator(type, operands);
    }

    public static BinaryLogicalOperator of(String type, LogicalExpression... operands) {
        return new BinaryLogicalOperator(type, operands != null ?
                new ArrayList<>(Arrays.asList(operands)) :
                null);
    }

    public static BinaryLogicalOperator of(LogicalExpression leftOperand, String type, LogicalExpression rightOperand) {
        List<LogicalExpression> operands = new ArrayList<>();
        operands.add(Objects.requireNonNull(leftOperand, "The left operand must not be null."));
        operands.add(Objects.requireNonNull(rightOperand, "The right operand must not be null."));
        return new BinaryLogicalOperator(type, operands);
    }

    public List<LogicalExpression> getOperands() {
        return operands;
    }

    public BinaryLogicalOperator reduce() {
        BinaryLogicalOperator reduced = this;
        while (reduced.operands.size() == 1
                && reduced.getOperands().get(0) instanceof BinaryLogicalOperator operator) {
            reduced = operator;
        }

        return reduced;
    }

    public BinaryLogicalOperator add(LogicalExpression... operands) {
        return operands != null ? add(Arrays.asList(operands)) : this;
    }

    public BinaryLogicalOperator add(List<LogicalExpression> operands) {
        if (operands != null && !operands.isEmpty()) {
            operands.stream()
                    .filter(Objects::nonNull)
                    .forEach(this.operands::add);
        }

        return this;
    }

    BinaryLogicalOperator fluentAnd(LogicalExpression operand) {
        if (operand != null) {
            if (hasType(Operators.OR)) {
                int index = operands.size() - 1;
                operands.set(index, Operators.and(operands.get(index), operand));
            } else {
                operands.add(operand);
            }
        }

        return this;
    }

    BinaryLogicalOperator fluentOr(LogicalExpression operand) {
        if (operand != null) {
            if (hasType(Operators.AND)) {
                return Operators.or(this, operand);
            } else {
                operands.add(operand);
            }
        }

        return this;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public BinaryLogicalOperator as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        operands.forEach(operand -> operand.getPlaceHolders(placeHolders));
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        if (operands.size() > 1) {
            builder.appendln("(")
                    .indentln(operands, " ", builder.keyword(type) + " ")
                    .appendln()
                    .append(")");
        } else {
            builder.append(operands.get(0));
        }
    }

    @Override
    public String toString() {
        return toSql();
    }
}
