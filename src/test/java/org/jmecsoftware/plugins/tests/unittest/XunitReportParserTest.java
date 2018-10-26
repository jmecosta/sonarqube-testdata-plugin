/*
 * SonarQube XML Plugin
 * Copyright (C) 2017-2018 Jorge Costa
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.jmecsoftware.plugins.tests.unittest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.jmecsoftware.plugins.tests.utils.TestUtils;

public class XunitReportParserTest {

  XunitReportParser parserHandler = new XunitReportParser();

  String pathPrefix = "/org/sonar/plugins/reports-project/xunit-reports/";

  @Test
  public void testParse() throws javax.xml.stream.XMLStreamException {

    Map<String, Integer> ioMap = new TreeMap<>();
    ioMap.put("xunit-result-2.xml", 5);
    ioMap.put("xunit-result-SAMPLE_with_fileName.xml", 3);
    ioMap.put("xunit-result-SAMPLE.xml", 3);
    ioMap.put("xunit-result-skippedonly.xml", 1);
    ioMap.put("xunit-result_with_emptyFileName.xml", 3);
    ioMap.put("nested_testsuites.xml", 2);

    for (Map.Entry<String, Integer> entry : ioMap.entrySet()) {
      parserHandler = new XunitReportParser();

      File report = TestUtils.loadResource(pathPrefix + entry.getKey());
      UnitTestResults results = new UnitTestResults();
      parserHandler.parse(report, results);
      assertEquals((int)entry.getValue(), results.getTests());
    }
  }

  @Test
  public void shouldThrowWhenGivenInvalidTime() {
      parserHandler = new XunitReportParser();

      File report = TestUtils.loadResource(pathPrefix + "invalid-time-xunit-report.xml");
      UnitTestResults results = new UnitTestResults();
      assertEquals(parserHandler.parse(report, results), false);
  }
}
