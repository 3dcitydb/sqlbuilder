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

package org.citydb.sqlbuilder.schema;

import java.util.List;
import java.util.Set;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.select.ProjectionToken;

public class Column implements ProjectionToken, Expression {
	private final Table table;
	private final String name;
	private final String asName;
	
	public Column(Table table, String name, String asName) {
		this.table = table;
		this.name = name;
		this.asName = asName;
	}
	
	public Column(Table table, String name) {
		this(table, name, null);
	}

	public Table getTable() {
		return table;
	}

	public String getName() {
		return name;
	}
	
	public String getAsName() {
		return asName;
	}

	@Override
	public void getInvolvedTables(Set<Table> tables) {
		tables.add(table);
	}
	
	@Override
	public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
		// nothing to do here
	}

	public String toString(boolean withAlias) {
		StringBuilder tmp = new StringBuilder().append(table.getAlias()).append(".").append(name);
		if (withAlias && asName != null)
			tmp.append(" as ").append(asName);
		
		return tmp.toString();
	}

	@Override
	public String toString() {
		return toString(true);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj instanceof Column) {
			Column other = (Column)obj;
			return table.equals(other.table) &&
					name.toUpperCase().equals(other.name.toUpperCase());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + table.hashCode();
		hash = hash * 31 + name.toUpperCase().hashCode();
		
		return hash;
	}

}
