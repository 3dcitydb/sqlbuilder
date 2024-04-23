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

package org.citydb.sqlbuilder.function;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.schema.Column;

import java.util.Arrays;
import java.util.List;

public class Functions {
    public static final String AVG = "avg";
    public static final String COUNT = "count";
    public static final String MAX = "max";
    public static final String MIN = "min";
    public static final String SUM = "sum";
    public static final String LENGTH = "length";
    public static final String UPPER = "upper";
    public static final String LOWER = "lower";
    public static final String TRIM = "trim";
    public static final String CONCAT = "concat";
    public static final String CUME_DIST = "cume_dist";
    public static final String DENSE_RANK = "dense_rank";
    public static final String FIRST_VALUE = "first_value";
    public static final String LAST_VALUE = "last_value";
    public static final String LAG = "lag";
    public static final String LEAD = "lead";
    public static final String NTH_VALUE = "nth_value";
    public static final String NTILE = "ntile";
    public static final String PERCENT_RANK = "percent_rank";
    public static final String RANK = "rank";
    public static final String ROW_NUMBER = "row_number";

    public static Function avg(Expression argument) {
        return Function.of(AVG, argument);
    }

    public static Function count() {
        return count(Column.WILDCARD);
    }

    public static Function count(Expression argument) {
        return Function.of(COUNT, argument);
    }

    public static Function max(Expression argument) {
        return Function.of(MAX, argument);
    }

    public static Function min(Expression argument) {
        return Function.of(MIN, argument);
    }

    public static Function sum(Expression argument) {
        return Function.of(SUM, argument);
    }

    public static Function length(Expression argument) {
        return Function.of(LENGTH, argument);
    }

    public static Function upper(Object argument) {
        return Function.of(UPPER, argument instanceof Expression expression ? expression : Literal.ofScalar(argument));
    }

    public static Function lower(Object argument) {
        return Function.of(LOWER, argument instanceof Expression expression ? expression : Literal.ofScalar(argument));
    }

    public static Function trim(Object argument) {
        return Function.of(TRIM, argument instanceof Expression expression ? expression : Literal.ofScalar(argument));
    }

    public static Function concat(Object argument) {
        return Function.of(CONCAT, argument instanceof Expression expression ? expression : Literal.ofScalar(argument));
    }

    public static Function concat(Object... arguments) {
        return Function.of(CONCAT, Arrays.stream(arguments)
                .map(argument -> argument instanceof Expression expression ? expression : Literal.ofScalar(argument))
                .toList());
    }

    public static Function concat(List<? extends Expression> arguments) {
        return Function.of(CONCAT, arguments);
    }

    public static Function cumeDist() {
        return Function.of(CUME_DIST);
    }

    public static Function denseRank() {
        return Function.of(DENSE_RANK);
    }

    public static Function firstValue(Expression argument) {
        return Function.of(FIRST_VALUE, argument);
    }

    public static Function lastValue(Expression argument) {
        return Function.of(LAST_VALUE, argument);
    }

    public static Function lag(Expression argument) {
        return Function.of(LAG, argument);
    }

    public static Function lag(Expression argument, Literal<?> offset) {
        return Function.of(LAG, argument, offset);
    }

    public static Function lag(Expression argument, Literal<?> offset, Expression defaultValue) {
        return Function.of(LAG, argument, offset, defaultValue);
    }

    public static Function lead(Expression argument) {
        return Function.of(LEAD, argument);
    }

    public static Function lead(Expression argument, Literal<?> offset) {
        return Function.of(LEAD, argument, offset);
    }

    public static Function lead(Expression argument, Literal<?> offset, Expression defaultValue) {
        return Function.of(LEAD, argument, offset, defaultValue);
    }

    public static Function nthValue(Expression argument, Literal<?> offset) {
        return Function.of(NTH_VALUE, argument, offset);
    }

    public static Function ntile(Literal<?> offset) {
        return Function.of(NTILE, offset);
    }

    public static Function percentRank() {
        return Function.of(PERCENT_RANK);
    }

    public static Function rank() {
        return Function.of(RANK);
    }

    public static Function rowNumber() {
        return Function.of(ROW_NUMBER);
    }
}
