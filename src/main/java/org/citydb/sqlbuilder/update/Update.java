/*
 * 3D City Database - The Open Source CityGML Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2013 - 2021
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.lrg.tum.de/gis/
 *
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 *
 * Virtual City Systems, Berlin <https://vc.systems/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
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

package org.citydb.sqlbuilder.update;

import org.citydb.sqlbuilder.SQLStatement;
import org.citydb.sqlbuilder.expression.CommonTableExpression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.PredicateToken;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class Update implements SQLStatement {
    private Table table;
    private final List<CommonTableExpression> ctes;
    private final List<UpdateToken> updateTokens;
    private final List<PredicateToken> predicateTokens;

    private String indentString = "  ";

    public Update() {
        ctes = new ArrayList<>();
        updateTokens = new ArrayList<>();
        predicateTokens = new ArrayList<>();
    }

    public Update(Update other) {
        this();
        if (!other.ctes.isEmpty()) ctes.addAll(other.ctes);
        if (!other.updateTokens.isEmpty()) updateTokens.addAll(other.updateTokens);
        if (!other.predicateTokens.isEmpty()) predicateTokens.addAll(other.predicateTokens);

        table = other.table;
        indentString = other.indentString;
    }

    public Table getTable() {
        return table;
    }

    public Update setTable(Table table) {
        this.table = table;
        return this;
    }

    public Update addWith(CommonTableExpression cte) {
        ctes.add(cte);
        return this;
    }

    public Update addWith(CommonTableExpression... ctes) {
        this.ctes.addAll(Arrays.asList(ctes));
        return this;
    }

    public List<CommonTableExpression> getWith() {
        return new ArrayList<CommonTableExpression>(ctes);
    }

    public boolean removeWith(CommonTableExpression cte) {
        return ctes.remove(cte);
    }

    public Update unsetWith() {
        ctes.clear();
        return this;
    }

    public Update addUpdateToken(UpdateToken token) {
        updateTokens.add(token);
        return this;
    }

    public Update addUpdateToken(UpdateToken... tokens) {
        updateTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public List<UpdateToken> getUpdateTokens() {
        return updateTokens;
    }

    public boolean removeUpdateToken(UpdateToken token) {
        return updateTokens.remove(token);
    }

    public Update unsetUpdateToken() {
        updateTokens.clear();
        return this;
    }

    public Update addSelection(PredicateToken token) {
        predicateTokens.add(token);
        return this;
    }

    public Update addSelection(PredicateToken... tokens) {
        predicateTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public List<PredicateToken> getSelection() {
        return new ArrayList<>(predicateTokens);
    }

    public boolean removeSelection(PredicateToken token) {
        return predicateTokens.remove(token);
    }

    public void unsetSelection() {
        predicateTokens.clear();
    }

    @Override
    public Set<Table> getInvolvedTables() {
        Set<Table> tables = new LinkedHashSet<>();
        tables.add(table);

        return tables;
    }

    @Override
    public List<PlaceHolder<?>> getInvolvedPlaceHolders() {
        List<PlaceHolder<?>> statements = new ArrayList<>();

        for (CommonTableExpression cte : ctes)
            cte.getInvolvedPlaceHolders(statements);

        for (UpdateToken token : updateTokens)
            token.getInvolvedPlaceHolders(statements);

        for (PredicateToken token : predicateTokens)
            token.getInvolvedPlaceHolders(statements);

        return statements;
    }

    @Override
    public void print(PrintWriter writer, boolean indent) {
        if (!ctes.isEmpty()) {
            writer.print("with ");
            if (indent)
                writer.println();

            print(writer, ctes, ",", indent, false);
        }

        writer.print("update ");
        writer.print(table);
        writer.print(' ');

        if (indent)
            writer.println();

        writer.print("set ");
        if (indent)
            writer.println();

        print(writer, updateTokens, ",", indent, false);

        if (!predicateTokens.isEmpty()) {
            writer.print("where ");
            if (indent)
                writer.println();

            print(writer, predicateTokens, " and", indent, false);
        }

        writer.flush();
    }

    private void print(PrintWriter writer, Collection<?> collection, String delimiter, boolean indent, boolean keepLastDelimiter) {
        Iterator<?> iter = collection.iterator();
        while (iter.hasNext()) {
            if (indent)
                writer.print(indentString);

            writer.print(iter.next());
            if (keepLastDelimiter || iter.hasNext())
                writer.print(delimiter);

            writer.print(' ');
            if (indent)
                writer.println();
        }
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        print(new PrintWriter(writer), false);

        return writer.toString();
    }

}
