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
import org.citydb.sqlbuilder.util.PlainText;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.BiFunction;

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
            return "";
        }
    }

    public String keyword(String keyword) {
        if (keyword != null) {
            return options.getKeywordCase() == SQLBuildOptions.KeywordCase.UPPERCASE ?
                    keyword.toUpperCase(Locale.ROOT) :
                    keyword.toLowerCase(Locale.ROOT);
        } else {
            return "";
        }
    }

    public SQLBuilder appendln() {
        return newline();
    }

    public SQLBuilder appendln(Buildable object) {
        return append(object).newline();
    }

    public SQLBuilder append(Buildable object) {
        if (object instanceof QueryExpression expression) {
            append(expression);
        } else {
            object.buildSQL(this);
        }

        return this;
    }

    public SQLBuilder appendln(QueryExpression expression) {
        return append(expression).newline();
    }

    public SQLBuilder append(QueryExpression expression) {
        if (expression instanceof PlainText plainText) {
            builder.append(plainText);
        } else if (expression instanceof LiteralList literalList) {
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

    public SQLBuilder appendln(String sql) {
        return append(sql).newline();
    }

    public SQLBuilder append(String sql) {
        builder.append(sql);
        return this;
    }

    public SQLBuilder append(Collection<? extends Buildable> objects, String delimiter) {
        return append(objects, delimiter, false, (object, i) -> null);
    }

    public SQLBuilder appendln(Collection<? extends Buildable> objects, String delimiter) {
        return append(objects, delimiter, true, (object, i) -> null);
    }

    public SQLBuilder append(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return append(objects, delimiter, false, (object, i) -> i > 0 ? prefix : null);
    }

    public SQLBuilder appendln(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return append(objects, delimiter, true, (object, i) -> i > 0 ? prefix : null);
    }

    public <T extends Buildable> SQLBuilder append(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return append(objects, delimiter, false, prefixBuilder);
    }

    public <T extends Buildable> SQLBuilder appendln(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return append(objects, delimiter, true, prefixBuilder);
    }

    private <T extends Buildable> SQLBuilder append(Collection<T> objects, String delimiter, boolean newline, BiFunction<T, Integer, String> prefixBuilder) {
        Iterator<T> iterator = objects.iterator();
        int counter = 0;

        while (iterator.hasNext()) {
            T object = iterator.next();
            String prefix = prefixBuilder.apply(object, counter++);
            if (prefix != null) {
                builder.append(prefix);
            }

            append(object);
            if (iterator.hasNext()) {
                builder.append(delimiter);
                if (newline) {
                    newline();
                }
            }
        }

        return this;
    }

    public SQLBuilder indentln(Buildable object) {
        return indent(object).newline();
    }

    public SQLBuilder indent(Buildable object) {
        return indent(1)
                .increaseIndent()
                .append(object)
                .decreaseIndent();
    }

    public SQLBuilder indentln(QueryExpression expression) {
        return indent(expression).newline();
    }

    public SQLBuilder indent(QueryExpression expression) {
        return indent(1)
                .increaseIndent()
                .append(expression)
                .decreaseIndent();
    }

    public SQLBuilder indentln(String sql) {
        return indent(sql).newline();
    }

    public SQLBuilder indent(String sql) {
        return indent(1).append(sql);
    }

    public SQLBuilder indent(Collection<? extends Buildable> objects, String delimiter) {
        return indent(objects, delimiter, false, (object, i) -> null);
    }

    public SQLBuilder indentln(Collection<? extends Buildable> objects, String delimiter) {
        return indent(objects, delimiter, true, (object, i) -> null);
    }

    public SQLBuilder indent(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return indent(objects, delimiter, false, (object, i) -> i > 0 ? prefix : null);
    }

    public SQLBuilder indentln(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return indent(objects, delimiter, true, (object, i) -> i > 0 ? prefix : null);
    }

    public <T extends Buildable> SQLBuilder indent(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return indent(objects, delimiter, false, prefixBuilder);
    }

    public <T extends Buildable> SQLBuilder indentln(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return indent(objects, delimiter, true, prefixBuilder);
    }

    private <T extends Buildable> SQLBuilder indent(Collection<T> objects, String delimiter, boolean newline, BiFunction<T, Integer, String> prefixBuilder) {
        return indent(1)
                .increaseIndent()
                .append(objects, delimiter, newline, prefixBuilder)
                .append(" ")
                .decreaseIndent();
    }

    public SQLBuilder openParenthesis() {
        builder.append("(");
        return increaseIndent().newline();
    }

    public SQLBuilder closeParenthesis() {
        decreaseIndent().newline();
        builder.append(")");
        return this;
    }

    private SQLBuilder newline() {
        if (options.isSetIndent()) {
            builder.append(options.getNewline());
            return indent();
        } else {
            return this;
        }
    }

    private SQLBuilder indent() {
        return indent(level);
    }

    private SQLBuilder indent(int repeat) {
        if (options.isSetIndent()) {
            builder.append(options.getIndent().repeat(repeat));
        }

        return this;
    }

    private SQLBuilder increaseIndent() {
        level++;
        return this;
    }

    private SQLBuilder decreaseIndent() {
        level = Math.max(--level, 0);
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
