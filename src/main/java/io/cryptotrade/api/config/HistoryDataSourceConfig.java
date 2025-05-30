package io.cryptotrade.api.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "io.cryptotrade.api.repository.history",
        entityManagerFactoryRef = "historyEntityManagerFactory",
        transactionManagerRef = "historyTransactionManager"
)
public class HistoryDataSourceConfig {

    // Carga las propiedades con prefijo spring.datasource.history
    @Bean(name = "historyDataSourceProperties")
    @ConfigurationProperties("spring.datasource.history")
    public DataSourceProperties historyDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Crea el DataSource usando las propiedades anteriores
    @Bean(name = "historyDataSource")
    public DataSource historyDataSource(
            @Qualifier("historyDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    // Configura el EntityManagerFactory para la base histórica
    @Bean(name = "historyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean historyEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("historyDataSource") DataSource dataSource) {

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return builder
                .dataSource(dataSource)
                .packages("io.cryptotrade.api.model") // paquete de entidades JPA
                .persistenceUnit("historyPU")
                .properties(jpaProperties)
                .build();
    }

    // Configura el TransactionManager para la base histórica
    @Bean(name = "historyTransactionManager")
    public PlatformTransactionManager historyTransactionManager(
            @Qualifier("historyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
