/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2026
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

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlVisitor;

import java.util.Objects;
import java.util.Optional;

public class OrderBy implements SqlObject {
    public static final String NULLS_FIRST = "nulls first";
    public static final String NULLS_LAST = "nulls last";
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    private final Selection<?> sortExpression;
    private final String sortOrder;
    private String nullOrder;

    private OrderBy(Selection<?> sortExpression, String sortOrder, String nullOrder) {
        this.sortExpression = Objects.requireNonNull(sortExpression, "The sort expression must not be null.");
        this.sortOrder = sortOrder != null ? sortOrder : ASCENDING;
        this.nullOrder = nullOrder;
    }

    public static OrderBy of(Selection<?> sortExpression, String sortOrder, String nullOrder) {
        return new OrderBy(sortExpression, sortOrder, nullOrder);
    }

    public static OrderBy of(Selection<?> sortExpression, String sortOrder) {
        return new OrderBy(sortExpression, sortOrder, null);
    }

    public static OrderBy of(Selection<?> sortExpression) {
        return new OrderBy(sortExpression, ASCENDING, null);
    }

    public Selection<?> getSortExpression() {
        return sortExpression;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public Optional<String> getNullOrder() {
        return Optional.ofNullable(nullOrder);
    }

    public OrderBy nullsFirst() {
        nullOrder = NULLS_FIRST;
        return this;
    }

    public OrderBy nullsLast() {
        nullOrder = NULLS_LAST;
        return this;
    }

    public OrderBy nullOrder(String nullOrder) {
        this.nullOrder = nullOrder;
        return this;
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return toSql();
    }
}
