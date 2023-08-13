package com.github.javachaos.chaosdungeons.utils;

import com.github.javachaos.chaosdungeons.constants.Constants;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PropertyManager class used to get property values from the Property file
 * defined in fileName.
 *
 * @author javachaos
 */
@SuppressWarnings("unused")
public final class PropertyManager {

  /**
   * Logger instance.
   */
  private static final Logger LOGGER =
      LogManager.getLogger(PropertyManager.class);

  /**
   * Property instance.
   */
  private final Properties props = new Properties();

  /**
   * Property file name.
   */
  private final String fileName = Constants.PROPERTY_FILE_NAME;

  /**
   * True if the property file has been loaded into memory.
   */
  private boolean isLoaded = false;

  /**
   * PropertyManager ctor.
   */
  public PropertyManager() {
  }

  /**
   * Initialize the property manager.
   */
  public void init() {
    loadProperties();
    Constants.init();
  }

  /**
   * Load properties into memory from file.
   */
  private void loadProperties() {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    try {
      props.load(inputStream);
    } catch (IOException e) {
      LOGGER.error(e);
      throw new RuntimeException(e);
    }
    LOGGER.debug("Properties loaded into memory.");
    LOGGER.debug(props.stringPropertyNames().toString());
    isLoaded = true;
  }

  /**
   * Write properties to file.
   */
  private void writeProperties() {
    try (FileOutputStream fos = new FileOutputStream(fileName)) {
      props.store(fos, null);
      LOGGER.debug("Properties written to disk.");
    } catch (final IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  /**
   * Add a property to this property manager.
   *
   * @param key   the key to store the property under
   * @param value the value of the property to be added
   * @param <T>   the type of the value to store
   */
  public <T extends Number> void addProperty(
      final String key, final T value) {
    if (!isLoaded) {
      loadProperties();
    }
    props.put(key, value);
    writeProperties();
  }

  /**
   * Add a string property to this property manager.
   *
   * @param key   the key to store the property under
   * @param value the value of the property to be added
   */
  public void addProperty(
      final String key, final String value) {
    if (!isLoaded) {
      loadProperties();
    }
    props.put(key, value);
    writeProperties();
  }

  /**
   * Return a string property from the underlying PROPS instance.
   *
   * @param key the key of the value to get
   * @return the value assigned the key key
   */
  public String getStringProperty(final String key) {
    if (!isLoaded) {
      loadProperties();
    }
    if ((key == null) || (key.isEmpty())) {
      throw new IllegalArgumentException("Key was null or zero length.");
    }
    return props.getProperty(key);
  }

  /**
   * Return the property as a string.
   *
   * @param key          the key of the property.
   * @param defaultValue the default value for the property.
   * @return the property if it exists or if it
   *     does not exist returns the defaultValue.
   */
  public String getStringProperty(final String key,
                                  final String defaultValue) {
    String temp;
    if (!isLoaded) {
      loadProperties();
    }
    if ((key == null) || (key.isEmpty())) {
      return defaultValue;
    }
    temp = props.getProperty(key);
    if (temp == null || (temp.isEmpty())) {
      return defaultValue;
    }

    return temp;
  }

  /**
   * GetFloatProperty.
   *
   * @param key the key to the property.
   * @return the property value as a float.
   *     if the value is not recognizable as a
   *     float an exception is thrown.
   */
  public float getFloatProperty(final String key) {
    return Float.parseFloat(getStringProperty(key));
  }

  /**
   * Return the property as a Float.
   *
   * @param key          the key of the property.
   * @param defaultValue the default value for the property.
   * @return the property if it exists as a Float or if it
   *     does not exist returns the defaultValue.
   */
  public float getFloatProperty(
      final String key, final float defaultValue) {
    return Float.parseFloat(getStringProperty(key, defaultValue + ""));
  }

  /**
   * GetIntegerProperty.
   *
   * @param key the key to the property.
   * @return the property value as an Integer.
   *     if the value is not recognizable as an
   *     Integer an exception is thrown.
   */
  public int getIntegerProperty(final String key) {
    return Integer.parseInt(getStringProperty(key));
  }

  /**
   * Return the property as an Integer.
   *
   * @param key          the key of the property.
   * @param defaultValue the default value for the property.
   * @return the property if it exists as an Integer or if it
   *     does not exist returns the defaultValue.
   */
  public int getIntegerProperty(
      final String key, final int defaultValue) {
    return Integer.parseInt(getStringProperty(
        key, defaultValue + ""));
  }

  /**
   * Get a property from the property as a long value.
   *
   * @param key the key of the property to get
   * @return the value of the property
   */
  public long getLongProperty(final String key) {
    return Long.parseLong(getStringProperty(key));
  }

  /**
   * Get a property from the property as a long value.
   * If the property cannot be returned from the underlying
   * PROPS instance then return defaultValue.
   *
   * @param key          the key of the property to get
   * @param defaultValue the defaultValue to be returned if
   *                     the value for key cannot be returned
   * @return the value for key or defaultValue
   *     if the value for key cannot be returned
   */
  public long getLongProperty(
      final String key, final long defaultValue) {
    return Long.parseLong(getStringProperty(
        key, defaultValue + ""));
  }

  /**
   * Get a property from the property as a boolean value.
   * If the property cannot be returned from the underlying
   * PROPS instance then return defaultValue.
   *
   * @param key          the key of the property to get
   * @param defaultValue the defaultValue to be returned if
   *                     the value for key cannot be returned
   * @return the value for key or defaultValue
   *     if the value for key cannot be returned
   */
  public boolean getBooleanProperty(final String key,
                                    final boolean defaultValue) {
    return Boolean.parseBoolean(getStringProperty(key, defaultValue + ""));
  }
}
