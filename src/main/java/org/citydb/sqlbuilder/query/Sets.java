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

package org.citydb.sqlbuilder.query;

import java.util.List;

public class Sets {
    public static final String INTERSECT = "intersect";
    public static final String UNION = "union";
    public static final String UNION_ALL = "union all";

    public static SetOperator intersect(List<Select> operands) {
        return SetOperator.of(INTERSECT, operands);
    }

    public static SetOperator intersect(Select... operands) {
        return SetOperator.of(INTERSECT, operands);
    }

    public static SetOperator union(List<Select> operands) {
        return SetOperator.of(UNION, operands);
    }

    public static SetOperator union(Select... operands) {
        return SetOperator.of(UNION, operands);
    }

    public static SetOperator unionAll(List<Select> operands) {
        return SetOperator.of(UNION_ALL, operands);
    }

    public static SetOperator unionAll(Select... operands) {
        return SetOperator.of(UNION_ALL, operands);
    }
}
