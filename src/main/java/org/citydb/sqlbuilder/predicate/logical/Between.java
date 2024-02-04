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

package org.citydb.sqlbuilder.predicate.logical;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Between implements LogicalOperator {
    private final Expression operand;
    private final Expression lowerBound;
    private final Expression upperBound;
    private boolean negate;

    private Between(Expression operand, Expression lowerBound, Expression upperBound, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.lowerBound = Objects.requireNonNull(lowerBound, "The lower bound must not be null.");
        this.upperBound = Objects.requireNonNull(upperBound, "The upper bound must not be null.");
        this.negate = negate;
    }

    public static Between of(Expression operand, Expression lowerBound, Expression upperBound, boolean negate) {
        return new Between(operand, lowerBound, upperBound, negate);
    }

    public static Between of(Expression operand, Expression lowerBound, Expression upperBound) {
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
    public LogicalOperatorType getType() {
        return !negate ? LogicalOperatorType.BETWEEN : LogicalOperatorType.NOT_BETWEEN;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        operand.buildInvolvedTables(tables);
        lowerBound.buildInvolvedTables(tables);
        upperBound.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        operand.buildInvolvedPlaceHolders(placeHolders);
        lowerBound.buildInvolvedPlaceHolders(placeHolders);
        upperBound.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(operand)
                .append(" " + getType().toSQL(builder) + " ")
                .append(lowerBound)
                .append(builder.keyword(" and "))
                .append(upperBound);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
