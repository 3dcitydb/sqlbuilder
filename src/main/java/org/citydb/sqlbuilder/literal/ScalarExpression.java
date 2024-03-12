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

package org.citydb.sqlbuilder.literal;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.operation.*;

import java.util.List;

public interface ScalarExpression extends Expression {

    default ScalarExpression concat(Object operand) {
        return Operators.concat(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default BinaryComparisonOperation eq(Object operand) {
        return Operators.eq(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default BinaryComparisonOperation eqAll(Expression expression) {
        return Operators.eq(this, Operators.all(expression));
    }

    default BinaryComparisonOperation eqAny(Expression expression) {
        return Operators.eq(this, Operators.any(expression));
    }

    default BinaryComparisonOperation eqSome(Expression expression) {
        return Operators.eq(this, Operators.some(expression));
    }

    default BinaryComparisonOperation ne(Object operand) {
        return Operators.eq(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default BinaryComparisonOperation neAll(Expression expression) {
        return Operators.eq(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation neAny(Expression expression) {
        return Operators.eq(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation neSome(Expression expression) {
        return Operators.eq(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation lt(Object operand) {
        return Operators.lt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default BinaryComparisonOperation ltAll(Expression expression) {
        return Operators.lt(this, Operators.all(expression));
    }

    default BinaryComparisonOperation ltAny(Expression expression) {
        return Operators.lt(this, Operators.any(expression));
    }

    default BinaryComparisonOperation ltSome(Expression expression) {
        return Operators.lt(this, Operators.some(expression));
    }

    default BinaryComparisonOperation nl(Object operand) {
        return Operators.lt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default BinaryComparisonOperation nlAll(Expression expression) {
        return Operators.lt(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation nlAny(Expression expression) {
        return Operators.lt(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation nlSome(Expression expression) {
        return Operators.lt(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation le(Object operand) {
        return Operators.le(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default BinaryComparisonOperation leAll(Expression expression) {
        return Operators.le(this, Operators.all(expression));
    }

    default BinaryComparisonOperation leAny(Expression expression) {
        return Operators.le(this, Operators.any(expression));
    }

    default BinaryComparisonOperation leSome(Expression expression) {
        return Operators.le(this, Operators.some(expression));
    }

    default BinaryComparisonOperation nle(Object operand) {
        return Operators.le(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default BinaryComparisonOperation nleAll(Expression expression) {
        return Operators.le(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation nleAny(Expression expression) {
        return Operators.le(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation nleSome(Expression expression) {
        return Operators.le(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation gt(Object operand) {
        return Operators.gt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default BinaryComparisonOperation gtAll(Expression expression) {
        return Operators.gt(this, Operators.all(expression));
    }

    default BinaryComparisonOperation gtAny(Expression expression) {
        return Operators.gt(this, Operators.any(expression));
    }

    default BinaryComparisonOperation gtSome(Expression expression) {
        return Operators.gt(this, Operators.some(expression));
    }

    default BinaryComparisonOperation ng(Object operand) {
        return Operators.gt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default BinaryComparisonOperation ngAll(Expression expression) {
        return Operators.gt(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation ngAny(Expression expression) {
        return Operators.gt(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation ngSome(Expression expression) {
        return Operators.gt(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation ge(Object operand) {
        return Operators.ge(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default BinaryComparisonOperation geAll(Expression expression) {
        return Operators.ge(this, Operators.all(expression));
    }

    default BinaryComparisonOperation geAny(Expression expression) {
        return Operators.ge(this, Operators.any(expression));
    }

    default BinaryComparisonOperation geSome(Expression expression) {
        return Operators.ge(this, Operators.some(expression));
    }

    default BinaryComparisonOperation nge(Object operand) {
        return Operators.ge(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default BinaryComparisonOperation ngeAll(Expression expression) {
        return Operators.ge(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation ngeAny(Expression expression) {
        return Operators.ge(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation ngeSome(Expression expression) {
        return Operators.ge(this, Operators.some(expression), true);
    }

    default IsNull isNull() {
        return Operators.isNull(this);
    }

    default IsNull isNotNull() {
        return Operators.isNull(this, true);
    }

    default Like like(Object operand) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default Like like(Object operand, String escapeCharacter) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand),
                StringLiteral.of(escapeCharacter));
    }

    default Like notLike(Object operand) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand),
                null, true);
    }

    default Like notLike(Object operand, String escapeCharacter) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand),
                StringLiteral.of(escapeCharacter), true);
    }

    default Between between(Object lowerBound, Object upperBound) {
        return Operators.between(this,
                lowerBound instanceof Expression expression ? expression : Literal.ofScalar(lowerBound),
                upperBound instanceof Expression expression ? expression : Literal.ofScalar(upperBound));
    }

    default Between notBetween(Object lowerBound, Object upperBound) {
        return Operators.between(this,
                lowerBound instanceof Expression expression ? expression : Literal.ofScalar(lowerBound),
                upperBound instanceof Expression expression ? expression : Literal.ofScalar(upperBound), true);
    }

    default In in(Object... values) {
        return Operators.in(this, Literal.ofScalarList(values));
    }

    default In in(List<ScalarExpression> values) {
        return Operators.in(this, values);
    }
}
