# SqlBuilder

A lightweight, type-safe, and dynamic SQL builder for Java.

```sql
select
  *
from
  author a
  join book b on a.id = b.author_id
where
  a.year_of_birth > 1920
  and a.first_name = 'Paulo'
order by
  a.title
```

```java
Table author = Table.of("author");
Table book = Table.of("book");

Select select = Select.newInstance()
  .from(author)
  .join(book).on(author.column("id").eq(book.column("author_id")))
  .where(author.column("year_of_birth").gt(1920)
    .and(author.column("first_name").eq("Paulo")))
  .orderBy(author.column("title"));

System.out.println(
  select.toSql(SqlBuildOptions.defaults().setIndent("  ")));
```

## Developer Guide

For a  guide on how to integrate and use **SqlBuilder** in your own Java projects, along with architecture details
and core design concepts, please refer to the [Developer Guide](DEVELOPER-GUIDE.md).