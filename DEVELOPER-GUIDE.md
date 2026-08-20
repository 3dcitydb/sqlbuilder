# SqlBuilder Developer Guide

This guide is designed for developers who want to use the **SqlBuilder** library to generate dynamic and type-safe SQL queries in Java.

SqlBuilder is a lightweight, flexible, and type-safe Object Abstract Syntax Tree (AST) optimized for relational database structures. It supports building complex SQL statements (especially `SELECT` and `UPDATE`) programmatically.

---

## 1. Quick Start

With its fluent API, SqlBuilder allows you to formulate SQL queries intuitively using Java objects to represent tables, columns, and operations.

### Simple Select Example

```java
import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.SqlBuildOptions;
import org.citydb.sqlbuilder.query.Select;
import org.citydb.sqlbuilder.schema.Table;

// Define table structures
Table author = Table.of("author");
Table book = Table.of("book");

// Assemble the SELECT query
Select select = Select.newInstance()
    .select(author.column("first_name"), book.column("title"))
    .from(author)
    .join(book).on(author.column("id").eq(book.column("author_id")))
    .where(author.column("year_of_birth").gt(1920)
        .and(author.column("first_name").eq("Paulo")))
    .orderBy(book.column("title"));

// Generate SQL (with formatting options)
SqlBuildOptions options = SqlBuildOptions.defaults()
    .setIndent("  ")
    .setKeywordCase(SqlBuildOptions.KeywordCase.UPPERCASE);

String sql = SqlBuilder.newInstance().build(select, options);
System.out.println(sql);
```

#### Generated SQL:
```sql
SELECT a.first_name, b.title
FROM author a
JOIN book b ON a.id = b.author_id
WHERE (a.year_of_birth > 1920 AND a.first_name = 'Paulo')
ORDER BY b.title
```

### Fluent API vs. Static Factory Methods

SqlBuilder is designed with a "dual-API" philosophy, offering two equally powerful paradigms to construct query objects based on your specific use case:

1. **Fluent Chain Style:** Highly convenient and compact for rapid query building with IDE auto-completion. Method chains are executed from left to right.
2. **Static Factory Method Style:** Highly modular and declarative. Ideal if your code dynamically or recursively builds query AST trees (e.g., when converting a custom JSON query structure into SqlBuilder elements). Here, you instantiate raw AST nodes directly using static `of()` constructors.

Here is a comparison showing the equivalence of both styles:

| Feature | Fluent Chain Style (Compact) | Static Factory Style / AST (Modular) | Typical Classes / Helpers |
| :--- | :--- | :--- | :--- |
| **Comparisons** | `author.column("id").eq(42)` | `BinaryComparisonOperation.of(author.column("id"), "=", IntegerLiteral.of(42))` | `BinaryComparisonOperation`, `Operators` |
| **String Functions** | `author.column("name").lower()` | `Functions.lower(author.column("name"))` | `Functions`, `Function` |
| **Range Checks** | `author.column("year").between(1900, 1950)` | `Between.of(author.column("year"), IntegerLiteral.of(1900), IntegerLiteral.of(1950))` | `Between` |
| **Null Comparisons** | `author.column("name").isNull()` | `IsNull.of(author.column("name"))` | `IsNull` |
| **Pattern Matching** | `author.column("name").like("A%")` | `Like.of(author.column("name"), StringLiteral.of("A%"))` | `Like` |

You can freely mix and match both styles within the same query.

---

## 2. Type Hierarchy & Compile-Time Safety

The most powerful aspect of SqlBuilder is its robust type system, which mirrors relational algebra. The Java compiler acts as a static analyzer, weeding out syntactically or logically invalid SQL constructions before they can ever hit the database.

### Core Interface Tree (Class Hierarchy Diagram)

