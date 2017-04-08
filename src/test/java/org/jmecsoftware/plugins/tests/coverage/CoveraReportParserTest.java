package org.jmecsoftware.plugins.tests.coverage;

import java.io.File;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.jmecsoftware.plugins.tests.utils.TestUtils;

/**
 *
 * @author Jorge Costa
 */
public class CoveraReportParserTest {
  String pathPrefix = "/org/sonar/plugins/reports-project/coverage-reports/";  
  CoverageCache cache = new CoverageCache();

  @Test
  public void parsesReportAndDoAMerge() {
    cache.coverageCache().clear();
    CoverageReportParser parser = new CoverageReportParser(cache);
    File report = TestUtils.loadResource(pathPrefix + "report-example.json");
    parser.parse(report);
    Map<String, CoverageMeasures> cacheData = cache.coverageCache();
    assertEquals(2, cacheData.size());
    CoverageMeasures measuresData = cacheData.get("C:\\Users\\punker76\\Documents\\GitHub\\MahApps.Metro\\MahApps.Metro\\Accent.cs");    
    CoverageMeasure [] measures = measuresData.getCoverageMeasures().toArray(new CoverageMeasure[2]);
    assertEquals(2, measures.length);    
    assertEquals(20, measures[0].getLine());
    assertEquals(1, measures[0].getHits());
    assertEquals(2, measures[0].getConditions());
    assertEquals(1, measures[0].getCoveredConditions());
    File report2 = TestUtils.loadResource(pathPrefix + "report-example-2.json");
    parser.parse(report2);
    assertEquals(2, cacheData.size());    
    measuresData = cacheData.get("C:\\Users\\punker76\\Documents\\GitHub\\MahApps.Metro\\MahApps.Metro\\Accent.cs");    
    measures = measuresData.getCoverageMeasures().toArray(new CoverageMeasure[2]);
    assertEquals(2, measures.length);    
    assertEquals(20, measures[0].getLine());
    assertEquals(2, measures[0].getHits());
    assertEquals(2, measures[0].getConditions());
    assertEquals(2, measures[0].getCoveredConditions());
    
  }

  @Test
  public void InvalidReportReportsZero() {
    cache.coverageCache().clear();
    CoverageReportParser parser = new CoverageReportParser(cache);
    File report = TestUtils.loadResource(pathPrefix + "report-example-invalid.json");
    parser.parse(report);
    Map<String, CoverageMeasures> cacheData = cache.coverageCache();
    assertEquals(0, cacheData.size());
  }
}
