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

package org.citydb.sqlbuilder.select.operator.set;

import java.util.List;

import org.citydb.sqlbuilder.select.Select;

public class SetOperationFactory {

	public static SetOperator intersect(List<Select> operands) {
		return new SetOperator(SetOperationName.INTERSECT, operands);
	}
	
	public static SetOperator intersect(Select... operands) {
		return new SetOperator(SetOperationName.INTERSECT, operands);
	}

	public static SetOperator union(List<Select> operands) {
		return new SetOperator(SetOperationName.UNION, operands);
	}

	public static SetOperator union(Select... operands) {
		return new SetOperator(SetOperationName.UNION, operands);
	}

	public static SetOperator unionAll(List<Select> operands) {
		return new SetOperator(SetOperationName.UNION_ALL, operands);
	}

	public static SetOperator unionAll(Select... operands) {
		return new SetOperator(SetOperationName.UNION_ALL, operands);
	}

	public static SetOperator generic(String sqlName, List<Select> operands) {
		return new SetOperator(sqlName, operands);
	}
	
	public static SetOperator generic(String sqlName, Select... operands) {
		return new SetOperator(sqlName, operands);
	}
	
}
