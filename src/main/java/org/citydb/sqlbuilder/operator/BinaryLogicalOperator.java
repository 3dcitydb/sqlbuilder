/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2024
 * virtualcitysystems GmbH, Germany
 * https://vc.systems/
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

package org.citydb.sqlbuilder.operator;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.*;

public class BinaryLogicalOperator implements LogicalOperator {
    private final List<LogicalOperator> operands;
    private final String name;
    private String alias;

    private BinaryLogicalOperator(String name, List<LogicalOperator> operands) {
        this.operands = Objects.requireNonNull(operands, "The operands list must not be null.");
        this.name = Objects.requireNonNull(name, "The operator name must not be null.");

        if (operands.isEmpty()) {
            throw new IllegalArgumentException("The operands list must not be empty.");
        } else if (!Operators.AND.equalsIgnoreCase(name) && !Operators.OR.equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("The operator '" + name + "' is not supported.");
        }
    }

    public static BinaryLogicalOperator of(String name, List<LogicalOperator> operands) {
        return new BinaryLogicalOperator(name, operands);
    }

    public static BinaryLogicalOperator of(String name, LogicalOperator... operands) {
        return new BinaryLogicalOperator(name, operands != null ?
                new ArrayList<>(Arrays.asList(operands)) :
                null);
    }

    public static BinaryLogicalOperator of(LogicalOperator leftOperand, String name, LogicalOperator rightOperand) {
        List<LogicalOperator> operands = new ArrayList<>();
        operands.add(Objects.requireNonNull(leftOperand, "The left operand must not be null."));
        operands.add(Objects.requireNonNull(rightOperand, "The right operand must not be null."));
        return new BinaryLogicalOperator(name, operands);
    }

    public List<LogicalOperator> getOperands() {
        return operands;
    }

    public BinaryLogicalOperator add(LogicalOperator operand) {
        if (operand != null) {
            operands.add(operand);
        }

        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public BinaryLogicalOperator as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        operands.forEach(operand -> operand.getPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        if (operands.size() > 1) {
            builder.appendln("(")
                    .indentln(operands, " ", builder.keyword(name) + " ")
                    .appendln()
                    .append(")");
        } else {
            builder.append(operands.get(0));
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
