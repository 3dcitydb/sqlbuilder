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

package org.citydb.sqlbuilder;

public class SQLBuildOptions {
    private String indent;
    private String identifierDelimiter;
    private IdentifierCase identifierCase = IdentifierCase.UNCHANGED;
    private KeywordCase keywordCase = KeywordCase.LOWERCASE;
    private boolean useJdbcEscapeNotation = true;

    public enum IdentifierCase {
        UPPERCASE,
        LOWERCASE,
        UNCHANGED
    }

    public enum KeywordCase {
        UPPERCASE,
        LOWERCASE
    }

    private SQLBuildOptions() {
    }

    public static SQLBuildOptions defaults() {
        return new SQLBuildOptions();
    }

    public String getIndent() {
        return indent;
    }

    boolean isSetIndent() {
        return indent != null && !indent.isEmpty();
    }

    public SQLBuildOptions setIndent(String indent) {
        this.indent = indent;
        return this;
    }

    public String getIdentifierDelimiter() {
        return identifierDelimiter;
    }

    boolean isSetIdentifierDelimiter() {
        return identifierDelimiter != null && !identifierDelimiter.isEmpty();
    }

    public SQLBuildOptions setIdentifierDelimiter(String identifierDelimiter) {
        this.identifierDelimiter = identifierDelimiter;
        return this;
    }

    public IdentifierCase getIdentifierCase() {
        return identifierCase;
    }

    public SQLBuildOptions setIdentifierCase(IdentifierCase identifierCase) {
        if (identifierCase != null) {
            this.identifierCase = identifierCase;
        }

        return this;
    }

    public KeywordCase getKeywordCase() {
        return keywordCase;
    }

    public SQLBuildOptions setKeywordCase(KeywordCase keywordCase) {
        if (keywordCase != null) {
            this.keywordCase = keywordCase;
        }

        return this;
    }

    public boolean isUseJDBCEscapeNotation() {
        return useJdbcEscapeNotation;
    }

    public SQLBuildOptions setUseJDBCEscapeNotation(boolean useJdbcEscapeNotation) {
        this.useJdbcEscapeNotation = useJdbcEscapeNotation;
        return this;
    }
}
