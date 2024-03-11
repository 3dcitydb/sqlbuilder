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
import org.citydb.sqlbuilder.query.Selection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComparisonOperation implements LogicalExpression, Operation, Selection<ComparisonOperation> {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final String operator;
    private String alias;

    protected ComparisonOperation(Expression leftOperand, String operator, Expression rightOperand) {
        this.leftOperand = Objects.requireNonNull(leftOperand, "The left operand must not be null.");
        this.rightOperand = Objects.requireNonNull(rightOperand, "The right operand must not be null.");
        this.operator = Objects.requireNonNull(operator, "The operator must not be null.");
    }

    public static ComparisonOperation of(Expression leftOperand, String operator, Expression rightOperand) {
        return new ComparisonOperation(leftOperand, operator, rightOperand);
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public ComparisonOperation as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        leftOperand.getPlaceHolders(placeHolders);
        rightOperand.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append(leftOperand)
                .append(" " + builder.keyword(operator) + " ")
                .append(rightOperand);
    }

    @Override
    public String toString() {
        return toSql();
    }
}