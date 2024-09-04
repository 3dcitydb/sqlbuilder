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

public class InOperator extends AbstractComparisonOperator {
    private final Expression operand;
    private final SubQueryExpression subQueryExpression;

    private boolean negate;

    public InOperator(Expression operand, SubQueryExpression subQueryExpression, boolean negate) {
        this.operand = operand;
        this.subQueryExpression = subQueryExpression;
        this.negate = negate;
    }

    public InOperator(Expression operand, SubQueryExpression subQueryExpression) {
        this(operand, subQueryExpression, false);
    }

    public Expression getOperand() {
        return operand;
    }

    public SubQueryExpression getSubQueryExpression() {
        return subQueryExpression;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public ComparisonName getOperationName() {
        return !negate ? ComparisonName.IN : ComparisonName.NOT_IN;
    }

    public SubQueryExpression getValueList() {
        return subQueryExpression;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        getInvolvedTables(operand, tables);
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        getInvolvedPlaceHolders(operand, placeHolders);
        subQueryExpression.getInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public String toString() {
        return operand + " " + getOperationName() + " (" + subQueryExpression + ")";
    }

}
