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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.Matchers.*;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import try4j.Failure;
import try4j.Success;
import try4j.Try;

public class FailureTest {

  Failure<String> failure;

  @Before
  public void setUp() {
    Exception exception = new Exception("cause");
    failure = Failure.of(exception);
  }

  @After
  public void tearDown() {
  }
  
  @Test
  public void ofConstructsFailureInstanceWithGivenThrowable() {
    Throwable throwable = failure.getException();
    assertThat(failure, is(Failure.of(throwable)));
  }

  @Test
  public void failedReturnsSuccessContainingEncapsulatedException() {
    assertThat(failure.failed(), is(Success.of(failure.getException())));
  }

  @Test(expected = Exception.class)
  public void getYieldsTheEncapsulatedException() {
    failure.get();
  }

  @Test
  public void orElseYieldsGivenDefaultValue() {
    assertThat(failure.orElse("default"), is("default"));
  }

  @Test
  public void orElseGetYieldsTryInstanceGeneratedByGivenSupplier() {
    assertThat(failure.orElseGet(() -> Success.of("default")),
        is(Success.of("default")));
  }

  @Test
  public void isFailureReturnsTrue() {
    assertThat(failure.isFailure(), is(true));
  }

  @Test
  public void testIsSuccess() {
    assertThat(failure.isSuccess(), is(false));
  }

  @Test
  public void toOptionalReturnsOptionalContainingEncapsulatedValue() {
    assertThat(failure.toOptional(), is(Optional.empty()));
  }

  @Test
  public void transformAppliesFToEncapsulatedException() {
    Function<String, Try<Integer>> s = x -> Success.of(x.length());
    Function<Throwable, Try<Integer>> f = x -> Success.of(-1);

    assertThat(failure.transform(s, f), is(Success.of(-1)));
  }

  @Test
  public void filterReturnsSameFailureInstanceRegardlessOfPredicate() {
    assertThat(failure.filter(s -> true), is(failure));
    assertThat(failure.filter(s -> false), is(failure));
  }

  @Test
  public void flatMapReturnsSameFailureInstance() {
    assertThat(failure.flatMap(s -> Try.to(() -> s.length())),
        is(sameInstance(failure)));
  }

  @Test
  public void flattenReturnsSameFailureInstance() {
    assertThat(failure.flatten(), is(sameInstance(failure)));
  }

  @Test
  public void forEachPerformsActionOnValue() {
    final List<Integer> counts = new ArrayList<>();
    failure.forEach(s -> counts.add(s.length()));
    assertThat(counts, is(empty()));
  }

  @Test
  public void mapReturnsSameFailureInstance() {
    assertThat(failure.map(s -> s.length()), is(sameInstance(failure)));
  }

  @Test
  public void testEquals() {
    assertThat(failure.equals(null), is(false));
    assertThat(failure.equals(failure), is(true));
    assertThat(failure.equals(new Object()), is(false));

    Exception different = new Exception("Different");
    assertThat(failure.equals(Failure.of(different)), is(false));

    assertThat(failure.equals(Failure.of(failure.getException())),
        is(true));
  }

  @Test
  public void testHashCode() {
    assertThat(failure.hashCode(), is(Objects.hash(failure.getException())));
  }
}
