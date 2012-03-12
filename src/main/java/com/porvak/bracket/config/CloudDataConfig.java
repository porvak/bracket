package com.porvak.bracket.config;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.apache.commons.dbcp.BasicDataSource;
import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.net.UnknownHostException;

@Configuration
@ImportResource("classpath:/com/porvak/bracket/config/mongo-repo.xml")
@Profile("cloud")
public class CloudDataConfig extends AbstractMongoConfiguration {

    @Inject
    private Environment environment;

    @Inject
    private TextEncryptor textEncryptor;

    @Override
    public String getDatabaseName() {
        return environment.getProperty("db.mongo.name", "bracket");
    }

    @Bean
    public Mongo mongo() throws UnknownHostException {
        Mongo mongo = new Mongo(environment.getProperty("db.mongo.host","127.0.0.1"), Integer.parseInt(environment.getProperty("db.mongo.port", "27017")));
        mongo.getDB(getDatabaseName()).authenticate(environment.getRequiredProperty("db.mongo.username"),
                environment.getRequiredProperty("db.mongo.password").toCharArray());
        return mongo;
    }

    @Bean @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = super.mongoTemplate();
        mongoTemplate.setWriteConcern(WriteConcern.JOURNAL_SAFE);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("db.jdbc.driver", Driver.class.getName()));
        dataSource.setUrl(environment.getRequiredProperty("db.jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("db.jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("db.jdbc.password"));
        return dataSource;
    }

}
