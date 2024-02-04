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

package org.citydb.sqlbuilder.predicate.comparison;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ComparisonOperator implements Predicate {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final ComparisonOperatorType type;

    protected ComparisonOperator(Expression leftOperand, ComparisonOperatorType type, Expression rightOperand) {
        this.leftOperand = Objects.requireNonNull(leftOperand, "The left operand must not be null.");
        this.rightOperand = Objects.requireNonNull(rightOperand, "The right operand must not be null.");
        this.type = Objects.requireNonNull(type, "The comparison type must not be null.");
    }

    public static ComparisonOperator of(Expression leftOperand, ComparisonOperatorType type, Expression rightOperand) {
        return new ComparisonOperator(leftOperand, type, rightOperand);
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    public ComparisonOperatorType getType() {
        return type;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        leftOperand.buildInvolvedTables(tables);
        rightOperand.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        leftOperand.buildInvolvedPlaceHolders(placeHolders);
        rightOperand.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(leftOperand)
                .append(" " + type.toSQL(builder) + " ")
                .append(rightOperand);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
