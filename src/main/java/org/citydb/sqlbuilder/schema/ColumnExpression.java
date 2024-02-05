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

package org.citydb.sqlbuilder.schema;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.Literals;
import org.citydb.sqlbuilder.literal.StringLiteral;
import org.citydb.sqlbuilder.predicate.Predicates;
import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperator;
import org.citydb.sqlbuilder.predicate.logical.Between;
import org.citydb.sqlbuilder.predicate.logical.In;
import org.citydb.sqlbuilder.predicate.logical.Like;
import org.citydb.sqlbuilder.predicate.logical.UnaryLogicalOperator;
import org.citydb.sqlbuilder.query.LiteralList;
import org.citydb.sqlbuilder.query.QueryExpression;

public interface ColumnExpression extends Expression {
    default ComparisonOperator eq(Object operand) {
        return Predicates.eq(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator eqAll(QueryExpression expression) {
        return Predicates.eq(this, Predicates.all(expression));
    }

    default ComparisonOperator eqAny(QueryExpression expression) {
        return Predicates.eq(this, Predicates.any(expression));
    }

    default ComparisonOperator eqSome(QueryExpression expression) {
        return Predicates.eq(this, Predicates.some(expression));
    }

    default ComparisonOperator ne(Object operand) {
        return Predicates.eq(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator neAll(QueryExpression expression) {
        return Predicates.eq(this, Predicates.all(expression), true);
    }

    default ComparisonOperator neAny(QueryExpression expression) {
        return Predicates.eq(this, Predicates.any(expression), true);
    }

    default ComparisonOperator neSome(QueryExpression expression) {
        return Predicates.eq(this, Predicates.some(expression), true);
    }

    default ComparisonOperator lt(Object operand) {
        return Predicates.lt(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator ltAll(QueryExpression expression) {
        return Predicates.lt(this, Predicates.all(expression));
    }

    default ComparisonOperator ltAny(QueryExpression expression) {
        return Predicates.lt(this, Predicates.any(expression));
    }

    default ComparisonOperator ltSome(QueryExpression expression) {
        return Predicates.lt(this, Predicates.some(expression));
    }

    default ComparisonOperator nl(Object operand) {
        return Predicates.lt(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator nlAll(QueryExpression expression) {
        return Predicates.lt(this, Predicates.all(expression), true);
    }

    default ComparisonOperator nlAny(QueryExpression expression) {
        return Predicates.lt(this, Predicates.any(expression), true);
    }

    default ComparisonOperator nlSome(QueryExpression expression) {
        return Predicates.lt(this, Predicates.some(expression), true);
    }

    default ComparisonOperator le(Object operand) {
        return Predicates.le(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator leAll(QueryExpression expression) {
        return Predicates.le(this, Predicates.all(expression));
    }

    default ComparisonOperator leAny(QueryExpression expression) {
        return Predicates.le(this, Predicates.any(expression));
    }

    default ComparisonOperator leSome(QueryExpression expression) {
        return Predicates.le(this, Predicates.some(expression));
    }

    default ComparisonOperator nle(Object operand) {
        return Predicates.le(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator nleAll(QueryExpression expression) {
        return Predicates.le(this, Predicates.all(expression), true);
    }

    default ComparisonOperator nleAny(QueryExpression expression) {
        return Predicates.le(this, Predicates.any(expression), true);
    }

    default ComparisonOperator nleSome(QueryExpression expression) {
        return Predicates.le(this, Predicates.some(expression), true);
    }

    default ComparisonOperator gt(Object operand) {
        return Predicates.gt(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator gtAll(QueryExpression expression) {
        return Predicates.gt(this, Predicates.all(expression));
    }

    default ComparisonOperator gtAny(QueryExpression expression) {
        return Predicates.gt(this, Predicates.any(expression));
    }

    default ComparisonOperator gtSome(QueryExpression expression) {
        return Predicates.gt(this, Predicates.some(expression));
    }

    default ComparisonOperator ng(Object operand) {
        return Predicates.gt(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator ngAll(QueryExpression expression) {
        return Predicates.gt(this, Predicates.all(expression), true);
    }

    default ComparisonOperator ngAny(QueryExpression expression) {
        return Predicates.gt(this, Predicates.any(expression), true);
    }

    default ComparisonOperator ngSome(QueryExpression expression) {
        return Predicates.gt(this, Predicates.some(expression), true);
    }

    default ComparisonOperator ge(Object operand) {
        return Predicates.ge(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator geAll(QueryExpression expression) {
        return Predicates.ge(this, Predicates.all(expression));
    }

    default ComparisonOperator geAny(QueryExpression expression) {
        return Predicates.ge(this, Predicates.any(expression));
    }

    default ComparisonOperator geSome(QueryExpression expression) {
        return Predicates.ge(this, Predicates.some(expression));
    }

    default ComparisonOperator nge(Object operand) {
        return Predicates.ge(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator ngeAll(QueryExpression expression) {
        return Predicates.ge(this, Predicates.all(expression), true);
    }

    default ComparisonOperator ngeAny(QueryExpression expression) {
        return Predicates.ge(this, Predicates.any(expression), true);
    }

    default ComparisonOperator ngeSome(QueryExpression expression) {
        return Predicates.ge(this, Predicates.some(expression), true);
    }

    default UnaryLogicalOperator isNull() {
        return Predicates.isNull(this);
    }

    default UnaryLogicalOperator isNotNull() {
        return Predicates.isNull(this, true);
    }

    default Like like(Object operand) {
        return Predicates.like(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default Like like(Object operand, String escapeCharacter) {
        return Predicates.like(this, operand instanceof Expression expression ? expression : Literals.of(operand),
                StringLiteral.of(escapeCharacter));
    }

    default Like notLike(Object operand) {
        return Predicates.like(this, operand instanceof Expression expression ? expression : Literals.of(operand),
                null, true);
    }

    default Like notLike(Object operand, String escapeCharacter) {
        return Predicates.like(this, operand instanceof Expression expression ? expression : Literals.of(operand),
                StringLiteral.of(escapeCharacter), true);
    }

    default Between between(Object lowerBound, Object upperBound) {
        return Predicates.between(this,
                lowerBound instanceof Expression expression ? expression : Literals.of(lowerBound),
                upperBound instanceof Expression expression ? expression : Literals.of(upperBound));
    }

    default Between notBetween(Object lowerBound, Object upperBound) {
        return Predicates.between(this,
                lowerBound instanceof Expression expression ? expression : Literals.of(lowerBound),
                upperBound instanceof Expression expression ? expression : Literals.of(upperBound), true);
    }

    default In in(Object... operands) {
        return Predicates.in(this, LiteralList.of(operands));
    }

    default In in(QueryExpression expression) {
        return Predicates.in(this, expression);
    }
}
