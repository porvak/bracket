/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.porvak.bracket.config;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import com.porvak.bracket.social.database.DatabaseUpgrader;
import com.porvak.bracket.social.jdbc.versioned.DatabaseChangeSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.net.UnknownHostException;

@Configuration
@ImportResource("classpath:/com/porvak/bracket/config/mongo-repo.xml")
@Profile("embedded")
public class EmbeddedDataConfig extends AbstractMongoConfiguration {

    @Inject
    private Environment environment;

    @Inject
    private TextEncryptor textEncryptor;

    @Override
    public String getDatabaseName() {
        return "bracket";
    }

    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new Mongo("127.0.0.1");
    }

    @Bean @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = super.mongoTemplate();
        mongoTemplate.setWriteConcern(WriteConcern.JOURNAL_SAFE);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
        factory.setDatabaseName("bracketcloud");
        factory.setDatabaseType(EmbeddedDatabaseType.H2);
        EmbeddedDatabaseConfigurer config = new EmbeddedDatabaseConfigurer() {
            @Override
            public void configureConnectionProperties(ConnectionProperties connectionProperties, String s) {
                connectionProperties.setDriverClass(org.h2.Driver.class);
                connectionProperties.setUrl("jdbc:h2:tcp://localhost/~/bracketcloud");
                connectionProperties.setUsername("sa");
            }

            @Override
            public void shutdown(DataSource dataSource, String s) {

            }
        };

        //factory.setDatabaseConfigurer(config);
        return populateDatabase(factory.getDatabase());
    }

    private EmbeddedDatabase populateDatabase(EmbeddedDatabase database) {
        new DatabaseUpgrader(database, environment, textEncryptor) {
            protected void addInstallChanges(DatabaseChangeSet changeSet) {
//                changeSet.add(SqlDatabaseChange.inResource(new ClassPathResource("test-data.sql", getClass())));
            }
        }.run();
        return database;
    }
}
