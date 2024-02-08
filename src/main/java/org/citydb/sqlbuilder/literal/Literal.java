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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.query.Selection;

import java.util.List;
import java.util.Optional;

public abstract class Literal<T> implements Expression, Selection<Literal<T>> {
    protected T value;
    private String alias;

    protected Literal(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
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
    public void buildSQL(SQLBuilder builder) {
        builder.append(value != null ? value.toString() : "null");
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
