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

package com.twitter.zipkin.hadoop

import java.io._
import collection.mutable.HashMap
import com.twitter.zipkin.hadoop.sources.Util
import mustache.MustacheTemplate

/**
 * A client which writes to a file. This is intended for use mainly to format emails
 * @param combineSimilarNames
 * @param jobname
 */

abstract class WriteToFileClient(combineSimilarNames: Boolean, jobname: String) extends HadoopJobClient(combineSimilarNames) {

  protected var outputDir = ""

  def toHtmlName(s: String) = outputDir + "/" + s + ".html"

  def start(input: String, outputDir: String) {
    this.outputDir = outputDir
    processDir(new File(input))
  }
}

/**
 * A companion object to the WriteToFileClient which ensures that only one writer is ever open per service
 */

object WriteToFileClient {

  def finish() = {
    for (service <- MustacheTemplate.services()) {
      val mt = MustacheTemplate.getTemplate(service)
      mt.write(new PrintWriter((new FileOutputStream(service, true))))
    }
  }
}

/**
 * A client which writes MemcacheRequest data to the file specified
 */

class MemcacheRequestClient extends WriteToFileClient(true, "MemcacheRequest") {

  def processKey(service: String, values: List[List[String]]) {
    val numberMemcacheRequests = {
      val valuesToInt = values.flatten.map({ s: String => augmentString(s).toInt })
      valuesToInt.foldLeft(0) ((left: Int, right: Int) => left + right )
    }
    val mt = MustacheTemplate.getTemplate(toHtmlName(service))
    mt.addOneLineResult("Service " + service + " made " + numberMemcacheRequests + " redundant memcache requests")
  }

}

/**
 * A client which writes to a file, per each service pair
 */

abstract class WriteToFilePerServicePairClient(jobname: String) extends WriteToFileClient(false, jobname) {

  def getTableResultHeader(service: String): String

  def getTableHeader(): List[String]

  def addTable(service: String, values: List[List[String]], mt: MustacheTemplate) = {
    mt.addTableResult(getTableResultHeader(service), getTableHeader(), values)
  }

  def processKey(service: String, values: List[List[String]]) {
    val mt = MustacheTemplate.getTemplate(toHtmlName(service))
    addTable(service, values, mt)
  }
}


/**
 * A client which writes Timeouts data to the file specified
 */

class TimeoutsClient extends WriteToFilePerServicePairClient("Timeouts") {

  def getTableResultHeader(service: String) = {
    service + " timed out in calls to the following services"
  }

  def getTableHeader() = {
    List("Service Called", "# of Timeouts")
  }
}


/**
 * A client which writes Retries data to the file specified
 */

class RetriesClient extends WriteToFilePerServicePairClient("Retries") {

  def getTableResultHeader(service: String) = {
    service + " retried in calls to the following services:"
  }

  def getTableHeader() = {
    List("Service Called", "# of Retries")
  }
}


/**
 * A client which writes WorstRuntimes data to the file specified
 */

class WorstRuntimesClient extends WriteToFilePerServicePairClient("WorstRuntimes") {

  def getTableResultHeader(service: String) = {
    "Service " + service + " took the longest for these spans:"
  }

  def getTableHeader() = {
    List("Span ID", "Duration")
  }
}


/**
 * A client which writes WorstRuntimesPerTrace data to the file specified. Formats it as a HTML url
 */

class WorstRuntimesPerTraceClient(zipkinUrl: String) extends WriteToFilePerServicePairClient("WorstRuntimesPerTrace") {

  def getTableResultHeader(service: String) = {
    "Service " + service + " took the longest for these traces:"
  }

  def getTableHeader() = {
    List("Trace ID", "Duration")
  }

  override def addTable(service: String, values: List[List[String]], mt: MustacheTemplate) = {
    mt.addUrlTableResult(getTableResultHeader(service), getTableHeader(), values)
  }

}


/**
 * A client which writes ExpensiveEndpoints data to the file specified
 */

class ExpensiveEndpointsClient extends WriteToFilePerServicePairClient("ExpensiveEndpoints") {

  def getTableResultHeader(service: String) = {
    "The most expensive calls for " + service + " were:"
  }

  def getTableHeader() = {
    List("Service Called", "Duration")
  }
}