```
                       [SqlObject]
                            │ (Implemented by all AST nodes)
                            ▼
                       [Expression]
                            │ (Base for any valid SQL expression fragment)
         ┌──────────────────┴──────────────────┐
         ▼                                     ▼
[BooleanExpression]                    [ScalarExpression]
 (Logical Predicates)                  (Value-producing expressions)
                                       ┌──────────────┴──────────────┐
                                       ▼                             ▼
                              (QueryExpression)             [NumericExpression]
                               - Subqueries                  - plus(), minus()
```

*Hinweis: Einige vielseitige AST-Klassen (z. B. `Column`, `Function`, `Cast`, `Case` und `PlainSql`) implementieren sowohl `NumericExpression` als auch `BooleanExpression`. Dadurch wird eine hohe Flexibilität gewährleistet, sodass sie je nach SQL-Kontext sowohl für logische Prädikate als auch für arithmetische Berechnungen verwendet werden können.*

### Roles / Responsibilities of Key Interfaces

| Interface | Description | Typical Implementing Classes | Allowed Contexts |
| :--- | :--- | :--- | :--- |
| **`SqlObject`** | Root of the syntax tree. Any node that can render itself into SQL. | All SQL AST nodes | Visitor traversals |
| **`Expression`** | Represents any valid operand or sub-expression in SQL. | `Column`, `Literal`, operations | AST clauses, assignments |
| **`ScalarExpression`** | Represents expressions evaluating to a **single scalar or row value**. Provides comparison methods (`eq()`, `lt()`, `between()`, `like()`, `isNull()`) and **fluent function shortcuts** (`avg()`, `upper()`, `lag()`, etc.). | `Column`, `Literal`, `Function`, `Cast`, `Case`, `PlainSql`, arithmetic operations | `SELECT`, `GROUP BY`, comparison operands |
| **`NumericExpression`** | Represents **numeric** values, adding support for math operators (`plus()`, `minus()`, `multiply()`, `divide()`, `modulo()`). | `ArithmeticOperation`, `Column`, `Cast`, `Case`, `Function`, `PlainSql`, numbers | Math formulas, ranges |
| **`BooleanExpression`** | Represents logical truths (**predicates**). Provides logical chaining (`and()`, `or()`, `not()`). | `BinaryComparisonOperation`, logical operations, `IsNull`, `Column`, `Cast`, `Case`, `Function`, `PlainSql` | `WHERE`, `HAVING`, `ON` (Join) |
| **`QueryExpression`** | Represents sub-statements or groupings acting as table sources or scalar lists. | `Select`, `SetOperator`, `PlainSql`, `LiteralList`, `Function` | Subqueries, `FROM` derived tables |
| **`Selection<?>`** | Any projection or expression in a `SELECT` clause that can be aliased (`.as("alias")`). | `Column`, `Literal`, `Case`, `Cast`, `Function`, `PlainSql` | `SELECT` projection, `ORDER BY` target |

### How Compile-Time Constraints Work

The class and interface hierarchy enforces correct SQL structure at compile-time rather than runtime. Below are key examples of how these constraints prevent invalid SQL patterns:

1. **Flexible Grouping Expressions:**
   The `groupBy(...)` clause accepts `Expression...`, allowing scalar expressions and other expression types. The caller is responsible for ensuring that the supplied expressions are valid for the target database.
2. **Type-Safe Filtering:**
   The `.where()` method requires `BooleanExpression...`. You cannot pass raw math formulas or bare values that do not resolve to logical predicates.
3. **Restricted Math Operators:**
   Mathematical chain operations like `.plus()` or `.multiply()` are restricted to `NumericExpression` implementations, guaranteeing you won't accidentally add values to non-numeric types.

---

## 3. Building Core SELECT Queries

SqlBuilder provides a clean, sequential flow to build relational queries, matching the logical order of standard SQL clauses.

### A. Projections & Table Sources (`select()`, `from()`)

An empty selection list automatically falls back to `SELECT *`. You specify sources by passing `Table` objects:

