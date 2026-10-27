/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2026
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

package org.citydb.sqlbuilder.common;

import org.citydb.sqlbuilder.literal.ScalarExpression;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.*;
import java.util.stream.Collectors;

public class ExpressionList implements QueryExpression {
    private final List<ScalarExpression> expressions;

    private ExpressionList(List<ScalarExpression> expressions) {
        this.expressions = Objects.requireNonNull(expressions, "The list of expressions must not be null.");
    }

    public static ExpressionList empty() {
        return new ExpressionList(new ArrayList<>());
    }

    public static ExpressionList of(Collection<?> values) {
        return values != null ?
                new ExpressionList(values.stream()
                        .map(value -> Expressions.require(value, ScalarExpression.class))
                        .collect(Collectors.toCollection(ArrayList::new))) :
                empty();
    }

    public static ExpressionList of(Object... values) {
        return values != null ? of(Arrays.asList(values)) : empty();
    }

    public List<ScalarExpression> getExpressions() {
        return expressions;
    }

    public ExpressionList add(ScalarExpression expression) {
        if (expression != null) {
            expressions.add(expression);
        }

        return this;
    }

    public boolean isEmpty() {
        return expressions.isEmpty();
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
