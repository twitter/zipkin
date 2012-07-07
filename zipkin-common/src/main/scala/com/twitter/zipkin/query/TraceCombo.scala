/*
 * Copyright 2012 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twitter.zipkin.query

import com.twitter.zipkin.common.{Trace, TraceSummary}

object TraceCombo {
  def apply(trace: Trace): TraceCombo = {
    TraceCombo(trace, TraceSummary(trace), TraceTimeline(trace), trace.toSpanDepths)
  }
}

/**
 * Combined trace, summary, timeline
 */
case class TraceCombo(trace: Trace, traceSummary: Option[TraceSummary], traceTimeline: Option[TraceTimeline],
                     spanDepths: Option[Map[Long, Int]])
