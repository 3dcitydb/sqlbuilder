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

package org.citydb.sqlbuilder.select.join;

import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.operator.comparison.BinaryComparisonOperator;
import org.citydb.sqlbuilder.select.operator.comparison.ComparisonName;

import java.util.Set;

public class Join {
	private final Column fromColumn;
	private final Column toColumn;
	private final BinaryComparisonOperator condition;
	private JoinName name;

	public Join(JoinName name, Table table, String column, ComparisonName binaryComparison, Column fromColumn) {
		this.name = name;
		this.toColumn = table.getColumn(column);
		this.fromColumn = fromColumn;
		condition = new BinaryComparisonOperator(this.fromColumn, binaryComparison, this.toColumn);
	}
	
	public Join(JoinName joinName, Column toColumn, ComparisonName binaryComparison, Column fromColumn) {
		this.name = joinName;
		this.toColumn = toColumn;
		this.fromColumn = fromColumn;
		condition = new BinaryComparisonOperator(this.fromColumn, binaryComparison, this.toColumn);
	}

	public Column getFromColumn() {
		return fromColumn;
	}

	public Column getToColumn() {
		return toColumn;
	}

	public BinaryComparisonOperator getCondition() {
		return condition;
	}

	public JoinName getJoinName() {
		return name;
	}

	public void setJoinName(JoinName name) {
		if (name != null)
			this.name = name;
	}
	
	public void getInvolvedTables(Set<Table> tables) {
		fromColumn.getInvolvedTables(tables);
		toColumn.getInvolvedTables(tables);
	}
	
	@Override
	public String toString() {		
		return name + " " + toColumn.getTable() + " on " + condition;
	}

}
