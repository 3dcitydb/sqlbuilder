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

package org.citydb.sqlbuilder.literal;

import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.*;
import java.util.stream.Collectors;

public class LiteralList implements QueryExpression {
    private final List<Literal<?>> literals;

    private LiteralList(List<Literal<?>> literals) {
        this.literals = Objects.requireNonNull(literals, "The list of literals must not be null.");
    }

    public static LiteralList empty() {
        return new LiteralList(new ArrayList<>());
    }

    public static LiteralList of(Collection<?> values) {
        return values != null ?
                new LiteralList(values.stream()
                        .map(Literal::of)
                        .collect(Collectors.toCollection(ArrayList::new))) :
                empty();
    }

    public static LiteralList of(Object... values) {
        return of(Arrays.asList(values));
    }

    public List<Literal<?>> getLiterals() {
        return literals;
    }

    public LiteralList add(Literal<?> literal) {
        if (literal != null) {
            literals.add(literal);
        }

        return this;
    }

    public boolean isEmpty() {
        return literals.isEmpty();
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
