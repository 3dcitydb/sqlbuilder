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

package org.citydb.sqlbuilder.schema;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LiteralColumn implements ColumnExpression, Projection<LiteralColumn> {
    private final Literal<?> literal;
    private String alias;

    private LiteralColumn(Literal<?> literal, String alias) {
        this.literal = Objects.requireNonNull(literal, "The literal must not be null.");
        this.alias = alias;
    }

    public static LiteralColumn of(Literal<?> literal, String alias) {
        return new LiteralColumn(literal, alias);
    }

    public static LiteralColumn of(Literal<?> literal) {
        return new LiteralColumn(literal, null);
    }

    public Literal<?> getLiteral() {
        return literal;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public LiteralColumn as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        literal.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        literal.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder, boolean withAlias) {
        builder.append(literal);
        if (withAlias && alias != null) {
            builder.append(builder.keyword(" as ") + alias);
        }
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        buildSQL(builder, false);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
