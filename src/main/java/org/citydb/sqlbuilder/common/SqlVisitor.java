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

public interface SqlVisitor {
    void visit(ArithmeticOperation operation);

    void visit(Between between);

    void visit(BinaryComparisonOperation operation);

    void visit(BinaryLogicalOperation operation);

    void visit(BooleanLiteral literal);

    void visit(Column column);

    void visit(CommonTableExpression expression);

    void visit(DateLiteral literal);

    void visit(DoubleLiteral literal);

    void visit(Exists exists);

    void visit(Frame frame);

    void visit(Function function);

    void visit(In in);

    void visit(IntegerLiteral literal);

    void visit(IsNull isNull);

    void visit(Join join);

    void visit(Like like);

    void visit(Not not);

    void visit(NullLiteral literal);

    void visit(OrderBy orderBy);

    void visit(Placeholder placeholder);

    void visit(PlainText plainText);

    void visit(Select select);

    void visit(SetOperator operator);

    void visit(SubQueryOperator operator);

    void visit(StringLiteral literal);

    void visit(Table table);

    void visit(TimestampLiteral literal);

    void visit(Update update);

    void visit(UpdateValue value);

    void visit(WildcardColumn column);

    void visit(Window window);

    void visit(WindowFunction function);
}
