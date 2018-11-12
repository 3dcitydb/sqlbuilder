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

package org.citydb.sqlbuilder.select.projection;

import org.citydb.sqlbuilder.expression.AbstractSQLLiteral;
import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.IntegerLiteral;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.StringLiteral;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.ProjectionToken;

import java.util.List;
import java.util.Set;

public class ConstantColumn implements ProjectionToken, Expression {
	private final AbstractSQLLiteral<?> literal;
	private final String asName;
	private Table table;
	
	public ConstantColumn(AbstractSQLLiteral<?> literal, String asName) {
		this.literal = literal;
		this.asName = asName;
	}
	
	public ConstantColumn(AbstractSQLLiteral<?> literal) {
		this(literal, null);
	}
	
	public ConstantColumn(int value, String asName) {
		this(new IntegerLiteral(value), asName);
	}
	
	public ConstantColumn(int value) {
		this(new IntegerLiteral(value), null);
	}
	
	public ConstantColumn(String value, String asName) {
		this(new StringLiteral(value), asName);
	}
	
	public ConstantColumn(String value) {
		this(new StringLiteral(value), null);
	}
	
	public AbstractSQLLiteral<?> getLiteral() {
		return literal;
	}

	public String getAsName() {
		return asName;
	}
	
	public ConstantColumn withFromTable(Table table) {
		this.table = table;
		return this;
	}
	
	public Table getTable() {
		return table;
	}

	@Override
	public void getInvolvedTables(Set<Table> tables) {
		if (table != null)
			tables.add(table);
	}

	@Override
	public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
		if (literal instanceof PlaceHolder<?>)
			statements.add((PlaceHolder<?>)literal);
		
		if (table != null && table.isSetQueryExpression())
			table.getQueryExpression().getInvolvedPlaceHolders(statements);
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder();
		tmp.append(literal);
		if (asName != null)
			tmp.append(" as ").append(asName);
		
		return tmp.toString();
	}

}
