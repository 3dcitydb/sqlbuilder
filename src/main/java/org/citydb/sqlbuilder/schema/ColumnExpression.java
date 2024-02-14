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
import org.citydb.sqlbuilder.function.Function;
import org.citydb.sqlbuilder.function.Functions;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.Literals;
import org.citydb.sqlbuilder.literal.StringLiteral;
import org.citydb.sqlbuilder.operation.*;
import org.citydb.sqlbuilder.query.LiteralList;
import org.citydb.sqlbuilder.query.QueryExpression;

public interface ColumnExpression extends Expression {

    default ArithmeticOperator plus(Object operand) {
        return Operators.plus(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ArithmeticOperator minus(Object operand) {
        return Operators.minus(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ArithmeticOperator multiplyBy(Object operand) {
        return Operators.multiplyBy(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ArithmeticOperator divideBy(Object operand) {
        return Operators.divideBy(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ArithmeticOperator modulo(Object operand) {
        return Operators.modulo(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ArithmeticOperator concat(Object operand) {
        return Operators.concat(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator eq(Object operand) {
        return Operators.eq(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator eqAll(Expression expression) {
        return Operators.eq(this, Operators.all(expression));
    }

    default ComparisonOperator eqAny(Expression expression) {
        return Operators.eq(this, Operators.any(expression));
    }

    default ComparisonOperator eqSome(Expression expression) {
        return Operators.eq(this, Operators.some(expression));
    }

    default ComparisonOperator ne(Object operand) {
        return Operators.eq(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator neAll(Expression expression) {
        return Operators.eq(this, Operators.all(expression), true);
    }

    default ComparisonOperator neAny(Expression expression) {
        return Operators.eq(this, Operators.any(expression), true);
    }

    default ComparisonOperator neSome(Expression expression) {
        return Operators.eq(this, Operators.some(expression), true);
    }

    default ComparisonOperator lt(Object operand) {
        return Operators.lt(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator ltAll(Expression expression) {
        return Operators.lt(this, Operators.all(expression));
    }

    default ComparisonOperator ltAny(Expression expression) {
        return Operators.lt(this, Operators.any(expression));
    }

    default ComparisonOperator ltSome(Expression expression) {
        return Operators.lt(this, Operators.some(expression));
    }

    default ComparisonOperator nl(Object operand) {
        return Operators.lt(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator nlAll(Expression expression) {
        return Operators.lt(this, Operators.all(expression), true);
    }

    default ComparisonOperator nlAny(Expression expression) {
        return Operators.lt(this, Operators.any(expression), true);
    }

    default ComparisonOperator nlSome(Expression expression) {
        return Operators.lt(this, Operators.some(expression), true);
    }

    default ComparisonOperator le(Object operand) {
        return Operators.le(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator leAll(Expression expression) {
        return Operators.le(this, Operators.all(expression));
    }

    default ComparisonOperator leAny(Expression expression) {
        return Operators.le(this, Operators.any(expression));
    }

    default ComparisonOperator leSome(Expression expression) {
        return Operators.le(this, Operators.some(expression));
    }

    default ComparisonOperator nle(Object operand) {
        return Operators.le(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator nleAll(Expression expression) {
        return Operators.le(this, Operators.all(expression), true);
    }

    default ComparisonOperator nleAny(Expression expression) {
        return Operators.le(this, Operators.any(expression), true);
    }

    default ComparisonOperator nleSome(Expression expression) {
        return Operators.le(this, Operators.some(expression), true);
    }

    default ComparisonOperator gt(Object operand) {
        return Operators.gt(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator gtAll(Expression expression) {
        return Operators.gt(this, Operators.all(expression));
    }

    default ComparisonOperator gtAny(Expression expression) {
        return Operators.gt(this, Operators.any(expression));
    }

    default ComparisonOperator gtSome(Expression expression) {
        return Operators.gt(this, Operators.some(expression));
    }

    default ComparisonOperator ng(Object operand) {
        return Operators.gt(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator ngAll(Expression expression) {
        return Operators.gt(this, Operators.all(expression), true);
    }

    default ComparisonOperator ngAny(Expression expression) {
        return Operators.gt(this, Operators.any(expression), true);
    }

    default ComparisonOperator ngSome(Expression expression) {
        return Operators.gt(this, Operators.some(expression), true);
    }

    default ComparisonOperator ge(Object operand) {
        return Operators.ge(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default ComparisonOperator geAll(Expression expression) {
        return Operators.ge(this, Operators.all(expression));
    }

    default ComparisonOperator geAny(Expression expression) {
        return Operators.ge(this, Operators.any(expression));
    }

    default ComparisonOperator geSome(Expression expression) {
        return Operators.ge(this, Operators.some(expression));
    }

    default ComparisonOperator nge(Object operand) {
        return Operators.ge(this, operand instanceof Expression expression ? expression : Literals.of(operand), true);
    }

    default ComparisonOperator ngeAll(Expression expression) {
        return Operators.ge(this, Operators.all(expression), true);
    }

    default ComparisonOperator ngeAny(Expression expression) {
        return Operators.ge(this, Operators.any(expression), true);
    }

    default ComparisonOperator ngeSome(Expression expression) {
        return Operators.ge(this, Operators.some(expression), true);
    }

    default IsNull isNull() {
        return Operators.isNull(this);
    }

    default IsNull isNotNull() {
        return Operators.isNull(this, true);
    }

    default Like like(Object operand) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literals.of(operand));
    }

    default Like like(Object operand, String escapeCharacter) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literals.of(operand),
                StringLiteral.of(escapeCharacter));
    }

    default Like notLike(Object operand) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literals.of(operand),
                null, true);
    }

    default Like notLike(Object operand, String escapeCharacter) {
        return Operators.like(this, operand instanceof Expression expression ? expression : Literals.of(operand),
                StringLiteral.of(escapeCharacter), true);
    }

    default Between between(Object lowerBound, Object upperBound) {
        return Operators.between(this,
                lowerBound instanceof Expression expression ? expression : Literals.of(lowerBound),
                upperBound instanceof Expression expression ? expression : Literals.of(upperBound));
    }

    default Between notBetween(Object lowerBound, Object upperBound) {
        return Operators.between(this,
                lowerBound instanceof Expression expression ? expression : Literals.of(lowerBound),
                upperBound instanceof Expression expression ? expression : Literals.of(upperBound), true);
    }

    default In in(Object... operands) {
        return Operators.in(this, LiteralList.of(operands));
    }

    default In in(QueryExpression expression) {
        return Operators.in(this, expression);
    }

    default Function avg() {
        return Functions.avg(this);
    }

    default Function count() {
        return Functions.count(this);
    }

    default Function max() {
        return Functions.max(this);
    }

    default Function min() {
        return Functions.min(this);
    }

    default Function sum() {
        return Functions.sum(this);
    }

    default Function length() {
        return Functions.length(this);
    }

    default Function upper() {
        return Functions.upper(this);
    }

    default Function lower() {
        return Functions.lower(this);
    }

    default Function trim() {
        return Functions.trim(this);
    }

    default Function concat(Expression argument) {
        return Functions.concat(this, argument);
    }

    default Function firstValue() {
        return Functions.firstValue(this);
    }

    default Function lastValue() {
        return Functions.lastValue(this);
    }

    default Function lag() {
        return Functions.lag(this);
    }

    default Function lag(Literal<?> offset) {
        return Functions.lag(this, offset);
    }

    default Function lag(Literal<?> offset, Expression defaultValue) {
        return Functions.lag(this, offset, defaultValue);
    }

    default Function lead() {
        return Functions.lead(this);
    }

    default Function lead(Literal<?> offset) {
        return Functions.lead(this, offset);
    }

    default Function lead(Literal<?> offset, Expression defaultValue) {
        return Functions.lead(this, offset, defaultValue);
    }

    default Function nthValue(Literal<?> offset) {
        return Functions.nthValue(this, offset);
    }
}
