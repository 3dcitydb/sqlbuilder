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

package org.citydb.sqlbuilder.literal;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.query.Selection;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Literal<T> implements Expression, Selection<Literal<T>> {
    protected T value;
    private String alias;

    protected Literal(T value) {
        this.value = value;
    }

    public static ScalarExpression ofScalar(Object value) {
        if (value instanceof ScalarExpression expression) {
            return expression;
        } else if (value instanceof String literal) {
            return StringLiteral.of(literal);
        } else if (value instanceof Double literal) {
            return DoubleLiteral.of(literal);
        } else if (value instanceof Long literal) {
            return IntegerLiteral.of(literal);
        } else if (value instanceof Integer literal) {
            return IntegerLiteral.of(literal);
        } else if (value instanceof Number literal) {
            return literal.doubleValue() == literal.longValue() ?
                    IntegerLiteral.of(literal.longValue()) :
                    DoubleLiteral.of(literal.doubleValue());
        } else if (value instanceof Boolean literal) {
            return BooleanLiteral.of(literal);
        } else if (value instanceof Date literal) {
            return DateLiteral.of(literal);
        } else if (value instanceof Timestamp literal) {
            return TimestampLiteral.of(literal);
        } else {
            return NullLiteral.getInstance();
        }
    }

    public static List<ScalarExpression> ofScalarList(Object... values) {
        return values != null ?
                ofScalarList(Arrays.asList(values)) :
                null;
    }

    public static List<ScalarExpression> ofScalarList(List<Object> values) {
        return values != null ?
                values.stream()
                        .map(Literal::ofScalar)
                        .collect(Collectors.toList()) :
                null;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Literal<T> as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
    }

    @Override
    public String toString() {
        return toSql();
    }
}
