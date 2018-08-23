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

public class UnaryComparisonOperator extends AbstractComparisonOperator {
	private final Expression operand;
	private final ComparisonName name;

	private boolean isPrefix;

	public UnaryComparisonOperator(Expression operand, ComparisonName name) {
		if (!ComparisonName.UNARY_COMPARISONS.contains(name))
			throw new IllegalArgumentException("Allowed binary comparisons only include " + ComparisonName.UNARY_COMPARISONS);

		this.operand = operand;
		this.name = name;

		isPrefix = (name == ComparisonName.EXISTS) || (name == ComparisonName.NOT_EXISTS);
	}

	public Expression getOperand() {
		return operand;
	}

	@Override
	public ComparisonName getOperationName() {
		return name;
	}

	@Override
	public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
		getInvolvedPlaceHolders(operand, statements);
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder();
		if (isPrefix)
			tmp.append(getOperationName()).append(" (");

		tmp.append(operand);

		if (!isPrefix)
			tmp.append(" ").append(getOperationName());
		else
			tmp.append(")");

		return tmp.toString();
	}
}
