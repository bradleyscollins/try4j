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

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import try4j.Failure;
import try4j.Success;
import try4j.Try;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TryTest {

  @Test
  public void toYieldsSuccessWhenPassedInputThatDoesNotThrowAnException() {
    Try<String> result = Try.to(() -> "Succeed");
    assertThat(result, is(instanceOf(Success.class)));
    assertThat(result.get(), is("Succeed"));
  }

  @Test
  public void toYieldsSuccessWhenPassedInputThatPotentiallyThrowsACheckedExceptionButDoesNot() {
    Try<URI> result = Try.to(() -> new URI("http://java.sun.com"));
    assertThat(result, is(instanceOf(Success.class)));
    assertThat(result.get(), is(instanceOf(URI.class)));
    assertThat(result.get().toString(), is("http://java.sun.com"));
  }

  @Test
  public void toYieldsFailureWhenPassedInputThatThrowsAnUncheckedException() {
    int n = 2;
    int d = 0;
    Try<String> result = Try.to(() -> Integer.toString(n / d));
    assertThat(result, is(instanceOf(Failure.class)));
    assertThat(result.failed().get(),
        is(instanceOf(ArithmeticException.class)));
  }

  @Test
  public void toYieldsFailureWhenPassedInputThatThrowsACheckedException() {
    Try<URI> result = Try.to(() -> new URI("!!http://"));
    assertThat(result, is(instanceOf(Failure.class)));
    assertThat(result.failed().get(),
        is(instanceOf(URISyntaxException.class)));
  }
}
