# SqlBuilder Developer Guide

**SqlBuilder** is a lightweight library for building SQL statements in Java. Statements are trees of Java objects (an abstract syntax tree, AST) that render themselves as SQL. It supports `SELECT`, `INSERT` and `UPDATE` statements.

---

## 1. Quick Start

```java
import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.SqlBuildOptions;
import org.citydb.sqlbuilder.query.Select;
import org.citydb.sqlbuilder.schema.Table;

Table author = Table.of("author");
Table book = Table.of("book");

Select select = Select.newInstance()
    .select(author.column("first_name"), book.column("title"))
    .from(author)
    .join(book).on(author.column("id").eq(book.column("author_id")))
    .where(author.column("year_of_birth").gt(1920)
        .and(author.column("first_name").eq("Paulo")))
    .orderBy(book.column("title"));

SqlBuildOptions options = SqlBuildOptions.defaults()
    .setIndent("  ")
    .setKeywordCase(SqlBuildOptions.KeywordCase.UPPERCASE);

String sql = SqlBuilder.newInstance().build(select, options);
```

Generated SQL:

```sql
SELECT
  a.first_name,
  b.title
FROM
  author a
  INNER JOIN book b ON b.author_id = a.id
WHERE
  a.year_of_birth > 1920
  AND a.first_name = 'Paulo'
ORDER BY
  b.title
```

Tables without an explicit alias get generated aliases (`a`, `b`, …) in the order they are rendered. Use `Table.of("author").alias("au")` to choose an alias yourself. If you set aliases explicitly, set them on all tables of a statement so they cannot clash with generated ones.

### Fluent API vs. Static Factory Methods

Every construct can be built in two equivalent ways: with fluent method chains, which are compact, or with static `of(...)` factories, which suit code that assembles statements dynamically. Both styles can be mixed.

| Feature | Fluent style | Static factory style |
| :--- | :--- | :--- |
| Comparison | `author.column("id").eq(42)` | `BinaryComparisonOperation.of(author.column("id"), "=", IntegerLiteral.of(42))` |
| Negation | `employee.column("salary").negate()` | `Negate.of(employee.column("salary"))` |
| Function | `author.column("name").lower()` | `Functions.lower(author.column("name"))` |
| Range | `author.column("year").between(1900, 1950)` | `Between.of(author.column("year"), IntegerLiteral.of(1900), IntegerLiteral.of(1950))` |
| Null check | `author.column("name").isNull()` | `IsNull.of(author.column("name"))` |
| Pattern | `author.column("name").like("A%")` | `Like.of(author.column("name"), StringLiteral.of("A%"))` |

---

## 2. Type System

The interface hierarchy lets the compiler reject many invalid statements before they reach the database.

```
                       [SqlObject]
                            │ (all AST nodes)
                            ▼
                       [Expression]
         ┌──────────────────┴──────────────────┐
         ▼                                     ▼
[BooleanExpression]                    [ScalarExpression]
 (predicates)                          (value-producing expressions)
                                       ┌──────────────┴──────────────┐
                                       ▼                             ▼
                              [QueryExpression]             [NumericExpression]
                               (subqueries, lists)           (arithmetic)
```

| Interface | Purpose | Typical classes |
| :--- | :--- | :--- |
| `SqlObject` | Any node that can render itself as SQL. | all AST nodes |
| `Expression` | Any operand or sub-expression. | `Column`, `Literal`, operations |
| `ScalarExpression` | A single value. Provides comparisons (`eq()`, `lt()`, `between()`, `like()`, `in()`, `isNull()`) and function shortcuts (`avg()`, `upper()`, `lag()`, …). | `Column`, `Literal`, `Function`, `Cast`, `Case`, `PlainSql`, arithmetic |
| `NumericExpression` | A numeric value. Provides `plus()`, `minus()`, `multiply()`, `divide()`, `modulo()` and `negate()`. | `ArithmeticOperation`, `Negate`, `IntegerLiteral`, `Column`, `Function`, … |
| `BooleanExpression` | A predicate. Provides `and()`, `or()`, `not()`. | comparisons, logical operations, `IsNull`, `Column`, `Function`, … |
| `QueryExpression` | A subquery or a list of values. | `Select`, `SetOperator`, `ExpressionList`, `Function`, `PlainSql` |
| `Selection<?>` | Anything that can appear in a `SELECT` list and be aliased with `as(...)`. | `Column`, `Literal`, `Function`, `Case`, `Cast`, `Select`, … |

