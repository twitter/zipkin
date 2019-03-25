/*
 * Copyright 2015-2019 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.collector.activemq;

import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import zipkin2.Span;
import zipkin2.TestObjects;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ITActiveMQCollector {
  List<Span> spans = Arrays.asList(TestObjects.LOTS_OF_SPANS[0], TestObjects.LOTS_OF_SPANS[1]);

  @ClassRule
  public static ActiveMQCollectorRule activemq = new ActiveMQCollectorRule();

  @After public void clear() {
    activemq.metrics.clear();
    activemq.storage.clear();
  }

  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test public void checkPasses() {
    assertThat(activemq.collector.check().ok()).isTrue();
  }

  @Test
  public void startFailsWithInvalidActiveMqServer() throws Exception {
    // we can be pretty certain ActiveMQ isn't running on localhost port 61614
    String notActiveMqAddress = "localhost:61614";
    try (ActiveMQCollector collector = ActiveMQCollector.builder().addresses(notActiveMqAddress).build()) {
      thrown.expect(IllegalStateException.class);
      thrown.expectMessage("Unable to establish connection to ActiveMQ server");
      collector.start();
    }
  }



}
