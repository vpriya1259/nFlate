package com.nflate;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class NflateMain {

  private static final String
      CSV_FILE_NAME =
      "C:/Users/vishnu/Documents/file2.csv";

  private static final String
      SETTINGS_FILE_NAME =
      "C:/Users/vishnu/Documents/settings.txt";

  //------------------------------------------------------------------------------------------------

  public static void main(String args[]) {
    String csvJsonString = ConvertCsvToJsonAndGetJsonString();
    String finalResult = UpdateSettingsAndGetJsonString(csvJsonString);
    System.out.println(finalResult);
  }

  //------------------------------------------------------------------------------------------------

  public static String ConvertCsvToJsonAndGetJsonString() {
    CsvMapper mapper = new CsvMapper();
    File csvFile = new File(CSV_FILE_NAME);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      CsvSchema schema = CsvSchema.emptySchema().withHeader();
      MappingIterator<Map<String, Object>> it = mapper.reader(Map.class)
          .with(schema)
          .readValues(csvFile);

      Map<String, Object> nestedDataMap = new HashMap<String, Object>();

      while (it.hasNext()) {
        Map<String, Object> rowAsMap = it.next();
        for (String key : rowAsMap.keySet()) {
          Object value = null;
          if (isInteger(rowAsMap.get(key).toString().trim())) {
            value = Integer.parseInt(rowAsMap.get(key).toString().trim());
          } else {
            value = rowAsMap.get(key);
          }

          if (!key.contains(".")) {
            // If the key is not a nested key.
            nestedDataMap.put(key, value);
            continue;
          }

          // If the key is a nested key.
          StringTokenizer tokenizer = new StringTokenizer(key, ".");
          int currentTokenIndex = 0;
          int tokenCount = tokenizer.countTokens();
          Map<String, Object> currentMap = nestedDataMap;
          while (currentTokenIndex < tokenCount) {
            String currentKey = tokenizer.nextToken();
            if (currentTokenIndex != (tokenCount - 1)) {
              // If it's not the last key.
              Map<String, Object>
                  currentValueMap = (Map<String, Object>) currentMap.get(currentKey);
              if (currentValueMap == null) {
                currentValueMap = new HashMap<String, Object>();
                currentMap.put(currentKey, currentValueMap);
              }
              currentMap = currentValueMap;
            } else {
              // Last key in the nested key. Put the value.
              currentMap.put(currentKey, value);
            }
            ++currentTokenIndex;
          }
        }
      }

      return objectMapper.writeValueAsString(nestedDataMap);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  //------------------------------------------------------------------------------------------------

  public static String UpdateSettingsAndGetJsonString(String csvJsonString) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      Settings settings = mapper.readValue(new File(SETTINGS_FILE_NAME), Settings.class);
      for (Settings.Setting setting : settings.getCart()) {
        setting.setTheme(csvJsonString);
      }

      for (Settings.Setting setting : settings.getHome()) {
        setting.setTheme(csvJsonString);
      }
      return mapper.writeValueAsString(settings);
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  //------------------------------------------------------------------------------------------------

  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    }
    
    // only got here if we didn't return false
    return true;
  }
}


