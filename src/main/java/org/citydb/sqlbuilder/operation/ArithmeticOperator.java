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
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ArithmeticOperator implements ArithmeticExpression, Operation {
    private final Expression leftOperand;
    private Expression rightOperand;
    private final String type;

    private final Map<String, Integer> precedence = Map.of(
            Operators.MULTIPLY, 1,
            Operators.DIVIDE, 1,
            Operators.MODULO, 1,
            Operators.PLUS, 2,
            Operators.MINUS, 2);

    protected ArithmeticOperator(Expression leftOperand, String type, Expression rightOperand) {
        this.leftOperand = Objects.requireNonNull(leftOperand, "The left operand must not be null.");
        this.rightOperand = Objects.requireNonNull(rightOperand, "The right operand must not be null.");
        this.type = Objects.requireNonNull(type, "The operator type must not be null.");
    }

    public static ArithmeticOperator of(Expression leftOperand, String type, Expression rightOperand) {
        return new ArithmeticOperator(leftOperand, type, rightOperand);
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    ArithmeticOperator fluentAppend(String operator, Expression operand) {
        if (precedence.getOrDefault(operator, Integer.MAX_VALUE) < precedence.getOrDefault(type, Integer.MAX_VALUE)) {
            rightOperand = new ArithmeticOperator(rightOperand, operator, operand);
            return this;
        } else {
            return new ArithmeticOperator(this, operator, operand);
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        leftOperand.getPlaceHolders(placeHolders);
        rightOperand.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append("(")
                .append(leftOperand)
                .append(" " + builder.keyword(type) + " ")
                .append(rightOperand)
                .append(")");
    }

    @Override
    public String toString() {
        return toSql();
    }
}
