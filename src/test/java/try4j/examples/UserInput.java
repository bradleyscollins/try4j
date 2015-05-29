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

package try4j.examples;

import try4j.Try;

public class UserInput {
  public static void main(String[] args) {
    Try<Integer> quotient = divide();
  }

  private static Try<Integer> divide() {
    System.out.println("Enter an integer that you'd like to divide:");
    Try<Integer> dividend = Try.to(() -> Integer.parseInt(System.console().readLine()));
    System.out.println("Enter an integer that you'd like to divide by:");
    Try<Integer> divisor = Try.to(() -> Integer.parseInt(System.console().readLine()));
    Try<Integer> problem = dividend.flatMap(x -> divisor.map(y -> x/y));
    
    if (problem.isSuccess()) {
      System.out.println(String.format("Result of %d/%d is %d",
          dividend.get(), divisor.get(), problem.get()));
      return problem;
    } else {
      System.out.println("You must've divided by zero or entered something that's not an integer. Try again!");
      System.out.println(String.format("Info from the exception: %s\n",
          problem.failed().get().getMessage()));
      return divide();
    }
  }
}


