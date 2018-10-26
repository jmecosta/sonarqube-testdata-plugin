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
package org.jmecsoftware.plugins.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.sonar.api.Plugin;
import org.sonar.api.Plugin.Context;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.jmecsoftware.plugins.tests.coverage.CoverageCache;
import org.jmecsoftware.plugins.tests.coverage.CoverageSensor;
import org.jmecsoftware.plugins.tests.unittest.TestImportSensor;
import org.sonar.api.config.Configuration;

/**
 * {@inheritDoc}
 */
public final class TestDataImporterPlugin implements Plugin {

  public static final String COV_REPORT_PATHS_KEY = "sonar.tests.coverage.reportPath";
  public static final String UNIT_REPORT_PATH_KEY = "sonar.tests.unit.reportPath";
  public static final String XSLT_URL_KEY = "sonar.tests.unit.xunit.xsltURL";  

  private static List<PropertyDefinition> testingAndCoverageProperties() {
    return new ArrayList<>(Arrays.asList(
      PropertyDefinition.builder(UNIT_REPORT_PATH_KEY)
      .name("Unit test execution report(s)")
      .description("Path to unit test execution report(s), relative to projects root."
        + " See <a href='https://github.com/SonarOpenCommunity/sonar-cxx/wiki/Get-test-execution-metrics'>here</a> for supported formats."
        + " Use <a href='https://ant.apache.org/manual/dirtasks.html'>Ant-style wildcards</a> if neccessary.")
      .onQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
      .index(5)
      .build(),
      PropertyDefinition.builder(XSLT_URL_KEY)
      .name("XSLT transformer")
      .description("By default, the unit test execution reports are expected to be in the JUnitReport format."
        + " To import a report in an other format, set this property to an URL to a XSLT stylesheet which is able to perform the according transformation.")
      .onQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
      .index(6)
      .build(),
      PropertyDefinition.builder(COV_REPORT_PATHS_KEY)
        .name("Coverage report paths")
        .description("List of comma-separated paths (absolute or relative) containing coverage report.")
        .onQualifiers(Qualifiers.PROJECT)
        .build()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void define(Context context) {
    List<Object> l = new ArrayList<>();
    l.add(TestImportSensor.class);   
    l.add(CoverageAggregator.class);
    l.add(CoverageSensorImpl.class);

    l.addAll(testingAndCoverageProperties());
    context.addExtensions(l);
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
  
  public static class CoverageAggregator extends CoverageCache {
    public CoverageAggregator() {                  
      super();
    }
  }
  
  public static class CoverageSensorImpl extends CoverageSensor {
    public CoverageSensorImpl(Configuration settings, CoverageAggregator cache) {
      super(settings, cache);      
    }
  }  
}
