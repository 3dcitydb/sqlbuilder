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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.ProjectionToken;

public class Function implements ProjectionToken, Expression {
	private final String name;
	private final String asName;
	private final boolean useParentheses;
	private final List<Expression> arguments;
	
	public Function(String name, String asName, boolean useParentheses) {
		this.name = name;
		this.asName = asName;
		this.useParentheses = useParentheses;
		this.arguments = new ArrayList<Expression>();
	}
	
	public Function(String name) {
		this(name, null, true);
	}
	
	public Function(String name, String asName) {
		this(name, asName, true);
	}

	public Function(String name, String asName, boolean useParentheses, Expression... arguments) {
		this.name = name;
		this.asName = asName;
		this.useParentheses = useParentheses;
		this.arguments = new ArrayList<>(Arrays.asList(arguments));
	}

	public Function(String name, Expression... arguments) {
		this(name, null, true, arguments);
	}

	public Function(String name, String asName, Expression... arguments) {
		this(name, asName, true, arguments);
	}

	public String getName() {
		return name;
	}

	public String getAsName() {
		return asName;
	}

	public boolean isUseParentheses() {
		return useParentheses;
	}

	public List<Expression> getArguments() {
		return new ArrayList<>(arguments);
	}
	
	public Function addArgument(Expression argument) {
		arguments.add(argument);
		return this;
	}

	@Override
	public void getInvolvedTables(Set<Table> tables) {
		for (Expression argument : arguments) {
			if (argument instanceof ProjectionToken)
				((ProjectionToken)argument).getInvolvedTables(tables);
		}
	}

	@Override
	public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
		for (Expression argument : arguments)
			if (argument instanceof PlaceHolder<?>)
				statements.add((PlaceHolder<?>)argument);
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder();
		tmp.append(name);

		tmp.append(useParentheses ? "(" : " ");

		Iterator<Expression> iter = arguments.iterator();
		while (iter.hasNext()) {
			tmp.append(iter.next());
			if (iter.hasNext())
				tmp.append(", ");
		}

		if (useParentheses)
			tmp.append(")");

		if (asName != null)
			tmp.append(" as ")
			.append(asName);

		return tmp.toString();
	}

}
