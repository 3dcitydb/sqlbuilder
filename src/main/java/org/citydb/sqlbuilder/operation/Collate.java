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
import org.citydb.sqlbuilder.literal.ScalarExpression;

import java.util.Objects;

public class Collate implements ScalarExpression, Operation {
    private final Expression expression;
    private final String collation;

    private Collate(Expression expression, String collation) {
        this.expression = Objects.requireNonNull(expression, "The expression must not be null.");
        this.collation = Objects.requireNonNull(collation, "The collation must not be null.");
    }

    public static Collate of(Expression expression, String collation) {
        return new Collate(expression, collation);
    }

    public Expression getExpression() {
        return expression;
    }

    public String getCollation() {
        return collation;
    }

    @Override
    public String getOperator() {
        return Operators.COLLATE;
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
