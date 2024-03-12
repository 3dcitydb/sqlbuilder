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

import java.util.*;

public class In implements ComparisonOperation {
    private final Expression operand;
    private final List<ScalarExpression> values;
    private boolean negate;
    private String alias;

    private In(Expression operand, List<ScalarExpression> values, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.values = Objects.requireNonNull(values, "The values list must not be null.");
        this.negate = negate;
    }

    public static In of(Expression operand, List<ScalarExpression> values, boolean negate) {
        return new In(operand, values, negate);
    }

    public static In of(Expression operand, List<ScalarExpression> values) {
        return new In(operand, values, false);
    }

    public static In of(Expression operand, ScalarExpression... values) {
        return new In(operand, values != null ? new ArrayList<>(Arrays.asList(values)) : null, false);
    }

    public Expression getOperand() {
        return operand;
    }

    public List<ScalarExpression> getValues() {
        return values;
    }

    public In add(List<ScalarExpression> values) {
        if (values != null && !values.isEmpty()) {
            values.stream()
                    .filter(Objects::nonNull)
                    .forEach(this.values::add);
        }

        return this;
    }

    public In add(ScalarExpression... values) {
        return values != null ? add(Arrays.asList(values)) : this;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public String getOperator() {
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
        values.forEach(value -> value.getPlaceHolders(placeHolders));
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append(operand)
                .append(" " + builder.keyword(getOperator()) + " ")
                .append("(")
                .append(SqlBuilder.of(builder.getOptions()).append(values, ", ").build())
                .append(")");
    }

    @Override
    public String toString() {
        return toSql();
    }
}
