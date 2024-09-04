/*
 * 3D City Database - The Open Source CityGML Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2013 - 2024
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.lrg.tum.de/gis/
 *
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 *
 * Virtual City Systems, Berlin <https://vc.systems/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
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

package org.citydb.sqlbuilder.expression;

import org.citydb.sqlbuilder.schema.AliasGenerator;
import org.citydb.sqlbuilder.schema.GlobalAliasGenerator;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.Select;
import org.citydb.sqlbuilder.select.operator.set.SetOperator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommonTableExpression {
    private final String name;
    private final SubQueryExpression queryExpression;
    private final List<String> columnNames;

    private CommonTableExpression(String name, SubQueryExpression queryExpression, List<String> columnNames) {
        this.name = name;
        this.queryExpression = queryExpression;
        this.columnNames = columnNames;
    }

    public CommonTableExpression(String name, Select select, String... columnNames) {
        this(name, select, Arrays.asList(columnNames));
    }

    public CommonTableExpression(String name, SetOperator setOperator, String... columnNames) {
        this(name, setOperator, Arrays.asList(columnNames));
    }

    public String getName() {
        return name;
    }

    public SubQueryExpression getQueryExpression() {
        return queryExpression;
    }

    public Table asTable(AliasGenerator aliasGenerator) {
        return new Table(name, aliasGenerator);
    }

    public Table asTable() {
        return new Table(name, GlobalAliasGenerator.getInstance());
    }

    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        queryExpression.getInvolvedPlaceHolders(statements);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name);

        if (columnNames != null && !columnNames.isEmpty()) {
            builder.append(" (")
                    .append(columnNames.stream().collect(Collectors.joining(", ")))
                    .append(")");
        }

        builder.append(" as (")
                .append(queryExpression)
                .append(")");

        return builder.toString();
    }

}
