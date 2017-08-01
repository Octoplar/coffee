package net.octoplar.backend.service;

/**
 * Created by Octoplar.
 */
public interface ConfigurationService {
    String getParam(String key);
    String SetParam(String key, String value);
    String removeParam(String key);
}
