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
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.common.SQLObject;
import org.citydb.sqlbuilder.literal.Literal;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.util.AliasGenerator;
import org.citydb.sqlbuilder.util.GlobalAliasGenerator;

import java.util.*;

public class Window implements SQLObject {
    private final List<Expression> partitionBy;
    private final List<OrderBy> orderBy;
    private String name;
    private Frame frame;
    private Window reference;

    private Window(String name, List<Expression> partitionBy, List<OrderBy> orderBy, Frame frame, Window reference) {
        this.name = name;
        this.partitionBy = partitionBy != null ? partitionBy : new ArrayList<>();
        this.orderBy = orderBy != null ? orderBy : new ArrayList<>();
        this.frame = frame;
        this.reference = reference;
    }

    public static Window newInstance() {
        return new Window(null, null, null, null, null);
    }

    public static Window of(String name, List<Expression> partitionBy, List<OrderBy> orderBy, Frame frame, Window reference) {
        return new Window(name, partitionBy, orderBy, frame, null);
    }

    public static Window of(List<Expression> partitionBy, List<OrderBy> orderBy, Frame frame) {
        return new Window(null, partitionBy, orderBy, frame, null);
    }

    public static Window of(List<Expression> partitionBy, List<OrderBy> orderBy) {
        return of(partitionBy, orderBy, null);
    }

    public static Window of(List<Expression> partitionBy) {
        return of(partitionBy, null);
    }

    public static Window of(Expression... partitionBy) {
        return of(partitionBy != null ? new ArrayList<>(Arrays.asList(partitionBy)) : null);
    }

    public static Window empty() {
        return newInstance();
    }

    public static Window asReferenceOf(Window window) {
        return newInstance().references(window);
    }

    public String getName() {
        if (name == null) {
            name(GlobalAliasGenerator.getInstance());
        }

        return name;
    }

    public Window name(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        return this;
    }

    public Window name(AliasGenerator generator) {
        this.name = generator.next();
        return this;
    }

    public List<Expression> getPartitionBy() {
        return partitionBy;
    }

    public Window partitionBy(Expression... partitionBy) {
        this.partitionBy.addAll(Arrays.asList(partitionBy));
        return this;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public Window orderBy(OrderBy... orderBy) {
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }

    public Window orderBy(Column column) {
        orderBy.add(OrderBy.of(column));
        return this;
    }

    public Optional<Frame> getFrame() {
        return Optional.ofNullable(frame);
    }

    public Window frame(Frame frame) {
        this.frame = frame;
        return this;
    }

    public Window getReference() {
        return reference;
    }

    public Window references(Window reference) {
        this.reference = reference;
        return this;
    }

    public boolean isReferenceOnly() {
        return reference != null
                && partitionBy.isEmpty()
                && orderBy.isEmpty()
                && frame == null;
    }

    public Window buildReference() {
        return Window.asReferenceOf(this);
    }

    public FrameBuilder groups() {
        return new FrameBuilder(FrameUnits.GROUPS);
    }

    public FrameBuilder range() {
        return new FrameBuilder(FrameUnits.RANGE);
    }

    public FrameBuilder rows() {
        return new FrameBuilder(FrameUnits.ROWS);
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        partitionBy.forEach(partitionBy -> partitionBy.getInvolvedTables(tables));
        orderBy.forEach(orderBy -> orderBy.getInvolvedTables(tables));
        if (frame != null) {
            frame.getInvolvedTables(tables);
        }
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        partitionBy.forEach(partitionBy -> partitionBy.getInvolvedPlaceHolders(placeHolders));
        orderBy.forEach(orderBy -> orderBy.getInvolvedPlaceHolders(placeHolders));
        if (frame != null) {
            frame.getInvolvedPlaceHolders(placeHolders);
        }
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append("(");
        if (reference != null) {
            builder.append(reference.getName())
                    .append(" ");
        }

        if (!partitionBy.isEmpty()) {
            builder.append(builder.keyword("partition "))
                    .append(partitionBy, ", ")
                    .append(" ");
        }

        if (!orderBy.isEmpty()) {
            builder.append(builder.keyword("order by "))
                    .append(orderBy, ", ")
                    .append(" ");
        }

        if (frame != null) {
            builder.append(frame)
                    .append(" ");
        }

        builder.append(")");
    }

    @Override
    public String toString() {
        return toSQL();
    }

    public class FrameBuilder {
        private final FrameUnits units;

        private FrameBuilder(FrameUnits units) {
            this.units = units;
        }

        public Window currentRow() {
            frame = Frame.of(units, FramePosition.CURRENT_ROW);
            return Window.this;
        }

        public Window following(Literal<?> following) {
            frame = Frame.of(units, FramePosition.FOLLOWING, following);
            return Window.this;
        }

        public Window preceding(Literal<?> preceding) {
            frame = Frame.of(units, FramePosition.PRECEDING, preceding);
            return Window.this;
        }

        public Window unboundedFollowing() {
            frame = Frame.of(units, FramePosition.UNBOUNDED_FOLLOWING);
            return Window.this;
        }

        public Window unboundedPreceding() {
            frame = Frame.of(units, FramePosition.UNBOUNDED_PRECEDING);
            return Window.this;
        }

        public FrameBetweenBuilder betweenCurrentRow() {
            return new FrameBetweenBuilder(units, FramePosition.CURRENT_ROW, null);
        }

        public FrameBetweenBuilder betweenFollowing(Literal<?> following) {
            return new FrameBetweenBuilder(units, FramePosition.FOLLOWING, following);
        }

        public FrameBetweenBuilder betweenPreceding(Literal<?> preceding) {
            return new FrameBetweenBuilder(units, FramePosition.PRECEDING, preceding);
        }

        public FrameBetweenBuilder betweenUnboundedFollowing() {
            return new FrameBetweenBuilder(units, FramePosition.UNBOUNDED_FOLLOWING, null);
        }

        public FrameBetweenBuilder betweenUnboundedPreceding() {
            return new FrameBetweenBuilder(units, FramePosition.UNBOUNDED_PRECEDING, null);
        }

    }

    public class FrameBetweenBuilder {
        private final FrameUnits units;
        private final FramePosition start;
        private final Literal<?> startExpression;

        private FrameBetweenBuilder(FrameUnits units, FramePosition start, Literal<?> startExpression) {
            this.units = units;
            this.start = start;
            this.startExpression = startExpression;
        }

        public Window andCurrentRow() {
            frame = Frame.of(units, start, startExpression, FramePosition.CURRENT_ROW);
            return Window.this;
        }

        public Window andFollowing(Literal<?> following) {
            frame = Frame.of(units, start, startExpression, FramePosition.FOLLOWING, following);
            return Window.this;
        }

        public Window andPreceding(Literal<?> preceding) {
            frame = Frame.of(units, start, startExpression, FramePosition.PRECEDING, preceding);
            return Window.this;
        }

        public Window andUnboundedFollowing() {
            frame = Frame.of(units, start, startExpression, FramePosition.UNBOUNDED_FOLLOWING);
            return Window.this;
        }

        public Window andUnboundedPreceding() {
            frame = Frame.of(units, start, startExpression, FramePosition.UNBOUNDED_PRECEDING);
            return Window.this;
        }
    }
}
