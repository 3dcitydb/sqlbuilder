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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.Statement;
import org.citydb.sqlbuilder.function.Function;
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.schema.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class QueryStatement<T extends QueryStatement<?>> implements Statement, QueryExpression {
    protected final List<Column> groupBy;
    protected final List<Expression> having;
    protected final List<OrderBy> orderBy;
    protected Long offset;
    protected Long fetch;

    protected abstract T self();

    protected QueryStatement() {
        groupBy = new ArrayList<>();
        having = new ArrayList<>();
        orderBy = new ArrayList<>();
    }

    protected QueryStatement(QueryStatement<?> other) {
        groupBy = new ArrayList<>(other.groupBy);
        having = new ArrayList<>(other.having);
        orderBy = new ArrayList<>(other.orderBy);
        offset = other.offset;
        fetch = other.fetch;
    }

    public List<Column> getGroupBy() {
        return groupBy;
    }

    public T groupBy(Column... columns) {
        groupBy.addAll(Arrays.asList(columns));
        return self();
    }

    public List<Expression> getHaving() {
        return having;
    }

    public T having(Predicate... predicates) {
        having.addAll(Arrays.asList(predicates));
        return self();
    }

    public T having(Function... functions) {
        having.addAll(Arrays.asList(functions));
        return self();
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public T orderBy(OrderBy... orderBy) {
        this.orderBy.addAll(Arrays.asList(orderBy));
        return self();
    }

    public T orderBy(Column column) {
        orderBy.add(OrderBy.of(column));
        return self();
    }

    public Optional<Long> getOffset() {
        return Optional.ofNullable(offset);
    }

    public T offset(long offset) {
        this.offset = offset;
        return self();
    }

    public T offset(long offset, long fetch) {
        this.offset = offset;
        this.fetch = fetch;
        return self();
    }

    public Optional<Long> getFetch() {
        return Optional.ofNullable(fetch);
    }

    public T fetch(long fetch) {
        this.fetch = fetch;
        return self();
    }

    public CommonTableExpression cte(String name, String... columns) {
        return CommonTableExpression.of(name, this, columns);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        if (!groupBy.isEmpty()) {
            builder.newline()
                    .append(builder.keyword("group by "))
                    .newline()
                    .indentAndAppend(groupBy, ", ", true);
        }

        if (!having.isEmpty()) {
            builder.newline()
                    .append(builder.keyword("having "))
                    .newline()
                    .indentAndAppend(having, ", ", true);
        }

        if (!orderBy.isEmpty()) {
            builder.newline()
                    .append(builder.keyword("order by "))
                    .newline()
                    .indentAndAppend(orderBy, ", ", true);
        }

        if (offset != null) {
            builder.newline()
                    .append(builder.keyword("offset "))
                    .append(String.valueOf(offset))
                    .append(builder.keyword(" rows "));
        }

        if (fetch != null) {
            if (offset == null) {
                builder.newline();
            }

            builder.append(builder.keyword("fetch "))
                    .append(builder.keyword(offset != null ? "next " : "first "))
                    .append(String.valueOf(fetch))
                    .append(builder.keyword(" rows only "));
        }
    }
}
