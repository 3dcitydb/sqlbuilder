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

package org.citydb.sqlbuilder.predicate;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.StringLiteral;
import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperator;
import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperatorType;
import org.citydb.sqlbuilder.predicate.logical.*;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.List;

public class Predicates {

    public static ComparisonOperator eq(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperator.of(leftOperand,
                !negate ? ComparisonOperatorType.EQUAL_TO : ComparisonOperatorType.NOT_EQUAL_TO,
                rightOperand);
    }

    public static ComparisonOperator eq(Expression leftOperand, Expression rightOperand) {
        return eq(leftOperand, rightOperand, false);
    }

    public static ComparisonOperator lt(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperator.of(leftOperand,
                !negate ? ComparisonOperatorType.LESS_THAN : ComparisonOperatorType.GREATER_THAN_OR_EQUAL_TO,
                rightOperand);
    }

    public static ComparisonOperator lt(Expression leftOperand, Expression rightOperand) {
        return lt(leftOperand, rightOperand, false);
    }

    public static ComparisonOperator le(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperator.of(leftOperand,
                !negate ? ComparisonOperatorType.LESS_THAN_OR_EQUAL_TO : ComparisonOperatorType.GREATER_THAN,
                rightOperand);
    }

    public static ComparisonOperator le(Expression leftOperand, Expression rightOperand) {
        return le(leftOperand, rightOperand, false);
    }

    public static ComparisonOperator gt(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperator.of(leftOperand,
                !negate ? ComparisonOperatorType.GREATER_THAN : ComparisonOperatorType.LESS_THAN_OR_EQUAL_TO,
                rightOperand);
    }

    public static ComparisonOperator gt(Expression leftOperand, Expression rightOperand) {
        return gt(leftOperand, rightOperand, false);
    }

    public static ComparisonOperator ge(Expression leftOperand, Expression rightOperand, boolean negate) {
        return ComparisonOperator.of(leftOperand,
                !negate ? ComparisonOperatorType.GREATER_THAN_OR_EQUAL_TO : ComparisonOperatorType.LESS_THAN,
                rightOperand);
    }

    public static ComparisonOperator ge(Expression leftOperand, Expression rightOperand) {
        return ge(leftOperand, rightOperand, false);
    }

    public static UnaryLogicalOperator isNull(Expression operand, boolean negate) {
        return UnaryLogicalOperator.of(operand,
                !negate ? LogicalOperatorType.IS_NULL : LogicalOperatorType.IS_NOT_NULL);
    }

    public static UnaryLogicalOperator isNull(Expression operand) {
        return isNull(operand, false);
    }

    public static UnaryLogicalOperator exists(Expression operand, boolean negate) {
        return UnaryLogicalOperator.of(operand,
                !negate ? LogicalOperatorType.EXISTS : LogicalOperatorType.NOT_EXISTS);
    }

    public static UnaryLogicalOperator exists(Expression operand) {
        return exists(operand, false);
    }

    public static UnaryLogicalOperator all(Expression operand) {
        return UnaryLogicalOperator.of(operand, LogicalOperatorType.ALL);
    }

    public static UnaryLogicalOperator any(Expression operand) {
        return UnaryLogicalOperator.of(operand, LogicalOperatorType.ANY);
    }

    public static UnaryLogicalOperator some(Expression operand) {
        return UnaryLogicalOperator.of(operand, LogicalOperatorType.SOME);
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

    public static BinaryLogicalOperator and(List<Predicate> operands) {
        return BinaryLogicalOperator.of(LogicalOperatorType.AND, operands);
    }

    public static BinaryLogicalOperator and(Predicate... operands) {
        return BinaryLogicalOperator.of(LogicalOperatorType.AND, operands);
    }

    public static BinaryLogicalOperator or(List<Predicate> operands) {
        return BinaryLogicalOperator.of(LogicalOperatorType.OR, operands);
    }

    public static BinaryLogicalOperator or(Predicate... operands) {
        return BinaryLogicalOperator.of(LogicalOperatorType.OR, operands);
    }

    public static Not not(Predicate operand) {
        return Not.of(operand);
    }
}
