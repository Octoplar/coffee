package net.octoplar.backend.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * Created by Octoplar.
 */
@Configuration
@EnableAutoConfiguration
public class SessionFactoryConfig {

    @Autowired
    private Environment env;


    //data source + connection pool
    @Bean
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //pool
        dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("spring.c3p0.min_size")));
        dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("spring.c3p0.max_size")));
        dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("spring.c3p0.idle_test_period")));

        //db access
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setUser(env.getProperty("spring.datasource.username"));
        dataSource.setDriverClass(env.getProperty("spring.datasource.driver"));
        return dataSource;
    }

    //sessionFactory
    @Bean
    public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("net.octoplar.backend.entity");
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", env.getProperty("spring.hibernate.properties.dialect"));
        hibernateProperties.put("hibernate.show_sql", env.getProperty("spring.hibernate.properties.show_sql"));
        hibernateProperties.put("hibernate.cache.use_second_level_cache",
                env.getProperty("spring.hibernate.properties.cache.use_second_level_cache"));
        hibernateProperties.put("hibernate.cache.region.factory_class", env.getProperty("spring.hibernate.properties.cache.region.factory_class"));
        //hibernateProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.hibernate.properties.ddl-auto"));
        sessionFactoryBean.setHibernateProperties(hibernateProperties);

        return sessionFactoryBean;
    }



    //transaction manager
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) throws PropertyVetoException {
        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);;
        return transactionManager;
    }


    //SQL exception translator
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }


}

