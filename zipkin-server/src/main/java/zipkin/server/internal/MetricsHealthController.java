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
package zipkin.server.internal;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsHealthController {
  private MeterRegistry meterRegistry;
  private HealthEndpoint healthEndpointDelegate;
  private final CollectorRegistry collectorRegistry;

  final JsonNodeFactory factory = JsonNodeFactory.instance;

  MetricsHealthController(MeterRegistry meterRegistry
    , HealthEndpoint healthEndpointDelegate
    , CollectorRegistry collectorRegistry){
    this.meterRegistry = meterRegistry;
    this.healthEndpointDelegate = healthEndpointDelegate;
    this.collectorRegistry = collectorRegistry;
  }

  // Extracts Zipkin metrics to provide backward compatibility
  @GetMapping("/metrics")
  public ObjectNode fetchMetricsFromMicrometer(){
    ObjectNode metrics  = factory.objectNode();
    // Iterate over the meters and get the Zipkin Custom meters for constructing the Metrics endpoint
    for (Meter meter: meterRegistry.getMeters()) {
      if (meter.getId().getName().contains("zipkin") && meter.getId().getName().contains("counter")){
        metrics.put(meter.getId().getName(), meterRegistry.get(meter.getId().getName()).counter().count());
      }
      if (meter.getId().getName().contains("zipkin") && meter.getId().getName().contains("gauge")){
        metrics.put(meter.getId().getName(), meterRegistry.get(meter.getId().getName()).gauge().value());
      }
    }
    return metrics;
  }

  // Delegates the health endpoint from the Actuator to the root context path and can be deprecated
  // in future in favour of Actuator endpoints
  @GetMapping("/health")
  public Map getHealth() {
    Map health = new HashMap();
    health.put("status", healthEndpointDelegate.health().getStatus().getCode());
    health.put("zipkin", healthEndpointDelegate.health().getDetails().get("zipkin"));
    return health;
  }

  // Prometheus scrape code ported from springboot actuator and can be deprecated later
  // through root context path for backward compatibility, Mimics the same code from
  // org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint
  @GetMapping("/prometheus")
  public String scrape() {
    try {
      Writer writer = new StringWriter();
      TextFormat.write004(writer, this.collectorRegistry.metricFamilySamples());
      return writer.toString();
    }
    catch (IOException e) {
      // This actually never happens since StringWriter::write() doesn't throw any
      // IOException
      throw new RuntimeException("Writing metrics failed", e);
    }
  }
}
