package zipkin.server.internal;

/**
 * Copyright 2015-2018 The OpenZipkin Authors
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

import com.google.common.util.concurrent.AtomicDouble;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import zipkin.collector.CollectorMetrics;
import zipkin.internal.Nullable;

import static zipkin.internal.Util.checkNotNull;

/**
 * This is a simple metric service that exports the following to the "/metrics" endpoint:
 *
 * <pre>
 * <ul>
 *     <li>counter.zipkin_collector.messages.$transport - cumulative messages received; should
 * relate to messages reported by instrumented apps</li>
 *     <li>counter.zipkin_collector.messages_dropped.$transport - cumulative messages dropped;
 * reasons include client disconnects or malformed content</li>
 *     <li>counter.zipkin_collector.bytes.$transport - cumulative message bytes</li>
 *     <li>counter.zipkin_collector.spans.$transport - cumulative spans read; should relate to
 * messages reported by instrumented apps</li>
 *     <li>counter.zipkin_collector.spans_dropped.$transport - cumulative spans dropped; reasons
 * include sampling or storage failures</li>
 *     <li>gauge.zipkin_collector.message_spans.$transport - last count of spans in a message</li>
 *     <li>gauge.zipkin_collector.message_bytes.$transport - last count of bytes in a message</li>
 * </ul>
 * </pre>
 *
 * See https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html
 *
 * <p>In-memory implementation mimics code from org.springframework.boot.actuate.metrics.buffer
 */

public final class ActuateCollectorMetrics implements CollectorMetrics{

  @Autowired private MeterRegistry meterRegistry;

  private final Counter messages;
  private final Counter messagesDropped;
  private final AtomicDouble messageBytes;
  private final AtomicDouble messageSpans;
  private final Counter bytes;
  private final Counter spans;
  private final Counter spansDropped;


  public ActuateCollectorMetrics(MeterRegistry registry){
    this(null, registry);
  }

  ActuateCollectorMetrics(@Nullable String transport, MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
    String transportType = transport == null ? "" : "." + transport;
    this.messages = this.meterRegistry
      .counter("counter.zipkin_collector.messages" + transportType);
    this.messagesDropped = this.meterRegistry
      .counter("counter.zipkin_collector.messages_dropped" + transportType);
    this.messageBytes = this.meterRegistry
      .gauge("gauge.zipkin_collector.message_bytes" + transportType, new AtomicDouble());
    this.messageSpans = this.meterRegistry
      .gauge("gauge.zipkin_collector.message_spans" + transportType, new AtomicDouble());
    this.bytes = this.meterRegistry
      .counter("counter.zipkin_collector.bytes" + transportType);
    this.spans = this.meterRegistry
      .counter("counter.zipkin_collector.spans" + transportType);
    this.spansDropped = this.meterRegistry
      .counter("counter.zipkin_collector.spans_dropped" + transportType);
  }

  @Override public ActuateCollectorMetrics forTransport(String transportType) {
    checkNotNull(transportType, "transportType");
    return new ActuateCollectorMetrics(transportType, meterRegistry);
  }

  @Override public void incrementMessages()  {
   messages.increment();
  }

  @Override public void incrementMessagesDropped() {
    messagesDropped.increment();
  }

  @Override public void incrementSpans(int quantity) {
    messageSpans.set(quantity);
    spans.increment();
  }

  @Override public void incrementBytes(int quantity) {
    messageBytes.set(quantity);
    bytes.increment();
  }

  @Override public void incrementSpansDropped(int quantity) {
    spansDropped.increment();
  }
}
