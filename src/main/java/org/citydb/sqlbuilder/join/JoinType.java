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

public enum JoinType {
    INNER_JOIN("inner join"),
    LEFT_JOIN("left join"),
    RIGHT_JOIN("right join"),
    FULL_JOIN("full join");

    private final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }

    public String toSQL(SQLBuilder builder) {
        return builder.keyword(sql);
    }

    @Override
    public String toString() {
        return sql;
    }
}
