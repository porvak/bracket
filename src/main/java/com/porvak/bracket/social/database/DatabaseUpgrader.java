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
package com.porvak.bracket.social.database;

import com.porvak.bracket.social.jdbc.versioned.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;


/**
 * Performs migrations against the Greenhouse database.
 * Allows a database migration to be automated as part of integrating a software change that impacts the schema.
 * If the Database is at version zero(), the current version of the database will be installed.
 * If the Database is at a version less than the current version, the database will be upgraded from that version to the current version.
 * If the Database is already at the current version, no migration will be performed.
 * This migration model was adapted from the <a href="http://www.liquibase.org/tutorial-using-oracle">LiquiBase Oracle tutorial</a>.
 * @author Keith Donald
 */
public class DatabaseUpgrader {
	
	private final Environment environment;
	
	private final TextEncryptor textEncryptor;

	//private final org.springframework.jdbc.versioned.DatabaseUpgrader upgrader;
    private final com.porvak.bracket.social.jdbc.versioned.DatabaseUpgrader upgrader;

	public DatabaseUpgrader(DataSource dataSource, Environment environment, TextEncryptor textEncryptor) {
		this.environment = environment;
		this.textEncryptor = textEncryptor;
		this.upgrader = createUpgrader(dataSource);
	}
	
	public void run() {
		upgrader.run();		
	}

	// subclassing hooks
	
	protected void addInstallChanges(DatabaseChangeSet changeSet) {}

	// internal helpers
	
	private com.porvak.bracket.social.jdbc.versioned.DatabaseUpgrader createUpgrader(DataSource dataSource) {
		GenericDatabaseUpgrader upgrader = new GenericDatabaseUpgrader(dataSource);
		if (upgrader.getCurrentDatabaseVersion().equals(DatabaseVersion.zero())) {
			addInstallChangeSet(upgrader);			
		} else {
			addUpgradeChangeSets(upgrader);
		}
		return upgrader;
	}

	private void addInstallChangeSet(GenericDatabaseUpgrader upgrader) {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("3"));
		changeSet.add(installScript("Member.sql"));

		changeSet.add(SqlDatabaseChange.inResource(new ClassPathResource("JdbcUsersConnectionRepository.sql", JdbcUsersConnectionRepository.class)));
		addInstallChanges(changeSet);
		upgrader.addChangeSet(changeSet);
	}

	private DatabaseChange installScript(String resource) {
		return SqlDatabaseChange.inResource(new ClassPathResource("install/" + resource, DatabaseUpgrader.class));
	}

	private void addUpgradeChangeSets(GenericDatabaseUpgrader upgrader) {
		upgrader.addChangeSet(version2ChangeSet());
		upgrader.addChangeSet(version3ChangeSet());
	}

	private DatabaseChangeSet version2ChangeSet() {		
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("2"));
		changeSet.add(upgradeScript("v2/AlterServiceProviderTable.sql"));
		return changeSet;
	}
	
	private DatabaseChangeSet version3ChangeSet() {
		DatabaseChangeSet changeSet = new DatabaseChangeSet(DatabaseVersion.valueOf("3"));
		//changeSet.add(new UpdateEncryptionMethod(environment, textEncryptor));
		changeSet.add(upgradeScript("v3/CreateUserConnectionTable.sql"));
		changeSet.add(upgradeScript("v3/PopulateUserConnectionTable.sql"));
		changeSet.add(upgradeScript("v3/DropAccountConnectionTables.sql"));
		return changeSet;
	}

	private DatabaseChange upgradeScript(String resource) {
		return SqlDatabaseChange.inResource(new ClassPathResource("upgrade/" + resource, DatabaseUpgrader.class));
	}
	
}