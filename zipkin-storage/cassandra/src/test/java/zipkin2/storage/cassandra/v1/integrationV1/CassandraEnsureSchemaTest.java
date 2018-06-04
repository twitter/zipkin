/*
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
package zipkin2.storage.cassandra.v1.integrationV1;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Session;
import org.junit.Test;
import zipkin2.storage.cassandra.v1.InternalForTests;

import static org.assertj.core.api.Assertions.assertThat;

abstract class CassandraEnsureSchemaTest {

  protected abstract String keyspace();

  protected abstract Session session();

  @Test
  public void installsKeyspaceWhenMissing() {
    InternalForTests.ensureExists(keyspace(), session());

    KeyspaceMetadata metadata = session().getCluster().getMetadata().getKeyspace(keyspace());
    assertThat(metadata).isNotNull();
    assertThat(InternalForTests.hasUpgrade1_defaultTtl(metadata)).isTrue();
  }

  @Test
  public void installsTablesWhenMissing() {
    session()
        .execute(
            "CREATE KEYSPACE "
                + keyspace()
                + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};");

    InternalForTests.ensureExists(keyspace(), session());

    KeyspaceMetadata metadata = session().getCluster().getMetadata().getKeyspace(keyspace());
    assertThat(metadata).isNotNull();
    assertThat(InternalForTests.hasUpgrade1_defaultTtl(metadata)).isTrue();
  }

  @Test
  public void upgradesOldSchema() {
    InternalForTests.applyCqlFile(keyspace(), session(), "/cassandra-schema-cql3-original.txt");

    InternalForTests.ensureExists(keyspace(), session());

    KeyspaceMetadata metadata = session().getCluster().getMetadata().getKeyspace(keyspace());
    assertThat(metadata).isNotNull();
    assertThat(InternalForTests.hasUpgrade1_defaultTtl(metadata)).isTrue();
  }
}
