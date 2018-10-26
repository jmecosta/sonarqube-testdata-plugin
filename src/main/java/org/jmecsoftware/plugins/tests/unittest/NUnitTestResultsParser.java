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
/*
 * from: https://github.com/SonarSource/sonar-dotnet-tests-library/blob/master/src/main/java/org/sonar/plugins/dotnet/tests/NUnitTestResultsFileParser.java
*/
package org.jmecsoftware.plugins.tests.unittest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLStreamException;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.jmecsoftware.plugins.tests.utils.XmlParserHelper;

public class NUnitTestResultsParser implements ReportParser {
  public static final Logger LOG = Loggers.get(NUnitTestResultsParser.class);

  @Override
  public Boolean parse(File file, UnitTestResults unitTestResults) {
    LOG.info("Parsing the NUnit Test Results file " + file.getAbsolutePath());
    return new Parser(file, unitTestResults).parse();
  }

  @Override
  public String name() {
    return "NUnitTestResultsParser";
  }

  private static class Parser {

    private final File file;
    private XmlParserHelper xmlParserHelper;
    private final UnitTestResults unitTestResults;

    public Parser(File file, UnitTestResults unitTestResults) {
      this.file = file;
      this.unitTestResults = unitTestResults;
    }

    public Boolean parse() {

      try {
          xmlParserHelper = new XmlParserHelper(file);

        xmlParserHelper.nextTag();
        handleTestResultsTags();
      } catch (FileNotFoundException | UnsupportedEncodingException | XMLStreamException ex) {
        if (xmlParserHelper != null) {
          xmlParserHelper.close();
        }

        return false;
      }
      
      if (xmlParserHelper != null) {
        xmlParserHelper.close();
      }

      return true;
    }

    private void handleTestResultsTags() throws XMLStreamException {
      LOG.info("Parsing the NUnit Test Results file: " + file.getAbsolutePath());
      int total = 0;
      int errors = 0;
      int failures = 0;
      int inconclusive = 0;
      int ignored = 0;
      int passed = 0;
      int skipped = 0;

      total = xmlParserHelper.getRequiredIntAttribute("total");

      if(xmlParserHelper.isAttributePresent("failures")) {
        failures = xmlParserHelper.getRequiredIntAttribute("failures");
      } else {
        if(xmlParserHelper.isAttributePresent("failed")) {
          failures = xmlParserHelper.getRequiredIntAttribute("failed");
        }
      }

      if(xmlParserHelper.isAttributePresent("inconclusive")) {
        inconclusive = xmlParserHelper.getRequiredIntAttribute("inconclusive");
      }

      if(xmlParserHelper.isAttributePresent("errors")) {
        errors = xmlParserHelper.getRequiredIntAttribute("errors");
      }

      if(xmlParserHelper.isAttributePresent("ignored")) {
        ignored = xmlParserHelper.getRequiredIntAttribute("ignored");
      } else {
        if(xmlParserHelper.isAttributePresent("not-run")) {
          ignored = xmlParserHelper.getRequiredIntAttribute("not-run");
        }
      }

      if(xmlParserHelper.isAttributePresent("skipped")) {
        skipped = xmlParserHelper.getRequiredIntAttribute("skipped");
      }

      if(xmlParserHelper.isAttributePresent("errors")) {
        errors = xmlParserHelper.getRequiredIntAttribute("errors");
      }

      int tests = total - inconclusive;
      if (passed == 0) {
        passed = total - errors - failures - inconclusive;
      }
      
      if (skipped == 0) {
        skipped = inconclusive + ignored;
      }

      unitTestResults.add(tests, passed, skipped, failures, errors, 0L);
    }
  }
}