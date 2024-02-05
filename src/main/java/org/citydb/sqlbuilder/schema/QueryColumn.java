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
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.query.Select;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class QueryColumn implements ColumnExpression, Projection {
    private final Select select;
    private final String alias;

    private QueryColumn(Select select, String alias) {
        this.select = Objects.requireNonNull(select, "The select must not be null.");
        this.alias = alias;
    }

    public static QueryColumn of(Select select, String alias) {
        return new QueryColumn(select, alias);
    }

    public static QueryColumn of(Select select) {
        return new QueryColumn(select, null);
    }

    public Select getSelect() {
        return select;
    }

    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        select.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        select.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder, boolean withAlias) {
        builder.append(select);
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