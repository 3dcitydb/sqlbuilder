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

import org.citydb.sqlbuilder.predicate.comparison.ComparisonOperatorType;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;

public class Joins {

    public static Join inner(Table table, String column, ComparisonOperatorType name, Column fromColumn) {
        return Join.of(JoinType.INNER_JOIN, table, column, name, fromColumn);
    }

    public static Join left(Table table, String column, ComparisonOperatorType name, Column fromColumn) {
        return Join.of(JoinType.LEFT_JOIN, table, column, name, fromColumn);
    }

    public static Join right(Table table, String column, ComparisonOperatorType name, Column fromColumn) {
        return Join.of(JoinType.RIGHT_JOIN, table, column, name, fromColumn);
    }

    public static Join full(Table table, String column, ComparisonOperatorType name, Column fromColumn) {
        return Join.of(JoinType.FULL_JOIN, table, column, name, fromColumn);
    }
}
