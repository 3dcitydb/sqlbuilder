/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * http://www.3dcitydb.org/
 *
 * Copyright 2013-2018 Claus Nagel <claus.nagel@gmail.com>
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

package org.citydb.sqlbuilder.select.operator.comparison;

import org.citydb.sqlbuilder.expression.Expression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.StringLiteral;
import org.citydb.sqlbuilder.select.operator.OperationName;

import java.util.List;

public class LikeOperator extends AbstractComparisonOperator {
    private final Expression operand;
    private final Expression pattern;
    private final StringLiteral escapeCharacter;

    private boolean negate;

    public LikeOperator(Expression operand, Expression pattern, StringLiteral escapeCharacter, boolean negate) {
        if (escapeCharacter != null && escapeCharacter.getValue() != null && escapeCharacter.getValue().length() > 1)
            throw new IllegalArgumentException("Escape sequence may only contain null or one character.");

        this.operand = operand;
        this.pattern = pattern;
        this.escapeCharacter = escapeCharacter;
        this.negate = negate;
    }

    public LikeOperator(Expression operand, Expression pattern, StringLiteral escapeCharacter) {
        this(operand, pattern, escapeCharacter, false);
    }

    public LikeOperator(Expression operand, Expression pattern, boolean negate) {
        this(operand, pattern, null, negate);
    }

    public LikeOperator(Expression operand, Expression pattern) {
        this(operand, pattern, null, false);
    }

    public Expression getOperand() {
        return operand;
    }

    public Expression getPattern() {
        return pattern;
    }

    public StringLiteral getEscapeCharacter() {
        return escapeCharacter;
    }

    public boolean isNegate() {
        return negate;
    }

    @Override
    public OperationName getOperationName() {
        return !negate ? ComparisonName.LIKE : ComparisonName.NOT_LIKE;
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> statements) {
        getInvolvedPlaceHolders(operand, statements);
        getInvolvedPlaceHolders(pattern, statements);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder().append(operand).append(" ").append(getOperationName()).append(" ").append(pattern);
        if (escapeCharacter != null)
            tmp.append(" escape ").append(escapeCharacter);

        return tmp.toString();
    }

}
