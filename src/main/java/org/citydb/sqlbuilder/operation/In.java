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
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class In implements LogicalExpression {
    private final Expression operand;
    private final QueryExpression queryExpression;
    private boolean negate;
    private String alias;

    private In(Expression operand, QueryExpression queryExpression, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.queryExpression = Objects.requireNonNull(queryExpression, "The query expression must not be null.");
        this.negate = negate;
    }

    public static In of(Expression operand, QueryExpression queryExpression, boolean negate) {
        return new In(operand, queryExpression, negate);
    }

    public static In of(Expression operand, QueryExpression queryExpression) {
        return new In(operand, queryExpression, false);
    }

    public Expression getOperand() {
        return operand;
    }

    public QueryExpression getQueryExpression() {
        return queryExpression;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public String getType() {
        return !negate ? Operators.IN : Operators.NOT_IN;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public In as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        operand.getPlaceHolders(placeHolders);
        queryExpression.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append(operand)
                .append(" " + builder.keyword(getType()) + " ")
                .append(queryExpression);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
