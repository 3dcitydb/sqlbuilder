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

import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.ScalarExpression;

public interface NumericExpression extends ScalarExpression {

    default ArithmeticOperation plus(Object operand) {
        return append(Operators.PLUS, operand);
    }

    default ArithmeticOperation minus(Object operand) {
        return append(Operators.MINUS, operand);
    }

    default ArithmeticOperation multiply(Object operand) {
        return append(Operators.MULTIPLY, operand);
    }

    default ArithmeticOperation divide(Object operand) {
        return append(Operators.DIVIDE, operand);
    }

    default ArithmeticOperation modulo(Object operand) {
        return append(Operators.MODULO, operand);
    }

    default ArithmeticOperation append(String operator, Object operand) {
        ScalarExpression rightOperand = Literal.ofScalar(operand);
        return this instanceof ArithmeticOperation operation ?
                operation.fluentAppend(operator, rightOperand) :
                ArithmeticOperation.of(this, operator, rightOperand);
    }
}
