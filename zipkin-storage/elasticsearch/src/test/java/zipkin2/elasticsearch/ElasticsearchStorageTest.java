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
package zipkin2.elasticsearch;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import zipkin2.CheckResult;

import static java.util.Arrays.asList;
import static okhttp3.tls.internal.TlsUtil.localhost;
import static org.assertj.core.api.Assertions.assertThat;
import static zipkin2.TestObjects.DAY;

public class ElasticsearchStorageTest {
  @Rule public MockWebServer es = new MockWebServer();

  ElasticsearchStorage storage =
      ElasticsearchStorage.newBuilder().hosts(asList(es.url("").toString())).build();

  @After
  public void close() {
    storage.close();
  }

  @Test
  public void memoizesIndexTemplate() throws Exception {
    es.enqueue(new MockResponse().setBody("{\"version\":{\"number\":\"2.4.0\"}}"));
    es.enqueue(new MockResponse()); // get span template
    es.enqueue(new MockResponse()); // get dependency template
    es.enqueue(new MockResponse()); // get tags template
    es.enqueue(new MockResponse()); // dependencies request
    es.enqueue(new MockResponse()); // dependencies request

    long endTs = storage.indexNameFormatter().parseDate("2016-10-02");
    storage.spanStore().getDependencies(endTs, DAY).execute();
    storage.spanStore().getDependencies(endTs, DAY).execute();

    es.takeRequest(); // get version
    es.takeRequest(); // get span template
    es.takeRequest(); // get dependency template
    es.takeRequest(); // get tags template

    assertThat(es.takeRequest().getPath())
        .startsWith("/zipkin:dependency-2016-10-01,zipkin:dependency-2016-10-02/_search");
    assertThat(es.takeRequest().getPath())
        .startsWith("/zipkin:dependency-2016-10-01,zipkin:dependency-2016-10-02/_search");
  }

  String healthResponse =
      "{\n"
          + "  \"cluster_name\": \"elasticsearch_zipkin\",\n"
          + "  \"status\": \"yellow\",\n"
          + "  \"timed_out\": false,\n"
          + "  \"number_of_nodes\": 1,\n"
          + "  \"number_of_data_nodes\": 1,\n"
          + "  \"active_primary_shards\": 5,\n"
          + "  \"active_shards\": 5,\n"
          + "  \"relocating_shards\": 0,\n"
          + "  \"initializing_shards\": 0,\n"
          + "  \"unassigned_shards\": 5,\n"
          + "  \"delayed_unassigned_shards\": 0,\n"
          + "  \"number_of_pending_tasks\": 0,\n"
          + "  \"number_of_in_flight_fetch\": 0,\n"
          + "  \"task_max_waiting_in_queue_millis\": 0,\n"
          + "  \"active_shards_percent_as_number\": 50\n"
          + "}";

  @Test
  public void check() {
    es.enqueue(new MockResponse().setBody(healthResponse));

    assertThat(storage.check()).isEqualTo(CheckResult.OK);
  }

  @Test
  public void check_oneHostDown() {
    storage.close();
    storage =
        ElasticsearchStorage.newBuilder()
            .hosts(asList("http://1.2.3.4:" + es.getPort(), es.url("").toString()))
            .connectTimeout(100)
            .build();

    es.enqueue(new MockResponse().setBody(healthResponse));

    assertThat(storage.check()).isEqualTo(CheckResult.OK);
  }

  private static final RestClientBuilder.HttpClientConfigCallback SSL_LOCALHOST_HTTP_CLIENT_CUSTOMIZER = new RestClientBuilder.HttpClientConfigCallback() {
    @Override
    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
      return httpClientBuilder.setSSLHostnameVerifier((host, session) -> true)
        .setSSLContext(localhost().sslContext());
    }
  };


  @Test
  public void check_ssl() throws Exception {
    storage.close();
    es.useHttps(localhost().sslSocketFactory(), false);

    storage = ElasticsearchStorage.newBuilder()
      .hosts(asList(es.url("").toString()))
      .httpClientCustomizer(SSL_LOCALHOST_HTTP_CLIENT_CUSTOMIZER)
      .build();

    es.enqueue(new MockResponse().setBody(healthResponse));

    assertThat(storage.check()).isEqualTo(CheckResult.OK);

    assertThat(es.takeRequest().getTlsVersion()).isNotNull();
  }

  @Test
  public void multipleSslNotYetSupported() {
    storage.close();
    es.useHttps(localhost().sslSocketFactory(), false);
    storage =
        ElasticsearchStorage.newBuilder()
            .hosts(asList("https://1.2.3.4:" + es.getPort(), es.url("").toString()))
            .httpClientCustomizer(SSL_LOCALHOST_HTTP_CLIENT_CUSTOMIZER)
            .build();

    storage.check();
  }
}
