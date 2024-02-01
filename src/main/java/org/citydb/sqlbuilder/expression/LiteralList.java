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

package org.citydb.sqlbuilder.expression;

import java.util.List;

public class LiteralList implements SubQueryExpression {
    private final SQLLiteral<?>[] literals;

    public LiteralList(boolean... literals) {
        this.literals = new BooleanLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new BooleanLiteral(literals[i]);
    }

    public LiteralList(Boolean... literals) {
        this.literals = new BooleanLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new BooleanLiteral(literals[i]);
    }

    public LiteralList(double... literals) {
        this.literals = new DoubleLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new DoubleLiteral(literals[i]);
    }

    public LiteralList(Double... literals) {
        this.literals = new DoubleLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new DoubleLiteral(literals[i]);
    }

    public LiteralList(int... literals) {
        this.literals = new IntegerLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new IntegerLiteral(literals[i]);
    }

    public LiteralList(Integer... literals) {
        this.literals = new IntegerLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new IntegerLiteral(literals[i]);
    }

    public LiteralList(long... literals) {
        this.literals = new LongLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new LongLiteral(literals[i]);
    }

    public LiteralList(Long... literals) {
        this.literals = new LongLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new LongLiteral(literals[i]);
    }

    public LiteralList(String... literals) {
        this.literals = new StringLiteral[literals.length];
        for (int i = 0; i < literals.length; i++)
            this.literals[i] = new StringLiteral(literals[i]);
    }

    @SafeVarargs
    public <T> LiteralList(PlaceHolder<T>... literals) {
        this.literals = new PlaceHolder[literals.length];
        System.arraycopy(literals, 0, this.literals, 0, literals.length);
    }

    public SQLLiteral<?>[] getLiterals() {
        return literals;
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        for (SQLLiteral<?> literal : literals) {
            if (literal instanceof PlaceHolder<?>)
                placeHolders.add((PlaceHolder<?>) literal);
            else
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();

        for (int i = 0; i < literals.length; i++) {
            tmp.append(literals[i]);
            if (i < literals.length - 1)
                tmp.append(", ");
        }

        return tmp.toString();
    }
}
