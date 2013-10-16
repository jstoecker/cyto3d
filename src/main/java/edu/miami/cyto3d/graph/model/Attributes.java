package edu.miami.cyto3d.graph.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapping from generic attribute names ("density", "interactivity", etc.) to primitive values (int,
 * bool, float, etc.).
 * 
 * @author justin
 */
public class Attributes {

  private Map<String, String> attributes = new HashMap<String, String>();
  private boolean             warnings   = true;

  public void set(String key, String value) {
    attributes.put(key, value);
  }

  public void set(String key, int value) {
    attributes.put(key, Integer.toString(value));
  }

  public void set(String key, float value) {
    attributes.put(key, Float.toString(value));
  }

  public void set(String key, double value) {
    attributes.put(key, Double.toString(value));
  }

  public void set(String key, boolean value) {
    attributes.put(key, Boolean.toString(value));
  }

  public int get(String key, int defaultValue) {
    String value = attributes.get(key);
    if (value == null) return defaultValue;

    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      printWarning(key, value, "INTEGER", "" + defaultValue);
      return defaultValue;
    }
  }

  public float get(String key, float defaultValue) {
    String value = attributes.get(key);
    if (value == null) return defaultValue;

    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException e) {
      printWarning(key, value, "FLOAT", "" + defaultValue);
      return defaultValue;
    }
  }

  public double get(String key, double defaultValue) {
    String value = attributes.get(key);
    if (value == null) return defaultValue;

    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      printWarning(key, value, "DOUBLE", "" + defaultValue);
      return defaultValue;
    }
  }

  public boolean get(String key, boolean defaultValue) {
    String value = attributes.get(key);
    if (value == null) return defaultValue;

    try {
      return Boolean.parseBoolean(value);
    } catch (NumberFormatException e) {
      printWarning(key, value, "BOOLEAN", "" + defaultValue);
      return defaultValue;
    }
  }

  public String get(String key, String defaultValue) {
    String value = attributes.get(key);
    if (value == null) return defaultValue;
    return value;
  }

  private void printWarning(String name, String value, String type, String defaultValue) {
    if (warnings) {
      System.err.printf("WARNING: property \"%s = %s\" is not type %s; defaulting to %s.\n", name,
          value, type, defaultValue);
    }
  }
  
  @Override
  public String toString() {
    return attributes.toString();
  }
}
