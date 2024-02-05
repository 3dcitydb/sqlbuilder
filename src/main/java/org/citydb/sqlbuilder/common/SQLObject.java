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

package org.citydb.sqlbuilder.common;

import org.citydb.sqlbuilder.SQLBuildOptions;
import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Set;

public interface SQLObject extends Buildable {
    void buildInvolvedTables(Set<Table> tables);

    void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders);

    default String toSQL(SQLBuildOptions options) {
        SQLBuilder builder = SQLBuilder.of(options);
        buildSQL(builder);
        return builder.build();
    }

    default String toSQL() {
        return toSQL(SQLBuildOptions.defaults());
    }
}