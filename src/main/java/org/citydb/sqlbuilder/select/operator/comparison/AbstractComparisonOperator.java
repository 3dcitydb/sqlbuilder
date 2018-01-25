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

import java.util.List;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.select.ProjectionToken;
import org.citydb.sqlbuilder.select.operator.Operator;

public abstract class AbstractComparisonOperator implements Operator {
	
	public void getInvolvedPlaceHolders(Expression operand, List<PlaceHolder<?>> statements) {
		if (operand instanceof PlaceHolder<?>)
			statements.add((PlaceHolder<?>)operand);
		else if (operand instanceof ProjectionToken)
			((ProjectionToken)operand).getInvolvedPlaceHolders(statements);
		else if (operand instanceof SubQueryExpression)
			((SubQueryExpression)operand).getInvolvedPlaceHolders(statements);
	}
}