```java
Table author = Table.of("author");

// "SELECT *" fallback:
Select selectAll = Select.newInstance().from(author);

// Specific projections:
Select selectFields = Select.newInstance()
    .select(author.column("first_name"), author.column("last_name"))
    .from(author);
```

### B. Joining Tables (`join()`, `on()`)

You can perform inner and outer joins fluently using `.join().on()` or the static `Joins` helper class:

```java
Table author = Table.of("author").alias("a");
Table book = Table.of("book").alias("b");

Select select = Select.newInstance()
    .select(author.column("last_name"), book.column("title"))
    .from(author)
    .join(book).on(author.column("id").eq(book.column("author_id")));
```

### C. Filtering & Logical Conditions (`where()`)

Filter sets are built using logical comparison operations (`eq`, `ne`, `gt`, `lt`, `between`, `like`, `isNull`) combined using `.and()`, `.or()`, and `.not()` logic:

```java
Select select = Select.newInstance()
    .from(author)
    .where(author.column("year_of_birth").between(1900, 1950)
        .and(author.column("first_name").eq("Paulo")
            .or(author.column("first_name").eq("Gabriel"))));
```

### D. Grouping & Aggregating (`groupBy()`, `having()`)

The `groupBy` clause accepts `Expression...`, so you can group by simple columns or compound expressions:

```java
Table employee = Table.of("employee");

Select select = Select.newInstance()
    .select(employee.column("department_id"), employee.column("salary").avg().as("avg_sal"))
    .from(employee)
    .groupBy(employee.column("department_id"))
    .having(employee.column("salary").avg().gt(5000));
```

### E. Ordering & Pagination (`orderBy()`, `offset()`, `fetch()`)

You can easily request specific sorting (including `nullsFirst` and `nullsLast` options) and define page limits:

```java
Select select = Select.newInstance()
    .from(author)
    .orderBy(OrderBy.of(author.column("last_name"), OrderBy.ASCENDING).nullsLast())
    .offset(10)
    .fetch(20);
```

---

## 4. Advanced Query Techniques

Once you are comfortable with core SELECT statements, SqlBuilder offers support for complex relational patterns.

### A. Common Table Expressions (CTE / WITH Clauses)

Subqueries can be cleanly defined and referenced at the top level using CTEs:

```java
Select subQuery = Select.newInstance().select(author.column("id")).from(author);
CommonTableExpression cte = CommonTableExpression.of("author_ids", subQuery);

Select mainSelect = Select.newInstance()
    .with(cte)
    .select(Column.WILDCARD)
    .from(Table.of(cte));
```

### B. Window Functions & Analytical Queries

SqlBuilder provides extensive support for analytical `OVER (PARTITION BY ... ORDER BY ... ROWS BETWEEN ...)` blocks:

```java
Table employee = Table.of("employee");

Window window = Window.newInstance()
    .partitionBy(employee.column("department_id"))
    .orderBy(employee.column("salary"));

// rows between 1 preceding and 3 following
window.rows().betweenPreceding(IntegerLiteral.of(1)).andFollowing(IntegerLiteral.of(3));

WindowFunction rowExpr = WindowFunction.of(Functions.rowNumber(), window);
Select select = Select.newInstance().select(rowExpr).from(employee);
```

### C. Parameterized Queries using Placeholders

By default, SqlBuilder produces clean, hardcoded literals if they are passed. To separate SQL logic from parameter values and safely execute prepared statements, use `Placeholder`.

You can store values inside placeholders either **during instantiation** or **dynamically at a later point**:

```java
// Option 1: Instantiate with a stored value
Placeholder agePlaceholder = Placeholder.of(1920);

// Option 2: Instantiate an empty placeholder, and store/update the value later
Placeholder namePlaceholder = Placeholder.empty();
namePlaceholder.setValue("Paulo"); // Stores/updates the value
```

