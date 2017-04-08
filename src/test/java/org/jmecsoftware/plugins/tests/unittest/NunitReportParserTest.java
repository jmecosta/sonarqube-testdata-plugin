package org.jmecsoftware.plugins.tests.unittest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.jmecsoftware.plugins.tests.utils.TestUtils;

public class NunitReportParserTest {

  NUnitTestResultsParser parserHandler = new NUnitTestResultsParser();

  String pathPrefix = "/org/sonar/plugins/reports-project/nunit-reports/";

  @Test
  public void testParse() throws javax.xml.stream.XMLStreamException {

    Map<String, Integer> ioMap = new TreeMap<>();
    ioMap.put("ConsoleApplicationCSharp.Test.unittest.report.xml", 2);

    for (Map.Entry<String, Integer> entry : ioMap.entrySet()) {
      parserHandler = new NUnitTestResultsParser();

      File report = TestUtils.loadResource(pathPrefix + entry.getKey());
      UnitTestResults results = new UnitTestResults();
      parserHandler.parse(report, results);
      assertEquals((int)entry.getValue(), results.getTests());
    }
  }

  @Test
  public void shouldThrowWhenGivenInvalidTime() {
      parserHandler = new NUnitTestResultsParser();

      File report = TestUtils.loadResource(pathPrefix + "invalid.xml");
      UnitTestResults results = new UnitTestResults();
      assertEquals(parserHandler.parse(report, results), false);
  }
}
