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

package org.citydb.sqlbuilder.common;

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
import org.citydb.sqlbuilder.util.PlainText;

public abstract class SqlWalker implements SqlVisitor {

    public void visit(SqlObject object) {
    }

    @Override
    public void visit(ArithmeticOperation operation) {
        visit((SqlObject) operation);
        operation.getLeftOperand().accept(this);
        operation.getRightOperand().accept(this);
    }

    @Override
    public void visit(Between between) {
        visit((SqlObject) between);
        between.getOperand().accept(this);
        between.getLowerBound().accept(this);
        between.getUpperBound().accept(this);
    }

    @Override
    public void visit(BinaryComparisonOperation operation) {
        visit((SqlObject) operation);
        operation.getLeftOperand().accept(this);
        operation.getRightOperand().accept(this);
    }

    @Override
    public void visit(BinaryLogicalOperation operation) {
        visit((SqlObject) operation);
        operation.getOperands().forEach(operand -> operand.accept(this));
    }

    @Override
    public void visit(BooleanLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(Collate collate) {
        visit((SqlObject) collate);
        collate.getExpression().accept(this);
    }

    @Override
    public void visit(Column column) {
        visit((SqlObject) column);
        column.getTable().accept(this);
    }

    @Override
    public void visit(CommonTableExpression expression) {
        visit((SqlObject) expression);
        expression.getQueryExpression().accept(this);
    }

    @Override
    public void visit(DateLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(DoubleLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(Exists exists) {
        visit((SqlObject) exists);
        exists.getOperand().accept(this);
    }

    @Override
    public void visit(Frame frame) {
        visit((SqlObject) frame);
        frame.getStartExpression().ifPresent(literal -> literal.accept(this));
        frame.getEndExpression().ifPresent(literal -> literal.accept(this));
    }

    @Override
    public void visit(Function function) {
        visit((SqlObject) function);
        function.getArguments().forEach(argument -> argument.accept(this));
    }

    @Override
    public void visit(In in) {
        visit((SqlObject) in);
        in.getOperand().accept(this);
        in.getValues().forEach(value -> value.accept(this));
    }

    @Override
    public void visit(IntegerLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(IsNull isNull) {
        visit((SqlObject) isNull);
        isNull.getOperand().accept(this);
    }

    @Override
    public void visit(Join join) {
        visit((SqlObject) join);
        join.getTable().accept(this);
        join.getConditions().forEach(condition -> condition.accept(this));
    }

    @Override
    public void visit(Like like) {
        visit((SqlObject) like);
        like.getOperand().accept(this);
        like.getPattern().accept(this);
        like.getEscapeCharacter().ifPresent(character -> character.accept(this));
    }

    @Override
    public void visit(Not not) {
        visit((SqlObject) not);
        not.getOperand().accept(this);
    }

    @Override
    public void visit(NullLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(OrderBy orderBy) {
        visit((SqlObject) orderBy);
        orderBy.getSortExpression().accept(this);
    }

    @Override
    public void visit(Placeholder placeholder) {
        visit((SqlObject) placeholder);
    }

    @Override
    public void visit(PlainText plainText) {
        visit((SqlObject) plainText);
        plainText.getTokens().stream()
                .filter(SqlObject.class::isInstance)
                .map(SqlObject.class::cast)
                .forEach(token -> token.accept(this));
    }

    @Override
    public void visit(Select select) {
        visit((SqlObject) select);
        select.getWith().forEach(with -> with.accept(this));
        select.getSelect().forEach(selection -> selection.accept(this));
        select.getFrom().forEach(from -> from.accept(this));
        select.getJoins().forEach(join -> join.accept(this));
        select.getWhere().forEach(where -> where.accept(this));
        visit((QueryStatement<?>) select);
    }

    @Override
    public void visit(SetOperator operator) {
        visit((SqlObject) operator);
        operator.getOperands().forEach(operand -> operand.accept(this));
        visit((QueryStatement<?>) operator);
    }

    @Override
    public void visit(SubQueryOperator operator) {
        visit((SqlObject) operator);
        operator.getOperand().accept(this);
    }

    @Override
    public void visit(StringLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(Table table) {
        visit((SqlObject) table);
        table.getQueryExpression().ifPresent(expression -> expression.accept(this));
    }

    @Override
    public void visit(TimestampLiteral literal) {
        visit((SqlObject) literal);
    }

    @Override
    public void visit(Update update) {
        visit((SqlObject) update);
        update.getWith().forEach(with -> with.accept(this));
        update.getTable().ifPresent(table -> table.accept(this));
        update.getSet().forEach(set -> set.accept(this));
        update.getWhere().forEach(where -> where.accept(this));
    }

    @Override
    public void visit(UpdateValue value) {
        visit((SqlObject) value);
        value.getColumn().accept(this);
        value.getValue().accept(this);
    }

    @Override
    public void visit(WildcardColumn column) {
        visit((SqlObject) column);
        column.getTable().ifPresent(table -> table.accept(this));
    }

    @Override
    public void visit(Window window) {
        visit((SqlObject) window);
        window.getPartitionBy().forEach(partition -> partition.accept(this));
        window.getOrderBy().forEach(orderBy -> orderBy.accept(this));
        window.getFrame().ifPresent(frame -> frame.accept(this));
    }

    @Override
    public void visit(WindowFunction function) {
        visit((SqlObject) function);
        function.getFunction().accept(this);
        function.getWindow().accept(this);
    }

    private void visit(QueryStatement<?> statement) {
        statement.getGroupBy().forEach(groupBy -> groupBy.accept(this));
        statement.getHaving().forEach(having -> having.accept(this));
        statement.getWindow().forEach(window -> window.accept(this));
        statement.getOrderBy().forEach(orderBy -> orderBy.accept(this));
        statement.getOffset().ifPresent(offset -> offset.accept(this));
        statement.getFetch().ifPresent(fetch -> fetch.accept(this));
    }
}
