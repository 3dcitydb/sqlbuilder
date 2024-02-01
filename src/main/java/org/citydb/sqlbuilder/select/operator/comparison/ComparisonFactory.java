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

package org.citydb.sqlbuilder.select.operator.comparison;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.StringLiteral;
import org.citydb.sqlbuilder.expression.SubQueryExpression;

public class ComparisonFactory {

    public static BinaryComparisonOperator equalTo(Expression leftOperand, Expression rightOperand, boolean negate) {
        return new BinaryComparisonOperator(leftOperand, !negate ? ComparisonName.EQUAL_TO : ComparisonName.NOT_EQUAL_TO, rightOperand);
    }

    public static BinaryComparisonOperator equalTo(Expression leftOperand, Expression rightOperand) {
        return equalTo(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperator notEqualTo(Expression leftOperand, Expression rightOperand, boolean negate) {
        return new BinaryComparisonOperator(leftOperand, !negate ? ComparisonName.NOT_EQUAL_TO : ComparisonName.EQUAL_TO, rightOperand);
    }

    public static BinaryComparisonOperator notEqualTo(Expression leftOperand, Expression rightOperand) {
        return notEqualTo(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperator lessThan(Expression leftOperand, Expression rightOperand, boolean negate) {
        return new BinaryComparisonOperator(leftOperand, !negate ? ComparisonName.LESS_THAN : ComparisonName.GREATER_THAN_OR_EQUAL_TO, rightOperand);
    }

    public static BinaryComparisonOperator lessThan(Expression leftOperand, Expression rightOperand) {
        return lessThan(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperator lessThanOrEqualTo(Expression leftOperand, Expression rightOperand, boolean negate) {
        return new BinaryComparisonOperator(leftOperand, !negate ? ComparisonName.LESS_THAN_OR_EQUAL_TO : ComparisonName.GREATER_THAN, rightOperand);
    }

    public static BinaryComparisonOperator lessThanOrEqualTo(Expression leftOperand, Expression rightOperand) {
        return lessThanOrEqualTo(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperator greaterThan(Expression leftOperand, Expression rightOperand, boolean negate) {
        return new BinaryComparisonOperator(leftOperand, !negate ? ComparisonName.GREATER_THAN : ComparisonName.LESS_THAN_OR_EQUAL_TO, rightOperand);
    }

    public static BinaryComparisonOperator greaterThan(Expression leftOperand, Expression rightOperand) {
        return greaterThan(leftOperand, rightOperand, false);
    }

    public static BinaryComparisonOperator greaterThanOrEqualTo(Expression leftOperand, Expression rightOperand, boolean negate) {
        return new BinaryComparisonOperator(leftOperand, !negate ? ComparisonName.GREATER_THAN_OR_EQUAL_TO : ComparisonName.LESS_THAN, rightOperand);
    }

    public static BinaryComparisonOperator greaterThanOrEqualTo(Expression leftOperand, Expression rightOperand) {
        return greaterThanOrEqualTo(leftOperand, rightOperand, false);
    }

    public static UnaryComparisonOperator isNull(Expression operand, boolean negate) {
        return new UnaryComparisonOperator(operand, !negate ? ComparisonName.IS_NULL : ComparisonName.IS_NOT_NULL);
    }

    public static UnaryComparisonOperator isNull(Expression operand) {
        return isNull(operand, false);
    }

    public static UnaryComparisonOperator isNotNull(Expression operand, boolean negate) {
        return new UnaryComparisonOperator(operand, !negate ? ComparisonName.IS_NOT_NULL : ComparisonName.IS_NULL);
    }

    public static UnaryComparisonOperator isNotNull(Expression operand) {
        return isNotNull(operand, false);
    }

    public static UnaryComparisonOperator exists(Expression operand, boolean negate) {
        return new UnaryComparisonOperator(operand, !negate ? ComparisonName.EXISTS : ComparisonName.NOT_EXISTS);
    }

    public static UnaryComparisonOperator exists(Expression operand) {
        return exists(operand, false);
    }

    public static UnaryComparisonOperator notExists(Expression operand, boolean negate) {
        return new UnaryComparisonOperator(operand, !negate ? ComparisonName.NOT_EXISTS : ComparisonName.EXISTS);
    }

    public static UnaryComparisonOperator notExists(Expression operand) {
        return notExists(operand, false);
    }

    public static LikeOperator like(Expression operand, Expression pattern, StringLiteral escapeCharacter, boolean negate) {
        return new LikeOperator(operand, pattern, escapeCharacter, negate);
    }

    public static LikeOperator like(Expression operand, Expression pattern, StringLiteral escapeCharacter) {
        return new LikeOperator(operand, pattern, escapeCharacter);
    }

    public static LikeOperator like(Expression operand, Expression pattern) {
        return new LikeOperator(operand, pattern);
    }

    public static LikeOperator notLike(Expression operand, Expression pattern, StringLiteral escapeCharacter, boolean negate) {
        return new LikeOperator(operand, pattern, escapeCharacter, !negate);
    }

    public static LikeOperator notLike(Expression operand, Expression pattern, StringLiteral escapeCharacter) {
        return new LikeOperator(operand, pattern, escapeCharacter, true);
    }

    public static LikeOperator notLike(Expression operand, Expression pattern) {
        return new LikeOperator(operand, pattern, true);
    }

    public static BetweenOperator between(Expression operand, Expression lowerBound, Expression upperBound, boolean negate) {
        return new BetweenOperator(operand, lowerBound, upperBound, negate);
    }

    public static BetweenOperator between(Expression operand, Expression lowerBound, Expression upperBound) {
        return new BetweenOperator(operand, lowerBound, upperBound);
    }

    public static BetweenOperator notBetween(Expression operand, Expression lowerBound, Expression upperBound, boolean negate) {
        return new BetweenOperator(operand, lowerBound, upperBound, !negate);
    }

    public static BetweenOperator notBetween(Expression operand, Expression lowerBound, Expression upperBound) {
        return new BetweenOperator(operand, lowerBound, upperBound, true);
    }

    public static InOperator in(Expression operand, SubQueryExpression subQueryExpression, boolean negate) {
        return new InOperator(operand, subQueryExpression, negate);
    }

    public static InOperator in(Expression operand, SubQueryExpression subQueryExpression) {
        return new InOperator(operand, subQueryExpression);
    }

    public static InOperator notIn(Expression operand, SubQueryExpression subQueryExpression, boolean negate) {
        return new InOperator(operand, subQueryExpression, !negate);
    }

    public static InOperator notIn(Expression operand, SubQueryExpression subQueryExpression) {
        return new InOperator(operand, subQueryExpression, true);
    }
}
