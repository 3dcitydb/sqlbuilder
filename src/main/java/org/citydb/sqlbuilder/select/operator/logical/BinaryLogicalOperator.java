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

package org.citydb.sqlbuilder.select.operator.logical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.select.PredicateToken;

public class BinaryLogicalOperator extends AbstractLogicalOperator {
	private final List<PredicateToken> operands;
	private final LogicalOperationName name;

	public BinaryLogicalOperator(LogicalOperationName name, List<PredicateToken> operands) {
		if (operands == null || operands.size() < 1)
			throw new IllegalArgumentException("A binary logical operator requires one or more operands.");

		this.operands = operands;
		this.name = name;
	}

	public BinaryLogicalOperator(LogicalOperationName name, PredicateToken... operands) {
		this(name, Arrays.asList(operands));
	}

	public BinaryLogicalOperator(PredicateToken leftOperand, LogicalOperationName name, PredicateToken rightOperand) {
		if (!LogicalOperationName.BINARY_OPERATIONS.contains(name))
			throw new IllegalArgumentException("Allowed binary comparisons only include " + LogicalOperationName.BINARY_OPERATIONS);

		operands = new ArrayList<>();
		operands.add(leftOperand);
		operands.add(rightOperand);
		this.name = name;
	}

	public void addOperand(PredicateToken operand) {
		operands.add(operand);
	}

	public List<PredicateToken> getOperands() {
		return operands;
	}

	@Override
	public LogicalOperationName getOperationName() {
		return name;
	}

	@Override
	public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
		for (PredicateToken operand : operands)
			operand.getInvolvedPlaceHolders(statements);
	}

	@Override
	public String toString() {
		if (operands.size() > 1) {		
			StringBuilder tmp = new StringBuilder();
			tmp.append("(");

			Iterator<PredicateToken> iter = operands.iterator();
			while (iter.hasNext()) {
				tmp.append(iter.next());
				if (iter.hasNext())
					tmp.append(" ").append(name).append(" ");
			}

			return tmp.append(")").toString();
		} else
			return operands.get(0).toString();
	}

}
