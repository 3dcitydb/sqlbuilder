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

package org.citydb.sqlbuilder.update;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.select.PredicateToken;
import org.citydb.sqlbuilder.select.ProjectionToken;

import java.util.List;

public class UpdateToken {
    private final Column column;
    private final Expression value;

    public UpdateToken(Column column, Expression value) {
        this.column = column;
        this.value = value;
    }

    public Column getColumn() {
        return column;
    }

    public Expression getValue() {
        return value;
    }

    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        if (value instanceof PlaceHolder<?>)
            statements.add((PlaceHolder<?>) value);
        else if (value instanceof PredicateToken)
            ((PredicateToken) value).getInvolvedPlaceHolders(statements);
        else if (value instanceof ProjectionToken)
            ((ProjectionToken) value).getInvolvedPlaceHolders(statements);
        else if (value instanceof SubQueryExpression)
            ((SubQueryExpression) value).getInvolvedPlaceHolders(statements);
    }

    @Override
    public String toString() {
        boolean isSubQuery = value instanceof SubQueryExpression;

        StringBuilder tmp = new StringBuilder()
                .append(column.getName()).append(" = ");
        if (isSubQuery)
            tmp.append("(");

        tmp.append(value);

        if (isSubQuery)
            tmp.append(")");

        return tmp.toString();
    }

}
