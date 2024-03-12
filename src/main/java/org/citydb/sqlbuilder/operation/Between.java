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

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.literal.ScalarExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Between implements ComparisonOperation {
    private final ScalarExpression operand;
    private final ScalarExpression lowerBound;
    private final ScalarExpression upperBound;
    private boolean negate;
    private String alias;

    private Between(ScalarExpression operand, ScalarExpression lowerBound, ScalarExpression upperBound, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.lowerBound = Objects.requireNonNull(lowerBound, "The lower bound must not be null.");
        this.upperBound = Objects.requireNonNull(upperBound, "The upper bound must not be null.");
        this.negate = negate;
    }

    public static Between of(ScalarExpression operand, ScalarExpression lowerBound, ScalarExpression upperBound, boolean negate) {
        return new Between(operand, lowerBound, upperBound, negate);
    }

    public static Between of(ScalarExpression operand, ScalarExpression lowerBound, ScalarExpression upperBound) {
        return new Between(operand, lowerBound, upperBound, false);
    }

    public Expression getOperand() {
        return operand;
    }

    public Expression getLowerBound() {
        return lowerBound;
    }

    public Expression getUpperBound() {
        return upperBound;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public String getOperator() {
        return !negate ? Operators.BETWEEN : Operators.NOT_BETWEEN;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Between as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        operand.getPlaceHolders(placeHolders);
        lowerBound.getPlaceHolders(placeHolders);
        upperBound.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append(operand)
                .append(" " + builder.keyword(getOperator()) + " ")
                .append(lowerBound)
                .append(builder.keyword(" and "))
                .append(upperBound);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
