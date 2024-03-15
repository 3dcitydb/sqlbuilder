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

package org.citydb.sqlbuilder.update;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Column;

import java.util.List;
import java.util.Objects;

public class UpdateValue implements SqlObject {
    private final Column column;
    private final Expression value;

    private UpdateValue(Column column, Expression value) {
        this.column = Objects.requireNonNull(column, "The column must not be null.");
        this.value = Objects.requireNonNull(value, "The value expression must not be null.");
    }

    public static UpdateValue of(Column column, Expression value) {
        return new UpdateValue(column, value);
    }

    public Column getColumn() {
        return column;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        column.getPlaceHolders(placeHolders);
        value.getPlaceHolders(placeHolders);
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
