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
import org.citydb.sqlbuilder.common.SQLObject;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrderBy implements SQLObject {
    private final Column column;
    private final SortOrder sortOrder;

    private OrderBy(Column column, SortOrder sortOrder) {
        this.column = Objects.requireNonNull(column, "The column must not be null.");
        this.sortOrder = Objects.requireNonNull(sortOrder, "The sort order must not be null.");
    }

    public static OrderBy of(Column column, SortOrder sortOrder) {
        return new OrderBy(column, sortOrder);
    }

    public static OrderBy of(Column column) {
        return new OrderBy(column, SortOrder.ASCENDING);
    }

    public Column getColumn() {
        return column;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        column.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        column.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(column);
        if (sortOrder != SortOrder.ASCENDING) {
            builder.append(" " + sortOrder.toSQL(builder));
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
