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
import org.citydb.sqlbuilder.literal.Literal;

import java.util.Objects;
import java.util.Optional;

public class Frame implements SqlObject {
    public static final String CURRENT_ROW = "current row";
    public static final String UNBOUNDED_PRECEDING = "unbounded preceding";
    public static final String UNBOUNDED_FOLLOWING = "unbounded following";
    public static final String PRECEDING = "preceding";
    public static final String FOLLOWING = "following";
    public static final String GROUPS = "groups";
    public static final String ROWS = "rows";
    public static final String RANGE = "range";

    private final String units;
    private final String start;
    private final Literal<?> startExpression;
    private final String end;
    private final Literal<?> endExpression;

    private Frame(String units, String start, Literal<?> startExpression, String end, Literal<?> endExpression) {
        this.units = Objects.requireNonNull(units, "The frame units must not be null.");
        this.start = Objects.requireNonNull(start, "The frame start must not be null.");
        this.startExpression = startExpression;
        this.end = end;
        this.endExpression = endExpression;

        if (startExpression == null && requiresExpression(start)) {
            throw new IllegalArgumentException("The frame start '" + start + "' requires an expression.");
        } else if (endExpression == null && requiresExpression(end)) {
            throw new IllegalArgumentException("The frame end '" + end + "' requires an expression.");
        }
    }

    public static Frame of(String units, String start, Literal<?> startExpression, String end, Literal<?> endExpression) {
        return new Frame(units, start, startExpression, end, endExpression);
    }

    public static Frame of(String units, String start, String end) {
        return new Frame(units, start, null, end, null);
    }

    public static Frame of(String units, String start, Literal<?> startExpression, String end) {
        return new Frame(units, start, startExpression, end, null);
    }

    public static Frame of(String units, String start, Literal<?> startExpression) {
        return of(units, start, startExpression, null);
    }

    public static Frame of(String units, String start) {
        return of(units, start, null, null);
    }

    public String getUnits() {
        return units;
    }

    public String getStart() {
        return start;
    }

    public Optional<Literal<?>> getStartExpression() {
        return Optional.ofNullable(startExpression);
    }

    public Optional<String> getEnd() {
        return Optional.ofNullable(end);
    }

    public Optional<Literal<?>> getEndExpression() {
        return Optional.ofNullable(endExpression);
    }

    private boolean requiresExpression(String position) {
        return PRECEDING.equalsIgnoreCase(position) || FOLLOWING.equalsIgnoreCase(position);
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
