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

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import try4j.Try;

public class NicsWithTry {
  public static void main(String[] args) {
    System.out.println("Getting network adapters from system ...");
    Try.to(() -> Collections.list(NetworkInterface.getNetworkInterfaces()))
        .orElse(new ArrayList<>())
        .stream()
        .filter(n -> Try.to(() -> n.supportsMulticast() && n.isUp()).orElse(false))
        .map(n -> String.format("%s (%s)",
                n.getName(),
                Collections.list(n.getInetAddresses()).stream()
                    .filter(a -> a.getAddress().length == 4)
                    .map(a -> a.getHostName())
                    .collect(Collectors.joining(", "))
            ))
        .forEach(s -> System.out.println(s));
  }
}
