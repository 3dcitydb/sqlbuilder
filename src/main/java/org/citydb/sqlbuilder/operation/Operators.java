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
import org.citydb.sqlbuilder.literal.StringLiteral;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.List;

public class Operators {
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    public static final String MODULO = "%";
    public static final String CONCAT = "||";
    public static final String EQUAL_TO = "=";
    public static final String NOT_EQUAL_TO = "<>";
    public static final String LESS_THAN = "<";
    public static final String GREATER_THAN = ">";
    public static final String LESS_THAN_OR_EQUAL_TO = "<=";
    public static final String GREATER_THAN_OR_EQUAL_TO = ">=";
    public static final String AND = "and";
    public static final String OR = "or";
    public static final String NOT = "not";
    public static final String LIKE = "like";
    public static final String NOT_LIKE = "not like";
    public static final String BETWEEN = "between";
    public static final String NOT_BETWEEN = "not between";
    public static final String IN = "in";
    public static final String NOT_IN = "not in";
    public static final String IS_NULL = "is null";
    public static final String IS_NOT_NULL = "is not null";
    public static final String EXISTS = "exists";
    public static final String NOT_EXISTS = "not exists";
    public static final String ALL = "all";
    public static final String ANY = "any";
    public static final String SOME = "some";

    public static ArithmeticOperation plus(Expression leftOperand, Expression rightOperand) {
        return ArithmeticOperation.of(leftOperand, PLUS, rightOperand);
    }

    public static ArithmeticOperation minus(Expression leftOperand, Expression rightOperand) {
        return ArithmeticOperation.of(leftOperand, MINUS, rightOperand);
    }

    public static ArithmeticOperation multiplyBy(Expression leftOperand, Expression rightOperand) {
        return ArithmeticOperation.of(leftOperand, MULTIPLY, rightOperand);
    }

    public static ArithmeticOperation divideBy(Expression leftOperand, Expression rightOperand) {
        return ArithmeticOperation.of(leftOperand, DIVIDE, rightOperand);
    }

    public static ArithmeticOperation modulo(Expression leftOperand, Expression rightOperand) {
        return ArithmeticOperation.of(leftOperand, MODULO, rightOperand);
    }

    public static ComparisonOperation eq(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperation.of(leftOperand,
                !negate ? EQUAL_TO : NOT_EQUAL_TO,
                rightOperand);
    }

    public static ComparisonOperation eq(Expression leftOperand, Expression rightOperand) {
        return eq(leftOperand, rightOperand, false);
    }

    public static ComparisonOperation lt(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperation.of(leftOperand,
                !negate ? LESS_THAN : GREATER_THAN_OR_EQUAL_TO,
                rightOperand);
    }

    public static ComparisonOperation lt(Expression leftOperand, Expression rightOperand) {
        return lt(leftOperand, rightOperand, false);
    }

    public static ComparisonOperation le(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperation.of(leftOperand,
                !negate ? LESS_THAN_OR_EQUAL_TO : GREATER_THAN,
                rightOperand);
    }

    public static ComparisonOperation le(Expression leftOperand, Expression rightOperand) {
        return le(leftOperand, rightOperand, false);
    }

    public static ComparisonOperation gt(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperation.of(leftOperand,
                !negate ? GREATER_THAN : LESS_THAN_OR_EQUAL_TO,
                rightOperand);
    }

    public static ComparisonOperation gt(Expression leftOperand, Expression rightOperand) {
        return gt(leftOperand, rightOperand, false);
    }

    public static ComparisonOperation ge(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperation.of(leftOperand,
                !negate ? GREATER_THAN_OR_EQUAL_TO : LESS_THAN,
                rightOperand);
    }

    public static ComparisonOperation ge(Expression leftOperand, Expression rightOperand) {
        return ge(leftOperand, rightOperand, false);
    }

    public static IsNull isNull(Expression operand, boolean negate) {
        return IsNull.of(operand, negate);
    }

    public static IsNull isNull(Expression operand) {
        return isNull(operand, false);
    }

    public static UnaryLogicalOperation exists(Expression operand, boolean negate) {
        return UnaryLogicalOperation.of(operand,
                !negate ? EXISTS : NOT_EXISTS);
    }

    public static UnaryLogicalOperation exists(Expression operand) {
        return exists(operand, false);
    }

    public static UnaryLogicalOperation all(Expression operand) {
        return UnaryLogicalOperation.of(operand, ALL);
    }

    public static UnaryLogicalOperation any(Expression operand) {
        return UnaryLogicalOperation.of(operand, ANY);
    }

    public static UnaryLogicalOperation some(Expression operand) {
        return UnaryLogicalOperation.of(operand, SOME);
    }

    public static Like like(Expression operand, Expression pattern, StringLiteral escapeCharacter, boolean negate) {
        return Like.of(operand, pattern, escapeCharacter, negate);
    }

    public static Like like(Expression operand, Expression pattern, StringLiteral escapeCharacter) {
        return Like.of(operand, pattern, escapeCharacter);
    }

    public static Like like(Expression operand, Expression pattern) {
        return Like.of(operand, pattern);
    }

    public static Between between(Expression operand, Expression lowerBound, Expression upperBound, boolean negate) {
        return Between.of(operand, lowerBound, upperBound, negate);
    }

    public static Between between(Expression operand, Expression lowerBound, Expression upperBound) {
        return Between.of(operand, lowerBound, upperBound);
    }

    public static In in(Expression operand, QueryExpression queryExpression, boolean negate) {
        return In.of(operand, queryExpression, negate);
    }

    public static In in(Expression operand, QueryExpression queryExpression) {
        return In.of(operand, queryExpression);
    }

    public static BinaryLogicalOperation and(List<LogicalExpression> operands) {
        return BinaryLogicalOperation.of(AND, operands);
    }

    public static BinaryLogicalOperation and(LogicalExpression... operands) {
        return BinaryLogicalOperation.of(AND, operands);
    }

    public static BinaryLogicalOperation or(List<LogicalExpression> operands) {
        return BinaryLogicalOperation.of(OR, operands);
    }

    public static BinaryLogicalOperation or(LogicalExpression... operands) {
        return BinaryLogicalOperation.of(OR, operands);
    }

    public static Not not(LogicalExpression operand) {
        return Not.of(operand);
    }
}
