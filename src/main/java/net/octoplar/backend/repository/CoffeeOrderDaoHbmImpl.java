package net.octoplar.backend.repository;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.entity.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Octoplar.
 */
@Repository
public class CoffeeOrderDaoHbmImpl implements CoffeeOrderDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void create(CoffeeOrder entity) {
        Session session=sessionFactory.getCurrentSession();
        session.save(entity);
    }

    @Override
    public CoffeeOrder byId(int id) {
        Session session=sessionFactory.getCurrentSession();
        return (CoffeeOrder) session.getNamedQuery("CoffeeOrder.byId").setInteger("id", id).uniqueResult();
    }

    //not supported in this implementation
    @Override
    public void update(CoffeeOrder entity) {
        throw new UnsupportedOperationException("net.octoplar.backend.repository.CoffeeOrderDaoHbmImpl  update method does not support");
    }

    @Override
    public void delete(CoffeeOrder entity) {
        Session session=sessionFactory.getCurrentSession();
        CoffeeOrder toDelete = (CoffeeOrder) session.getNamedQuery("CoffeeOrder.byId").setInteger("id", entity.getId()).uniqueResult();
        session.delete(toDelete);
    }
}
