/**
 * Copyright 2015 Bradley S. Collins.
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

package try4j.function;

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of one argument, but could
 * throw an exception.
 *
 * <p>This is a functional interface whose functional method is
 * {@link #test(Object)}.
 *
 * @param <T> the type of the input to the predicate
 *
 * @author Brad Collins
 * @since 1.8
 *
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface ThrowingPredicate<T> {
  /**
   * Evaluates this predicate on the given argument, throwing an exception in
   * the case of an error.
   *
   * @param t the input argument
   * @return {@code true} if the input argument matches the predicate,
   *    otherwise {@code false}
   * @throws Exception if some error occurs
   */
  boolean test(T t) throws Exception;

  /**
   * Returns a composed predicate that represents a short-circuiting logical
   * AND of this predicate and another.  When evaluating the composed
   * predicate, if this predicate is {@code false}, then the {@code other}
   * predicate is not evaluated.
   *
   * <p>Any exceptions thrown during evaluation of either predicate are relayed
   * to the caller; if evaluation of this predicate throws an exception, the
   * {@code other} predicate will not be evaluated.
   *
   * @param other a predicate that will be logically-ANDed with this
   *              predicate
   * @return a composed predicate that represents the short-circuiting logical
   *    AND of this predicate and the {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default ThrowingPredicate<T> and(ThrowingPredicate<? super T> other) {
    Objects.requireNonNull(other);
    return (T t) -> test(t) && other.test(t);
  }

  /**
   * Returns a predicate that represents the logical negation of this
   * predicate.
   *
   * @return a predicate that represents the logical negation of this
   *    predicate
   */
  default ThrowingPredicate<T> negate() { return (T t) -> !test(t); }

  /**
   * Returns a composed predicate that represents a short-circuiting logical
   * OR of this predicate and another.  When evaluating the composed
   * predicate, if this predicate is {@code true}, then the {@code other}
   * predicate is not evaluated.
   *
   * <p>Any exceptions thrown during evaluation of either predicate are relayed
   * to the caller; if evaluation of this predicate throws an exception, the
   * {@code other} predicate will not be evaluated.
   *
   * @param other a predicate that will be logically-ORed with this
   *              predicate
   * @return a composed predicate that represents the short-circuiting logical
   *    OR of this predicate and the {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default ThrowingPredicate<T> or(ThrowingPredicate<? super T> other) {
    Objects.requireNonNull(other);
    return (T t) -> test(t) || other.test(t);
  }
}
