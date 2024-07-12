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

package org.citydb.sqlbuilder.operation;

import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.Selection;
import org.citydb.sqlbuilder.schema.ColumnExpression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Case implements ColumnExpression, Selection<Case> {
    private final Map<BooleanExpression, Expression> conditions;
    private Expression otherwise;
    private String alias;

    private Case(Map<BooleanExpression, Expression> conditions, Expression otherwise, String alias) {
        this.conditions = Objects.requireNonNull(conditions, "The conditions must not be null.");
        this.otherwise = otherwise;
        this.alias = alias;
    }

    public static Case newInstance() {
        return new Case(new LinkedHashMap<>(), null, null);
    }

    public static Case of(Map<BooleanExpression, Expression> conditions, Expression otherwise, String alias) {
        return new Case(conditions, otherwise, alias);
    }

    public static Case of(Map<BooleanExpression, Expression> conditions, String alias) {
        return new Case(conditions, null, alias);
    }

    public static Case of(Map<BooleanExpression, Expression> conditions) {
        return new Case(conditions, null, null);
    }

    public Map<BooleanExpression, Expression> getConditions() {
        return conditions;
    }

    public ConditionBuilder when(BooleanExpression condition) {
        return new ConditionBuilder(condition);
    }

    public Optional<Expression> getElse() {
        return Optional.ofNullable(otherwise);
    }

    public Case orElse(Expression otherwise) {
        this.otherwise = otherwise;
        return this;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Case as(String alias) {
        this.alias = alias;
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

    public class ConditionBuilder {
        private final BooleanExpression condition;

        private ConditionBuilder(BooleanExpression condition) {
            this.condition = condition;
        }

        public Case then(Expression expression) {
            conditions.put(condition, expression);
            return Case.this;
        }
    }
}
