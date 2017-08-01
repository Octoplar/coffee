package net.octoplar.backend.repository;


import net.octoplar.backend.entity.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Created by Octoplar.
 */

@Repository
public class ConfigurationDaoHbmImpl implements ConfigurationDao{

    @Autowired
    private SessionFactory sessionFactory;

    //returns old value or null
    @Override
    @CacheEvict(cacheNames = "configuration", key = "#key")
    public String put(String key, String value) {
        Session session=sessionFactory.getCurrentSession();
        Configuration e= (Configuration) session.getNamedQuery("Configuration.byId").setString("id",key).uniqueResult();

        String oldValue = e==null? null:e.getValue();

        if (e==null)
            session.save(new Configuration(key, value));
        else{
            e.setValue(value);
            session.update(e);
        }
        return oldValue;

    }

    @Override
    @Cacheable(cacheNames = "configuration", key = "#key")
    public String get(String key) {
        Session session=sessionFactory.getCurrentSession();
        Configuration e= (Configuration) session.getNamedQuery("Configuration.byId").setString("id",key).uniqueResult();
        return e==null? null:e.getValue();
    }

    //returns deleted value or null
    @Override
    @CacheEvict(cacheNames = "configuration", key = "#key")
    public String remove(String key) {
        Session session=sessionFactory.getCurrentSession();
        Configuration e= (Configuration) session.getNamedQuery("Configuration.byId").setString("id",key).uniqueResult();
        session.delete(e);
        return e==null? null:e.getValue();
    }
}

