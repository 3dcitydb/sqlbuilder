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

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.Literals;

public interface ArithmeticExpression extends Expression {

    default ArithmeticOperator plus(Object operand) {
        return append(Operators.PLUS, operand);
    }

    default ArithmeticOperator minus(Object operand) {
        return append(Operators.MINUS, operand);
    }

    default ArithmeticOperator multiplyBy(Object operand) {
        return append(Operators.MULTIPLY, operand);
    }

    default ArithmeticOperator divideBy(Object operand) {
        return append(Operators.DIVIDE, operand);
    }

    default ArithmeticOperator modulo(Object operand) {
        return append(Operators.MODULO, operand);
    }

    default ArithmeticOperator concat(Object operand) {
        return Operators.concat(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ArithmeticOperator append(String operator, Object operand) {
        Expression rightOperand = operand instanceof Expression expression ? expression : Literals.of(operand);
        return this instanceof ArithmeticOperator arithmeticOperator ?
                arithmeticOperator.fluentAppend(operator, rightOperand) :
                ArithmeticOperator.of(this, operator, rightOperand);
    }
}
