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

package org.citydb.sqlbuilder.operation;

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.literal.PlaceHolder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Not implements LogicalOperation {
    private final BooleanExpression operand;
    private String alias;

    private Not(BooleanExpression operand) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
    }

    public static Not of(BooleanExpression operand) {
        return new Not(operand);
    }

    public BooleanExpression getOperand() {
        return operand;
    }

    @Override
    public String getOperator() {
        return Operators.NOT;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Not as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        operand.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append(builder.keyword(getOperator()) + " ")
                .append(operand);
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
