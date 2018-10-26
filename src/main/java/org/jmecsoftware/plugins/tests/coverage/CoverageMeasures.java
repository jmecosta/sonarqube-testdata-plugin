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
