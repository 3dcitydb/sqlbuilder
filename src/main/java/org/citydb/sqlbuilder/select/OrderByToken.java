/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * http://www.3dcitydb.org/
 *
 * Copyright 2013-2018 Claus Nagel <claus.nagel@gmail.com>
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

package org.citydb.sqlbuilder.select;

import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.orderBy.SortOrder;

import java.util.List;
import java.util.Set;

public class OrderByToken implements SelectToken {
    private final Column column;
    private final SortOrder sortOrder;

    public OrderByToken(Column column, SortOrder sortOrder) {
        this.column = column;
        this.sortOrder = sortOrder;
    }

    public OrderByToken(Column column) {
        this(column, SortOrder.ASCENDING);
    }

    public Column getColumn() {
        return column;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        column.getInvolvedTables(tables);
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        column.getInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append(column.toString(false));
        if (sortOrder != SortOrder.ASCENDING)
            tmp.append(" desc");

        return tmp.toString();
    }

}
