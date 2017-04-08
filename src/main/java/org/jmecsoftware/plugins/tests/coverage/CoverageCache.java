package org.jmecsoftware.plugins.tests.coverage;

import java.util.HashMap;
import java.util.Map;
import org.sonar.api.batch.BatchSide;

/**
 *
 * @author Jorge Costa
 */
@BatchSide
public class CoverageCache {

  private final static Map<String, CoverageMeasures> CACHE_COVERAGE = new HashMap<>();
  
  public CoverageCache() {
  }
  public Map<String, CoverageMeasures> coverageCache() {
    return CACHE_COVERAGE;
  }
  public Boolean isEmpty() {
    return CACHE_COVERAGE.isEmpty();
  }
}