You can retrieve placeholders in order of SQL appearance to easily bind parameters to a JDBC `PreparedStatement` (see [6. Execution with JDBC](#6-execution-with-jdbc) for complete code examples).

Additionally, you can customize how placeholders are drawn (e.g., as named parameters `:authorId` or positions `$1`, `$2` in PostgreSQL) by implementing a custom `PlaceholderBuilder` through `SqlBuildOptions`.

### D. Fluent Function Shortcuts

To enhance developer ergonomics and facilitate method chaining, `ScalarExpression` provides built-in `default` shortcuts to wrap expressions inside SQL functions:

* **Aggregates:** `.avg()`, `.count()`, `.max()`, `.min()`, `.sum()`
* **String Utilities:** `.length()`, `.upper()`, `.lower()`, `.trim()`, `.concatFunc(Object argument)`
* **Analytical / Navigation Functions:** `.firstValue()`, `.lastValue()`, `.lag()`, `.lag(Literal<?> offset)`, `.lead()`, `.nthValue(Literal<?> offset)`

#### Fluent Chaining Example:
```java
// Traditional style:
Function upperName = Functions.upper(employee.column("name"));

// Fluent style using shortcuts:
Function upperNameFluent = employee.column("name").upper();

// Complex analytical query with fluent chaining:
Select select = Select.newInstance()
    .select(
        employee.column("id"),
        employee.column("salary").avg().as("avg_salary"),
        employee.column("salary").lag(IntegerLiteral.of(1)).as("prev_salary")
    )
    .from(employee);
```

### E. PlainSql for Custom Extensibility

For proprietary or database-specific features not natively modeled in the library, `PlainSql` serves as an extensible fallback. It supports parameter bindings using `{}` tokens:

```java
PlainSql customFunc = PlainSql.of("ST_Distance({}, {})", geomColumnA, geomColumnB);
```
*Security note:* Values passed as tokens are automatically escaped safely against RegEx injections during replacement.

---

## 5. Customizing Formatting & Dialects (`SqlBuildOptions`)

Generating and outputting SQL is not just for developers debugging code. In many real-world applications, generating formatted SQL is a core production feature—such as for audit logging, exporting SQL scripts to files, displaying query analyzers in user interfaces, or generating migration DDLs.

SqlBuilder provides extensive support for both compact, single-line output and highly structured pretty-printed formatting.

### A. Compact Inline SQL Output (e.g., for Logging and Simple Exports)

Every AST node in SqlBuilder overrides `.toString()`, which internally calls `.toSql()` using the default options. This is perfect for quick logging, diagnostic dumps, or inline outputs:

```java
Select select = Select.newInstance()
    .select(author.column("first_name"))
    .from(author);

// Quick print to logs (outputs single-line SQL by default)
System.out.println(select); 
// Generates: (select a.first_name from author a )
```

Alternatively, call `.toSql()` manually to get the default, unformatted SQL string:
```java
String sql = select.toSql();
```

### B. Formatted Multi-Line SQL (Pretty Printing for UI Displays and File Exports)

If your software needs to write SQL scripts into file exports (e.g. `.sql` dump files) or display the queries to database administrators in a graphical user interface (GUI), instantiate `SqlBuildOptions` and configure the **indentation** and **newline** characters to get a highly readable, pretty-printed block:

```java
SqlBuildOptions prettyPrintOptions = SqlBuildOptions.defaults()
    .setNewline("\n")                     // Choose your line-break character
    .setIndent("  ")                      // Choose indent character (e.g. 2 spaces or 4 spaces)
    .setKeywordCase(SqlBuildOptions.KeywordCase.UPPERCASE); // Choose keywords case

// Build and Pretty Print
String formattedSql = SqlBuilder.newInstance().build(select, prettyPrintOptions);
System.out.println(formattedSql);
```

#### Example Output:
```sql
SELECT
  a.first_name
FROM
  author a
```

### C. Advanced Formatting Customizations

A variety of options can be customized via `SqlBuildOptions` to adjust SQL generation for specific database dialects or conventions:

```java
SqlBuildOptions options = SqlBuildOptions.defaults()
    .setNewline("\r\n")                                      // Choose line separator
    .setIndent("    ")                                       // Choose indentation levels
    .setIdentifierDelimiter("\"")                            // Delimiters (e.g., "my_table")
    .setIdentifierCase(SqlBuildOptions.IdentifierCase.UPPERCASE) // Convert identifier casing
    .setKeywordCase(SqlBuildOptions.KeywordCase.UPPERCASE)       // Convert keyword casing
    .setUseJdbcEscapeNotation(true);                         // Use JDBC dates {d 'yyyy-mm-dd'}
```

---

## 6. Execution with JDBC

SqlBuilder compiles your Java query trees (`Select`, `Update`, etc.) into standard SQL strings. You can then execute these strings using JDBC.

### Non-Parameterized Queries (Using standard `Statement`)

If your query contains clean, hardcoded literals (and no user inputs), you can build and run it directly:

```java
Select select = Select.newInstance()
    .select(author.column("first_name"), book.column("title"))
    .from(author)
    .join(book).on(author.column("id").eq(book.column("author_id")));

// Compile to SQL
String sql = SqlBuilder.newInstance().build(select);

// Execute via JDBC Statement
try (Statement stmt = connection.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {
    while (rs.next()) {
        String firstName = rs.getString("first_name");
        String title = rs.getString("title");
    }
}
```

### Parameterized Queries (Using `PreparedStatement` & placeholders)

To secure user input, pass `Placeholder` instances. You set their values in Java, build the SQL String, and bind them dynamically.

In simple or toy projects, the generic `PreparedStatement.setObject(...)` method might suffice. However, **in real-world production code**, you should check the type of raw values to bind them using specific JDBC setters (`setInt()`, `setString()`, `setNull()`, etc.). This avoids driver-specific type-coercion issues and guarantees full database driver compliance.

Here is the recommended production binding pattern:

```java
Placeholder keywordPlaceholder = Placeholder.empty();

Select select = Select.newInstance()
    .select(book.column("title"))
    .from(book)
    .where(book.column("title").like(keywordPlaceholder));

// Set parameter value dynamically
keywordPlaceholder.setValue("%City%");

// Compile to parameterized SQL: SELECT b.title FROM book b WHERE b.title LIKE ?
String sql = SqlBuilder.newInstance().build(select);

try (PreparedStatement ps = connection.prepareStatement(sql)) {
    // 1. Collect ordered placeholders inside the select tree
    List<Placeholder> placeholders = select.getPlaceholders();
    
    // 2. Bind parameter values by safely inspecting their real types
    for (int i = 0; i < placeholders.size(); i++) {
        Object value = placeholders.get(i).getValue().orElse(null);
        bindParameter(ps, i + 1, value); // Parameter index is 1-based
    }

    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String title = rs.getString("title");
        }
    }
}

// ...

/**
 * Production Helper: Safely maps Java types to driver-compliant JDBC setters.
 * This is crucial since direct setObject(index, null) fails on some database drivers.
 */
private void bindParameter(PreparedStatement ps, int paramIndex, Object value) throws SQLException {
    if (value == null) {
        ps.setNull(paramIndex, java.sql.Types.NULL);
    } else if (value instanceof String) {
        ps.setString(paramIndex, (String) value);
    } else if (value instanceof Integer) {
        ps.setInt(paramIndex, (Integer) value);
    } else if (value instanceof Long) {
        ps.setLong(paramIndex, (Long) value);
    } else if (value instanceof Double) {
        ps.setDouble(paramIndex, (Double) value);
    } else if (value instanceof Boolean) {
        ps.setBoolean(paramIndex, (Boolean) value);
    } else if (value instanceof java.time.LocalDate) {
        ps.setDate(paramIndex, java.sql.Date.valueOf((java.time.LocalDate) value));
    } else {
        // Fallback for other objects
        ps.setObject(paramIndex, value);
    }
}
```
