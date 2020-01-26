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

package org.citydb.sqlbuilder.select;

import org.citydb.sqlbuilder.SQLStatement;
import org.citydb.sqlbuilder.expression.CommonTableExpression;
import org.citydb.sqlbuilder.expression.PlaceHolder;
import org.citydb.sqlbuilder.expression.SubQueryExpression;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.select.join.Join;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Select implements SQLStatement, SubQueryExpression {
    private final List<CommonTableExpression> ctes = new ArrayList<>();
    private final Set<ProjectionToken> projectionTokens = new LinkedHashSet<>();
    private final List<Join> joins = new ArrayList<>();
    private final List<PredicateToken> predicateTokens = new ArrayList<>();
    private final List<GroupByToken> groupByTokens = new ArrayList<>();
    private final List<HavingToken> havingTokens = new ArrayList<>();
    private final List<OrderByToken> orderByTokens = new ArrayList<>();
    private final List<String> optimizerHints = new ArrayList<>();
    private OffsetToken offsetToken;
    private FetchToken fetchToken;

    private boolean distinct;
    private String indentString = "  ";
    private String pseudoTable;

    public Select() {
    }

    public Select(Select other) {
        if (!other.ctes.isEmpty()) ctes.addAll(other.ctes);
        if (!other.projectionTokens.isEmpty()) projectionTokens.addAll(other.projectionTokens);
        if (!other.joins.isEmpty()) joins.addAll(other.joins);
        if (!other.predicateTokens.isEmpty()) predicateTokens.addAll(other.predicateTokens);
        if (!other.groupByTokens.isEmpty()) groupByTokens.addAll(other.groupByTokens);
        if (!other.havingTokens.isEmpty()) havingTokens.addAll(other.havingTokens);
        if (!other.orderByTokens.isEmpty()) orderByTokens.addAll(other.orderByTokens);
        if (!other.optimizerHints.isEmpty()) optimizerHints.addAll(other.optimizerHints);

        offsetToken = other.offsetToken;
        fetchToken = other.fetchToken;

        distinct = other.distinct;
        indentString = other.indentString;
        pseudoTable = other.pseudoTable;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public Select setDistinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public String getPseudoTable() {
        return pseudoTable;
    }

    public Select setPseudoTable(String pseudoTable) {
        this.pseudoTable = pseudoTable;
        return this;
    }

    public Select addOptimizerHint(String hint) {
        optimizerHints.add(hint);
        return this;
    }

    public Select addOptimizerHint(String... hints) {
        optimizerHints.addAll(Arrays.asList(hints));
        return this;
    }

    public List<String> getOptimizerHints() {
        return new ArrayList<>(optimizerHints);
    }

    public Select removeOptimizerHint(String hint) {
        optimizerHints.remove(hint);
        return this;
    }

    public Select removeOptimizerHintsIf(Predicate<String> filter) {
        optimizerHints.removeIf(filter);
        return this;
    }

    public Select unsetOptimizerHints() {
        optimizerHints.clear();
        return this;
    }

    public Select addWith(CommonTableExpression cte) {
        ctes.add(cte);
        return this;
    }

    public Select addWith(CommonTableExpression... ctes) {
        this.ctes.addAll(Arrays.asList(ctes));
        return this;
    }

    public List<CommonTableExpression> getWith() {
        return new ArrayList<>(ctes);
    }

    public Select removeWith(CommonTableExpression cte) {
        ctes.remove(cte);
        return this;
    }

    public Select removeWithIf(Predicate<CommonTableExpression> filter) {
        ctes.removeIf(filter);
        return this;
    }

    public Select unsetWith() {
        ctes.clear();
        return this;
    }

    public Select addProjection(ProjectionToken token) {
        projectionTokens.add(token);
        return this;
    }

    public Select addProjection(ProjectionToken... tokens) {
        projectionTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public List<ProjectionToken> getProjection() {
        return new ArrayList<>(projectionTokens);
    }

    public Select removeProjection(ProjectionToken token) {
        projectionTokens.remove(token);
        return this;
    }

    public Select removeProjectionIf(Predicate<ProjectionToken> filter) {
        projectionTokens.removeIf(filter);
        return this;
    }

    public Select unsetProjection() {
        projectionTokens.clear();
        return this;
    }

    public Select addJoin(Join join) {
        joins.add(join);
        return this;
    }

    public Select addJoin(Join... joins) {
        this.joins.addAll(Arrays.asList(joins));
        return this;
    }

    public List<Join> getJoins() {
        return new ArrayList<>(joins);
    }

    public Select removeJoin(Join join) {
        joins.remove(join);
        return this;
    }

    public Select removeJoinsIf(Predicate<Join> filter) {
        joins.removeIf(filter);
        return this;
    }

    public Select unsetJoins() {
        joins.clear();
        return this;
    }

    public Select addSelection(PredicateToken token) {
        predicateTokens.add(token);
        return this;
    }

    public Select addSelection(PredicateToken... tokens) {
        predicateTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public List<PredicateToken> getSelection() {
        return new ArrayList<>(predicateTokens);
    }

    public Select removeSelection(PredicateToken token) {
        predicateTokens.remove(token);
        return this;
    }

    public Select removeSelectionIf(Predicate<PredicateToken> filter) {
        predicateTokens.removeIf(filter);
        return this;
    }

    public Select unsetSelection() {
        predicateTokens.clear();
        return this;
    }

    public Select addHaving(HavingToken token) {
        havingTokens.add(token);
        return this;
    }

    public Select addHaving(HavingToken... tokens) {
        havingTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public List<HavingToken> getHaving() {
        return new ArrayList<>(havingTokens);
    }

    public Select removeHaving(HavingToken token) {
        havingTokens.remove(token);
        return this;
    }

    public Select removeHavingIf(Predicate<HavingToken> filter) {
        havingTokens.removeIf(filter);
        return this;
    }

    public Select unsetHaving() {
        havingTokens.clear();
        return this;
    }

    public Select addGroupBy(GroupByToken token) {
        groupByTokens.add(token);
        return this;
    }

    public Select addGroupBy(GroupByToken... tokens) {
        groupByTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public Select addGroupBy(Column column) {
        return addGroupBy(new GroupByToken(column));
    }

    public Select addGroupBy(Column... columns) {
        for (Column column : columns)
            addGroupBy(column);

        return this;
    }

    public List<GroupByToken> getGroupBy() {
        return new ArrayList<>(groupByTokens);
    }

    public Select removeGroupBy(GroupByToken token) {
        groupByTokens.remove(token);
        return this;
    }

    public Select removeGroupByIf(Predicate<GroupByToken> filter) {
        groupByTokens.removeIf(filter);
        return this;
    }

    public Select unsetGroupBy() {
        groupByTokens.clear();
        return this;
    }

    public Select addOrderBy(OrderByToken token) {
        orderByTokens.add(token);
        return this;
    }

    public Select addOrderBy(OrderByToken... tokens) {
        orderByTokens.addAll(Arrays.asList(tokens));
        return this;
    }

    public List<OrderByToken> getOrderBy() {
        return new ArrayList<>(orderByTokens);
    }

    public Select removeOrderBy(OrderByToken token) {
        orderByTokens.remove(token);
        return this;
    }

    public Select removeOrderByIf(Predicate<OrderByToken> filter) {
        orderByTokens.removeIf(filter);
        return this;
    }

    public Select unsetOrderBy() {
        orderByTokens.clear();
        return this;
    }

    public Select withOffset(OffsetToken token) {
        offsetToken = token;
        return this;
    }

    public OffsetToken getOffset() {
        return offsetToken;
    }

    public Select unsetOffset() {
        offsetToken = null;
        return this;
    }

    public Select withFetch(FetchToken token) {
        fetchToken = token;
        return this;
    }

    public FetchToken getFetch() {
        return fetchToken;
    }

    public Select unsetFetch() {
        fetchToken = null;
        return this;
    }

    @Override
    public Set<Table> getInvolvedTables() {
        Set<Table> tables = new LinkedHashSet<>();

        for (ProjectionToken token : projectionTokens)
            token.getInvolvedTables(tables);

        for (Join join : joins)
            join.getInvolvedTables(tables);

        return tables;
    }

    @Override
    public List<PlaceHolder<?>> getInvolvedPlaceHolders() {
        List<PlaceHolder<?>> statements = new ArrayList<>();

        for (CommonTableExpression cte : ctes)
            cte.getInvolvedPlaceHolders(statements);

        for (ProjectionToken token : projectionTokens)
            token.getInvolvedPlaceHolders(statements);

        Set<Table> tables = new LinkedHashSet<>();
        for (ProjectionToken token : projectionTokens)
            token.getInvolvedTables(tables);

        for (Table table : tables) {
            if (table.isSetQueryExpression())
                table.getQueryExpression().getInvolvedPlaceHolders(statements);
        }

        for (Join join : joins) {
            Table table = join.getToColumn().getTable();
            if (table.isSetQueryExpression() && !tables.contains(table))
                table.getQueryExpression().getInvolvedPlaceHolders(statements);

            join.getInvolvedPlaceHolders(statements);
        }

        for (PredicateToken token : predicateTokens)
            token.getInvolvedPlaceHolders(statements);

        for (HavingToken token : havingTokens)
            token.getInvolvedPlaceHolders(statements);

        if (offsetToken != null)
            offsetToken.getInvolvedPlaceHolders(statements);

        if (fetchToken != null)
            fetchToken.getInvolvedPlaceHolders(statements);

        return statements;
    }

    @Override
    public void print(PrintWriter writer, boolean indent) {
        if (!ctes.isEmpty()) {
            print(writer, "with ", indent);
            print(writer, ctes, ",", indent, false);
        }

        writer.print("select ");

        if (!optimizerHints.isEmpty()) {
            writer.print("/*+ ");
            print(writer, optimizerHints, " ", false, false);
            writer.print("*/ ");
        }

        if (distinct)
            writer.print("distinct ");

        if (indent)
            writer.println();

        print(writer, projectionTokens, ",", indent, false);

        Set<Table> tables = getInvolvedTables();
        if (!joins.isEmpty() || !tables.isEmpty()) {
            print(writer, "from ", indent);

            if (!joins.isEmpty()) {
                Set<Table> removeTables = new HashSet<>();
                for (Join join : joins)
                    join.getInvolvedTables(removeTables);

                tables.removeAll(removeTables);
                tables.add(joins.get(0).getFromColumn().getTable());
            }

            if (!tables.isEmpty())
                print(writer, tables, ",", indent, false);

            if (!joins.isEmpty())
                print(writer, joins, "", indent, !tables.isEmpty());

        } else if (pseudoTable != null) {
            print(writer, "from ", indent);
            writer.print(pseudoTable);
            writer.print(' ');
        }

        if (!predicateTokens.isEmpty()) {
            print(writer, "where ", indent);
            print(writer, predicateTokens, " and", indent, false);
        }

        if (!groupByTokens.isEmpty()) {
            print(writer, "group by ", indent);
            print(writer, groupByTokens, ",", indent, false);
        }

        if (!havingTokens.isEmpty()) {
            print(writer, "having ", indent);
            print(writer, havingTokens, ",", indent, false);
        }

        if (!orderByTokens.isEmpty()) {
            print(writer, "order by ", indent);
            print(writer, orderByTokens, ",", indent, false);
        }

        if (offsetToken == null && fetchToken != null && fetchToken.isForceOffset())
            offsetToken = new OffsetToken(0);

        if (offsetToken != null) {
            writer.print("offset ");
            writer.print(offsetToken.getExpression());
            print(writer, " rows ", indent);
        }

        if (fetchToken != null) {
            writer.print("fetch " + (offsetToken != null ? "next" : "first") + " ");
            writer.print(fetchToken.getExpression());
            print(writer, " rows only", indent);
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

            print(writer, " ", indent);
        }
    }

    private void print(PrintWriter writer, String text, boolean indent) {
        writer.print(text);
        if (indent)
            writer.println();
    }

    @Override
    public void getInvolvedPlaceHolders(List<PlaceHolder<?>> placeHolders) {
        placeHolders.addAll(getInvolvedPlaceHolders());
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        print(new PrintWriter(writer), false);

        return writer.toString();
    }

}
