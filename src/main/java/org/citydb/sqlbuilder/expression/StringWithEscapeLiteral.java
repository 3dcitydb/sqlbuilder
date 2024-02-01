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

package org.citydb.sqlbuilder.expression;

public class StringWithEscapeLiteral extends JDBCEscapeLiteral<String> {
    private final String escapeCharacter;

    public StringWithEscapeLiteral(String value, String escapeCharacter) {
        super(value, JDBCEscape.ESCAPE);
        this.escapeCharacter = escapeCharacter;

        if (escapeCharacter != null && escapeCharacter.length() > 1)
            throw new IllegalArgumentException("Escape sequence may only contain null or one character.");
    }

    public StringWithEscapeLiteral(String value) {
        this(value, null);
    }

    @Override
    public SQLLiteralType getType() {
        return SQLLiteralType.STRING_WITH_ESCAPE;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder().append("'").append(value.replace("'", "''")).append("'");

        if (escapeCharacter != null) {
            if (isUseEscapeSyntax())
                tmp.append(" ").append("{").append(escape).append("'").append(escapeCharacter).append("'").append("}");
            else
                tmp.append(" ").append("escape ").append("'").append(escapeCharacter).append("'");
        }

        return tmp.toString();
    }
}
