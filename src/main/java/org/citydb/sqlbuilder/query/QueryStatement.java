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

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.Statement;
import org.citydb.sqlbuilder.function.Function;
import org.citydb.sqlbuilder.literal.IntegerLiteral;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.operation.Operator;
import org.citydb.sqlbuilder.schema.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class QueryStatement<T extends QueryStatement<?>> implements Statement, QueryExpression {
    protected final List<Column> groupBy;
    protected final List<Expression> having;
    protected final List<Window> window;
    protected final List<OrderBy> orderBy;
    protected Literal<?> offset;
    protected Literal<?> fetch;

    protected abstract T self();

    protected QueryStatement() {
        groupBy = new ArrayList<>();
        having = new ArrayList<>();
        window = new ArrayList<>();
        orderBy = new ArrayList<>();
    }

    protected QueryStatement(QueryStatement<?> other) {
        groupBy = new ArrayList<>(other.groupBy);
        having = new ArrayList<>(other.having);
        window = new ArrayList<>(other.window);
        orderBy = new ArrayList<>(other.orderBy);
        offset = other.offset;
        fetch = other.fetch;
    }

    public List<Column> getGroupBy() {
        return groupBy;
    }

    public T groupBy(Column... columns) {
        if (columns != null) {
            groupBy.addAll(Arrays.asList(columns));
        }

        return self();
    }

    public List<Expression> getHaving() {
        return having;
    }

    public T having(Operator... operators) {
        if (operators != null) {
            having.addAll(Arrays.asList(operators));
        }

        return self();
    }

    public T having(Function... functions) {
        if (functions != null) {
            having.addAll(Arrays.asList(functions));
        }

        return self();
    }

    public List<Window> getWindow() {
        return window;
    }

    public T window(Window... windows) {
        if (windows != null) {
            this.window.addAll(Arrays.asList(windows));
        }

        return self();
    }

    public T window(java.util.function.Function<Window, Window> builder) {
        window.add(builder.apply(Window.newInstance()));
        return self();
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public T orderBy(OrderBy... orderBy) {
        if (orderBy != null) {
            this.orderBy.addAll(Arrays.asList(orderBy));
        }

        return self();
    }

    public T orderBy(Column column) {
        orderBy.add(OrderBy.of(column));
        return self();
    }

    public Optional<Literal<?>> getOffset() {
        return Optional.ofNullable(offset);
    }

    public T offset(long offset) {
        return offset(IntegerLiteral.of(offset));
    }

    public T offset(long offset, long fetch) {
        return offset(IntegerLiteral.of(offset), IntegerLiteral.of(fetch));
    }

    public T offset(Literal<?> offset) {
        return offset(offset, null);
    }

    public T offset(Literal<?> offset, Literal<?> fetch) {
        this.offset = offset;
        this.fetch = fetch;
        return self();
    }

    public Optional<Literal<?>> getFetch() {
        return Optional.ofNullable(fetch);
    }

    public T fetch(long fetch) {
        return fetch(IntegerLiteral.of(fetch));
    }

    public T fetch(Literal<?> fetch) {
        this.fetch = fetch;
        return self();
    }

    public CommonTableExpression cte(String name, String... columns) {
        return CommonTableExpression.of(name, this, columns);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        if (!groupBy.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("group by "))
                    .indentln(groupBy, ", ");
        }

        if (!having.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("having "))
                    .indentln(having, ", ");
        }

        if (!window.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("window "))
                    .indentln(window, ", ", (window, i) -> window.getName() + builder.keyword(" as "));
        }

        if (!orderBy.isEmpty()) {
            builder.appendln()
                    .appendln(builder.keyword("order by "))
                    .indentln(orderBy, ", ");
        }

        if (offset != null) {
            builder.appendln()
                    .append(builder.keyword("offset "))
                    .append(offset)
                    .append(builder.keyword(" rows "));
        }

        if (fetch != null) {
            if (offset == null) {
                builder.appendln();
            }

            builder.append(builder.keyword("fetch "))
                    .append(builder.keyword(offset != null ? "next " : "first "))
                    .append(fetch)
                    .append(builder.keyword(" rows only "));
        }
    }
}
