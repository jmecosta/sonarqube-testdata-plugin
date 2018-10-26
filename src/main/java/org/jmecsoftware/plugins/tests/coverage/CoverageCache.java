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

import java.util.HashMap;
import java.util.Map;
import org.sonar.api.batch.ScannerSide;

/**
 *
 * @author Jorge Costa
 */
@ScannerSide
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
