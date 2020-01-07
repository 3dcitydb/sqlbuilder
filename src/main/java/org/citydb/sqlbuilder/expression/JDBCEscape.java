/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * http://www.3dcitydb.org/
 *
 * Copyright 2013-2018 Claus Nagel <claus.nagel@gmail.com>
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

package org.citydb.sqlbuilder.expression;

public enum JDBCEscape {
    SCALAR_FUNCTION("fn "),
    DATE("d "),
    TIME("t "),
    TIMESTAMP("ts "),
    OUTER_JOIN("oj "),
    STORED_PROCEDURE("call "),
    STORED_PROCEDURE_WITH_RETURN("?= call "),
    ESCAPE("escape ");

    final String escape;

    JDBCEscape(String escape) {
        this.escape = escape;
    }

    @Override
    public String toString() {
        return escape;
    }

}
