/**
 * Copyright 2014 Bradley S. Collins.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package try4j;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@link Try} represents a computation that may either result in an exception
 * or return a successfully computed value. It is inspired by the Scala's
 * <a href="http://www.scala-lang.org/api/2.10.3/#scala.util.Try">Try</a>.
 * 
 * An instance of {@link Try<T>} is either an instance of {@link Success} or
 * {@link Failure}.
 * 
 * For example, {@link Try} can be used to perform division on a user-defined
 * input, without the need to do explicit exception-handling in all of the
 * places that an exception might occur.
 * 
 * Example:
 * 
 * <pre>
 * public class UserInput {
 *   public static void main(String[] args) {
 *     Try<Integer> quotient = divide();
 *   }
 *   
 *   private static Try<Integer> divide() {
 *     System.out.println("Enter an integer that you'd like to divide:");
 *     Try<Integer> dividend = Try.to(() -> Integer.parseInt(System.console().readLine()));
 *     System.out.println("Enter an integer that you'd like to divide by:");
 *     Try<Integer> divisor = Try.to(() -> Integer.parseInt(System.console().readLine()));
 *     Try<Integer> problem = dividend.flatMap(x -> divisor.map(y -> x/y));
 *   
 *     if (problem.isSuccess()) {
 *       System.out.println(String.format("Result of %d/%d is %d",
 *       dividend.get(), divisor.get(), problem.get()));
 *       return problem;
 *     } else {
 *       System.out.println("You must've divided by zero or entered something " +
 *           "that's not an integer. Try again!");
 *       System.out.println(String.format("Info from the exception: %s\n",
 *       problem.failed().get().getMessage()));
 *       return divide();
 *     }
 *   }
 * }
 * </pre>
 * 
 * @author Brad Collins
 * @param <T> the type of element contained in a successful computation
 */
