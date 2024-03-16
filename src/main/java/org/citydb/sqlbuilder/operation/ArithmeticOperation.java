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
import org.citydb.sqlbuilder.literal.ScalarExpression;
import org.citydb.sqlbuilder.query.Selection;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ArithmeticOperation implements NumericExpression, Operation, Selection<ArithmeticOperation> {
    private final ScalarExpression leftOperand;
    private ScalarExpression rightOperand;
    private final String operator;
    private String alias;

    private final Map<String, Integer> precedence = Map.of(
            Operators.MULTIPLY, 1,
            Operators.DIVIDE, 1,
            Operators.MODULO, 1,
            Operators.PLUS, 2,
            Operators.MINUS, 2);

    protected ArithmeticOperation(ScalarExpression leftOperand, String operator, ScalarExpression rightOperand) {
        this.leftOperand = Objects.requireNonNull(leftOperand, "The left operand must not be null.");
        this.rightOperand = Objects.requireNonNull(rightOperand, "The right operand must not be null.");
        this.operator = Objects.requireNonNull(operator, "The operator must not be null.");
    }

    public static ArithmeticOperation of(ScalarExpression leftOperand, String operator, ScalarExpression rightOperand) {
        return new ArithmeticOperation(leftOperand, operator, rightOperand);
    }

    public ScalarExpression getLeftOperand() {
        return leftOperand;
    }

    public ScalarExpression getRightOperand() {
        return rightOperand;
    }

    ArithmeticOperation fluentAppend(String operator, ScalarExpression operand) {
        if (precedence.getOrDefault(operator, Integer.MAX_VALUE) < precedence.getOrDefault(this.operator, Integer.MAX_VALUE)) {
            rightOperand = new ArithmeticOperation(rightOperand, operator, operand);
            return this;
        } else {
            return new ArithmeticOperation(this, operator, operand);
        }
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
    public ArithmeticOperation as(String alias) {
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
