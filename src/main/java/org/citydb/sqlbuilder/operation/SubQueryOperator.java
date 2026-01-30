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
import org.citydb.sqlbuilder.literal.ScalarExpression;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.Objects;

public class SubQueryOperator implements Operation, ScalarExpression {
    private final String operator;
    private final QueryExpression operand;

    private SubQueryOperator(String operator, QueryExpression operand) {
        this.operator = Objects.requireNonNull(operator, "The operator must not be null.");
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
    }

    public static SubQueryOperator of(String operator, QueryExpression operand) {
        return new SubQueryOperator(operator, operand);
    }

    public static SubQueryOperator all(QueryExpression operand) {
        return new SubQueryOperator(Operators.ALL, operand);
    }

    public static SubQueryOperator any(QueryExpression operand) {
        return new SubQueryOperator(Operators.ANY, operand);
    }

    public static SubQueryOperator some(QueryExpression operand) {
        return new SubQueryOperator(Operators.SOME, operand);
    }

    @Override
    public String getOperator() {
        return operator;
    }

    public QueryExpression getOperand() {
        return operand;
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
