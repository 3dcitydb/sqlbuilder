/*
 * 3D City Database - The Open Source CityGML Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2013 - 2021
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

package org.citydb.sqlbuilder.select.operator.set;

import org.citydb.sqlbuilder.select.Select;

import java.util.List;

public class SetOperationFactory {

    public static SetOperator intersect(List<Select> operands) {
        return new SetOperator(SetOperationName.INTERSECT, operands);
    }

    public static SetOperator intersect(Select... operands) {
        return new SetOperator(SetOperationName.INTERSECT, operands);
    }

    public static SetOperator union(List<Select> operands) {
        return new SetOperator(SetOperationName.UNION, operands);
    }

    public static SetOperator union(Select... operands) {
        return new SetOperator(SetOperationName.UNION, operands);
    }

    public static SetOperator unionAll(List<Select> operands) {
        return new SetOperator(SetOperationName.UNION_ALL, operands);
    }

    public static SetOperator unionAll(Select... operands) {
        return new SetOperator(SetOperationName.UNION_ALL, operands);
    }

    public static SetOperator generic(String sqlName, List<Select> operands) {
        return new SetOperator(sqlName, operands);
    }

    public static SetOperator generic(String sqlName, Select... operands) {
        return new SetOperator(sqlName, operands);
    }

}
