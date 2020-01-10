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

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.IntegerLiteral;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Set;

public class OffsetToken implements SelectToken {
    private final Expression expression;

    public OffsetToken(Expression expression) {
        this.expression = expression;
    }

    public OffsetToken(int offset) {
        this(new IntegerLiteral(Math.max(offset, 0)));
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        if (expression instanceof ProjectionToken)
            ((ProjectionToken) expression).getInvolvedTables(tables);
        else if (expression instanceof PredicateToken)
            ((PredicateToken) expression).getInvolvedTables(tables);
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        if (expression instanceof PlaceHolder<?>)
            placeHolders.add((PlaceHolder<?>) expression);
        else if (expression instanceof ProjectionToken)
            ((ProjectionToken) expression).getInvolvedPlaceHolders(placeHolders);
        else if (expression instanceof SubQueryExpression)
            ((SubQueryExpression) expression).getInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
