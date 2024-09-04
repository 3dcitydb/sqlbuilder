/*
 * 3D City Database - The Open Source CityGML Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2013 - 2024
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.lrg.tum.de/gis/
 *
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 *
 * Virtual City Systems, Berlin <https://vc.systems/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
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

package org.citydb.sqlbuilder.select.join;

import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.PredicateToken;
import org.citydb.sqlbuilder.select.operator.comparison.BinaryComparisonOperator;
import org.citydb.sqlbuilder.select.operator.comparison.ComparisonName;
import org.citydb.sqlbuilder.select.operator.logical.BinaryLogicalOperator;
import org.citydb.sqlbuilder.select.operator.logical.LogicalOperationName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Join {
    private final Column fromColumn;
    private final Column toColumn;
    private final List<PredicateToken> conditions;
    private JoinName name;
    private LogicalOperationName conditionOperationName = LogicalOperationName.AND;

    public Join(JoinName name, Column toColumn, ComparisonName binaryComparison, Column fromColumn) {
        this.name = name;
        this.toColumn = toColumn;
        this.fromColumn = fromColumn;

        conditions = new ArrayList<>();
        conditions.add(new BinaryComparisonOperator(this.fromColumn, binaryComparison, this.toColumn));
    }

    public Join(JoinName name, Table table, String column, ComparisonName binaryComparison, Column fromColumn) {
        this(name, table.getColumn(column), binaryComparison, fromColumn);
    }

    public Column getFromColumn() {
        return fromColumn;
    }

    public Column getToColumn() {
        return toColumn;
    }

    public JoinName getJoinName() {
        return name;
    }

    public void setJoinName(JoinName name) {
        if (name != null)
            this.name = name;
    }

    public Join addCondition(PredicateToken condition) {
        conditions.add(condition);
        return this;
    }

    public Join addConditions(PredicateToken... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public boolean removeCondition(PredicateToken condition) {
        return conditions.remove(condition);
    }

    public List<PredicateToken> getConditions() {
        return new ArrayList<>(conditions);
    }

    public Join unsetConditions() {
        conditions.clear();
        return this;
    }

    public LogicalOperationName getConditionOperationName() {
        return conditionOperationName;
    }

    public void setConditionOperationName(LogicalOperationName conditionOperationName) {
        this.conditionOperationName = conditionOperationName;
    }

    public void getInvolvedTables(Set<Table> tables) {
        fromColumn.getInvolvedTables(tables);
        toColumn.getInvolvedTables(tables);
    }

    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        for (PredicateToken condition : conditions)
            condition.getInvolvedPlaceHolders(statements);
    }

    @Override
    public String toString() {
        return name + " " + toColumn.getTable() + " on " + new BinaryLogicalOperator(conditionOperationName, conditions);
    }

}