`Column`, `Function`, `Cast`, `Case` and `PlainSql` are both numeric and boolean expressions, so they can be used in calculations as well as in predicates. Some examples of what the types enforce:

- `where(...)` and `having(...)` only accept `BooleanExpression`s.
- `plus()`, `multiply()` and the other arithmetic operators are only available on `NumericExpression`s.
- `groupBy(...)` accepts any `Expression`; whether the expression is valid for grouping is up to the caller.

### Raw Java Values

Many methods also accept plain Java values (`eq(42)`, `between(1900, 1950)`, `in("a", "b")`, `Insert.values(...)`, `Update.set(...).value(...)`). An `Expression` is used as is; any other value is converted into a literal:

| Java type | Literal |
| :--- | :--- |
| `String` | `StringLiteral` (single quotes are escaped by doubling) |
| `Integer`, `Long`, other `Number` types with an integral value | `IntegerLiteral` |
| `Double`, other `Number` types with a fractional value | `DoubleLiteral` |
| `Boolean` | `BooleanLiteral` |
| `java.sql.Date`, `LocalDate` | `DateLiteral` |
| `java.sql.Timestamp`, `java.util.Date`, `Instant`, `OffsetDateTime`, `Calendar` | `TimestampLiteral` |
| `null` | `NullLiteral` |

