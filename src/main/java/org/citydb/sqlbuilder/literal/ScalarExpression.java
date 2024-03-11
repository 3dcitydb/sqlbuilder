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

    default ComparisonOperation eq(Object operand) {
        return Operators.eq(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default ComparisonOperation eqAll(Expression expression) {
        return Operators.eq(this, Operators.all(expression));
    }

    default ComparisonOperation eqAny(Expression expression) {
        return Operators.eq(this, Operators.any(expression));
    }

    default ComparisonOperation eqSome(Expression expression) {
        return Operators.eq(this, Operators.some(expression));
    }

    default ComparisonOperation ne(Object operand) {
        return Operators.eq(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default ComparisonOperation neAll(Expression expression) {
        return Operators.eq(this, Operators.all(expression), true);
    }

    default ComparisonOperation neAny(Expression expression) {
        return Operators.eq(this, Operators.any(expression), true);
    }

    default ComparisonOperation neSome(Expression expression) {
        return Operators.eq(this, Operators.some(expression), true);
    }

    default ComparisonOperation lt(Object operand) {
        return Operators.lt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default ComparisonOperation ltAll(Expression expression) {
        return Operators.lt(this, Operators.all(expression));
    }

    default ComparisonOperation ltAny(Expression expression) {
        return Operators.lt(this, Operators.any(expression));
    }

    default ComparisonOperation ltSome(Expression expression) {
        return Operators.lt(this, Operators.some(expression));
    }

    default ComparisonOperation nl(Object operand) {
        return Operators.lt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default ComparisonOperation nlAll(Expression expression) {
        return Operators.lt(this, Operators.all(expression), true);
    }

    default ComparisonOperation nlAny(Expression expression) {
        return Operators.lt(this, Operators.any(expression), true);
    }

    default ComparisonOperation nlSome(Expression expression) {
        return Operators.lt(this, Operators.some(expression), true);
    }

    default ComparisonOperation le(Object operand) {
        return Operators.le(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default ComparisonOperation leAll(Expression expression) {
        return Operators.le(this, Operators.all(expression));
    }

    default ComparisonOperation leAny(Expression expression) {
        return Operators.le(this, Operators.any(expression));
    }

    default ComparisonOperation leSome(Expression expression) {
        return Operators.le(this, Operators.some(expression));
    }

    default ComparisonOperation nle(Object operand) {
        return Operators.le(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default ComparisonOperation nleAll(Expression expression) {
        return Operators.le(this, Operators.all(expression), true);
    }

    default ComparisonOperation nleAny(Expression expression) {
        return Operators.le(this, Operators.any(expression), true);
    }

    default ComparisonOperation nleSome(Expression expression) {
        return Operators.le(this, Operators.some(expression), true);
    }

    default ComparisonOperation gt(Object operand) {
        return Operators.gt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default ComparisonOperation gtAll(Expression expression) {
        return Operators.gt(this, Operators.all(expression));
    }

    default ComparisonOperation gtAny(Expression expression) {
        return Operators.gt(this, Operators.any(expression));
    }

    default ComparisonOperation gtSome(Expression expression) {
        return Operators.gt(this, Operators.some(expression));
    }

    default ComparisonOperation ng(Object operand) {
        return Operators.gt(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default ComparisonOperation ngAll(Expression expression) {
        return Operators.gt(this, Operators.all(expression), true);
    }

    default ComparisonOperation ngAny(Expression expression) {
        return Operators.gt(this, Operators.any(expression), true);
    }

    default ComparisonOperation ngSome(Expression expression) {
        return Operators.gt(this, Operators.some(expression), true);
    }

    default ComparisonOperation ge(Object operand) {
        return Operators.ge(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand));
    }

    default ComparisonOperation geAll(Expression expression) {
        return Operators.ge(this, Operators.all(expression));
    }

    default ComparisonOperation geAny(Expression expression) {
        return Operators.ge(this, Operators.any(expression));
    }

    default ComparisonOperation geSome(Expression expression) {
        return Operators.ge(this, Operators.some(expression));
    }

    default ComparisonOperation nge(Object operand) {
        return Operators.ge(this, operand instanceof Expression expression ? expression : Literal.ofScalar(operand), true);
    }

    default ComparisonOperation ngeAll(Expression expression) {
        return Operators.ge(this, Operators.all(expression), true);
    }

    default ComparisonOperation ngeAny(Expression expression) {
        return Operators.ge(this, Operators.any(expression), true);
    }

    default ComparisonOperation ngeSome(Expression expression) {
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
