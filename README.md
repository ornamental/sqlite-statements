# SQLite statement builders
This is a collection of SQL statement builders (for SQLite dialect) imitating SQL statement structure
to the extent allowed by Java syntax.

## Usage
It is presumed that when one wants to build a statement, one performs `import static` of the necessary methods 
from one or more of the classes mentioned in the table below. These methods serve as entry points for construction 
of statements and there parts such as SQL expressions.
One than uses chained method calls to complete the statement. As soon as all the necessary statement clauses 
are supplied, one invokes the `SqlStatement::build()` or `SqlStatement::build(StringBuilder)` method to produce
the string representation of the statement.

For some examples, see the test classes.

| Class | Purpose of the class's static members |
| -- | -- |
| `SqlStatements` | Start construction of SQLite statements (other than `SELECT` and `VALUES` statements without a `WITH` clause). |
| `SelectStatements` | Start construction of `SELECT` and `VALUES` statements without a `WITH` clause. |
| `Pragmas` | Produce the standard non-deprecated `PRAGMA` statements. |
| `ResultElements` | Obtain certain implementations of `ResultElement` (designating `*` and `tableName.*`). |
| `SqlExpressions` | Create SQL expressions, except for the literals, function calls, and `RAISE` expressions. |
| `Literal` | Wrap values into literal expressions. |
| `Functions` | Produce core function call expressions (including the aggregate functions). |
| `DateTimeFunctions` | Produce date and time function call expressions. |
| `Raise` | Produce `RAISE` expressions. |
| `TableExpressions` | Produce unaliased references to tables and virtual tables for use in contexts where table expressions are required. |
| `SortingOrder` | Sorting order enumeration (`ASC` and `DESC`). |
| `OnConflictAction` | Enumeration of possible statement behaviours in case of constraint violation. |
| `ForeignKeyAction` | Enumeration of possible referential actions. |
| `Collation` | Enumeration of standard collations and a method creating references to the custom ones. |

## Limitations
* The builders are mostly based on SQLite syntax diagrams (see [here](https://www.sqlite.org/syntax/sql-stmt.html)). 
As a consequence, there exist statements which may be produced using the builders
but will cause some kind of error upon execution attempt.
* The objects produced when building statements, including the objects representing intermediate stages 
of statement construction, are structurally immutable. While this allows implementing a fluent builder interface 
mimicking the SQLite syntax, it also means that once a part of a statement is specified, it cannot be removed 
or replaced later. There are ways to somewhat mitigate this limitation.
   * Some builder methods accept arguments which may be mutable (like `CharSequence` instead of `String`,
   as well as arrays and `Iterable` instances); modifying the instances passed to builder methods 
   between calls to the `SqlStatement::build()` method will result in different statements. 
   Some mutable delegating wrappers for interfaces are already implemented. Add `Mutable` prefix 
   to the interface name (`CharSequence`, `ResultElement`, `SelectStatement`, `SqlExpression`, or `TableExpression`)
   to get the wrapper class name.
   * Each object representing a stage of statement construction may be used to build multiple statements 
   having a common initial part. Invoking different methods on the same instance does not modify 
   the instance itself, rather returning a new object.
* No output formatting or customization is currently supported.
   
## License
This software is distributed under the conditions of the MIT License. See the `LICENSE` file for more information.
