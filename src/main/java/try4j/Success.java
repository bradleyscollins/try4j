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

import try4j.function.ThrowingFunction;
import try4j.function.ThrowingSupplier;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Success<T> implements Try<T> {
  private final T value;

  /**
   * Creates a new {@link try4j.Success} containing {@code value}.
   * @param value value representing a successful operation
   * @throws java.lang.NullPointerException if {@code value} is {@code null}
   */
  public Success(T value) {
    this.value = Objects.requireNonNull(value,
        "Success must be initialized with a non-null value");
  }

  /**
   * Creates a new {@link try4j.Success} containing {@code value}.
   * <p>
   * Named constructor for syntactic sugar.
   * @param value represents a successful operation
   * @param <U> type of {@code value}
   * @return a new {@link try4j.Success} containing {@code value}.
   */
  public static <U> Success<U> of(U value) {
    return new Success<>(value);
  }

  /**
   * Returns the value this {@link try4j.Success} contains.
   * @return the value this {@link try4j.Success} contains.
   */
  public T getValue() { return value; }

  @Override
  public Try<Exception> failed() {
    return Success.of(new UnsupportedOperationException("Success.failed"));
  }

  @Override public T get() { return value; }

  @Override public T orElse(T instead) { return get(); }

  @Override
  public Try<? super T> orElseTry(ThrowingSupplier<Try<? super T>> instead) {
    return this;
  }

  @Override public boolean isFailure() { return false; }
  @Override public boolean isSuccess() { return true; }

  @Override public Optional<T> toOptional() { return Optional.of(value); }

  @Override
  public <U> Try<U> transform(ThrowingFunction<? super T, Try<U>> s,
                              ThrowingFunction<Exception, Try<U>> f) {
    try {
      return s.apply(value);
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public Try<T> filter(Predicate<T> predicate) {
    try {
      if (predicate.test(value)) {
        return this;
      } else {
        String msg = String.format("Predicate does not hold for '%s'", value);
        return Failure.of(new NoSuchElementException(msg));
      }
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public <U> Try<U> flatMap(ThrowingFunction<? super T, Try<U>> mapper) {
    try {
      return mapper.apply(value);
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public <U extends Try<?>> Try<U> flatten() {
    if (value instanceof Try) {
      @SuppressWarnings("unchecked")
      Try<U> ret = (Try<U>) value;
      return ret;
    } else {
      String msg = String.format("%s is not an instance of Try", value);
      return Failure.of(new UnsupportedOperationException(msg));
    }
  }

  @Override public void forEach(Consumer<? super T> action) {
    action.accept(value);
  }

  @Override public <U> Try<U> map(ThrowingFunction<? super T, ? extends U> mapper) {
    return Try.to(() -> mapper.apply(value));
  }
  
  @Override
  public Try<? super T> recover(ThrowingFunction<Exception, ? super T> rescue) {
    return this;
  }

  @Override
  public Try<? super T> recoverWith(ThrowingFunction<Exception, Try<? super T>> rescue) {
    return this;
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if ( !(obj instanceof Success) ) return false;
    @SuppressWarnings("rawtypes")
    Success<?> other = (Success) obj;
    return Objects.equals(value, other.value);
  }

  @Override public int hashCode() {
    return Objects.hash(value);
  }

  @Override public String toString() {
    return String.format("Success(%s)", value);
  }
}