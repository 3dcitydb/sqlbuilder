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

public abstract class AbstractJDBCEscapeLiteral<T> extends AbstractSQLLiteral<T> {
    protected final JDBCEscape escape;
    private boolean useEscapeSyntax = true;

    public AbstractJDBCEscapeLiteral(T value, JDBCEscape esacpe) {
        super(value);
        this.escape = esacpe;
    }

    public boolean isUseEscapeSyntax() {
        return useEscapeSyntax;
    }

    public void setUseEscapeSyntax(boolean useEscapeSyntax) {
        this.useEscapeSyntax = useEscapeSyntax;
    }

    @Override
    public String toString() {
        return useEscapeSyntax ?
                "{" + escape + "'" + value.toString() + "'" + "}" :
                "'" + value.toString() + "'";
    }

}
