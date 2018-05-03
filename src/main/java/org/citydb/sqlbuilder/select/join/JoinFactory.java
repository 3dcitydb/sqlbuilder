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
import org.citydb.sqlbuilder.select.operator.comparison.ComparisonName;

public class JoinFactory {

	public static Join inner(Table table, String column, ComparisonName name, Column fromColumn) {
		return new Join(JoinName.INNER_JOIN, table, column, name, fromColumn);
	}
	
	public static Join left(Table table, String column, ComparisonName name, Column fromColumn) {
		return new Join(JoinName.LEFT_JOIN, table, column, name, fromColumn);
	}
	
	public static Join right(Table table, String column, ComparisonName name, Column fromColumn) {
		return new Join(JoinName.RIGHT_JOIN, table, column, name, fromColumn);
	}
	
	public static Join full(Table table, String column, ComparisonName name, Column fromColumn) {
		return new Join(JoinName.FULL_JOIN, table, column, name, fromColumn);
	}

}
