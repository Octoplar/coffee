package net.octoplar.backend.repository;

import net.octoplar.backend.entity.CoffeeType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.List;

/**
 * Created by Octoplar.
 */
@Repository
public class CoffeeTypeDaoHbmImpl implements CoffeeTypeDao{
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @CacheEvict(cacheNames={"coffeeTypeAll", "coffeeTypeAvailable"}, allEntries = true)
    public void create(CoffeeType entity) {
        Session session=sessionFactory.getCurrentSession();
        session.save(entity);
    }

    @Override
    @Cacheable("coffeeTypeAll")
    public List<CoffeeType> readAll() {
        Session session=sessionFactory.getCurrentSession();
        return (List<CoffeeType>)session.getNamedQuery("CoffeeType.all").list();

    }

    @Override
    @Cacheable("coffeeTypeAvailable")
    public List<CoffeeType> readAvailable() {
        Session session=sessionFactory.getCurrentSession();
        return session.getNamedQuery("CoffeeType.available").list();
    }

    @Override
    public CoffeeType byId(int id) {
        Session session=sessionFactory.getCurrentSession();
        return (CoffeeType) session.getNamedQuery("CoffeeType.byId").setInteger("id", id).uniqueResult();
    }

    @Override
    @CacheEvict(cacheNames={"coffeeTypeAll", "coffeeTypeAvailable"}, allEntries = true)
    public void update(CoffeeType entity) {
        if (entity.isPersisted()){
            Session session=sessionFactory.getCurrentSession();
            int id=entity.getId();
            CoffeeType old=(CoffeeType) session.getNamedQuery("CoffeeType.byId").setInteger("id", id).uniqueResult();
            if (old!=null){
                old.setPrice(entity.getPrice());
                old.setDisabled(entity.isDisabled());
                old.setTypeName(entity.getTypeName());
                session.save(old);
            }
        }

    }

    @Override
    @CacheEvict(cacheNames={"coffeeTypeAll", "coffeeTypeAvailable"}, allEntries = true)
    public void delete(CoffeeType entity) {
        if (entity.isPersisted()){
            Session session=sessionFactory.getCurrentSession();
            int id=entity.getId();
            CoffeeType old=(CoffeeType) session.getNamedQuery("CoffeeType.byId").setInteger("id", id).uniqueResult();
            session.delete(old);
        }
    }
}

