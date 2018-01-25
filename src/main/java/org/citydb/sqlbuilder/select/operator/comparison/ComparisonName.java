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

import java.util.EnumSet;

import org.citydb.sqlbuilder.select.operator.OperationName;

public enum ComparisonName implements OperationName {
	EQUAL_TO("="),
	NOT_EQUAL_TO("<>"),
	LESS_THAN("<"),
	GREATER_THAN(">"),
	LESS_THAN_OR_EQUAL_TO("<="),
	GREATER_THAN_OR_EQUAL_TO(">="),
	LIKE("like"),
	NOT_LIKE("not like"),
	BETWEEN("between"),
	NOT_BETWEEN("not between"),
	IN("in"),
	NOT_IN("not in"),
	IS_NULL("is null"),
	IS_NOT_NULL("is not null"),
	EXISTS("exists"),
	NOT_EXISTS("not exists"),
	GENERIC("generic");
	
	public static final EnumSet<ComparisonName> BINARY_COMPARISONS = EnumSet.of(
			EQUAL_TO, NOT_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO, GREATER_THAN, GREATER_THAN_OR_EQUAL_TO);
	
	public static final EnumSet<ComparisonName> UNARY_COMPARISONS = EnumSet.of(
			IS_NULL, IS_NOT_NULL, EXISTS, NOT_EXISTS);
	
	final String symbol;
	
	ComparisonName(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String toString() {
		return symbol;
	}
}
