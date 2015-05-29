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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import try4j.Success;
import try4j.Try;

public class UsageTest {
  @Test
  public void dropDivideByZero() {
    List<Integer> denominators = Arrays.asList(-2, -1, 0, 1, 2, 3);
    List<Integer> results = denominators.stream()
        .map(d -> Try.to(() -> 100 / d))
        .filter(t -> t.isSuccess())
        .map(t -> t.get())
        .collect(Collectors.toList());
    
    assertThat(results, is(Arrays.asList(-50, -100, 100, 50, 33)));
  }
  
  @Test
  public void handleDivideByZero() {
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
    
    assertThat(results, 
        is(Arrays.asList("-50", "-100", "n/a", "100", "50", "33")));
  }
  
  @Test
  public void callingGetOnAFailtureThrowsTheOriginalExceptionWrappedInARuntimeException() {
    try {
      Try<Integer> failure = Try.to(() -> Integer.parseInt("pig"));
      failure.get();
    } catch (RuntimeException e) {
      assertThat(e.getCause(), is(instanceOf(NumberFormatException.class)));
    }
  }
  
  @Test
  public void forEachDoesNothingOnFailures() {
    List<Integer> successes = new LinkedList<>();
    
    Try.to(() -> Integer.parseInt("1"))
        .map(m -> m + 1)
        .forEach(n -> successes.add(n));
    
    assertThat(successes, hasItems(2));
    
    Try.to(() -> Integer.parseInt("x"))
        .map(m -> m + 1)
        .forEach(n -> successes.add(n));
    
    assertThat(successes, hasItems(2));
  }
}
