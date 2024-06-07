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
import org.citydb.sqlbuilder.common.SqlVisitor;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PlaceholderHelper {

    private PlaceholderHelper() {
    }

    public static PlaceholderHelper newInstance() {
        return new PlaceholderHelper();
    }

    public List<Placeholder> getPlaceholders(SqlObject object) {
        Processor processor = new Processor();
        object.accept(processor);
        return new ArrayList<>(processor.placeholders);
    }

    private static class Processor implements SqlVisitor {
        private final Set<Placeholder> placeholders = new LinkedHashSet<>();

        @Override
        public void visit(ArithmeticOperation operation) {
            operation.getLeftOperand().accept(this);
            operation.getRightOperand().accept(this);
        }

        @Override
        public void visit(Between between) {
            between.getOperand().accept(this);
            between.getLowerBound().accept(this);
            between.getUpperBound().accept(this);
        }

        @Override
        public void visit(BinaryComparisonOperation operation) {
            operation.getLeftOperand().accept(this);
            operation.getRightOperand().accept(this);
        }

        @Override
        public void visit(BinaryLogicalOperation operation) {
            operation.getOperands().forEach(operand -> operand.accept(this));
        }

        @Override
        public void visit(BooleanLiteral literal) {
        }

        @Override
        public void visit(Collate collate) {
            collate.getExpression().accept(this);
        }

        @Override
        public void visit(Column column) {
            column.getTable().accept(this);
        }

        @Override
        public void visit(CommonTableExpression expression) {
            expression.getQueryStatement().accept(this);
        }

        @Override
        public void visit(DateLiteral literal) {
        }

        @Override
        public void visit(DoubleLiteral literal) {
        }

        @Override
        public void visit(Exists exists) {
            exists.getOperand().accept(this);
        }

        @Override
        public void visit(Frame frame) {
            frame.getStartExpression().ifPresent(expression -> expression.accept(this));
            frame.getEndExpression().ifPresent(expression -> expression.accept(this));
        }

        @Override
        public void visit(Function function) {
            function.getArguments().forEach(argument -> argument.accept(this));
        }

        @Override
        public void visit(In in) {
            in.getOperand().accept(this);
            in.getValues().forEach(value -> value.accept(this));
        }

        @Override
        public void visit(IntegerLiteral literal) {
        }

        @Override
        public void visit(IsNull isNull) {
            isNull.getOperand().accept(this);
        }

        @Override
        public void visit(Join join) {
            join.getTable().accept(this);
            join.getConditions().forEach(condition -> condition.accept(this));
        }

        @Override
        public void visit(Like like) {
            like.getOperand().accept(this);
            like.getPattern().accept(this);
            like.getEscapeCharacter().ifPresent(character -> character.accept(this));
        }

        @Override
        public void visit(Not not) {
            not.getOperand().accept(this);
        }

        @Override
        public void visit(NullLiteral literal) {
        }

        @Override
        public void visit(OrderBy orderBy) {
            orderBy.getColumn().accept(this);
        }

        @Override
        public void visit(Placeholder placeholder) {
            placeholders.add(placeholder);
        }

        @Override
        public void visit(PlainText plainText) {
        }

        @Override
        public void visit(Select select) {
            select.getWith().forEach(cte -> cte.accept(this));
            select.getSelect().forEach(projection -> projection.accept(this));
            select.getFrom().forEach(from -> from.accept(this));
            select.getJoins().forEach(join -> join.accept(this));
            select.getWhere().forEach(operator -> operator.accept(this));
            collect(select);
        }

        @Override
        public void visit(SetOperator operator) {
            operator.getOperands().forEach(operand -> operand.accept(this));
            collect(operator);
        }

        @Override
        public void visit(SubQueryOperator operator) {
            operator.getOperand().accept(this);
        }

        @Override
        public void visit(StringLiteral literal) {
        }

        @Override
        public void visit(Table table) {
            table.getQueryExpression().ifPresent(expression -> expression.accept(this));
        }

        @Override
        public void visit(TimestampLiteral literal) {
        }

        @Override
        public void visit(Update update) {
            update.getWith().forEach(cte -> cte.accept(this));
            update.getTable().ifPresent(table -> table.accept(this));
            update.getSet().forEach(value -> value.accept(this));
            update.getWhere().forEach(operator -> operator.accept(this));
        }

        @Override
        public void visit(UpdateValue value) {
            value.getColumn().accept(this);
            value.getValue().accept(this);
        }

        @Override
        public void visit(WildcardColumn column) {
            column.getTable().ifPresent(table -> table.accept(this));
        }

        @Override
        public void visit(Window window) {
            window.getPartitionBy().forEach(partitionBy -> partitionBy.accept(this));
            window.getOrderBy().forEach(orderBy -> orderBy.accept(this));
            window.getFrame().ifPresent(frame -> frame.accept(this));
        }

        @Override
        public void visit(WindowFunction function) {
            function.getFunction().accept(this);
            function.getWindow().accept(this);
        }

        private void collect(QueryStatement<?> statement) {
            statement.getGroupBy().forEach(groupBy -> groupBy.accept(this));
            statement.getHaving().forEach(having -> having.accept(this));
            statement.getWindow().forEach(window -> window.accept(this));
            statement.getOrderBy().forEach(orderBy -> orderBy.accept(this));
            statement.getOffset().ifPresent(offset -> offset.accept(this));
            statement.getFetch().ifPresent(fetch -> fetch.accept(this));
        }
    }
}
