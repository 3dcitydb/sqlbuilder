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
import org.citydb.sqlbuilder.query.Selection;

public interface LogicalExpression extends Operation, Selection<LogicalExpression> {

    default BinaryLogicalOperator and(LogicalExpression operand) {
        return this instanceof BinaryLogicalOperator operator ?
                operator.fluentAnd(operand) :
                Operators.and(this, operand);
    }

    default BinaryLogicalOperator andExists(Expression operand) {
        return and(Operators.exists(operand));
    }

    default BinaryLogicalOperator andNot(LogicalExpression operand) {
        return and(Operators.not(operand));
    }

    default BinaryLogicalOperator andNotExists(Expression operand) {
        return and(Operators.not(Operators.exists(operand)));
    }

    default BinaryLogicalOperator or(LogicalExpression operand) {
        return this instanceof BinaryLogicalOperator operator ?
                operator.fluentOr(operand) :
                Operators.or(this, operand);
    }

    default BinaryLogicalOperator orExists(Expression operand) {
        return or(Operators.exists(operand));
    }

    default BinaryLogicalOperator orNot(LogicalExpression operand) {
        return or(Operators.not(operand));
    }

    default BinaryLogicalOperator orNotExists(Expression operand) {
        return or(Operators.not(Operators.exists(operand)));
    }

    default Not not() {
        return Operators.not(this);
    }
}
