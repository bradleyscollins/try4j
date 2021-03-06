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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import try4j.function.ThrowingFunction;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class SuccessTest {

  Success<String> success;

  @Before
  public void setUp() {
    success = Success.of("Success");
  }

  @After
  public void tearDown() {
  }

  @Test
  public void ofConstructsSuccessInstanceWithGivenValue() {
    String value = success.getValue();
    assertThat(success, is(Success.of(value)));
  }

  @Test
  public void failedReturnsUnsupportedOperationExceptionWrappedInASuccess() {
    assertThat(success.failed(), is(instanceOf(Success.class)));
    assertThat(success.failed().get(),
        is(instanceOf(UnsupportedOperationException.class)));
  }

  @Test
  public void getReturnsEncapsulatedValue() {
    assertThat(success.get(), is(success.getValue()));
  }

  @Test
  public void orElseReturnsEncapsulatedValue() {
    assertThat(success.orElse("default"), is("Success"));
    assertThat(success.orElse(() -> "default"), is("Success"));
  }

  @Test
  public void orElseTryReturnsSameSuccessInstance() {
    assertThat(success.orElseTry(() -> Success.of("default")),
        is(sameInstance(success)));
    assertThat(success.orElseTry(() -> { throw new Exception(); }),
        is(sameInstance(success)));
  }

  @Test
  public void isFailureReturnsFalse() {
    assertThat(success.isFailure(), is(false));
  }

  @Test
  public void isSuccessReturnsTrue() {
    assertThat(success.isSuccess(), is(true));
  }

  @Test
  public void toOptionalReturnsOptionalContainingEncapsulatedValue() {
    assertThat(success.toOptional(), is(Optional.of("Success")));
  }

  @Test
  public void transformAppliesSToEncapsulatedValue() {
    ThrowingFunction<String, Try<Integer>> s = x -> Success.of(x.length());
    ThrowingFunction<Exception, Try<Integer>> f = x -> Success.of(-1);

    assertThat(success.transform(s, f),
        is(Success.of(success.get().length())));
  }

  @Test
  public void transformReturnsFailureIfSThrowsException() {
    ThrowingFunction<String, Try<Integer>> s = x -> { throw new Exception(); };
    ThrowingFunction<Exception, Try<Integer>> f = x -> Success.of(-1);

    assertThat(success.transform(s, f), is(instanceOf(Failure.class)));
  }

  @Test
  public void filterReturnsSameSuccessInstanceIfInputPredicateHolds() {
    assertThat(success.filter(s -> s.equals("Success")), is(success));
  }

  @Test
  public void filterReturnsAFailureIfInputPredicateFails() {
    assertThat(success.filter(s -> s.equals("FAIL")),
        is(instanceOf(Failure.class)));
  }

  @Test
  public void filterReturnsAFailureIfInputPredicateThrowsAnException() {
    assertThat(success.filter(s -> { throw new Exception(); }),
        is(instanceOf(Failure.class)));
  }

  @Test
  public void flatMapReturnsSuccessWithMapperAppliedToEncapsulatedValue() {
    assertThat(success.flatMap(s -> Try.to(() -> s.length())),
        is(Success.of(success.get().length())));
  }

  @Test
  public void flatMapReturnsFailureIfMapperThrowsAnException() {
    assertThat(success.flatMap(s -> { throw new Exception(); }),
        is(instanceOf(Failure.class)));
  }

  @Test
  public void flattenReturnsEncapsulatedTry() {
    Try<Try<String>> stacked = Success.of(success);
    assertThat(stacked.flatten(), is(success));
  }

  @Test
  public void flattenReturnsFailureIfEncapsulatedValueIsNotAnInstanceOfTry() {
    assertThat(success.flatten(), is(instanceOf(Failure.class)));
  }

  @Test
  public void forEachPerformsActionOnValue() {
    final List<Integer> counts = new ArrayList<>();
    success.forEach(s -> counts.add(s.length()));
    assertThat(counts.get(0), is(success.get().length()));
  }

  @Test
  public void mapReturnsSuccessInstanceContainingMappedValue() {
    assertThat(success.map(s -> s.length()),
        is(Success.of(success.get().length())));
  }

  @Test
  public void mapReturnsFailureIfMapperThrowsAnException() {
    assertThat(success.map(s -> { throw new Exception(); }),
        is(instanceOf(Failure.class)));
  }

  @Test
  public void recoverReturnsSameSuccessInstance() {
    assertThat(success.recover(t -> t.getMessage()),
        is(sameInstance(success)));
    assertThat(success.recover(t -> { throw new Exception(); }),
        is(sameInstance(success)));
  }

  @Test
  public void recoverWithReturnsSameSuccessInstance() {
    assertThat(success.recoverWith(t -> Success.of(t.getMessage())),
        is(sameInstance(success)));
    assertThat(success.recoverWith(t -> { throw new Exception(); }),
        is(sameInstance(success)));
  }

  @Test
  public void testEquals() {
    assertThat(success.equals(null), is(false));
    assertThat(success.equals(success), is(true));
    assertThat(success.equals(new Object()), is(false));
    assertThat(success.equals(Success.of("Different")), is(false));

    assertThat(success.equals(Success.of("Success")), is(true));
  }

  @Test
  public void testHashCode() {
    assertThat(success.hashCode(), is(Objects.hash("Success")));
  }
}
