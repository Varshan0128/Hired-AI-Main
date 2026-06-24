package com.HiredAI.heiredAi.Config;

import jakarta.persistence.EntityManagerFactory;
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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "intelligenceEntityManagerFactory",
        transactionManagerRef = "intelligenceTransactionManager",
        basePackages = {"com.HiredAI.heiredAi.IntelligenceRepository"}
)
public class IntelligenceDbConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.intelligence")
    public DataSourceProperties intelligenceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "intelligenceDataSource")
    public DataSource intelligenceDataSource() {
        return intelligenceDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "intelligenceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean intelligenceEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("intelligenceDataSource") DataSource dataSource) {
        
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        // H2 mode uses H2 dialect, Postgres uses PostgreSQL dialect
        // Leave to Hibernate's automatic dialect detection or default to PG dialect if postgres is used.
        // Spring Boot automatically resolves the correct dialect, but we can set it if we want.

        return builder
                .dataSource(dataSource)
                .packages("com.HiredAI.heiredAi.IntelligenceEntity")
                .persistenceUnit("intelligence")
                .properties(properties)
                .build();
    }

    @Bean(name = "intelligenceTransactionManager")
    public PlatformTransactionManager intelligenceTransactionManager(
            @Qualifier("intelligenceEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
