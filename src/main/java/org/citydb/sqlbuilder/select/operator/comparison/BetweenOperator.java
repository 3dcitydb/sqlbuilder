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

package org.citydb.sqlbuilder.select.operator.comparison;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;

import java.util.List;

public class BetweenOperator extends AbstractComparisonOperator {
    private final Expression operand;
    private final Expression lowerBound;
    private final Expression upperBound;

    private boolean negate;

    public BetweenOperator(Expression operand, Expression lowerBound, Expression upperBound, boolean negate) {
        this.operand = operand;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.negate = negate;
    }

    public BetweenOperator(Expression operand, Expression lowerBound, Expression upperBound) {
        this(operand, lowerBound, upperBound, false);
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public Expression getOperand() {
        return operand;
    }

    public Expression getLowerBound() {
        return lowerBound;
    }

    public Expression getUpperBound() {
        return upperBound;
    }

    @Override
    public ComparisonName getOperationName() {
        return !negate ? ComparisonName.BETWEEN : ComparisonName.NOT_BETWEEN;
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        getInvolvedPlaceHolders(operand, statements);
        getInvolvedPlaceHolders(lowerBound, statements);
        getInvolvedPlaceHolders(upperBound, statements);
    }

    @Override
    public String toString() {
        return getOperand() + " " + getOperationName() + " " + lowerBound + " and " + upperBound;
    }

}
