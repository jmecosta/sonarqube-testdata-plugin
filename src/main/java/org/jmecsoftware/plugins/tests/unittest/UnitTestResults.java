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

/**
 *
 * @author Jorge Costa
 */
public class UnitTestResults {
  int tests = 0;
  int passed = 0;
  int skipped = 0;
  int failures = 0;
  int errors = 0;
  Long duration = 0L;
  
  void add(int tests, int passed, int skipped, int failures, int errors, Long duration) {
    this.tests += tests;
    this.passed += passed;
    this.skipped += skipped;
    this.failures += failures;
    this.errors += errors;
    this.duration += duration;
  }  

  public int getTests() {
    return this.tests;
  }

  public int getTestErrors() {
    return this.errors;
  }

  public int getTestFailures() {
    return this.failures;
  }

  public Integer getSkippedTests() {
    return this.skipped;
  }

  public Long getTestTime() {
    return this.duration;
  }
}
