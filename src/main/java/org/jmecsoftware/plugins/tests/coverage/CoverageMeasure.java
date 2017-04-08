package org.jmecsoftware.plugins.tests.coverage;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jorge Costa
 */
class CoverageMeasure {

  private int hits;  
  private final Integer line;  
  private final Map<Integer, Path> paths;  


  CoverageMeasure(int line) {
    this.paths = new HashMap();
    this.line = line;
  }

  int getHits() {
    return this.hits;
  }

  int getConditions() {
    return this.paths.size();
  }

  int getCoveredConditions() {
    Integer coveredConditions = 0;
    for (Map.Entry<Integer, Path> entry : paths.entrySet()) {
      Path value = entry.getValue();
      if (value.hits > 0) {
        coveredConditions++;
      }      
    }
    return coveredConditions;
  }

  void setHits(int lineId, int i) {
    this.hits += i;
  }

  void setConditions(int pathId, Boolean covered) {
    if (paths.containsKey(pathId)) {
      Path path = this.paths.get(pathId);
      path.setCovered(covered);
    } else {
      Path path = new Path(pathId);
      path.setCovered(covered);
      this.paths.put(pathId, path);
    }
  }
    
  int getLine() {
    return this.line;
  }
  
  class Path {

    private final Integer path;
    private int hits;
    Path(Integer path) {
      this.path = path;
    }
    
    void setCovered(Boolean covered) {
      if (covered )
      {
        this.hits++;
      }
    }
  }
}
 