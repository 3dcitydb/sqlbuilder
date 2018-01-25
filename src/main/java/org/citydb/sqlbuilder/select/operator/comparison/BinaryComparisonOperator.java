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
	public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
		getInvolvedPlaceHolders(leftOperand, statements);
		getInvolvedPlaceHolders(rightOperand, statements);
	}

	@Override
	public String toString() {
		boolean rightOperandIsSubQuery = rightOperand instanceof SubQueryExpression;

		StringBuilder tmp = new StringBuilder()
		.append(leftOperand).append(" ").append(sqlName).append(" ");
		if (rightOperandIsSubQuery)
			tmp.append("(");
		
		tmp.append(rightOperand);
		
		if (rightOperandIsSubQuery)
			tmp.append(")");
		
		return tmp.toString();
	}

}
