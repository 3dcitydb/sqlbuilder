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
import org.citydb.sqlbuilder.query.QueryExpression;
import org.citydb.sqlbuilder.util.PlainText;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.BiFunction;

public class SqlBuilder {
    private final StringBuilder builder = new StringBuilder();
    private final SqlBuildOptions options;
    private int level;

    private SqlBuilder(SqlBuildOptions options) {
        this.options = options;
    }

    public static SqlBuilder newInstance() {
        return new SqlBuilder(SqlBuildOptions.defaults());
    }

    public static SqlBuilder of(SqlBuildOptions options) {
        return new SqlBuilder(options);
    }

    public SqlBuildOptions getOptions() {
        return options;
    }

    public boolean isUseJdbcEscapeNotation() {
        return options.isUseJDBCEscapeNotation();
    }

    public String build() {
        try {
            return options.isStripParentheses() ?
                    stripParentheses(builder.toString()) :
                    builder.toString();
        } finally {
            builder.setLength(0);
            level = 0;
        }
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
            return options.getKeywordCase() == SqlBuildOptions.KeywordCase.UPPERCASE ?
                    keyword.toUpperCase(Locale.ROOT) :
                    keyword.toLowerCase(Locale.ROOT);
        } else {
            return "";
        }
    }

    public SqlBuilder appendln() {
        return newline();
    }

    public SqlBuilder appendln(Buildable object) {
        return append(object).newline();
    }

    public SqlBuilder append(Buildable object) {
        if (object instanceof QueryExpression expression) {
            append(expression);
        } else {
            object.buildSql(this);
        }

        return this;
    }

    public SqlBuilder appendln(QueryExpression expression) {
        return append(expression).newline();
    }

    public SqlBuilder append(QueryExpression expression) {
        if (expression instanceof PlainText plainText) {
            builder.append(plainText);
        } else {
            leftParenthesis();
            expression.buildSql(this);
            rightParenthesis();
        }

        return this;
    }

    public SqlBuilder appendln(String sql) {
        return append(sql).newline();
    }

    public SqlBuilder append(String sql) {
        builder.append(sql);
        return this;
    }

    public SqlBuilder append(Collection<? extends Buildable> objects, String delimiter) {
        return append(objects, delimiter, false, (object, i) -> null);
    }

    public SqlBuilder appendln(Collection<? extends Buildable> objects, String delimiter) {
        return append(objects, delimiter, true, (object, i) -> null);
    }

    public SqlBuilder append(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return append(objects, delimiter, false, (object, i) -> i > 0 ? prefix : null);
    }

    public SqlBuilder appendln(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return append(objects, delimiter, true, (object, i) -> i > 0 ? prefix : null);
    }

    public <T extends Buildable> SqlBuilder append(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return append(objects, delimiter, false, prefixBuilder);
    }

    public <T extends Buildable> SqlBuilder appendln(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return append(objects, delimiter, true, prefixBuilder);
    }

    private <T extends Buildable> SqlBuilder append(Collection<T> objects, String delimiter, boolean newline, BiFunction<T, Integer, String> prefixBuilder) {
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

    public SqlBuilder indentln(Buildable object) {
        return indent(object).newline();
    }

    public SqlBuilder indent(Buildable object) {
        return indent(1)
                .increaseIndent()
                .append(object)
                .decreaseIndent();
    }

    public SqlBuilder indentln(QueryExpression expression) {
        return indent(expression).newline();
    }

    public SqlBuilder indent(QueryExpression expression) {
        return indent(1)
                .increaseIndent()
                .append(expression)
                .decreaseIndent();
    }

    public SqlBuilder indentln(String sql) {
        return indent(sql).newline();
    }

    public SqlBuilder indent(String sql) {
        return indent(1).append(sql);
    }

    public SqlBuilder indent(Collection<? extends Buildable> objects, String delimiter) {
        return indent(objects, delimiter, false, (object, i) -> null);
    }

    public SqlBuilder indentln(Collection<? extends Buildable> objects, String delimiter) {
        return indent(objects, delimiter, true, (object, i) -> null);
    }

    public SqlBuilder indent(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return indent(objects, delimiter, false, (object, i) -> i > 0 ? prefix : null);
    }

    public SqlBuilder indentln(Collection<? extends Buildable> objects, String delimiter, String prefix) {
        return indent(objects, delimiter, true, (object, i) -> i > 0 ? prefix : null);
    }

    public <T extends Buildable> SqlBuilder indent(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return indent(objects, delimiter, false, prefixBuilder);
    }

    public <T extends Buildable> SqlBuilder indentln(Collection<T> objects, String delimiter, BiFunction<T, Integer, String> prefixBuilder) {
        return indent(objects, delimiter, true, prefixBuilder);
    }

    private <T extends Buildable> SqlBuilder indent(Collection<T> objects, String delimiter, boolean newline, BiFunction<T, Integer, String> prefixBuilder) {
        return indent(1)
                .increaseIndent()
                .append(objects, delimiter, newline, prefixBuilder)
                .append(" ")
                .decreaseIndent();
    }

    public SqlBuilder leftParenthesis() {
        builder.append("(");
        return increaseIndent().newline();
    }

    public SqlBuilder rightParenthesis() {
        decreaseIndent().newline();
        builder.append(")");
        return this;
    }

    private SqlBuilder newline() {
        if (options.isSetIndent()) {
            builder.append(options.getNewline());
            return indent();
        } else {
            return this;
        }
    }

    private SqlBuilder indent() {
        return indent(level);
    }

    private SqlBuilder indent(int repeat) {
        if (options.isSetIndent()) {
            builder.append(options.getIndent().repeat(repeat));
        }

        return this;
    }

    private SqlBuilder increaseIndent() {
        level++;
        return this;
    }

    private SqlBuilder decreaseIndent() {
        level = Math.max(--level, 0);
        return this;
    }

    private String stripParentheses(String sql) {
        while (sql.startsWith("(") && sql.endsWith(")")) {
            char[] chars = sql.toCharArray();
            int count = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    count++;
                } else if (chars[i] == ')'
                        && --count == 0
                        && i < chars.length - 1) {
                    return sql;
                }
            }

            sql = sql.substring(1, sql.length() - 1);
        }

        return sql;
    }
}
