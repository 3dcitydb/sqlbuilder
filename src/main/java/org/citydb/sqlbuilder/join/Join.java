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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.SQLObject;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.predicate.Predicates;
import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperator;
import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperatorType;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Join implements SQLObject {
    private final JoinType type;
    private final Column fromColumn;
    private final Column toColumn;
    private final List<Predicate> conditions = new ArrayList<>();

    private Join(JoinType type, Column toColumn, ComparisonOperatorType operatorType, Column fromColumn) {
        this.type = Objects.requireNonNull(type, "The join type must not be null.");
        this.toColumn = Objects.requireNonNull(toColumn, "The target column must not be null.");
        this.fromColumn = Objects.requireNonNull(fromColumn, "The source column must not be null.");
        Objects.requireNonNull(operatorType, "The comparison operator must not be null.");
        conditions.add(ComparisonOperator.of(fromColumn, operatorType, toColumn));
    }

    public static Join of(JoinType type, Column toColumn, ComparisonOperatorType operatorType, Column fromColumn) {
        return new Join(type, toColumn, operatorType, fromColumn);
    }

    public static Join of(JoinType type, Table toTable, String toColumn, ComparisonOperatorType operatorType, Column fromColumn) {
        Objects.requireNonNull(toTable, "The target table must not be null.");
        return new Join(type, toTable.column(toColumn), operatorType, fromColumn);
    }

    public Column getFromColumn() {
        return fromColumn;
    }

    public Column getToColumn() {
        return toColumn;
    }

    public JoinType getType() {
        return type;
    }

    public List<Predicate> getConditions() {
        return conditions;
    }

    public Join condition(Predicate condition) {
        if (condition != null) {
            conditions.add(condition);
        }

        return this;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        toColumn.getInvolvedTables(tables);
        conditions.forEach(condition -> condition.getInvolvedTables(tables));
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        toColumn.getInvolvedPlaceHolders(placeHolders);
        conditions.forEach(condition -> condition.getInvolvedPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(type.toSQL(builder) + " ")
                .append(toColumn.getTable())
                .append(builder.keyword(" on "))
                .append(Predicates.and(conditions));
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
