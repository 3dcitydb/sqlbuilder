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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.ColumnExpression;
import org.citydb.sqlbuilder.schema.Projection;
import org.citydb.sqlbuilder.schema.Table;

import java.util.*;

public class Function implements ColumnExpression, Projection {
    private final String name;
    private final String alias;
    private final List<Expression> arguments;
    private final boolean useParentheses;

    private Function(String name, String alias, boolean useParentheses, List<Expression> arguments) {
        this.name = Objects.requireNonNull(name, "The name must not be null.");
        this.alias = alias;
        this.arguments = arguments != null ? arguments : new ArrayList<>();
        this.useParentheses = useParentheses;
    }

    public static Function of(String name, String alias, boolean useParentheses, List<Expression> arguments) {
        return new Function(name, alias, useParentheses, arguments);
    }

    public static Function of(String name, String alias, boolean useParentheses, Expression... arguments) {
        return new Function(name, alias, useParentheses, arguments != null ? Arrays.asList(arguments) : null);
    }

    public static Function of(String name) {
        return of(name, null, true);
    }

    public static Function of(String name, String alias) {
        return of(name, alias, true);
    }

    public static Function of(String name, List<Expression> arguments) {
        return of(name, null, true, arguments);
    }

    public static Function of(String name, Expression... arguments) {
        return of(name, null, true, arguments);
    }

    public static Function of(String name, String alias, List<Expression> arguments) {
        return of(name, alias, true, arguments);
    }

    public static Function of(String name, String alias, Expression... arguments) {
        return of(name, alias, true, arguments);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public Function argument(Expression argument) {
        if (argument != null) {
            arguments.add(argument);
        }

        return this;
    }

    public boolean isUseParentheses() {
        return useParentheses;
    }

    @Override
    public void buildInvolvedTables(Set<Table> tables) {
        arguments.forEach(argument -> argument.buildInvolvedTables(tables));
    }

    @Override
    public void buildInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        arguments.forEach(argument -> argument.buildInvolvedPlaceHolders(placeHolders));
    }

    @Override
    public void buildSQL(SQLBuilder builder, boolean withAlias) {
        builder.append(name)
                .append(useParentheses ? "(" : " ")
                .append(arguments, ", ");

        if (useParentheses) {
            builder.append(")");
        }

        if (withAlias && alias != null) {
            builder.append(builder.keyword(" as ") + alias);
        }
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        buildSQL(builder, false);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
