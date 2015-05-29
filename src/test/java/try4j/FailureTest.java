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
import java.util.function.Function;

import static org.hamcrest.Matchers.*;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import try4j.function.ThrowingFunction;

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
    Exception ex = failure.getException();
    assertThat(failure, is(Failure.of(ex)));
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
  public void orElseYieldsSuppliedDefaultValue() {
    assertThat(failure.orElse(() -> "default"), is("default"));
  }

  @Test
  public void orElseGetYieldsTryInstanceGeneratedByGivenSupplier() {
    assertThat(failure.orElseTry(() -> Success.of("default")),
        is(Success.of("default")));
  }

  @Test
  public void orElseTryYieldsFailureIfSupplierThrowsAnException() {
    assertThat(failure.orElseTry(() -> { throw new Exception(); }),
        is(instanceOf(Failure.class)));
  }

  @Test
  public void isFailureReturnsTrue() {
    assertThat(failure.isFailure(), is(true));
  }

  @Test
  public void isSuccessReturnsFalse() {
    assertThat(failure.isSuccess(), is(false));
  }

  @Test
  public void toOptionalReturnsEmpty() {
    assertThat(failure.toOptional(), is(Optional.empty()));
  }

  @Test
  public void transformAppliesFToEncapsulatedException() {
    ThrowingFunction<String, Try<Integer>> s = x -> Success.of(x.length());
    ThrowingFunction<Exception, Try<Integer>> f = x -> Success.of(-1);

    assertThat(failure.transform(s, f), is(Success.of(-1)));
  }

  @Test
  public void transformReturnsFailureIfFThrowsAnException() {
    ThrowingFunction<String, Try<Integer>> s = x -> Success.of(x.length());
    ThrowingFunction<Exception, Try<Integer>> f = x -> { throw new Exception(); };

    assertThat(failure.transform(s, f), is(instanceOf(Failure.class)));
  }

  @Test
  public void filterReturnsSameFailureInstanceRegardlessOfPredicate() {
    assertThat(failure.filter(s -> true), is(failure));
    assertThat(failure.filter(s -> false), is(failure));
    assertThat(failure.filter(s -> { throw new Exception(); }), is(failure));
  }

  @Test
  public void flatMapReturnsSameFailureInstance() {
    assertThat(failure.flatMap(s -> Try.to(() -> s.length())),
        is(sameInstance(failure)));
    assertThat(failure.flatMap(s -> { throw new Exception(); }),
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
    assertThat(failure.map(s -> { throw new Exception(); }),
        is(sameInstance(failure)));
  }

  @Test
  public void recoverReturnsTransformedFailure() {
    assertThat(failure.recover(t -> t.getMessage()),
        is(Success.of(failure.getException().getMessage())));
  }

  @Test
  public void recoverHandlesExceptionInRescueFunction() {
    Try<? super String> actual = failure.recover(e -> {
      throw new RuntimeException("failure");
    });
    
    assertThat(actual.failed().get().getMessage(), is("failure"));
  }
  
  @Test
  public void recoverWithReturnsTransformedFailure() {
    assertThat(failure.recoverWith(t -> Success.of(t.getMessage())),
        is(Success.of(failure.getException().getMessage())));
  }

  @Test
  public void recoverWithHandlesExceptionInRescueFunction() {
    Try<? super String> actual = failure.recoverWith(e -> {
      throw new RuntimeException("failure");
    });
    
    assertThat(actual.failed().get().getMessage(), is("failure"));
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
