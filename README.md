# Try4J

Try4J is a Java 8 implementation of Scala's `Try`, `Success`, and `Failure`.

Try4J allows you to write lambda expressions that throw exceptions in a compact and fluent fashion rather than having to enclose it in a bulky `try-catch` block. The result of a successful operation is wrapped in a `Success`. The exception thrown in a failed operation is wrapped in a `Failure`. Filter or map based on whether the operation is a success or a failure.

## Usage

Wrap functional style code that may throw an exception in a `Try` to deal with the exception in a more functional manner.

The following code filters out the divide-by-zero case and keeps only the valid results:

```java
List<Integer> denominators = Arrays.asList(-2, -1, 0, 1, 2, 3);
List<Integer> results = denominators.stream()
    .map(d -> Try.to(() -> 100 / d))
    .filter(t -> t.isSuccess())
    .map(t -> t.get())
    .collect(Collectors.toList());

// results = List(-50, -100, 100, 50, 33)
```

Sometimes, though, you don't want to drop the failure, but rather reflect it in the results:

```java
List<Integer> denominators = Arrays.asList(-2, -1, 0, 1, 2, 3);
List<String> results = denominators.stream()
    .map(d -> Try.to(() -> 100 / d))
    .map(t -> {
      return t.transform(
          n -> Success.of(Integer.toString(n)), 
          e -> Success.of("n/a")
      ).get();
    })
    .collect(Collectors.toList());

// results = List("-50", "-100", "n/a", "100", "50", "33")
```

## Examples

Let's say that you need a list of network adapters that are active and support multicast. Because many of the `NetworkInterface` operations throw a `SocketException`, you have to wrap them in bulky `try-catch` blocks, cluttering up your nice, clean functional code:

```java
try {
  List<NetworkInterface> nics = Collections
      .list(NetworkInterface.getNetworkInterfaces()).stream()
      .filter(n -> {
        try {
          return n.supportsMulticast() && n.isUp();
        } catch (SocketException e) {
          logger.log(Level.SEVERE, null, e);
          return false;
        }
      })
      .collect(Collectors.toList());
  
  // do something with nics
} catch (SocketException e) {
  logger.log(Level.SEVERE, null, e);
}
```

But with Try4J, you can wrap those calls in a `Try` and get rid of those noisy `try-catch` blocks:

```java
List<NetworkInterface> nics = 
    Try.to(() -> Collections.list(NetworkInterface.getNetworkInterfaces()))
    .orElse(new ArrayList<>())
    .stream()
    .filter(n -> Try.to(() -> n.supportsMulticast() && n.isUp()).orElse(false))
    .collect(Collectors.toList());

// do something with nics
```
