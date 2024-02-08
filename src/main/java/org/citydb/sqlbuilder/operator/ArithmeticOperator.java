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

package org.citydb.sqlbuilder.operator;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.List;
import java.util.Objects;

public class ArithmeticOperator implements Operator {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final String name;

    protected ArithmeticOperator(Expression leftOperand, String name, Expression rightOperand) {
        this.leftOperand = Objects.requireNonNull(leftOperand, "The left operand must not be null.");
        this.rightOperand = Objects.requireNonNull(rightOperand, "The right operand must not be null.");
        this.name = Objects.requireNonNull(name, "The operator name must not be null.");
    }

    public static ArithmeticOperator of(Expression leftOperand, String name, Expression rightOperand) {
        return new ArithmeticOperator(leftOperand, name, rightOperand);
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        leftOperand.getPlaceHolders(placeHolders);
        rightOperand.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(leftOperand)
                .append(" " + builder.keyword(name) + " ")
                .append(rightOperand);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
