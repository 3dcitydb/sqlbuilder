/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2026
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

import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.Objects;
import java.util.Optional;

public class Exists implements LogicalOperation {
    private final QueryExpression operand;
    private boolean negate;
    private String alias;

    private Exists(QueryExpression operand, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.negate = negate;
    }

    public static Exists of(QueryExpression operand, boolean negate) {
        return new Exists(operand, negate);
    }

    public static Exists of(QueryExpression operand) {
        return new Exists(operand, false);
    }

    public QueryExpression getOperand() {
        return operand;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public String getOperator() {
        return !negate ? Operators.EXISTS : Operators.NOT_EXISTS;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Exists as(String alias) {
        this.alias = alias;
        return this;
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
