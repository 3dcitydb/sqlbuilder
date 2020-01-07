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

package org.citydb.sqlbuilder.select.projection;

import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.ProjectionToken;
import org.citydb.sqlbuilder.select.Select;

import java.util.List;
import java.util.Set;

public class ColumnExpression implements ProjectionToken {
    private final Select select;
    private final String asName;

    public ColumnExpression(Select select, String asName) {
        this.select = select;
        this.asName = asName;
    }

    public ColumnExpression(Select select) {
        this(select, null);
    }

    public Select getSelect() {
        return select;
    }

    public String getAsName() {
        return asName;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        // nothing to do here
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        select.getInvolvedPlaceHolders(statements);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder("(").append(select).append(")");
        if (asName != null)
            tmp.append(" as ").append(asName);

        return tmp.toString();
    }

}
