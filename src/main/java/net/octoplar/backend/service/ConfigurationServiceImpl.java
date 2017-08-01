package net.octoplar.backend.service;

import net.octoplar.backend.repository.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Octoplar.
 */
@Service
@Transactional(timeout = 30, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class ConfigurationServiceImpl implements ConfigurationService{

    @Autowired
    private ConfigurationDao configurationDao;


    @Override
    @Transactional(readOnly = true)
    public String getParam(String key) {
        return configurationDao.get(key);
    }

    @Override
    public String SetParam(String key, String value) {
        return configurationDao.put(key, value);
    }

    @Override
    public String removeParam(String key) {
        return configurationDao.remove(key);
    }
}

