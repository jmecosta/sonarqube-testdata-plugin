package org.jmecsoftware.plugins.tests.coverage;

import java.io.FileInputStream;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class CoverageReportParser {
  
  private static final Logger LOG = Loggers.get(CoverageSensor.class);
  private final CoverageCache cache;

  public CoverageReportParser(CoverageCache cache) {
    this.cache = cache;
  }
  
  public void parse(java.io.File reportFile) {
            
    try {
      FileInputStream fis = new FileInputStream(reportFile);
      byte[] data = new byte[(int) reportFile.length()];
      fis.read(data);
      fis.close();
      
      String str = new String(data, "UTF-8");
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(str);
      JSONArray array = (JSONArray)obj;
      Map<String, CoverageMeasures> cacheData = cache.coverageCache();
      for (Object file : array) {
                
        JSONObject fileObject = (JSONObject)file;
        Object path = fileObject.get("file");
        
        // create new measures
        CoverageMeasures measures = CoverageMeasures.create();
        Boolean createNew = true;
        if (cacheData.containsKey(path.toString())) {
          createNew = false;
          measures = cache.coverageCache().get(path.toString());
        }

        JSONArray lines = (JSONArray)fileObject.get("lines");        

        for (Object line : lines) {
          JSONObject lineObject = (JSONObject)line;
          String lineData = lineObject.get("line").toString();
          String coveredData = lineObject.get("covered").toString();
          int lineId = Integer.valueOf(lineData);
          measures.setHits(lineId, coveredData.equals("true") ? 1 : 0);
          JSONArray branches = (JSONArray)lineObject.get("branches");
          for (Object branch : branches) {
            JSONObject branchObject = (JSONObject)branch;
            int pathData = Integer.valueOf(branchObject.get("branch").toString());
            String branchCoveredData = branchObject.get("covered").toString();
            measures.setConditions(lineId, pathData, branchCoveredData.equals("true"));  
          }
        }
        
        if (createNew) {
          this.cache.coverageCache().put(path.toString(), measures);
        }        
      }
    } catch (Exception ex) {
      LOG.info("Parsing {} failed: {}", reportFile, ex.getMessage());
    }
  }
}