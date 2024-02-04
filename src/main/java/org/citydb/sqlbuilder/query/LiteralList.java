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

package org.citydb.sqlbuilder.query;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.Literals;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class LiteralList implements QueryExpression {
    private final List<Literal<?>> literals;

    private LiteralList(List<Literal<?>> literals) {
        this.literals = Objects.requireNonNull(literals, "The literal list must not be null.");
    }

    public static LiteralList empty() {
        return new LiteralList(new ArrayList<>());
    }

    public static LiteralList of(Object... values) {
        return of(values != null ? Arrays.asList(values) : null);
    }

    public static LiteralList of(List<Object> values) {
        List<Literal<?>> literals = new ArrayList<>();
        if (values != null) {
            values.stream()
                    .map(Literals::of)
                    .forEach(literals::add);
        }

        return new LiteralList(literals);
    }

    public List<Literal<?>> getLiterals() {
        return literals;
    }

    public LiteralList add(Literal<?> literal) {
        literals.add(literal);
        return this;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        literals.forEach(literal -> literal.buildInvolvedTables(tables));
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        literals.forEach(literal -> literal.buildInvolvedPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(literals, ", ");
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
