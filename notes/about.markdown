[Try4J](https://github.com/bradleyscollins/try4j) is a Java 8 implementation of [Scala's](http://www.scala-lang.org) [`Try`](http://www.scala-lang.org/api/current/#scala.util.Try), [`Success`](http://www.scala-lang.org/api/current/#scala.util.Success), and [`Failure`](http://www.scala-lang.org/api/current/#scala.util.Failure).

Try4J allows you to write lambda expressions that throw exceptions in a compact and fluent fashion rather than having to enclose it in a bulky `try-catch` block. The result of a successful operation is wrapped in a `Success`. The exception thrown in a failed operation is wrapped in a `Failure`. Filter or map based on whether the operation is a success or a failure.

