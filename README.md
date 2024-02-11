# sqlbuilder
Dynamic SQL builder for the 3D City Database

<table>
<tr>
<td>

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

</td>
<td>

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
  select.toSQL(SQLBuildOptions.defaults().setIndent("  ")));
```

</td>
</tr>
</table>