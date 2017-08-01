package net.octoplar.backend.service;

import net.octoplar.backend.entity.CoffeeType;
import net.octoplar.backend.repository.CoffeeTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Octoplar.
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, timeout = 30, isolation = Isolation.READ_COMMITTED)
public class CoffeeTypeServiceImpl implements CoffeeTypeService {
    @Autowired
    private CoffeeTypeDao dao;

    @Override
    public List<CoffeeType> readAll() {
        return dao.readAll();
    }

    @Override
    public List<CoffeeType> readAvailable() {
        return dao.readAvailable();
    }
}

