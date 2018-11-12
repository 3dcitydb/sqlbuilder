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

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.ProjectionToken;

import java.util.List;
import java.util.Set;

public class WildCardColumn implements ProjectionToken, Expression {
	private final Table table;

	public WildCardColumn() {
		table = null;
	}

	public WildCardColumn(Table table) {
		this.table = table;
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
		// nothing to do here
	}

	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder();
		if (table != null)
			tmp.append(table.getAlias()).append(".");

		return tmp.append("*").toString();
	}


}