Any other type (e.g. `UUID`, enums, or a collection passed as a single argument) throws an `IllegalArgumentException`. Convert such values yourself, or pass them as a [`Placeholder`](#5-placeholders-and-plainsql).

`Number` types other than `Integer`, `Long` and `Double` are converted through `doubleValue()`, so a `BigDecimal` with many digits may lose precision. Use a placeholder for exact decimal values.

---

## 3. SELECT Queries

The SQL in the comments below is the default single-line output of `toSql()`, with whitespace simplified.

### A. Projections and Tables

An empty selection list renders as `select *`.

```java
Table author = Table.of("author");

// select * from author a
Select all = Select.newInstance().from(author);

// select a.first_name, a.last_name from author a
Select names = Select.newInstance()
    .select(author.column("first_name"), author.column("last_name"))
    .from(author);
```

### B. Joins

```java
Table author = Table.of("author");
Table book = Table.of("book");

// select a.last_name, b.title from author a inner join book b on b.author_id = a.id
Select select = Select.newInstance()
    .select(author.column("last_name"), book.column("title"))
    .from(author)
    .join(book).on(author.column("id").eq(book.column("author_id")));
```

The static `Joins` helper creates joins of other types, e.g. `Joins.left(...)`.

### C. Filtering

Combine predicates with `and()`, `or()` and `not()`:

```java
// select * from author a
//   where a.year_of_birth between 1900 and 1950
//   and (a.first_name = 'Paulo' or a.first_name = 'Gabriel')
Select select = Select.newInstance()
    .from(author)
    .where(author.column("year_of_birth").between(1900, 1950)
        .and(author.column("first_name").eq("Paulo")
            .or(author.column("first_name").eq("Gabriel"))));
```

`in(...)` and `notIn(...)` test against a list or a subquery. A list may contain any scalar expressions, such as literals, columns, placeholders or functions:

```java
// a.first_name in ('Paulo', 'Gabriel')
author.column("first_name").in("Paulo", "Gabriel");

// a.id in (1, a.coauthor_id, ?)
author.column("id").in(1, author.column("coauthor_id"), Placeholder.of(42));

// a.id in (select b.author_id from book b)
author.column("id").in(Select.newInstance().select(book.column("author_id")).from(book));
```

### D. Calculations and Functions

```java
Table employee = Table.of("employee");

// select -(a.salary) as negative_salary, (a.salary + 100) as raised from employee a
Select select = Select.newInstance()
    .select(employee.column("salary").negate().as("negative_salary"),
        employee.column("salary").plus(100).as("raised"))
    .from(employee);
```

`ScalarExpression` offers shortcuts for common functions:

- **Aggregates:** `avg()`, `count()`, `max()`, `min()`, `sum()`
- **Strings:** `length()`, `upper()`, `lower()`, `trim()`, `concatFunc(...)`
- **Analytical:** `firstValue()`, `lastValue()`, `lag()`, `lag(offset)`, `lead()`, `nthValue(offset)`

`employee.column("name").upper()` is equivalent to `Functions.upper(employee.column("name"))`. Use `Function.of(name, arguments...)` for any other function.

### E. Grouping

```java
// select a.department_id, avg(a.salary) as avg_sal from employee a
//   group by a.department_id having avg(a.salary) > 5000
Select select = Select.newInstance()
    .select(employee.column("department_id"), employee.column("salary").avg().as("avg_sal"))
    .from(employee)
    .groupBy(employee.column("department_id"))
    .having(employee.column("salary").avg().gt(5000));
```

### F. Sorting and Pagination

```java
// select * from author a order by a.last_name nulls last
//   offset 10 rows fetch next 20 rows only
Select select = Select.newInstance()
    .from(author)
    .orderBy(OrderBy.of(author.column("last_name"), OrderBy.ASCENDING).nullsLast())
    .offset(10)
    .fetch(20);
```

### G. Common Table Expressions (`WITH`)

```java
CommonTableExpression cte = CommonTableExpression.of("author_ids",
    Select.newInstance().select(author.column("id")).from(author));

// with author_ids as (select a.id from author a) select * from author_ids b
Select select = Select.newInstance()
    .with(cte)
    .from(Table.of(cte));
```

Use `withRecursive(...)` instead of `with(...)` to render `with recursive`. `removeWith()` removes all CTEs and resets the recursive flag. `Insert` and `Update` support CTEs in the same way.

### H. Set Operations

```java
// (select a.id from author a) union all (select b.id from book b)
SetOperator union = Sets.unionAll(
    Select.newInstance().select(author.column("id")).from(author),
    Select.newInstance().select(book.column("id")).from(book));
```

### I. Window Functions

```java
Window window = Window.newInstance()
    .partitionBy(employee.column("department_id"))
    .orderBy(employee.column("salary"));
window.rows().betweenPreceding(IntegerLiteral.of(1)).andFollowing(IntegerLiteral.of(3));

// select row_number() over (partition by a.department_id order by a.salary
//   rows between 1 preceding and 3 following) from employee a
Select select = Select.newInstance()
    .select(WindowFunction.of(Functions.rowNumber(), window))
    .from(employee);
```

---

## 4. Modifying Data

### A. INSERT

`Insert` adds rows either from values or from a query. The target table is rendered without an alias, and columns without a table prefix.

```java
Table author = Table.of("author", "library");

// insert into library.author (first_name, last_name)
//   values ('Paulo', 'Coelho'), ('Isabel', 'Allende')
Insert insert = Insert.into(author)
    .columns(author.column("first_name"), author.column("last_name"))
    .values("Paulo", "Coelho")
    .values("Isabel", "Allende");
```

Each call to `values(...)` adds one row; calls without values are ignored. Columns can also be given by name: `columns("first_name", "last_name")`. With placeholders, the same statement can be reused for JDBC batches:

```java
// insert into library.author (first_name, last_name) values (?, ?)
Insert insert = Insert.into(author)
    .columns("first_name", "last_name")
    .values(Placeholder.empty(), Placeholder.empty());
```

To insert the result of a query, pass it to `query(...)`. It is rendered without enclosing parentheses:

```java
Table book = Table.of("book");

// insert into book_archive (id) select a.id from book a where a.year < 1900
Insert insert = Insert.into(Table.of("book_archive"))
    .columns("id")
    .query(Select.newInstance()
        .select(book.column("id"))
        .from(book)
        .where(book.column("year").lt(1900)));
```

A statement uses either values or a query, and the last call wins: `query(...)` removes all rows added before, and `values(...)` removes a query set before. `removeValues()` and `removeQuery()` remove them explicitly.

Several rows in one `values` clause require Oracle 23c or later. For older versions, insert one row per statement or use JDBC batches.

### B. UPDATE

```java
Table author = Table.of("author");

// update author a set first_name = 'Paulo', year_of_birth = 1947 where a.id = 42
Update update = Update.newInstance()
    .table(author)
    .set(author.column("first_name")).value("Paulo")
    .set(author.column("year_of_birth")).value(1947)
    .where(author.column("id").eq(42));
```

Assigned columns are rendered without a table prefix. The table keeps its alias, so `where` conditions and values can refer to its columns, e.g. `.set(author.column("year_of_birth")).value(author.column("year_of_birth").plus(1))`. Values can be literals, expressions, placeholders or subqueries.

---

## 5. Placeholders and PlainSql

### A. Placeholders

Use `Placeholder` instead of literals to keep values out of the SQL string and bind them through a JDBC `PreparedStatement`. A placeholder renders as `?` and can get its value when it is created or later:

```java
Placeholder year = Placeholder.of(1920);

Placeholder name = Placeholder.empty();
name.setValue("Paulo");
```

`getPlaceholders()` returns all placeholders of a statement in the order they appear in the SQL (see [7. Execution with JDBC](#7-execution-with-jdbc)). To render placeholders differently, e.g. as `$1` or `:name`, set a custom `PlaceholderBuilder` in `SqlBuildOptions`.

### B. PlainSql

`PlainSql` inserts SQL text for features the library does not model. `{}` tokens are replaced by the given arguments; AST nodes are rendered as SQL:

```java
// st_distance(a.geom_a, a.geom_b)
PlainSql distance = PlainSql.of("st_distance({}, {})", table.column("geom_a"), table.column("geom_b"));
```

Other arguments are inserted as text **without escaping**. Never pass user input as a `PlainSql` argument; use a placeholder instead.

---

## 6. Formatting (`SqlBuildOptions`)

`toSql()` and `toString()` render a statement on a single line with default options:

```java
Select select = Select.newInstance().select(author.column("first_name")).from(author);

// select a.first_name from author a
String sql = select.toSql();
```

For readable output, e.g. for logs, files or user interfaces, pass `SqlBuildOptions`:

```java
SqlBuildOptions options = SqlBuildOptions.defaults()
    .setNewline("\n")                                            // line separator
    .setIndent("  ")                                             // indentation; enables multi-line output
    .setKeywordCase(SqlBuildOptions.KeywordCase.UPPERCASE)       // keyword case
    .setIdentifierCase(SqlBuildOptions.IdentifierCase.UPPERCASE) // identifier case
    .setIdentifierDelimiter("\"")                                // quote identifiers, e.g. "AUTHOR"
    .setUseJdbcEscapeNotation(true);                             // dates as {d '2026-01-02'}

String sql = SqlBuilder.newInstance().build(select, options);
```

With `setIndent("  ")` and uppercase keywords, the statement above renders as:

```sql
SELECT
  a.first_name
FROM
  author a
```

---

## 7. Execution with JDBC

The rendered SQL can be executed with plain JDBC. Without placeholders, use a `Statement`:

```java
try (Statement stmt = connection.createStatement();
     ResultSet rs = stmt.executeQuery(select.toSql())) {
    while (rs.next()) {
        String firstName = rs.getString("first_name");
    }
}
```

With placeholders, use a `PreparedStatement` and bind the values in the order returned by `getPlaceholders()`:

```java
Placeholder keyword = Placeholder.of("%City%");

// select a.title from book a where a.title like ?
Select select = Select.newInstance()
    .select(book.column("title"))
    .from(book)
    .where(book.column("title").like(keyword));

try (PreparedStatement ps = connection.prepareStatement(select.toSql())) {
    List<Placeholder> placeholders = select.getPlaceholders();
    for (int i = 0; i < placeholders.size(); i++) {
        bind(ps, i + 1, placeholders.get(i).getValue().orElse(null));
    }

    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String title = rs.getString("title");
        }
    }
}
```

Bind values with type-specific setters rather than a generic `setObject(...)`. Some drivers fail on `setObject(index, null)` or coerce types unexpectedly:

```java
private void bind(PreparedStatement ps, int index, Object value) throws SQLException {
    if (value == null) {
        ps.setNull(index, Types.NULL);
    } else if (value instanceof String string) {
        ps.setString(index, string);
    } else if (value instanceof Integer number) {
        ps.setInt(index, number);
    } else if (value instanceof Long number) {
        ps.setLong(index, number);
    } else if (value instanceof Double number) {
        ps.setDouble(index, number);
    } else if (value instanceof Boolean bool) {
        ps.setBoolean(index, bool);
    } else if (value instanceof LocalDate date) {
        ps.setDate(index, Date.valueOf(date));
    } else {
        ps.setObject(index, value);
    }
}
```

`Insert` and `Update` are executed the same way with `executeUpdate()`.
