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

package org.citydb.sqlbuilder.predicate;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.predicate.logical.BinaryLogicalOperator;
import org.citydb.sqlbuilder.predicate.logical.Not;

public interface Predicate extends Expression {
    default BinaryLogicalOperator and(Predicate operand) {
        return Predicates.and(this, operand);
    }

    default BinaryLogicalOperator andExists(Expression operand) {
        return Predicates.and(this, Predicates.exists(operand));
    }

    default BinaryLogicalOperator andNot(Predicate operand) {
        return Predicates.and(this, Not.of(operand));
    }

    default BinaryLogicalOperator andNotExists(Expression operand) {
        return Predicates.and(this, Not.of(Predicates.exists(operand)));
    }

    default BinaryLogicalOperator or(Predicate operand) {
        return Predicates.or(this, operand);
    }

    default BinaryLogicalOperator orExists(Expression operand) {
        return Predicates.or(this, Predicates.exists(operand));
    }

    default BinaryLogicalOperator orNot(Predicate operand) {
        return Predicates.or(this, Not.of(operand));
    }

    default BinaryLogicalOperator orNotExists(Expression operand) {
        return Predicates.or(this, Not.of(Predicates.exists(operand)));
    }

    default Not not() {
        return Not.of(this);
    }
}
