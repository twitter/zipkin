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
package zipkin2.elasticsearch.internal;

import com.linecorp.armeria.client.HttpClient;
import com.linecorp.armeria.client.HttpClientBuilder;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit4.server.ServerRule;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class BasicAuthInterceptorTest {
  static final BlockingQueue<AggregatedHttpRequest> CAPTURED_REQUESTS = new LinkedBlockingQueue<>();
  static final BlockingQueue<AggregatedHttpResponse> MOCK_RESPONSES = new LinkedBlockingQueue<>();

  @ClassRule public static ServerRule server = new ServerRule() {
    @Override public void configure(ServerBuilder sb) {
      sb.service("/", (ctx, req) -> {
        CompletableFuture<HttpResponse> responseFuture = new CompletableFuture<>();
        req.aggregate().thenAccept(agg -> {
          CAPTURED_REQUESTS.add(agg);
          AggregatedHttpResponse response = MOCK_RESPONSES.remove();
          responseFuture.complete(HttpResponse.of(response));
        }).exceptionally(t -> {
          responseFuture.completeExceptionally(t);
          return null;
        });
        return HttpResponse.from(responseFuture);
      });
    }
  };

  HttpClient client;

  @Before public void setUp() {
    client = new HttpClientBuilder(server.httpUri("/"))
      .decorator((delegate, ctx, req) ->
        new BasicAuthInterceptor(delegate, "Aladdin", "OpenSesame").execute(ctx, req)
      ).build();
  }

  @Test public void addsAuthHeader() throws Exception {
    MOCK_RESPONSES.add(AggregatedHttpResponse.of(200));

    client.get("/").aggregate().join();

    // hard coded for sanity taken from https://en.wikipedia.org/wiki/Basic_access_authentication
    assertThat(CAPTURED_REQUESTS.take().headers().get("Authorization"))
      .isEqualTo("Basic QWxhZGRpbjpPcGVuU2VzYW1l");
  }

  @Test public void intercept_whenESReturns403AndJsonBody_throwsWithResponseBodyMessage() {
    MOCK_RESPONSES.add(AggregatedHttpResponse.of(
      HttpStatus.FORBIDDEN, MediaType.JSON_UTF_8, "{\"message\":\"Sadness.\"}"));

    try {
      client.get("/").aggregate().join();
      failBecauseExceptionWasNotThrown(CompletionException.class);
    } catch (CompletionException e) {
      assertThat(e.getCause()).hasMessage("Sadness.");
    }
  }
}
