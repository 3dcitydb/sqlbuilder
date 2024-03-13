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
import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.query.Selection;
import org.citydb.sqlbuilder.query.Window;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class WindowFunction implements Selection<WindowFunction> {
    private final Function function;
    private final Window window;
    private String alias;

    private WindowFunction(Function function, Window window, String alias) {
        this.function = Objects.requireNonNull(function, "The function must not be null.");
        this.window = Objects.requireNonNull(window, "The window definition must not be null.");
        this.alias = alias;
    }

    public static WindowFunction of(Function function, Window window, String alias) {
        return new WindowFunction(function, window, alias);
    }

    public static WindowFunction of(Function function, Window window) {
        return new WindowFunction(function, window, null);
    }

    public static WindowFunction of(Function function, String alias) {
        return new WindowFunction(function, Window.empty(), alias);
    }

    public static WindowFunction of(Function function) {
        return of(function, Window.empty());
    }

    public Function getFunction() {
        return function;
    }

    public Window getWindow() {
        return window;
    }

    @Override
    public Optional<String> getAlias() {
        return Optional.ofNullable(alias);
    }

    @Override
    public WindowFunction as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void getPlaceHolders(List<PlaceHolder> placeHolders) {
        function.getPlaceHolders(placeHolders);
        window.getPlaceHolders(placeHolders);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        function.buildSql(builder);
        builder.append(builder.keyword(" over "));

        if (window.isReferenceOnly()) {
            builder.append(window.getReference().getName());
        } else {
            builder.append(window);
        }
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
