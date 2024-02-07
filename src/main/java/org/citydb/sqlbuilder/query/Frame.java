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

package org.citydb.sqlbuilder.query;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.SQLObject;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Frame implements SQLObject {
    private final FrameUnits units;
    private final FramePosition start;
    private final Literal<?> startExpression;
    private final FramePosition end;
    private final Literal<?> endExpression;

    private Frame(FrameUnits units, FramePosition start, Literal<?> startExpression, FramePosition end, Literal<?> endExpression) {
        this.units = Objects.requireNonNull(units, "The frame units must not be null.");
        this.start = Objects.requireNonNull(start, "The frame start must not be null.");
        this.startExpression = startExpression;
        this.end = end;
        this.endExpression = endExpression;

        if (startExpression == null && FramePosition.REQUIRES_EXPRESSION.contains(start)) {
            throw new IllegalArgumentException("The frame start '" + start + "' requires an expression.");
        } else if (startExpression != null && !FramePosition.REQUIRES_EXPRESSION.contains(start)) {
            throw new IllegalArgumentException("A start expression cannot be used with '" + start + "'.");
        } else if (endExpression == null && FramePosition.REQUIRES_EXPRESSION.contains(end)) {
            throw new IllegalArgumentException("The frame end '" + end + "' requires an expression.");
        } else if (endExpression != null && !FramePosition.REQUIRES_EXPRESSION.contains(end)) {
            throw new IllegalArgumentException("An end expression cannot be used with '" + end + "'.");
        }
    }

    public static Frame of(FrameUnits units, FramePosition start, Literal<?> startExpression, FramePosition end, Literal<?> endExpression) {
        return new Frame(units, start, startExpression, end, endExpression);
    }

    public static Frame of(FrameUnits units, FramePosition start, FramePosition end) {
        return new Frame(units, start, null, end, null);
    }

    public static Frame of(FrameUnits units, FramePosition start, Literal<?> startExpression, FramePosition end) {
        return new Frame(units, start, startExpression, end, null);
    }

    public static Frame of(FrameUnits units, FramePosition start, Literal<?> startExpression) {
        return of(units, start, startExpression, null);
    }

    public static Frame of(FrameUnits units, FramePosition start) {
        return of(units, start, null, null);
    }

    public FrameUnits getUnits() {
        return units;
    }

    public FramePosition getStart() {
        return start;
    }

    public Optional<Literal<?>> getStartExpression() {
        return Optional.ofNullable(startExpression);
    }

    public Optional<FramePosition> getEnd() {
        return Optional.ofNullable(end);
    }

    public Optional<Literal<?>> getEndExpression() {
        return Optional.ofNullable(endExpression);
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        if (startExpression != null) {
            startExpression.getInvolvedTables(tables);
        }

        if (endExpression != null) {
            endExpression.getInvolvedTables(tables);
        }
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        if (startExpression != null) {
            startExpression.getInvolvedPlaceHolders(placeHolders);
        }

        if (endExpression != null) {
            endExpression.getInvolvedPlaceHolders(placeHolders);
        }
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(units.toSQL(builder))
                .append(" ");
        if (end != null) {
            builder.append(builder.keyword("between "));
        }

        if (startExpression != null) {
            builder.append(startExpression)
                    .append(" ");
        }

        builder.append(start.toSQL(builder));
        if (end != null) {
            builder.append(builder.keyword(" and "));
            if (endExpression != null) {
                builder.append(endExpression)
                        .append(" ");
            }

            builder.append(end.toSQL(builder));
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
