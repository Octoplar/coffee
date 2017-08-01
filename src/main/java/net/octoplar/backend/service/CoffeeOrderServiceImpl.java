package net.octoplar.backend.service;

import net.octoplar.backend.entity.CoffeeOrder;
import net.octoplar.backend.repository.CoffeeOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Octoplar.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, timeout = 30, isolation = Isolation.READ_COMMITTED)
public class CoffeeOrderServiceImpl implements CoffeeOrderService{

    @Autowired
    private CoffeeOrderDao coffeeOrderDao;

    @Override
    public void create(CoffeeOrder entity) {
        coffeeOrderDao.create(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CoffeeOrder read(int id) {
        return coffeeOrderDao.byId(id);
    }

    @Override
    public void update(CoffeeOrder entity) {
        coffeeOrderDao.update(entity);
    }

    @Override
    public void delete(CoffeeOrder entity) {
        coffeeOrderDao.delete(entity);
    }
}

