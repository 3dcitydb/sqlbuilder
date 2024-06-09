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

package org.citydb.sqlbuilder.util;

import org.citydb.sqlbuilder.common.SqlObject;
import org.citydb.sqlbuilder.common.SqlWalker;
import org.citydb.sqlbuilder.function.Function;
import org.citydb.sqlbuilder.function.WindowFunction;
import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.literal.*;
import org.citydb.sqlbuilder.operation.*;
import org.citydb.sqlbuilder.query.*;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.schema.WildcardColumn;
import org.citydb.sqlbuilder.update.Update;
import org.citydb.sqlbuilder.update.UpdateValue;

import java.util.*;

public class PlaceholderHelper {

    private PlaceholderHelper() {
    }

    public static PlaceholderHelper newInstance() {
        return new PlaceholderHelper();
    }

    public List<Placeholder> getPlaceholders(SqlObject object) {
        Processor processor = new Processor();
        object.accept(processor);
        return processor.placeholders;
    }

    private static class Processor extends SqlWalker {
        private final List<Placeholder> placeholders = new ArrayList<>();
        private final Set<SqlObject> visited = Collections.newSetFromMap(new IdentityHashMap<>());

        @Override
        public void visit(ArithmeticOperation operation) {
            if (visited.add(operation)) {
                super.visit(operation);
            }
        }

        @Override
        public void visit(Between between) {
            if (visited.add(between)) {
                super.visit(between);
            }
        }

        @Override
        public void visit(BinaryComparisonOperation operation) {
            if (visited.add(operation)) {
                super.visit(operation);
            }
        }

        @Override
        public void visit(BinaryLogicalOperation operation) {
            if (visited.add(operation)) {
                super.visit(operation);
            }
        }

        @Override
        public void visit(BooleanLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(Collate collate) {
            if (visited.add(collate)) {
                super.visit(collate);
            }
        }

        @Override
        public void visit(Column column) {
            if (visited.add(column)) {
                super.visit(column);
            }
        }

        @Override
        public void visit(CommonTableExpression expression) {
            if (visited.add(expression)) {
                super.visit(expression);
            }
        }

        @Override
        public void visit(DateLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(DoubleLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(Exists exists) {
            if (visited.add(exists)) {
                super.visit(exists);
            }
        }

        @Override
        public void visit(Frame frame) {
            if (visited.add(frame)) {
                super.visit(frame);
            }
        }

        @Override
        public void visit(Function function) {
            if (visited.add(function)) {
                super.visit(function);
            }
        }

        @Override
        public void visit(In in) {
            if (visited.add(in)) {
                super.visit(in);
            }
        }

        @Override
        public void visit(IntegerLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(IsNull isNull) {
            if (visited.add(isNull)) {
                super.visit(isNull);
            }
        }

        @Override
        public void visit(Join join) {
            if (visited.add(join)) {
                super.visit(join);
            }
        }

        @Override
        public void visit(Like like) {
            if (visited.add(like)) {
                super.visit(like);
            }
        }

        @Override
        public void visit(Not not) {
            if (visited.add(not)) {
                super.visit(not);
            }
        }

        @Override
        public void visit(NullLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(OrderBy orderBy) {
            if (visited.add(orderBy)) {
                super.visit(orderBy);
            }
        }

        @Override
        public void visit(Placeholder placeholder) {
            if (visited.add(placeholder)) {
                placeholders.add(placeholder);
            }
        }

        @Override
        public void visit(PlainText plainText) {
            if (visited.add(plainText)) {
                super.visit(plainText);
            }
        }

        @Override
        public void visit(Select select) {
            if (visited.add(select)) {
                super.visit(select);
            }
        }

        @Override
        public void visit(SetOperator operator) {
            if (visited.add(operator)) {
                super.visit(operator);
            }
        }

        @Override
        public void visit(SubQueryOperator operator) {
            if (visited.add(operator)) {
                super.visit(operator);
            }
        }

        @Override
        public void visit(StringLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(Table table) {
            if (visited.add(table)) {
                super.visit(table);
            }
        }

        @Override
        public void visit(TimestampLiteral literal) {
            if (visited.add(literal)) {
                super.visit(literal);
            }
        }

        @Override
        public void visit(Update update) {
            if (visited.add(update)) {
                super.visit(update);
            }
        }

        @Override
        public void visit(UpdateValue value) {
            if (visited.add(value)) {
                super.visit(value);
            }
        }

        @Override
        public void visit(WildcardColumn column) {
            if (visited.add(column)) {
                super.visit(column);
            }
        }

        @Override
        public void visit(Window window) {
            if (visited.add(window)) {
                window.getPartitionBy().forEach(partition -> partition.accept(this));
                window.getOrderBy().forEach(orderBy -> orderBy.accept(this));
                window.getFrame().ifPresent(frame -> frame.accept(this));
            }
        }

        @Override
        public void visit(WindowFunction function) {
            if (visited.add(function)) {
                super.visit(function);
            }
        }
    }
}
