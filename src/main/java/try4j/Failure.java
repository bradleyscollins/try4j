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

package try4j;

import try4j.function.ThrowingFunction;
import try4j.function.ThrowingPredicate;
import try4j.function.ThrowingSupplier;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Failure<T> implements Try<T> {
  private final Exception exception;

  /**
   * Creates a new {@link try4j.Failure} containing the exception {@code e}.
   * @param e exception representing a failed operation
   * @throws java.lang.NullPointerException if {@code e} is {@code null}
   */
  public Failure(Exception e) {
    this.exception = Objects.requireNonNull(e,
        "Success must be initialized with a non-null value");
  }

  /**
   * Creates a new {@link try4j.Failure} containing the exception {@code e}.
   * <p>
   * Named constructor for syntactic sugar.
   * @param e exception representing a failed operation
   * @param <U> type of {@link try4j.Try} this {@link try4j.Failure} represents
   * @return a new {@link try4j.Failure} containing the exception {@code e}.
   */
  public static <U> Failure<U> of(Exception e) { return new Failure<>(e); }

  /**
   * Returns the exception this {@link try4j.Failure} contains.
   * @return the exception this {@link try4j.Failure} contains.
   */
  public Exception getException() { return exception; }

  @Override public Try<Exception> failed() { return Success.of(exception); }

  @Override public T get() { throw new RuntimeException(exception); }

  @Override public T orElse(T instead) { return instead; }

  @Override public T orElse(Supplier<T> instead) { return instead.get(); }

  @Override
  public Try<? super T> orElseTry(ThrowingSupplier<Try<? super T>> instead) {
    try {
      return instead.get();
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public boolean isFailure() { return true; }
  @Override public boolean isSuccess() { return false; }

  @Override public Optional<T> toOptional() { return Optional.<T>empty(); }

  @Override
  public <U> Try<U> transform(ThrowingFunction<? super T, Try<U>> s,
                              ThrowingFunction<Exception, Try<U>> f) {
    try {
      return f.apply(exception);
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public Try<T> filter(ThrowingPredicate<T> predicate) { return this; }

  @Override public <U> Try<U> flatMap(ThrowingFunction<? super T, Try<U>> mapper) {
    @SuppressWarnings("unchecked")
    Try<U> ret = (Try<U>) this;
    return ret;
  }

  @Override public <U extends Try<?>> Try<U> flatten() {
    @SuppressWarnings("unchecked")
    Try<U> ret = (Try<U>) this;
    return ret;
  }

  @Override public void forEach(Consumer<? super T> action) { }

  @Override public <U> Try<U> map(ThrowingFunction<? super T, ? extends U> mapper) {
    @SuppressWarnings("unchecked")
    Try<U> ret = (Try<U>) this;
    return ret;
  }

  @Override
  public Try<? super T> recover(ThrowingFunction<Exception, ? super T> rescue) {
    try {
      return Success.of(rescue.apply(exception));
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override
  public Try<? super T> recoverWith(ThrowingFunction<Exception, Try<? super T>> rescue) {
    try {
      return rescue.apply(exception);
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if ( !(obj instanceof Failure) ) return false;
    @SuppressWarnings("rawtypes")
    Failure<?> other = (Failure) obj;
    return Objects.equals(exception, other.exception);
  }

  @Override public int hashCode() {
    return Objects.hash(exception);
  }

  @Override public String toString() {
    return String.format("Failure(%s)", exception);
  }
}