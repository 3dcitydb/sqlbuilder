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

package org.citydb.sqlbuilder.predicate.logical;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.predicate.Predicate;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Not implements LogicalOperator {
    private final Predicate predicate;

    private Not(Predicate predicate) {
        this.predicate = Objects.requireNonNull(predicate, "The predicate must not be null.");
    }

    public static Not of(Predicate predicate) {
        return new Not(predicate);
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public LogicalOperatorType getType() {
        return LogicalOperatorType.NOT;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        predicate.buildInvolvedTables(tables);
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        predicate.buildInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(getType().toSQL(builder) + " ")
                .append(predicate);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
