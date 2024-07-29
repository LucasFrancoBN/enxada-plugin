package org.enxada.enxadaPlugin.utils;

import java.util.HashMap;
import java.util.Map;

public class ParseArgs {
  public static Map<String, String> parse(String[] args) {
    Map<String, String> params = new HashMap<>();
    for (String arg : args) {
      String[] splitArg = arg.split(":", 2);
      if (splitArg.length == 2) {
        String key = splitArg[0].replaceFirst("--", "");
        String value = splitArg[1];
        params.put(key, value);
      }
    }
    return params;
  }
}
