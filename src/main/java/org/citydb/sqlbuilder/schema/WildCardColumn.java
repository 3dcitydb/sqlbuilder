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

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WildCardColumn implements Projection {
    private final Table table;

    private WildCardColumn(Table table) {
        this.table = table;
    }

    public static WildCardColumn newInstance() {
        return new WildCardColumn(null);
    }

    public static WildCardColumn of(Table table) {
        return new WildCardColumn(table);
    }

    public Optional<Table> getTable() {
        return Optional.ofNullable(table);
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        if (table != null) {
            table.buildInvolvedTables(tables);
        }
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        if (table != null) {
            table.buildInvolvedPlaceHolders(placeHolders);
        }
    }

    @Override
    public void buildSQL(SQLBuilder builder, boolean withAlias) {
        buildSQL(builder);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(table != null ? table.getAlias() + ".*" : "*");
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
