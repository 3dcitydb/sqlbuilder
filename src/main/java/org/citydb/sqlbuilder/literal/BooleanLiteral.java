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

package org.citydb.sqlbuilder.literal;

import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.operation.BooleanExpression;

public class BooleanLiteral extends Literal<Boolean> implements BooleanExpression, ScalarExpression {
    public static final BooleanLiteral TRUE = new BooleanLiteral(true);
    public static final BooleanLiteral FALSE = new BooleanLiteral(false);

    private BooleanLiteral(boolean value) {
        super(value);
    }

    private BooleanLiteral(Boolean value) {
        super(value);
    }

    public static BooleanLiteral of(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static BooleanLiteral of(Boolean value) {
        return value != null ? of(value.booleanValue()) : new BooleanLiteral(null);
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }
}
