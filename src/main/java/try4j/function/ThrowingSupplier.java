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

/**
 * Represents a supplier of results that could throw an exception.
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 * <p>
 * This is a functional interface whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 *
 * @author Brad Collins
 * @since 1.5
 *
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {
  /**
   * Get a result.
   * @return a result
   * @throws Exception if the underlying operation throws an exception
   */
  T get() throws Exception;
}
