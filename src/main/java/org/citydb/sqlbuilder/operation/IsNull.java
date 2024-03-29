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

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.SqlVisitor;

import java.util.Objects;
import java.util.Optional;

public class IsNull implements ComparisonOperation {
    private final Expression operand;
    private boolean negate;
    private String alias;

    private IsNull(Expression operand, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.negate = negate;
    }

    public static IsNull of(Expression operand, boolean negate) {
        return new IsNull(operand, negate);
    }

    public static IsNull of(Expression operand) {
        return new IsNull(operand, false);
    }

    public Expression getOperand() {
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
        return !negate ? Operators.IS_NULL : Operators.IS_NOT_NULL;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public IsNull as(String alias) {
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
