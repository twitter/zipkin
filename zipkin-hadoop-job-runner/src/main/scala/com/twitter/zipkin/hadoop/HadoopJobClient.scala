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

import collection.mutable.HashMap
import java.util.Scanner
import java.io.File
import com.twitter.zipkin.hadoop.sources._

/**
 * Basic client for postprocessing hadoop jobs
 * @param combineSimilarNames whether or not we should differentiate between similar names
 */

abstract class HadoopJobClient(val combineSimilarNames: Boolean) {

  /**
   * Process a key and its value
   * @param s the key passed
   * @param value values associated with the key
   */
  def processKey(s: String, value: List[List[String]])

  /**
   * Starts the postprocessing for the client
   * @param filename the input filename
   * @param output the output filename
   */
  def start(filename : String, output : String)

  /**
   * Returns the key value of a line
   * @param line a single line
   * @return the key value returned
   */
  def getKeyValue(line: List[String]) = {
    line.head.replace("/", ".")
  }

  def addKey(key: String) = {
    HadoopJobClient.serviceNameSet.addServiceName(key)
  }

  def getValue(line: List[String]) = {
    line.tail
  }

  /**
   * Populate the name list
   * @param file a file representing a directory where the data is stored
   */

  def populateServiceNameList(file: File) {
    val populateOneFile = {f: File =>
      val s = new Scanner(f)
      if (!combineSimilarNames) return
      while (s.hasNextLine()) {
        val line = s.nextLine().split("\t").toList
        addKey(getKeyValue(line))
      }
    }
    Util.traverseFileTree(populateOneFile, file)
  }

  def populateAndStart(filename: String, output: String) = {
    populateServiceNameList(new File(filename))
    start(filename, output)
  }

  /**
   * Processes a single directory, with data files expected in TSV format, with key being the first value on each row
   * @param file a file representing a directory where the data is stored
   */
  def processDir(file: File) {
    val serviceToValues = new HashMap[String, List[List[String]]]()
    val processFile = { f: File =>
      val s = new Scanner(f)
      while (s.hasNextLine()) {
        val line = s.nextLine.split("\t").toList.map({_.trim()})
        val currentString = getKeyValue(line)
        var value = getValue(line)
        val serviceName = if (combineSimilarNames) HadoopJobClient.serviceNameSet.getStandardizedName(currentString) else currentString
        if (serviceToValues.contains(serviceName)) {
          serviceToValues(serviceName) ::= value
        } else {
          serviceToValues += serviceName -> List(value)
        }
      }
    }
    Util.traverseFileTree(processFile, file)
    for (t <- serviceToValues) {
      val (service, values) = t
      println(service + ", " + values)
      processKey(service, values)
    }
  }
}

object HadoopJobClient {

  val DELIMITER = ":"
  val serviceNameSet = new ServiceNameSet()

}