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
import org.citydb.sqlbuilder.common.Expressions;
import org.citydb.sqlbuilder.operation.*;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.Collection;

public interface ScalarExpression extends Expression {

    default ScalarExpression castAs(String targetType) {
        return Operators.cast(this, targetType);
    }

    default ScalarExpression concat(Object operand) {
        return Operators.concat(this, Expressions.require(operand, ScalarExpression.class));
    }

    default BinaryComparisonOperation eq(Object operand) {
        return Operators.eq(this, Expressions.require(operand, ScalarExpression.class));
    }

    default BinaryComparisonOperation eqAll(QueryExpression expression) {
        return Operators.eq(this, Operators.all(expression));
    }

    default BinaryComparisonOperation eqAny(QueryExpression expression) {
        return Operators.eq(this, Operators.any(expression));
    }

    default BinaryComparisonOperation eqSome(QueryExpression expression) {
        return Operators.eq(this, Operators.some(expression));
    }

    default BinaryComparisonOperation ne(Object operand) {
        return Operators.eq(this, Expressions.require(operand, ScalarExpression.class), true);
    }

    default BinaryComparisonOperation neAll(QueryExpression expression) {
        return Operators.eq(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation neAny(QueryExpression expression) {
        return Operators.eq(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation neSome(QueryExpression expression) {
        return Operators.eq(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation lt(Object operand) {
        return Operators.lt(this, Expressions.require(operand, ScalarExpression.class));
    }

    default BinaryComparisonOperation ltAll(QueryExpression expression) {
        return Operators.lt(this, Operators.all(expression));
    }

    default BinaryComparisonOperation ltAny(QueryExpression expression) {
        return Operators.lt(this, Operators.any(expression));
    }

    default BinaryComparisonOperation ltSome(QueryExpression expression) {
        return Operators.lt(this, Operators.some(expression));
    }

    default BinaryComparisonOperation nl(Object operand) {
        return Operators.lt(this, Expressions.require(operand, ScalarExpression.class), true);
    }

    default BinaryComparisonOperation nlAll(QueryExpression expression) {
        return Operators.lt(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation nlAny(QueryExpression expression) {
        return Operators.lt(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation nlSome(QueryExpression expression) {
        return Operators.lt(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation le(Object operand) {
        return Operators.le(this, Expressions.require(operand, ScalarExpression.class));
    }

    default BinaryComparisonOperation leAll(QueryExpression expression) {
        return Operators.le(this, Operators.all(expression));
    }

    default BinaryComparisonOperation leAny(QueryExpression expression) {
        return Operators.le(this, Operators.any(expression));
    }

    default BinaryComparisonOperation leSome(QueryExpression expression) {
        return Operators.le(this, Operators.some(expression));
    }

    default BinaryComparisonOperation nle(Object operand) {
        return Operators.le(this, Expressions.require(operand, ScalarExpression.class), true);
    }

    default BinaryComparisonOperation nleAll(QueryExpression expression) {
        return Operators.le(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation nleAny(QueryExpression expression) {
        return Operators.le(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation nleSome(QueryExpression expression) {
        return Operators.le(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation gt(Object operand) {
        return Operators.gt(this, Expressions.require(operand, ScalarExpression.class));
    }

    default BinaryComparisonOperation gtAll(QueryExpression expression) {
        return Operators.gt(this, Operators.all(expression));
    }

    default BinaryComparisonOperation gtAny(QueryExpression expression) {
        return Operators.gt(this, Operators.any(expression));
    }

    default BinaryComparisonOperation gtSome(QueryExpression expression) {
        return Operators.gt(this, Operators.some(expression));
    }

    default BinaryComparisonOperation ng(Object operand) {
        return Operators.gt(this, Expressions.require(operand, ScalarExpression.class), true);
    }

    default BinaryComparisonOperation ngAll(QueryExpression expression) {
        return Operators.gt(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation ngAny(QueryExpression expression) {
        return Operators.gt(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation ngSome(QueryExpression expression) {
        return Operators.gt(this, Operators.some(expression), true);
    }

    default BinaryComparisonOperation ge(Object operand) {
        return Operators.ge(this, Expressions.require(operand, ScalarExpression.class));
    }

    default BinaryComparisonOperation geAll(QueryExpression expression) {
        return Operators.ge(this, Operators.all(expression));
    }

    default BinaryComparisonOperation geAny(QueryExpression expression) {
        return Operators.ge(this, Operators.any(expression));
    }

    default BinaryComparisonOperation geSome(QueryExpression expression) {
        return Operators.ge(this, Operators.some(expression));
    }

    default BinaryComparisonOperation nge(Object operand) {
        return Operators.ge(this, Expressions.require(operand, ScalarExpression.class), true);
    }

    default BinaryComparisonOperation ngeAll(QueryExpression expression) {
        return Operators.ge(this, Operators.all(expression), true);
    }

    default BinaryComparisonOperation ngeAny(QueryExpression expression) {
        return Operators.ge(this, Operators.any(expression), true);
    }

    default BinaryComparisonOperation ngeSome(QueryExpression expression) {
        return Operators.ge(this, Operators.some(expression), true);
    }

    default IsNull isNull() {
        return Operators.isNull(this);
    }

    default IsNull isNotNull() {
        return Operators.isNull(this, true);
    }

    default Like like(Object pattern) {
        return Operators.like(this, Expressions.require(pattern, ScalarExpression.class));
    }

    default Like like(Object pattern, String escapeCharacter) {
        return Operators.like(this, Expressions.require(pattern, ScalarExpression.class),
                StringLiteral.of(escapeCharacter));
    }

    default Like notLike(Object pattern) {
        return Operators.like(this, Expressions.require(pattern, ScalarExpression.class), null, true);
    }

    default Like notLike(Object pattern, String escapeCharacter) {
        return Operators.like(this, Expressions.require(pattern, ScalarExpression.class),
                StringLiteral.of(escapeCharacter), true);
    }

    default Between between(Object lowerBound, Object upperBound) {
        return Operators.between(this, Expressions.require(lowerBound, ScalarExpression.class),
                Expressions.require(upperBound, ScalarExpression.class));
    }

    default Between notBetween(Object lowerBound, Object upperBound) {
        return Operators.between(this, Expressions.require(lowerBound, ScalarExpression.class),
                Expressions.require(upperBound, ScalarExpression.class), true);
    }

    default In in(QueryExpression queryExpression) {
        return Operators.in(this, queryExpression);
    }

    default In in(Object... values) {
        return Operators.in(this, LiteralList.of(values));
    }

    default In in(Collection<?> values) {
        return Operators.in(this, LiteralList.of(values));
    }

    default In notIn(QueryExpression queryExpression) {
        return Operators.in(this, queryExpression, true);
    }

    default In notIn(Object... values) {
        return Operators.in(this, LiteralList.of(values), true);
    }

    default In notIn(Collection<?> values) {
        return Operators.in(this, LiteralList.of(values), true);
    }

    default Collate collate(String collation) {
        return Operators.collate(this, collation);
    }
}
