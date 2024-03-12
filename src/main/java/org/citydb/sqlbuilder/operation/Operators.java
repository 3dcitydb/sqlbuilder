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
import org.citydb.sqlbuilder.literal.ScalarExpression;
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

    public static ScalarExpression concat(Expression leftOperand, Expression rightOperand) {
        return ArithmeticOperation.of(leftOperand, CONCAT, rightOperand);
    }

    public static BinaryComparisonOperation eq(ScalarExpression leftOperand, ScalarExpression rightOperand, boolean negate) {
        return BinaryComparisonOperation.of(leftOperand,
                !negate ? EQUAL_TO : NOT_EQUAL_TO,
                rightOperand);
    }

    public static BinaryComparisonOperation eq(ScalarExpression leftOperand, ScalarExpression rightOperand) {
        return eq(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperation lt(ScalarExpression leftOperand, ScalarExpression rightOperand, boolean negate) {
        return BinaryComparisonOperation.of(leftOperand,
                !negate ? LESS_THAN : GREATER_THAN_OR_EQUAL_TO,
                rightOperand);
    }

    public static BinaryComparisonOperation lt(ScalarExpression leftOperand, ScalarExpression rightOperand) {
        return lt(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperation le(ScalarExpression leftOperand, ScalarExpression rightOperand, boolean negate) {
        return BinaryComparisonOperation.of(leftOperand,
                !negate ? LESS_THAN_OR_EQUAL_TO : GREATER_THAN,
                rightOperand);
    }

    public static BinaryComparisonOperation le(ScalarExpression leftOperand, ScalarExpression rightOperand) {
        return le(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperation gt(ScalarExpression leftOperand, ScalarExpression rightOperand, boolean negate) {
        return BinaryComparisonOperation.of(leftOperand,
                !negate ? GREATER_THAN : LESS_THAN_OR_EQUAL_TO,
                rightOperand);
    }

    public static BinaryComparisonOperation gt(ScalarExpression leftOperand, ScalarExpression rightOperand) {
        return gt(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperation ge(ScalarExpression leftOperand, ScalarExpression rightOperand, boolean negate) {
        return BinaryComparisonOperation.of(leftOperand,
                !negate ? GREATER_THAN_OR_EQUAL_TO : LESS_THAN,
                rightOperand);
    }

    public static BinaryComparisonOperation ge(ScalarExpression leftOperand, ScalarExpression rightOperand) {
        return ge(leftOperand, rightOperand, false);
    }

    public static IsNull isNull(Expression operand, boolean negate) {
        return IsNull.of(operand, negate);
    }

    public static IsNull isNull(Expression operand) {
        return isNull(operand, false);
    }

    public static LogicalOperation exists(QueryExpression operand, boolean negate) {
        return Exists.of(operand, negate);
    }

    public static LogicalOperation exists(QueryExpression operand) {
        return exists(operand, false);
    }

    public static SubQueryOperator all(QueryExpression operand) {
        return SubQueryOperator.all(operand);
    }

    public static SubQueryOperator any(QueryExpression operand) {
        return SubQueryOperator.any(operand);
    }

    public static SubQueryOperator some(QueryExpression operand) {
        return SubQueryOperator.some(operand);
    }

    public static Like like(Expression operand, ScalarExpression pattern, StringLiteral escapeCharacter, boolean negate) {
        return Like.of(operand, pattern, escapeCharacter, negate);
    }

    public static Like like(Expression operand, ScalarExpression pattern, StringLiteral escapeCharacter) {
        return Like.of(operand, pattern, escapeCharacter);
    }

    public static Like like(Expression operand, ScalarExpression pattern) {
        return Like.of(operand, pattern);
    }

    public static Between between(ScalarExpression operand, ScalarExpression lowerBound, ScalarExpression upperBound, boolean negate) {
        return Between.of(operand, lowerBound, upperBound, negate);
    }

    public static Between between(ScalarExpression operand, ScalarExpression lowerBound, ScalarExpression upperBound) {
        return Between.of(operand, lowerBound, upperBound);
    }

    public static In in(Expression operand, List<ScalarExpression> values, boolean negate) {
        return In.of(operand, values, negate);
    }

    public static In in(Expression operand, List<ScalarExpression> values) {
        return In.of(operand, values);
    }

    public static In in(Expression operand, ScalarExpression... values) {
        return In.of(operand, values);
    }

    public static BinaryLogicalOperation and(List<BooleanExpression> operands) {
        return BinaryLogicalOperation.of(AND, operands);
    }

    public static BinaryLogicalOperation and(BooleanExpression... operands) {
        return BinaryLogicalOperation.of(AND, operands);
    }

    public static BinaryLogicalOperation or(List<BooleanExpression> operands) {
        return BinaryLogicalOperation.of(OR, operands);
    }

    public static BinaryLogicalOperation or(BooleanExpression... operands) {
        return BinaryLogicalOperation.of(OR, operands);
    }

    public static Not not(BooleanExpression operand) {
        return Not.of(operand);
    }
}
