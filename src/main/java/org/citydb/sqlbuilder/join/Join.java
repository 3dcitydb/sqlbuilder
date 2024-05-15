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

package org.citydb.sqlbuilder.join;

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.operation.BinaryComparisonOperation;
import org.citydb.sqlbuilder.operation.BooleanExpression;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Join implements SqlObject {
    private final String type;
    private final Column fromColumn;
    private final Column toColumn;
    private final List<BooleanExpression> conditions = new ArrayList<>();

    private Join(String type, Column toColumn, String operator, Column fromColumn) {
        this.type = Objects.requireNonNull(type, "The join type must not be null.");
        this.toColumn = Objects.requireNonNull(toColumn, "The target column must not be null.");
        this.fromColumn = Objects.requireNonNull(fromColumn, "The source column must not be null.");
        Objects.requireNonNull(operator, "The operator type must not be null.");
        conditions.add(BinaryComparisonOperation.of(toColumn, operator, fromColumn));
    }

    public static Join of(String type, Column toColumn, String operator, Column fromColumn) {
        return new Join(type, toColumn, operator, fromColumn);
    }

    public static Join of(String type, Table toTable, String toColumn, String operator, Column fromColumn) {
        Objects.requireNonNull(toTable, "The target table must not be null.");
        return new Join(type, toTable.column(toColumn), operator, fromColumn);
    }

    public Column getFromColumn() {
        return fromColumn;
    }

    public Column getToColumn() {
        return toColumn;
    }

    public String getType() {
        return type;
    }

    public boolean hasType(String type) {
        return this.type.equalsIgnoreCase(type);
    }

    public List<BooleanExpression> getConditions() {
        return conditions;
    }

    public Join condition(BooleanExpression condition) {
        if (condition != null) {
            conditions.add(condition);
        }

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
