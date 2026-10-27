package org.citydb.sqlbuilder;

import org.citydb.sqlbuilder.insert.Insert;
import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.join.Joins;
import org.citydb.sqlbuilder.literal.*;
import org.citydb.sqlbuilder.operation.ArithmeticOperation;
import org.citydb.sqlbuilder.operation.Case;
import org.citydb.sqlbuilder.operation.In;
import org.citydb.sqlbuilder.operation.Negate;
import org.citydb.sqlbuilder.operation.Operators;
import org.citydb.sqlbuilder.query.*;
import org.citydb.sqlbuilder.schema.Column;
import org.citydb.sqlbuilder.schema.Table;
import org.citydb.sqlbuilder.schema.WildcardColumn;
import org.citydb.sqlbuilder.update.Update;
import org.citydb.sqlbuilder.util.ArrayValue;
import org.citydb.sqlbuilder.util.PlainSql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SqlBuilderTest {

    @Test
    public void testWindowFrameBounds() {
        Frame frame = Frame.of(Frame.ROWS, Frame.PRECEDING, IntegerLiteral.of(1), Frame.FOLLOWING, IntegerLiteral.of(3));
        String sql = frame.toSql();
        System.out.println("Frame SQL: " + sql);
        // Verify exact window boundary construction
        assertEquals("rows between 1 preceding and 3 following", sql);
    }

    @Test
    public void testPlainSqlSubstitution() {
        PlainSql plain = PlainSql.of("select {}", "$foo$");
        String sql = plain.toSql();
        assertNotNull(sql);
        System.out.println("Plain SQL: " + sql);
        assertEquals("select $foo$", sql);
    }

    @Test
    public void testIdentifierQuoting() {
        SqlBuildOptions options = SqlBuildOptions.defaults().setIdentifierDelimiter("\"");
        Table table = Table.of("my_table");
        String sql = SqlBuilder.newInstance().build(table, options);
        System.out.println("Delimiter SQL: " + sql);
        // Verify identifier delimiter quoting
        assertEquals("\"my_table\" a", sql);
    }

    @Test
    public void testUpdateTableWithOptions() {
        Table table = Table.of("my_table");
        Update update = Update.newInstance().table(table).set(table.column("id")).value(IntegerLiteral.of(1));
        SqlBuildOptions options = SqlBuildOptions.defaults().setIdentifierCase(SqlBuildOptions.IdentifierCase.UPPERCASE);
        String sql = SqlBuilder.newInstance().build(update, options);
        System.out.println("Update SQL: " + sql);
        // Verify that custom options are properly inherited inside update statements
        assertEquals("update MY_TABLE a set ID = 1 ", sql);
    }

    @Test
    public void testInsertValues() {
        Table table = Table.of("my_table", "my_schema");
        Insert insert = Insert.into(table)
                .columns(table.column("id"), table.column("name"))
                .values(1, "first")
                .values(2, "second");
        String sql = insert.toSql();
        System.out.println("Insert values SQL: " + sql);
        // Verify that the target table is rendered without an alias and columns are unqualified
        assertEquals("insert into my_schema.my_table (id, name) values (1, 'first'), (2, 'second') ", sql);
    }

    @Test
    public void testInsertValuesWithPlaceholders() {
        Insert insert = Insert.into(Table.of("my_table"))
                .columns("id", "name")
                .values(Placeholder.empty(), Placeholder.empty());
        assertEquals("insert into my_table (id, name) values (?, ?) ", insert.toSql());
        assertEquals(2, insert.getPlaceholders().size());
    }

    @Test
    public void testInsertQuery() {
        Table source = Table.of("source");
        Placeholder placeholder = Placeholder.of(10);
        Select select = Select.newInstance()
                .select(source.column("id"))
                .from(source)
                .where(source.column("value").gt(placeholder))
                .orderBy(OrderBy.of(source.column("id")))
                .fetch(5);
        Insert insert = Insert.into(Table.of("target")).columns("id").query(select);
        String sql = insert.toSql().replace("\r\n", "\n").replace("\n", " ");
        System.out.println("Insert query SQL: " + sql);
        // Verify that the query is rendered without enclosing parentheses and its placeholders are collected
        assertEquals("insert into target (id) select a.id from source a where a.value > ? order by a.id fetch first 5 rows only ", sql);
        assertEquals(1, insert.getPlaceholders().size());
        assertSame(placeholder, insert.getPlaceholders().get(0));
    }

    @Test
    public void testInsertWithOptions() {
        Insert insert = Insert.into(Table.of("my_table")).columns("id").values(1);
        SqlBuildOptions options = SqlBuildOptions.defaults()
                .setIdentifierCase(SqlBuildOptions.IdentifierCase.UPPERCASE)
                .setKeywordCase(SqlBuildOptions.KeywordCase.UPPERCASE);
        String sql = SqlBuilder.newInstance().build(insert, options);
        System.out.println("Insert with options SQL: " + sql);
        assertEquals("INSERT INTO MY_TABLE (ID) VALUES (1) ", sql);
    }

    @Test
    public void testInsertCopy() {
        Insert insert = Insert.into(Table.of("my_table")).columns("id").values(1);
        Insert copy = Insert.of(insert).values(2);
        assertEquals(1, insert.getValues().size());
        assertEquals(2, copy.getValues().size());
    }

    @Test
    public void testInsertValuesAndQueryReplaceEachOther() {
        Table source = Table.of("source");
        Select select = Select.newInstance().select(source.column("id")).from(source);

        // a query replaces all previously added rows
        Insert insert = Insert.into(Table.of("target")).columns("id").values(1).values(2).query(select);
        assertTrue(insert.getValues().isEmpty());
        assertTrue(insert.toSql().startsWith("insert into target (id) select"));

        // values replace a previously set query
        insert.values(3);
        assertTrue(insert.getQuery().isEmpty());
        assertEquals("insert into target (id) values (3) ", insert.toSql());

        // query(null) removes the query without touching the values
        assertEquals(1, insert.query(null).getValues().size());
    }

    @Test
    public void testRemoveWithResetsRecursive() {
        Table source = Table.of("source");
        Select cte = Select.newInstance().select(source.column("id")).from(source);

        Select select = Select.newInstance().withRecursive("r", cte).removeWith().with("c", cte);
        assertFalse(select.isWithRecursive());
        assertTrue(select.toSql().startsWith("with c"));

        Update update = Update.newInstance().withRecursive("r", cte).removeWith().with("c", cte);
        assertFalse(update.isWithRecursive());
        assertTrue(update.toSql().startsWith("with c"));

        Insert insert = Insert.into(Table.of("target")).withRecursive("r", cte).removeWith().with("c", cte);
        assertFalse(insert.isWithRecursive());
        assertTrue(insert.toSql().startsWith("with c"));
    }

    @Test
    public void testInsertIgnoresEmptyRow() {
        Insert insert = Insert.into(Table.of("my_table")).columns("id").values().values((Object[]) null);
        assertTrue(insert.getValues().isEmpty());
    }

    @Test
    public void testUnsupportedLiteralTypesAreRejected() {
        Table table = Table.of("my_table").alias("t");
        java.util.UUID uuid = java.util.UUID.randomUUID();

        // Values that cannot be converted must fail instead of silently becoming null
        assertThrows(IllegalArgumentException.class, () -> Literal.of(uuid));
        assertThrows(IllegalArgumentException.class, () -> table.column("id").eq(uuid));
        assertThrows(IllegalArgumentException.class, () -> table.column("id").in(1, uuid));
        assertThrows(IllegalArgumentException.class, () -> Insert.into(table).values(uuid));
        assertThrows(IllegalArgumentException.class, () -> Update.newInstance().table(table)
                .set(table.column("id")).value(uuid));
    }

    @Test
    public void testInWithExpressions() {
        Table table = Table.of("my_table").alias("t");
        Placeholder placeholder = Placeholder.of(3);
        In in = table.column("id").in(1, table.column("other_id"), placeholder, table.column("value").plus(1));

        assertEquals("t.id in (1, t.other_id, ?, (t.value + 1))", in.toSql());
        assertEquals(1, in.getPlaceholders().size());
        assertSame(placeholder, in.getPlaceholders().get(0));
        assertEquals("t.id not in ('a', 'b')", table.column("id").notIn(java.util.List.of("a", "b")).toSql());
    }

    @Test
    public void testNotInWithVarargs() {
        Table table = Table.of("my_table").alias("t");

        assertEquals("t.id not in ('a', 'b')", table.column("id").notIn("a", "b").toSql());
        assertEquals("t.id not in (1, t.other_id)", table.column("id").notIn(1, table.column("other_id")).toSql());
    }

    @Test
    public void testSchemaRenderedAsIdentifier() {
        SqlBuildOptions options = SqlBuildOptions.defaults()
                .setIdentifierCase(SqlBuildOptions.IdentifierCase.UPPERCASE)
                .setIdentifierDelimiter("\"");
        Table table = Table.of("my_table", "my_schema").alias("t");

        Select select = Select.newInstance().select(table.column("id")).from(table);
        assertEquals("select t.\"ID\" from \"MY_SCHEMA\".\"MY_TABLE\" t ",
                SqlBuilder.newInstance().build(select, options));

        Insert insert = Insert.into(Table.of("my_table", "my_schema")).columns("id").values(1);
        assertEquals("insert into \"MY_SCHEMA\".\"MY_TABLE\" (\"ID\") values (1) ",
                SqlBuilder.newInstance().build(insert, options));

        org.citydb.sqlbuilder.function.Function function = org.citydb.sqlbuilder.function.Function.of("my_function")
                .setSchema("my_schema");
        assertEquals("\"MY_SCHEMA\".my_function()", SqlBuilder.newInstance().build(function, options));

        // default options leave the schema unchanged
        assertEquals("select t.id from my_schema.my_table t ", select.toSql());
    }

    @Test
    public void testNullAndTemporalLiterals() {
        Table table = Table.of("my_table").alias("t");

        assertEquals("null", Literal.of(null).toSql());
        assertEquals("insert into my_table (id) values (null) ",
                Insert.into(Table.of("my_table")).columns("id").values((Object) null).toSql());

        java.time.Instant instant = java.time.Instant.ofEpochMilli(1718697600000L);
        String expected = TimestampLiteral.of(instant).toSql();
        assertEquals(expected, Literal.of(instant).toSql());
        assertEquals(expected, Literal.of(java.util.Date.from(instant)).toSql());
        assertEquals(expected, Literal.of(instant.atOffset(java.time.ZoneOffset.UTC)).toSql());
        assertEquals("t.created = " + expected, table.column("created").eq(instant).toSql());
    }

    @Test
    public void testGroupByExpression() {
        Table table = Table.of("my_table");
        Select select = Select.newInstance()
                .select(table.column("id"))
                .from(table)
                .groupBy(PlainSql.of("year(a.date)"));
        String sql = select.toSql();
        System.out.println("GroupBy Expression SQL: " + sql);
        // Since it is a top-level QueryStatement, it is formatted without enclosing parentheses for maximum readability:
        assertEquals("select a.id from my_table a group by year(a.date) ", sql.replace("\r\n", "\n").replace("\n", " "));
    }

    @Test
    public void testFluentFunctions() {
        Table table = Table.of("my_table");

        // Testing avg() and upper()
        String avgSql = table.column("salary").avg().toSql();
        assertEquals("avg(a.salary)", avgSql);

        String upperSql = table.column("name").upper().toSql();
        assertEquals("upper(a.name)", upperSql);

        // Testing lag with offset
        String lagSql = table.column("salary").lag(IntegerLiteral.of(2)).toSql();
        assertEquals("lag(a.salary, 2)", lagSql);
    }

    @Test
    public void testNegate() {
        Table table = Table.of("my_table").alias("a");

        assertEquals("-(a.salary)", Operators.negate(table.column("salary")).toSql());
        assertEquals("-(a.salary)", table.column("salary").negate().toSql());
    }

    @Test
    public void testNegatePreservesArithmeticPrecedence() {
        Table table = Table.of("my_table").alias("a");

        assertEquals("(-(a.salary) + 100)", table.column("salary").negate().plus(100).toSql());
        assertEquals("-((a.salary + 100))", table.column("salary").plus(100).negate().toSql());
    }

    @Test
    public void testNegateAsSelection() {
        Table table = Table.of("my_table").alias("a");
        Select select = Select.newInstance()
                .select(table.column("salary").negate().as("negative_salary"))
                .from(table);

        String sql = select.toSql().replace("\r\n", "\n").replace("\n", " ");
        assertTrue(sql.contains("select -(a.salary) as negative_salary"));
    }

    @Test
    public void testNegateTraversesPlaceholders() {
        Table table = Table.of("my_table").alias("a");
        Placeholder placeholder = Placeholder.empty();

        Negate negate = table.column("salary").plus(placeholder).negate();

        assertEquals(1, negate.getPlaceholders().size());
        assertSame(placeholder, negate.getPlaceholders().get(0));
    }

    @Test
    public void testComplexSelectQuery() {
        Table emp = Table.of("employee").alias("emp");
        Table dept = Table.of("department").alias("dept");

        Select select = Select.newInstance()
                .select(
                        emp.column("id"),
                        ArithmeticOperation.of(emp.column("first_name"), Operators.CONCAT, emp.column("last_name")).as("full_name"),
                        IntegerLiteral.of(1000).plus(emp.column("salary")).as("projected_salary")
                )
                .from(emp)
                .join(dept).on(emp.column("dept_id").eq(dept.column("id")))
                .where(
                        emp.column("salary").between(2000, 10000)
                                .and(emp.column("status").eq("ACTIVE").or(emp.column("role").eq("MANAGER")))
                )
                .orderBy(OrderBy.of(emp.column("salary"), OrderBy.DESCENDING).nullsLast())
                .offset(10)
                .fetch(20);

        String sql = select.toSql().replace("\r\n", "\n").replace("\n", " ");
        System.out.println("Complex Select SQL: " + sql);

        assertTrue(sql.contains("select emp.id"));
        assertTrue(sql.contains("(emp.first_name || emp.last_name) as full_name"));
        assertTrue(sql.contains("(1000 + emp.salary) as projected_salary"));
        assertTrue(sql.contains("inner join department dept on dept.id = emp.dept_id"));
        assertTrue(sql.contains("where emp.salary between 2000 and 10000 and (emp.status = 'ACTIVE' or emp.role = 'MANAGER' )"));
        assertTrue(sql.contains("order by emp.salary desc nulls last"));
        assertTrue(sql.contains("offset 10 rows fetch next 20 rows only"));
    }

    @Test
    public void testSetOperations() {
        Table tableA = Table.of("table_a").alias("a");
        Table tableB = Table.of("table_b").alias("b");

        Select selectA = Select.newInstance().select(tableA.column("id")).from(tableA);
        Select selectB = Select.newInstance().select(tableB.column("id")).from(tableB);

        SetOperator unionAll = Sets.unionAll(selectA, selectB);
        String sql = unionAll.toSql().replace("\r\n", "\n").replace("\n", " ");
        System.out.println("Union All SQL: " + sql);

        assertEquals("(select a.id from table_a a ) union all (select b.id from table_b b )", sql);
    }

    @Test
    public void testLiteralsAndDatatypes() {
        assertEquals("null", NullLiteral.getInstance().toSql());
        assertEquals("1.5", DoubleLiteral.of(1.5).toSql());
        assertEquals("true", BooleanLiteral.of(true).toSql());
        assertEquals("false", BooleanLiteral.of(false).toSql());

        java.time.LocalDate date = java.time.LocalDate.of(2026, 6, 18);
        assertEquals("'2026-06-18'", DateLiteral.of(date).toSql());

        java.time.Instant instant = java.time.Instant.ofEpochMilli(1718697600000L); // 2024-06-18
        assertNotNull(TimestampLiteral.of(instant).toSql());
    }

    @Test
    public void testArrayValueUtility() {
        ArrayValue arrayValue = ArrayValue.of(java.util.List.of(1, 2, 3), Integer.class);
        assertEquals(3, arrayValue.size());
        assertEquals(Integer.class, arrayValue.getElementType());
    }

    @Test
    public void testWildcardColumns() {
        Table table = Table.of("my_table").alias("t");
        assertEquals("*", Column.WILDCARD.toSql());
        assertEquals("t.*", WildcardColumn.of(table).toSql());
    }

    @Test
    public void testOperatorsAndPredicates() {
        Table table = Table.of("my_table").alias("t");

        // IsNull & Like Predicates
        assertEquals("t.id is null", table.column("id").isNull().toSql());
        assertEquals("t.id is not null", table.column("id").isNotNull().toSql());
        assertEquals("t.name like 'test%'", table.column("name").like("test%").toSql());
        assertEquals("t.name not like 'test%'", table.column("name").notLike("test%").toSql());

        // Case expression / conditions
        Case caseExpr = Case.newInstance()
                .when(table.column("id").eq(1)).then(StringLiteral.of("one"))
                .orElse(StringLiteral.of("other"));
        assertTrue(caseExpr.toSql().contains("case"));

        // Collate
        assertEquals("t.name collate utf8", table.column("name").collate("utf8").toSql());
    }

    @Test
    public void testJoinsStaticBuilders() {
        Table table = Table.of("my_table").alias("t");
        Join join = Joins.inner(Table.of("other"), "other_id", "=", table.column("id"));
        assertNotNull(join);
    }
}