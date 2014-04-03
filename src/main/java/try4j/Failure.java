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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Failure<T> implements Try<T> {
  private final Throwable exception;

  public Failure(Throwable t) {
    this.exception = Objects.requireNonNull(t,
        "Success must be initialized with a non-null value");
  }
  
  public static <U> Failure<U> of(Throwable t) {
    return new Failure<>(t);
  }

  public Throwable getException() { return exception; }

  @Override public Try<Throwable> failed() { return Success.of(exception); }

  @Override public T get() { throw new RuntimeException(exception); }

  @Override public T orElse(T instead) { return instead; }

  @Override
  public Try<? super T> orElseGet(Supplier<Try<? super T>> instead) {
    return instead.get();
  }

  @Override public boolean isFailure() { return true; }
  @Override public boolean isSuccess() { return false; }

  @Override public Optional<T> toOptional() { return Optional.<T>empty(); }

  @Override
  public <U> Try<U> transform(Function<? super T, Try<U>> s, Function<Throwable, Try<U>> f) {
    try {
      return f.apply(exception);
    } catch (Exception e) {
      return Failure.of(e);
    }
  }

  @Override public Try<T> filter(Predicate<T> predicate) {
    return this;
  }

  @Override public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
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

  @Override public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
    @SuppressWarnings("unchecked")
    Try<U> ret = (Try<U>) this;
    return ret;
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj == this) return true;
    if ( !(obj instanceof Failure) ) return false;
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