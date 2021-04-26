/*
 * 3D City Database - The Open Source CityGML Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2013 - 2021
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

package org.citydb.sqlbuilder.select.operator.set;

import org.citydb.sqlbuilder.SQLStatement;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.Select;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SetOperator implements SQLStatement, SubQueryExpression {
    private final SetOperationName operationName;
    private final List<Select> operands;
    private final String sqlName;

    public SetOperator(String name, List<Select> operands) {
        if (operands == null || operands.size() < 2)
            throw new IllegalArgumentException("A set operator requires two or more operands.");

        this.operationName = SetOperationName.GENERIC;
        this.operands = operands;
        this.sqlName = name;
    }

    public SetOperator(String name, Select... operands) {
        this(name, Arrays.asList(operands));
    }

    public SetOperator(SetOperationName name, List<Select> operands) {
        if (operands == null || operands.size() < 2)
            throw new IllegalArgumentException("A set operator requires two or more operands.");

        this.operationName = name;
        this.operands = operands;
        this.sqlName = name.toString();
    }

    public SetOperator(SetOperationName name, Select... operands) {
        this(name, Arrays.asList(operands));
    }

    public SetOperationName getOperationName() {
        return operationName;
    }

    public String getSQLName() {
        return sqlName;
    }

    public List<Select> getOperands() {
        return operands;
    }

    @Override
    public Set<Table> getInvolvedTables() {
        Set<Table> tables = new LinkedHashSet<>();
        for (Select operand : operands)
            tables.addAll(operand.getInvolvedTables());

        return tables;
    }

    @Override
    public List<PlaceHolder<?>> getInvolvedPlaceHolders() {
        List<PlaceHolder<?>> statements = new ArrayList<>();
        for (Select operand : operands)
            statements.addAll(operand.getInvolvedPlaceHolders());

        return statements;
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        for (Select operand : operands)
            placeHolders.addAll(operand.getInvolvedPlaceHolders());
    }

    @Override
    public void print(PrintWriter writer, boolean indent) {
        Iterator<Select> iter = operands.iterator();
        while (iter.hasNext()) {
            writer.print('(');
            iter.next().print(writer, indent);
            writer.print(')');

            if (iter.hasNext()) {
                if (indent)
                    writer.println();
                else
                    writer.print(' ');

                writer.print(sqlName);

                if (indent)
                    writer.println();
                else
                    writer.print(' ');
            }
        }

        writer.flush();
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        print(new PrintWriter(writer), false);

        return writer.toString();
    }

}
