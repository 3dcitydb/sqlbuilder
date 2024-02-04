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

import org.citydb.sqlbuilder.common.Buildable;
import org.citydb.sqlbuilder.query.LiteralList;
import org.citydb.sqlbuilder.query.QueryExpression;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

public class SQLBuilder {
    private final StringBuilder builder = new StringBuilder();
    private final SQLBuildOptions options;
    private int level;

    private SQLBuilder(SQLBuildOptions options) {
        this.options = options;
    }

    public static SQLBuilder newInstance() {
        return new SQLBuilder(SQLBuildOptions.defaults());
    }

    public static SQLBuilder of(SQLBuildOptions options) {
        return new SQLBuilder(options);
    }

    public SQLBuildOptions getOptions() {
        return options;
    }

    public boolean isUseJdbcEscapeNotation() {
        return options.isUseJDBCEscapeNotation();
    }

    public String identifier(String identifier) {
        if (identifier != null) {
            identifier = switch (options.getIdentifierCase()) {
                case UPPERCASE -> identifier.toUpperCase(Locale.ROOT);
                case LOWERCASE -> identifier.toLowerCase(Locale.ROOT);
                default -> identifier;
            };

            return options.isSetIdentifierDelimiter() ?
                    options.getIdentifierDelimiter() + identifier + options.getIdentifierDelimiter() :
                    identifier;
        } else {
            return null;
        }
    }

    public String keyword(String keyword) {
        return keyword != null && options.getKeywordCase() == SQLBuildOptions.KeywordCase.UPPERCASE ?
                keyword.toUpperCase(Locale.ROOT) :
                keyword;
    }

    public SQLBuilder append(Buildable object) {
        if (object instanceof QueryExpression expression) {
            append(expression);
        } else {
            object.buildSQL(this);
        }

        return this;
    }

    public SQLBuilder append(QueryExpression expression) {
        if (expression instanceof LiteralList literalList) {
            builder.append("(");
            literalList.buildSQL(this);
            builder.append(")");
        } else {
            openParenthesis();
            expression.buildSQL(this);
            closeParenthesis();
        }

        return this;
    }

    public SQLBuilder append(String sql) {
        builder.append(sql);
        return this;
    }

    public SQLBuilder append(Collection<? extends Buildable> objects, String delimiter) {
        return append(objects, delimiter, false, null);
    }

    public SQLBuilder append(Collection<? extends Buildable> objects, String delimiter, boolean newline) {
        return append(objects, delimiter, newline, null);
    }

    public SQLBuilder append(Collection<? extends Buildable> objects, String delimiter, boolean newline, String prefix) {
        for (Iterator<? extends Buildable> iterator = objects.iterator(); iterator.hasNext(); ) {
            append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(delimiter);
                if (newline) {
                    newline();
                }

                if (prefix != null && !prefix.isEmpty()) {
                    builder.append(prefix);
                }
            }
        }

        return this;
    }

    public SQLBuilder indentAndAppend(Collection<? extends Buildable> objects, String delimiter) {
        return indentAndAppend(objects, delimiter, false, null);
    }

    public SQLBuilder indentAndAppend(Collection<? extends Buildable> objects, String delimiter, boolean newline) {
        return indentAndAppend(objects, delimiter, newline, null);
    }

    public SQLBuilder indentAndAppend(Collection<? extends Buildable> objects, String delimiter, boolean newline, String prefix) {
        return indent(1)
                .increaseIndent()
                .append(objects, delimiter, newline, prefix)
                .append(" ")
                .decreaseIndent();
    }

    public SQLBuilder openParenthesis() {
        builder.append("(");
        return newlineAndIncreaseIndent();
    }

    public SQLBuilder closeParenthesis() {
        newlineAndDecreaseIndent();
        builder.append(")");
        return this;
    }

    public SQLBuilder newline() {
        if (options.isSetIndent()) {
            builder.append("\n");
            return indent();
        } else {
            return this;
        }
    }

    public SQLBuilder newlineAndIncreaseIndent() {
        return increaseIndent()
                .newline();
    }

    public SQLBuilder newlineAndDecreaseIndent() {
        return decreaseIndent()
                .newline();
    }

    public SQLBuilder increaseIndent() {
        level++;
        return this;
    }

    public SQLBuilder decreaseIndent() {
        level = Math.max(--level, 0);
        return this;
    }

    public SQLBuilder indent() {
        return indent(level);
    }

    public SQLBuilder indent(int repeat) {
        if (options.isSetIndent()) {
            builder.append(options.getIndent().repeat(repeat));
        }

        return this;
    }

    public String build() {
        try {
            return builder.toString();
        } finally {
            builder.setLength(0);
            level = 0;
        }
    }
}
