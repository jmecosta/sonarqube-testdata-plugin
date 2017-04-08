package org.jmecsoftware.plugins.tests.coverage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 *
 * @author Jorge Costa
 */
class CoverageMeasures {
  private final Map<Integer, CoverageMeasure> coverage = new HashMap<>();
  
  private static final Logger LOG = Loggers.get(CoverageMeasures.class);
  
  private CoverageMeasures() {
    
  }
  
  static CoverageMeasures create() {
    CoverageMeasures measures = new CoverageMeasures();
    return measures;
  }

  void setHits(int lineId, int i) {
    if (coverage.containsKey(lineId)) {
      CoverageMeasure existentData = coverage.get(lineId);
      existentData.setHits(lineId, i);      
    } else {
      CoverageMeasure newLineHit = new CoverageMeasure(lineId);
      newLineHit.setHits(lineId, i);
      coverage.put(lineId, newLineHit);
    }
  }

  void setConditions(int lineId, int pathId, Boolean covered) {
    if (coverage.containsKey(lineId)) {
      CoverageMeasure existentData = coverage.get(lineId);
      existentData.setConditions(pathId, covered);
    } else {
      CoverageMeasure newLineHit = new CoverageMeasure(lineId);
      newLineHit.setConditions(pathId, covered);
      coverage.put(lineId, newLineHit);
    }
  }

  Collection<CoverageMeasure> getCoverageMeasures() {
    Map<Integer, CoverageMeasure> measures = new HashMap<>();
    measures.putAll(coverage);
    return measures.values();
  }  
}
