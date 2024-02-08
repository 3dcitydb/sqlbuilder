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
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.query.Selection;

import java.util.List;
import java.util.Optional;

public class WildcardColumn implements Expression, Selection<WildcardColumn> {
    private final Table table;

    private WildcardColumn(Table table) {
        this.table = table;
    }

    public static WildcardColumn newInstance() {
        return new WildcardColumn(null);
    }

    public static WildcardColumn of(Table table) {
        return new WildcardColumn(table);
    }

    public Optional<Table> getTable() {
        return Optional.ofNullable(table);
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.empty();
    }

    @Override
    public WildcardColumn as(String alias) {
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        if (table != null) {
            table.getPlaceHolders(placeHolders);
        }
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
