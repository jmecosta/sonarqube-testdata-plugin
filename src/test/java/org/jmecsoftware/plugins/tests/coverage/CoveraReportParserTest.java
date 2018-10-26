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
