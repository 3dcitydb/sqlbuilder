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

package org.citydb.sqlbuilder.select.operator.comparison;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Set;

public class BinaryComparisonOperator extends AbstractComparisonOperator {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final ComparisonName name;
    private final String sqlName;

    public BinaryComparisonOperator(Expression leftOperand, ComparisonName name, Expression rightOperand) {
        if (!ComparisonName.BINARY_COMPARISONS.contains(name))
            throw new IllegalArgumentException("Allowed binary comparisons only include " + ComparisonName.BINARY_COMPARISONS);

        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.name = name;
        this.sqlName = name.toString();
    }

    public BinaryComparisonOperator(Expression leftOperand, String name, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.name = ComparisonName.GENERIC;
        this.sqlName = name;
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    @Override
    public ComparisonName getOperationName() {
        return name;
    }

    public String getSQLName() {
        return sqlName;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        getInvolvedTables(leftOperand, tables);
        getInvolvedTables(rightOperand, tables);
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        getInvolvedPlaceHolders(leftOperand, placeHolders);
        getInvolvedPlaceHolders(rightOperand, placeHolders);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();

        if (leftOperand instanceof SubQueryExpression)
            tmp.append("(").append(leftOperand).append(")");
        else
            tmp.append(leftOperand);

        tmp.append(" ").append(sqlName).append(" ");

        if (rightOperand instanceof SubQueryExpression)
            tmp.append("(").append(rightOperand).append(")");
        else
            tmp.append(rightOperand);

        return tmp.toString();
    }

}
