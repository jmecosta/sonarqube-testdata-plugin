package org.jmecsoftware.plugins.tests.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 *
 * @author Jorge Costa
 */
public class ReportUtils {
  public static final Logger LOG = Loggers.get(ReportUtils.class);
  
   public static List<File> getReports(Settings settings, final File moduleBaseDir,
      String reportPathPropertyKey) {

    List<File> reports = new ArrayList<>();

    List<String> reportPaths = Arrays.asList(settings.getStringArray(reportPathPropertyKey));
    if (!reportPaths.isEmpty()) {
      List<String> includes = new ArrayList<>();
      for (String reportPath : reportPaths) {
        // Normalization can return null if path is null, is invalid, or is a path with back-ticks outside known directory structure
        String normalizedPath = FilenameUtils.normalize(reportPath);
        if (normalizedPath != null && new File(normalizedPath).isAbsolute()) {
          includes.add(normalizedPath);
          continue;
        }

        // Prefix with absolute module base dir, attempt normalization again -- can still get null here
        normalizedPath = FilenameUtils.normalize(moduleBaseDir.getAbsolutePath() + File.separator + reportPath);
        if (normalizedPath != null) {
          includes.add(normalizedPath);
          continue;
        }

        LOG.debug("Not a valid report path '{}'", reportPath);
      }

      LOG.debug("Normalized report includes to '{}'", includes);

      // Includes array cannot contain null elements
      DirectoryScanner directoryScanner = new DirectoryScanner();
      directoryScanner.setIncludes(includes.toArray(new String[includes.size()]));
      directoryScanner.scan();

      String [] includeFiles = directoryScanner.getIncludedFiles();
      LOG.info("Scanner found '{}' report files", includeFiles.length);
      for (String found : includeFiles) {        
        reports.add(new File(found));
      }

      if (reports.isEmpty()) {
        LOG.warn("Cannot find a report for '{}'", reportPathPropertyKey);
      } else {
        LOG.info("Parser will parse '{}' report files", reports.size());
      }
    } else {
      LOG.info("Undefined report path value for key '{}'", reportPathPropertyKey);
    }

    return reports;
  }
  
  
}
