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

package org.citydb.sqlbuilder.schema;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.function.Function;
import org.citydb.sqlbuilder.function.Functions;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.ScalarExpression;
import org.citydb.sqlbuilder.operation.ArithmeticExpression;

public interface ColumnExpression extends ArithmeticExpression, ScalarExpression {

    default Function avg() {
        return Functions.avg(this);
    }

    default Function count() {
        return Functions.count(this);
    }

    default Function max() {
        return Functions.max(this);
    }

    default Function min() {
        return Functions.min(this);
    }

    default Function sum() {
        return Functions.sum(this);
    }

    default Function length() {
        return Functions.length(this);
    }

    default Function upper() {
        return Functions.upper(this);
    }

    default Function lower() {
        return Functions.lower(this);
    }

    default Function trim() {
        return Functions.trim(this);
    }

    default Function concat(Expression argument) {
        return Functions.concat(this, argument);
    }

    default Function firstValue() {
        return Functions.firstValue(this);
    }

    default Function lastValue() {
        return Functions.lastValue(this);
    }

    default Function lag() {
        return Functions.lag(this);
    }

    default Function lag(Literal<?> offset) {
        return Functions.lag(this, offset);
    }

    default Function lag(Literal<?> offset, Expression defaultValue) {
        return Functions.lag(this, offset, defaultValue);
    }

    default Function lead() {
        return Functions.lead(this);
    }

    default Function lead(Literal<?> offset) {
        return Functions.lead(this, offset);
    }

    default Function lead(Literal<?> offset, Expression defaultValue) {
        return Functions.lead(this, offset, defaultValue);
    }

    default Function nthValue(Literal<?> offset) {
        return Functions.nthValue(this, offset);
    }
}
