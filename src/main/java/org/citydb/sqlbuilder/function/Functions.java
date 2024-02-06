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

public class Functions {

    public static Function avg(Expression expression) {
        return Function.of("avg", expression);
    }

    public static Function count() {
        return count(Column.WILDCARD);
    }

    public static Function count(Expression expression) {
        return Function.of("count", expression);
    }

    public static Function max(Expression expression) {
        return Function.of("max", expression);
    }

    public static Function min(Expression expression) {
        return Function.of("min", expression);
    }

    public static Function sum(Expression expression) {
        return Function.of("sum", expression);
    }

    public static Function length(Expression expression) {
        return Function.of("length", expression);
    }

    public static Function upper(Expression expression) {
        return Function.of("upper", expression);
    }

    public static Function lower(Expression expression) {
        return Function.of("lower", expression);
    }

    public static Function trim(Expression expression) {
        return Function.of("trim", expression);
    }

    public static Function cumeDist() {
        return Function.of("cume_dist");
    }

    public static Function denseRank() {
        return Function.of("dense_rank");
    }

    public static Function firstValue(Expression expression) {
        return Function.of("first_value", expression);
    }

    public static Function lastValue(Expression expression) {
        return Function.of("last_value", expression);
    }

    public static Function lag(Expression expression) {
        return Function.of("lag", expression);
    }

    public static Function lag(Expression expression, Literal<?> offset) {
        return Function.of("lag", expression, offset);
    }

    public static Function lag(Expression expression, Literal<?> offset, Expression defaultValue) {
        return Function.of("lag", expression, defaultValue);
    }

    public static Function lead(Expression expression) {
        return Function.of("lead", expression);
    }

    public static Function lead(Expression expression, Literal<?> offset) {
        return Function.of("lead", expression, offset);
    }

    public static Function lead(Expression expression, Literal<?> offset, Expression defaultValue) {
        return Function.of("lead", expression, defaultValue);
    }

    public static Function nthValue(Expression expression, Literal<?> offset) {
        return Function.of("nth_value", expression, offset);
    }

    public static Function ntile(Literal<?> offset) {
        return Function.of("ntile", offset);
    }

    public static Function percentRank() {
        return Function.of("percent_rank");
    }

    public static Function rank() {
        return Function.of("rank");
    }

    public static Function rowNumber() {
        return Function.of("row_number");
    }
}
