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

package org.citydb.sqlbuilder.update;

import java.util.List;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.select.PredicateToken;

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
			statements.add((PlaceHolder<?>)value);
		else if (value instanceof PredicateToken)
			((PredicateToken)value).getInvolvedPlaceHolders(statements);
		else if (value instanceof SubQueryExpression)
			((SubQueryExpression)value).getInvolvedPlaceHolders(statements);
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
