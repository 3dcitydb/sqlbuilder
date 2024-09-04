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
import org.citydb.sqlbuilder.select.PredicateToken;
import org.citydb.sqlbuilder.select.ProjectionToken;
import org.citydb.sqlbuilder.select.Select;
import org.citydb.sqlbuilder.select.operator.Operator;

import java.util.List;
import java.util.Set;

public abstract class AbstractComparisonOperator implements Operator {

    public void getInvolvedTables(Expression operand, Set<Table> tables) {
        if (operand instanceof ProjectionToken)
            ((ProjectionToken) operand).getInvolvedTables(tables);
        else if (operand instanceof PredicateToken)
            ((PredicateToken) operand).getInvolvedTables(tables);
        else if (operand instanceof Select)
            tables.addAll(((Select) operand).getOuterTables());
    }

    public void getInvolvedPlaceHolders(Expression operand, List<PlaceHolder<?>> statements) {
        if (operand instanceof PlaceHolder<?>)
            statements.add((PlaceHolder<?>) operand);
        else if (operand instanceof ProjectionToken)
            ((ProjectionToken) operand).getInvolvedPlaceHolders(statements);
        else if (operand instanceof SubQueryExpression)
            ((SubQueryExpression) operand).getInvolvedPlaceHolders(statements);
    }
}
