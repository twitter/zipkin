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
package zipkin2.storage.mysql.v1;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.junit.ClassRule;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import zipkin.Span;
import zipkin.internal.MergeById;
import zipkin.internal.V2StorageComponent;
import zipkin.storage.StorageComponent;
import zipkin2.DependencyLink;

import static org.assertj.core.api.Assertions.assertThat;
import static zipkin2.storage.mysql.v1.internal.generated.tables.ZipkinDependencies.ZIPKIN_DEPENDENCIES;

@RunWith(Enclosed.class)
public class ITMySQLStorage {

  static LazyMySQLStorage classRule() {
    return new LazyMySQLStorage("2.4.6");
  }

  public static class SpanStoreTest extends zipkin.storage.SpanStoreTest {
    @ClassRule public static LazyMySQLStorage storage = classRule();

    @Override protected StorageComponent storage() {
      return V2StorageComponent.create(storage.get());
    }

    @Override
    public void clear() {
      storage.get().clear();
    }
  }

  public static class StrictTraceIdFalseTest extends zipkin.storage.StrictTraceIdFalseTest {
    @ClassRule public static LazyMySQLStorage storageRule = classRule();

    private MySQLStorage storage;

    @Override protected StorageComponent storage() {
      return V2StorageComponent.create(storage);
    }

    /** current implementation cannot return exact form reported */
    @Override public void getTrace_retrievesBy64Or128BitTraceId() {
      List<Span> trace = MergeById.apply(accept128BitTrace(storage()));
      assertThat(store().getTrace(0L, trace.get(0).traceId))
        .containsOnlyElementsOf(trace);
      assertThat(store().getTrace(trace.get(0).traceIdHigh, trace.get(0).traceId))
        .containsOnlyElementsOf(trace);
    }

    @Override public void clear() {
      storage = storageRule.computeStorageBuilder().strictTraceId(false).build();
      storage.clear();
    }
  }

  public static class DependenciesTest extends zipkin.storage.DependenciesTest {
    @ClassRule public static LazyMySQLStorage storage = classRule();

    @Override protected StorageComponent storage() {
      return V2StorageComponent.create(storage.get());
    }

    @Override public void clear() {
      storage.get().clear();
    }
  }

  public static class ITSpanStore extends zipkin2.storage.ITSpanStore {
    @ClassRule public static LazyMySQLStorage storage = classRule();

    @Override protected zipkin2.storage.StorageComponent storage() {
      return storage.get();
    }

    @Override
    public void clear() {
      storage.get().clear();
    }
  }

  public static class ITStrictTraceIdFalse extends zipkin2.storage.ITStrictTraceIdFalse {
    @ClassRule public static LazyMySQLStorage storageRule = classRule();

    private MySQLStorage storage;

    @Override protected zipkin2.storage.StorageComponent storage() {
      return storage;
    }

    @Override public void clear() {
      storage = storageRule.computeStorageBuilder().strictTraceId(false).build();
      storage.clear();
    }
  }

  public static class ITDependenciesPreAggregated extends zipkin2.storage.ITDependencies {
    @ClassRule public static LazyMySQLStorage storage = classRule();

    @Override protected zipkin2.storage.StorageComponent storage() {
      return storage.get();
    }

    /**
     * The current implementation does not include dependency aggregation. It includes retrieval of
     * pre-aggregated links, usually made via zipkin-dependencies
     */
    @Override protected void processDependencies(List<zipkin2.Span> spans) throws Exception {
      try (Connection conn = storage.get().datasource.getConnection()) {
        DSLContext context = storage.get().context.get(conn);

        // batch insert the rows at timestamp midnight
        List<Query> inserts = new ArrayList<>();
        aggregateLinks(spans).forEach((midnight, links) -> {
          Date day = new Date(midnight);
          for (DependencyLink link : links) {
            inserts.add(context.insertInto(ZIPKIN_DEPENDENCIES)
              .set(ZIPKIN_DEPENDENCIES.DAY, day)
              .set(ZIPKIN_DEPENDENCIES.PARENT, link.parent())
              .set(ZIPKIN_DEPENDENCIES.CHILD, link.child())
              .set(ZIPKIN_DEPENDENCIES.CALL_COUNT, link.callCount())
              .set(ZIPKIN_DEPENDENCIES.ERROR_COUNT, link.errorCount())
              .onDuplicateKeyIgnore());
          }
        });
        context.batch(inserts).execute();
      }
    }

    @Override public void clear() {
      storage.get().clear();
    }
  }

  public static class ITDependenciesOnDemand extends zipkin2.storage.ITDependencies {
    @ClassRule public static LazyMySQLStorage storage = classRule();

    @Override protected zipkin2.storage.StorageComponent storage() {
      return storage.get();
    }

    @Override public void clear() {
      storage.get().clear();
    }
  }
}
