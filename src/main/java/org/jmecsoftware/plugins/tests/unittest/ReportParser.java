package org.jmecsoftware.plugins.tests.unittest;

import java.io.File;

/**
 *
 * @author Jorge Costa
 */
public interface ReportParser {
  Boolean parse(File report, UnitTestResults unitTestResults);
  String name();
}
