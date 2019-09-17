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
package zipkin2.server.internal.banner;

import org.springframework.boot.ansi.AnsiElement;

public class ZipkinAnsi256Color implements AnsiElement {

  private final int xtermNumber;

  ZipkinAnsi256Color(int xtermNumber) {
    if (xtermNumber < 0 || xtermNumber > 255) {
      throw new IllegalArgumentException("'xtermNumber' must be 0-255!");
    }
    this.xtermNumber = xtermNumber;
  }

  @Override
  public String toString() {
    return "38;5;" + this.xtermNumber;
  }
}
