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

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.query.Selection;
import org.citydb.sqlbuilder.query.Window;
import org.citydb.sqlbuilder.schema.ColumnExpression;

import java.util.*;
import java.util.stream.Collectors;

public class Function implements ColumnExpression, Selection<Function> {
    private final String name;
    private final List<Expression> arguments;
    private final List<String> qualifiers = new ArrayList<>();
    private String alias;

    protected Function(String name, String alias, List<Expression> arguments) {
        this.name = Objects.requireNonNull(name, "The name must not be null.");
        this.alias = alias;
        this.arguments = arguments != null ? arguments : new ArrayList<>();
    }

    public static Function of(String name, String alias, List<Expression> arguments) {
        return new Function(name, alias, arguments);
    }

    public static Function of(String name, String alias, Expression... arguments) {
        return new Function(name, alias, arguments != null ?
                new ArrayList<>(Arrays.asList(arguments)) :
                null);
    }

    public static Function of(String name, List<Expression> arguments) {
        return new Function(name, null, arguments);
    }

    public static Function of(String name, Expression... arguments) {
        return of(name, null, arguments);
    }

    public String getName() {
        return name;
    }

    public List<String> getQualifiers() {
        return qualifiers;
    }

    public Function qualifier(String qualifier) {
        qualifiers.add(qualifier);
        return this;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public Function as(String alias) {
        this.alias = alias;
        return this;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public Function add(Expression... arguments) {
        return arguments != null ? add(Arrays.asList(arguments)) : this;
    }

    public Function add(List<Expression> arguments) {
        if (arguments != null && !arguments.isEmpty()) {
            arguments.stream()
                    .filter(Objects::nonNull)
                    .forEach(this.arguments::add);
        }

        return this;
    }

    public WindowFunction over() {
        return WindowFunction.of(this, Window.empty());
    }

    public WindowFunction over(Window window) {
        return WindowFunction.of(this, window);
    }

    public WindowFunction over(java.util.function.Function<Window, Window> builder) {
        return WindowFunction.of(this, builder.apply(Window.newInstance()));
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        arguments.forEach(argument -> argument.getPlaceHolders(placeHolders));
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        builder.append(builder.keyword(name))
                .append("(");
        if (!qualifiers.isEmpty()) {
            builder.append(qualifiers.stream()
                            .map(builder::keyword)
                            .collect(Collectors.joining(" ")))
                    .append(" ");
        }

        builder.append(SqlBuilder.of(builder.getOptions()).append(arguments, ", ").build())
                .append(")");
    }

    @Override
    public String toString() {
        return toSql();
    }
}
