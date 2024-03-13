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

package org.citydb.sqlbuilder.query;

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.*;

public class SetOperator extends QueryStatement<SetOperator> {
    private final String type;
    private final List<Select> operands;

    private SetOperator(String type, List<Select> operands) {
        this.type = Objects.requireNonNull(type, "The set type must not be null.");
        this.operands = Objects.requireNonNull(operands, "The operands list must not be null.");
        if (operands.size() < 2) {
            throw new IllegalArgumentException("A set operator requires at leas two or more operands.");
        }
    }

    public static SetOperator of(String type, List<Select> operands) {
        return new SetOperator(type, operands);
    }

    public static SetOperator of(String type, Select... operands) {
        return new SetOperator(type, operands != null ?
                new ArrayList<>(Arrays.asList(operands)) :
                null);
    }

    public String getType() {
        return type;
    }

    public boolean hasType(String type) {
        return this.type.equalsIgnoreCase(type);
    }

    public List<Select> getOperands() {
        return operands;
    }

    public SetOperator add(Select... operands) {
        return operands != null ? add(Arrays.asList(operands)) : this;
    }

    public SetOperator add(List<Select> operands) {
        if (operands != null && !operands.isEmpty()) {
            operands.stream()
                    .filter(Objects::nonNull)
                    .forEach(this.operands::add);
        }

        return this;
    }

    @Override
    public List<PlaceHolder> getPlaceHolders() {
        return operands.stream()
                .map(Select::getPlaceHolders)
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        operands.forEach(operand -> operand.getPlaceHolders(placeHolders));
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        for (Iterator<Select> iterator = operands.iterator(); iterator.hasNext(); ) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.appendln(" ")
                        .append(builder.keyword(type))
                        .appendln(" ");
            }
        }

        super.buildSql(builder);
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return toSql();
    }

    @Override
    protected SetOperator self() {
        return this;
    }
}
