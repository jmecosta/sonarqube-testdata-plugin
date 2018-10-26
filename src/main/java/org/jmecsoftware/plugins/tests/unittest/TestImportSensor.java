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

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.sonar.api.batch.sensor.Sensor;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.ParsingUtils;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.jmecsoftware.plugins.tests.TestDataImporterPlugin;
import org.jmecsoftware.plugins.tests.utils.ReportUtils;
import org.sonar.api.config.Configuration;

/**
 * {@inheritDoc}
 */
public class TestImportSensor implements Sensor {
  public static final Logger LOG = Loggers.get(TestImportSensor.class);
  private static final double PERCENT_BASE = 100d;
  private final List<ReportParser> parsers = new LinkedList<>();
  private final Configuration settings;
  
  /**
   * {@inheritDoc}
   */
  public TestImportSensor(Configuration settings) {
    this.settings = settings;
    ReportParser xunit = new XunitReportParser(settings);
    ReportParser nunit = new NUnitTestResultsParser();
    this.parsers.add(xunit);
    this.parsers.add(nunit);
  }

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor.name("Unit Test Metrics Report").global().onlyWhenConfiguration(conf -> conf.hasKey(TestDataImporterPlugin.UNIT_REPORT_PATH_KEY));
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(SensorContext context) {    
    String moduleKey = context.settings().getString("sonar.moduleKey");
    if (moduleKey != null) {
        LOG.debug("Runs unit test import sensor only at top level project skip : Module Key = '{}'", moduleKey);
        return;        
    }
    
    LOG.debug("Root module imports test metrics: Module Key = '{}'", context.module());    
    
    List<File> reports = ReportUtils.getReports(settings, context.fileSystem().baseDir(), TestDataImporterPlugin.UNIT_REPORT_PATH_KEY);
    if (!reports.isEmpty()) {

      UnitTestResults results = new UnitTestResults();
      for(File report : reports) {
        for (ReportParser parser : this.parsers) {
          if (parser.parse(report, results)) {
            LOG.info("Parsed report '{}' with parser '{}'", report, parser.name());
            break;
          }
        }          
      }

      saveMetrics(context, results);
    } else {
      LOG.debug("No reports found, nothing to process");
    }
  }

  private void saveMetrics(final SensorContext context, UnitTestResults results) {
        
    if (results.getTests() > 0) {
      double testsPassed = results.getTests() - results.getTestErrors() - results.getTestFailures();
      double successDensity = testsPassed * PERCENT_BASE / results.getTests();

      context.<Integer>newMeasure()
         .forMetric(CoreMetrics.TESTS)
         .on(context.module())
         .withValue(results.getTests())
         .save();
       context.<Integer>newMeasure()
         .forMetric(CoreMetrics.TEST_ERRORS)
         .on(context.module())
         .withValue(results.getTestErrors())
         .save();
       context.<Integer>newMeasure()
         .forMetric(CoreMetrics.TEST_FAILURES)
         .on(context.module())
         .withValue(results.getTestFailures())
         .save();
       context.<Integer>newMeasure()
         .forMetric(CoreMetrics.SKIPPED_TESTS)
         .on(context.module())
         .withValue(results.getSkippedTests())
         .save();
       context.<Double>newMeasure()
         .forMetric(CoreMetrics.TEST_SUCCESS_DENSITY)
         .on(context.module())
         .withValue(ParsingUtils.scaleValue(successDensity))
         .save();
        context.<Long>newMeasure()
         .forMetric(CoreMetrics.TEST_EXECUTION_TIME)
         .on(context.module())
         .withValue(results.getTestTime())
         .save();
    } else {
      LOG.debug("The reports contain no testcases");
    }      

  }

}
