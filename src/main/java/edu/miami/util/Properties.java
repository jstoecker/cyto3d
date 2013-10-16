package edu.miami.util;

/**
 * Enhanced properties class with built-in type casting for getters.
 * 
 * @author justin
 */
public class Properties extends java.util.Properties {

  boolean warnings = true;

  public Properties() {
  }

  public Properties(Properties props) {
    super(props);
  }

  public void setWarnings(boolean warnings) {
    this.warnings = warnings;
  }

  public int get(String key, int defaultValue) {
    String propertyValue = getProperty(key);
    if (propertyValue == null) return defaultValue;

    try {
      return Integer.parseInt(propertyValue);
    } catch (NumberFormatException e) {
      printWarning(key, propertyValue, "INTEGER", "" + defaultValue);
      return defaultValue;
    }

  }

  public float get(String key, float defaultValue) {
    String propertyValue = getProperty(key);
    if (propertyValue == null) return defaultValue;

    try {
      return Float.parseFloat(propertyValue);
    } catch (NumberFormatException e) {
      printWarning(key, propertyValue, "FLOAT", "" + defaultValue);
      return defaultValue;
    }
  }

  public double get(String key, double defaultValue) {
    String propertyValue = getProperty(key);
    if (propertyValue == null) return defaultValue;

    try {
      return Double.parseDouble(propertyValue);
    } catch (NumberFormatException e) {
      printWarning(key, propertyValue, "DOUBLE", "" + defaultValue);
      return defaultValue;
    }
  }

  public boolean get(String key, boolean defaultValue) {
    String propertyValue = getProperty(key);
    if (propertyValue == null) return defaultValue;

    try {
      return Boolean.parseBoolean(propertyValue);
    } catch (NumberFormatException e) {
      printWarning(key, propertyValue, "BOOLEAN", "" + defaultValue);
      return defaultValue;
    }
  }

  public String get(String key, String defaultValue) {
    String value = getProperty(key);
    return (value == null) ? defaultValue : value;
  }

  public synchronized Object set(String key, int value) {
    return setProperty(key, Integer.toString(value));
  }

  public synchronized Object set(String key, float value) {
    return setProperty(key, Float.toString(value));
  }

  public synchronized Object set(String key, double value) {
    return setProperty(key, Double.toString(value));
  }

  public synchronized Object set(String key, boolean value) {
    return setProperty(key, Boolean.toString(value));
  }

  public synchronized Object set(String key, String value) {
    return setProperty(key, value);
  }

  private void printWarning(String name, String value, String type, String defaultValue) {
    if (warnings) {
      System.err.printf("WARNING: property \"%s = %s\" is not type %s; defaulting to %s.\n", name,
          value, type, defaultValue);
    }
  }
}
