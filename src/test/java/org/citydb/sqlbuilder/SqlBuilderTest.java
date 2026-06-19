package org.citydb.sqlbuilder;

import org.citydb.sqlbuilder.join.Join;
import org.citydb.sqlbuilder.join.Joins;
import org.citydb.sqlbuilder.literal.*;
import org.citydb.sqlbuilder.operation.ArithmeticOperation;
import org.citydb.sqlbuilder.operation.Case;
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