public interface Try<T> {
  /**
   * Takes a callable instance, executes it, and returns either a
   * {@link Success} or a {@link Failure}.
   * @param <U> they type of element returned by {@code callable} if
   *    successful
   * @param callable the operation to be evaluated, which may throw an exception
   * @return a {@link Success} containing the return value of {@code callable} if
   *    {@code callable} completes without throwing an exception, or a
   *    {@link Failure} containing the exception {@code callable} throws if
   *    unsuccessful.
   */
  public static <U> Try<U> to(Callable<U> callable) {
    try {
      return Success.of(callable.call());
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  /**
   * Completes this {@link Try} with an exception wrapped in a {@link Success}.
   * @return a {@link Success} containing either the exception that the
   *    {@link Try} failed with (if a {@link Failure}) or an 
   *    UnsupportedOperationException (if a {@link Success}).
   */
  Try<Exception> failed();

  /**
   * Returns the value from this {@link Try} if it is a {@link Success} or
   * throws the exception if this it is a {@link Failure}.
   * @return if a {@link Success}, the encapsulated value; if a {@link Failure},
   *    throws the encapsulated exception
   */
  T get();

  /**
   * Returns the value from this {@link Try} if it is a {@link Success} or the
   * given default argument if it is a {@link Failure}.
   * @param instead the value to return if this is a {@link Failure}
   * @return if a {@link Success}, the encapsulated value; if a {@link Failure},
   *    {@code instead}
   */
  T orElse(T instead);

  /**
   * Returns the value from this {@link Try} if it is a {@link Success} or the
   * {@link Try} supplied by the given {@link Supplier} if it is a
   * {@link Failure}.
   * 
   * <strong>Note</strong>: This throws an exception if it is a {@link Failure},
   * and {@code instead} throws an exception. 
   * @param instead the supplier to invoke if this is a {@link Failure}
   * @return if a {@link Success}, the encapsulated value; if a {@link Failure},
   *    the {@link Try} that {@code instead} produces.
   */
  Try<? super T> orElseGet(Supplier<Try<? super T>> instead);

  /**
   * Returns {@code true} if this {@link Try} is a {@link Success}, or
   * {@code false} otherwise.
   * @return {@code true} if this {@link Try} is a {@link Success}, or
   *    {@code false} otherwise.
   */
  boolean isFailure();

  /**
   * Returns {@code true} if this {@link Try} is a {@link Failure}, or
   * {@code false} otherwise.
   * @return {@code true} if this {@link Try} is a {@link Failure}, or
   *    {@code false} otherwise.
   */
  boolean isSuccess();

  /**
   * Returns an empty {@link Optional} if this is a {@link Failure} or an
   * {@link Optional} containing the encapsulated value if this is a
   * {@link Success}.
   * @return an empty {@link Optional} if this is a {@link Failure} or an
   *    {@link Optional} containing the encapsulated value if this is a
   *    {@link Success}.
   */
  Optional<T> toOptional();

  /**
   * Completes this {@link Try} by invoking {@code f} on the encapsulated
   * exception this if this is a {@link Failure}, or conversely, by invoking
   * {@code s} on the encapsulated value if this is a {@link Success}.
   * @param <U> the type of the new {@link Try} that this operation returns
   * @param s the function invoked on the encapsulated value if this is a
   *    {@link Success}
   * @param f the function invoked on the encapsulated exception if this is a
   *    {@link Failure}
   * @return the result of {@code s} if this is a {@link Success}, or the result
   *    of {@code f} otherwise
   */
  <U> Try<U> transform(Function<? super T, Try<U>> s, Function<Exception, Try<U>> f);

  /**
   * Converts this to a {@link Failure} if the predicate is not satisfied.
   * @param predicate the test applied to the encapsulated value if this is a
   *    {@link Success}
   * @return this instance if this is already a {@link Failure}, or if this is a
   *    {@link Success} that holds for the given {@code predicate}; otherwise a
   *    new {@link Failure}
   */
  Try<T> filter(Predicate<T> predicate);

  /**
   * Returns the given function applied to the value from this {@link Try} if it
   * is a {@link Success}, or returns this instance if it is a {@link Failure}.
   * @param <U> the type of the new {@link Try} that {@code mapper} returns
   * @param mapper invoked on the value in this {@link Try}
   * @return if this is a {@link Success}, a new {@link Try} resulting from the
   *    the invocation of {@code mapper} to the encapsulated value; otherwise,
   *    this instance.
   */
  <U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

  /**
   * Transforms a nested {@link Try}, i.e., a {@link Try} of type
   * {@link Try<Try<T>>}, into an un-nested {@link Try}, i.e., a {@link Try} of
   * type {@link Try<T>}.
   * @param <U> type of value encapsulated in new {@link Try} resulting from
   *    this operation
   * @return 
   */
  <U extends Try<?>> Try<U> flatten();

  /**
   * Applies the given function if this is a {@link Success}, or otherwise does
   * nothing.
   * @param action a non-interfering action to perform on the encapsulated value
   *    if this is a {@link Success}
   */
  void forEach(Consumer<? super T> action);

  /**
   * Invokes the given function on the encapsulated value if this is a
   * {@link Success}, or returns this instance if this is a {@link Failure}.
   * @param <U> the type of the {@link Try} returned from this operation
   * @param mapper the mapping function applied to the encapsulated value if this
   *    is a {@link Success}
   * @return The result of invoking the given function on the encapsulated value
   *    if this is a {@link Success}, or returns this instance if this is a
   *    {@link Failure}.
   */
  <U> Try<U> map(Function<? super T, ? extends U> mapper);
  
  /**
   * Applies the given function if this is a {@link Failure}, otherwise returns
   * this if this is a {@link Success}.
   * @param <T> the type of the {@link Try} returned from this operation
   * @param rescue the mapping function applied to the encapsulated exception
   *    if this is a {@link Failure}
   * @return The result of invoking the given function on the encapsulated value
   *    if this is a {@link Failure}, or returns this instance if this is a
   *    {@link Success}.
   */
  Try<? super T> recover(Function<Exception, ? super T> rescue);

  /**
   * Applies the given function if this is a {@link Failure}, otherwise returns
   * this if this is a {@link Success}.
   * @param <T> the type of the {@link Try} returned from this operation
   * @param rescue the mapping function applied to the encapsulated exception
   *    if this is a {@link Failure}
   * @return The result of invoking the given function on the encapsulated value
   *    if this is a {@link Failure}, or returns this instance if this is a
   *    {@link Success}.
   */
  Try<? super T> recoverWith(Function<Exception, Try<? super T>> rescue);
}
