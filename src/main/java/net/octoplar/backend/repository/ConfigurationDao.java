package net.octoplar.backend.repository;

/**
 * Created by Octoplar.
 */
public interface ConfigurationDao {
    String put(String key, String value);
    String get(String key);
    String remove(String key);
}
