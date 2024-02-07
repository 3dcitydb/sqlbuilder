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

package org.citydb.sqlbuilder.predicate.logical;

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.common.Expression;
import org.citydb.sqlbuilder.literal.PlaceHolder;
import org.citydb.sqlbuilder.literal.StringLiteral;
import org.citydb.sqlbuilder.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Like implements LogicalOperator {
    private final Expression operand;
    private final Expression pattern;
    private final StringLiteral escapeCharacter;
    private boolean negate;

    private Like(Expression operand, Expression pattern, StringLiteral escapeCharacter, boolean negate) {
        this.operand = Objects.requireNonNull(operand, "The operand must not be null.");
        this.pattern = Objects.requireNonNull(pattern, "The like pattern must not be null.");
        this.escapeCharacter = escapeCharacter;
        this.negate = negate;
    }

    public static Like of(Expression operand, Expression pattern, StringLiteral escapeCharacter, boolean negate) {
        return new Like(operand, pattern, escapeCharacter, negate);
    }

    public static Like of(Expression operand, Expression pattern, StringLiteral escapeCharacter) {
        return new Like(operand, pattern, escapeCharacter, false);
    }

    public static Like of(Expression operand, Expression pattern, boolean negate) {
        return new Like(operand, pattern, null, negate);
    }

    public static Like of(Expression operand, Expression pattern) {
        return new Like(operand, pattern, null, false);
    }

    public Expression getOperand() {
        return operand;
    }

    public Expression getPattern() {
        return pattern;
    }

    public Optional<StringLiteral> getEscapeCharacter() {
        return Optional.ofNullable(escapeCharacter);
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public LogicalOperatorType getType() {
        return !negate ? LogicalOperatorType.LIKE : LogicalOperatorType.NOT_LIKE;
    }

    @Override
    public void getInvolvedTables(Set<Table> tables) {
        operand.getInvolvedTables(tables);
        pattern.getInvolvedTables(tables);
        escapeCharacter.getInvolvedTables(tables);
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        operand.getInvolvedPlaceHolders(placeHolders);
        pattern.getInvolvedPlaceHolders(placeHolders);
        escapeCharacter.getInvolvedPlaceHolders(placeHolders);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append(operand)
                .append(" " + getType().toSQL(builder) + " ")
                .append(pattern);
        if (escapeCharacter != null) {
            if (builder.isUseJdbcEscapeNotation()) {
                builder.append(" {escape ")
                        .append(escapeCharacter)
                        .append("}");
            } else {
                builder.append(builder.keyword(" escape "))
                        .append(escapeCharacter);
            }
        }
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
