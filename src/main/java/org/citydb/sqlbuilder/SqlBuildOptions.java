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

import org.citydb.sqlbuilder.util.AliasGenerator;
import org.citydb.sqlbuilder.util.PlaceholderBuilder;

import java.util.Optional;

public class SqlBuildOptions {
    private String newline = "\n";
    private String indent;
    private String identifierDelimiter;
    private IdentifierCase identifierCase = IdentifierCase.UNCHANGED;
    private KeywordCase keywordCase = KeywordCase.LOWERCASE;
    private boolean useJdbcEscapeNotation = false;
    private boolean stripParentheses = true;
    private AliasGenerator aliasGenerator;
    private PlaceholderBuilder placeholderBuilder;

    public enum IdentifierCase {
        UPPERCASE,
        LOWERCASE,
        UNCHANGED
    }

    public enum KeywordCase {
        UPPERCASE,
        LOWERCASE
    }

    private SqlBuildOptions() {
    }

    public static SqlBuildOptions defaults() {
        return new SqlBuildOptions();
    }

    public String getNewline() {
        return newline;
    }

    public SqlBuildOptions setNewline(String newline) {
        this.newline = newline;
        return this;
    }

    public String getIndent() {
        return indent;
    }

    boolean isSetIndent() {
        return indent != null && !indent.isEmpty();
    }

    public SqlBuildOptions setIndent(String indent) {
        this.indent = indent;
        return this;
    }

    public String getIdentifierDelimiter() {
        return identifierDelimiter;
    }

    boolean isSetIdentifierDelimiter() {
        return identifierDelimiter != null && !identifierDelimiter.isEmpty();
    }

    public SqlBuildOptions setIdentifierDelimiter(String identifierDelimiter) {
        this.identifierDelimiter = identifierDelimiter;
        return this;
    }

    public IdentifierCase getIdentifierCase() {
        return identifierCase;
    }

    public SqlBuildOptions setIdentifierCase(IdentifierCase identifierCase) {
        if (identifierCase != null) {
            this.identifierCase = identifierCase;
        }

        return this;
    }

    public KeywordCase getKeywordCase() {
        return keywordCase;
    }

    public SqlBuildOptions setKeywordCase(KeywordCase keywordCase) {
        if (keywordCase != null) {
            this.keywordCase = keywordCase;
        }

        return this;
    }

    public boolean isUseJdbcEscapeNotation() {
        return useJdbcEscapeNotation;
    }

    public SqlBuildOptions setUseJdbcEscapeNotation(boolean useJdbcEscapeNotation) {
        this.useJdbcEscapeNotation = useJdbcEscapeNotation;
        return this;
    }

    public boolean isStripParentheses() {
        return stripParentheses;
    }

    public SqlBuildOptions setStripParentheses(boolean stripParentheses) {
        this.stripParentheses = stripParentheses;
        return this;
    }

    public Optional<AliasGenerator> getAliasGenerator() {
        return Optional.ofNullable(aliasGenerator);
    }

    public SqlBuildOptions setAliasGenerator(AliasGenerator aliasGenerator) {
        this.aliasGenerator = aliasGenerator;
        return this;
    }

    public Optional<PlaceholderBuilder> getPlaceholderBuilder() {
        return Optional.ofNullable(placeholderBuilder);
    }

    public SqlBuildOptions setPlaceholderBuilder(PlaceholderBuilder placeholderBuilder) {
        this.placeholderBuilder = placeholderBuilder;
        return this;
    }
}
