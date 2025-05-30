package io.cryptotrade.api.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "io.cryptotrade.api.repository.transactional",  // paquete de repositorios transactional
        entityManagerFactoryRef = "transactionalEntityManagerFactory",
        transactionManagerRef = "transactionalTransactionManager"
)
public class TransactionalDataSourceConfig {

    @Primary
    @Bean(name = "transactionalDataSourceProperties")
    @ConfigurationProperties("spring.datasource.transactional")
    public DataSourceProperties transactionalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "transactionalDataSource")
    public DataSource transactionalDataSource(
            @Qualifier("transactionalDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "transactionalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean transactionalEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("transactionalDataSource") DataSource dataSource) {

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return builder
                .dataSource(dataSource)
                .packages("io.cryptotrade.api.model")  // paquete de entidades JPA
                .persistenceUnit("transactionalPU")
                .properties(props)
                .build();
    }

    @Primary
    @Bean(name = "transactionalTransactionManager")
    public PlatformTransactionManager transactionalTransactionManager(
            @Qualifier("transactionalEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}