package com.porvak.bracket.socialize.account;

import com.porvak.bracket.domain.User;
import com.porvak.bracket.socialize.account.exception.SignInNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoAccountRepository implements AccountRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoAccountRepository.class);

	private final MongoTemplate mongoTemplate;

	@Autowired
	public MongoAccountRepository(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public Account createAccount(User user) {

        LOGGER.debug("Creating account for user: {}", user);

        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }

        mongoTemplate.insert(user);

        return new Account(user.getId(), user.getDisplayName(), user.getEmail(), user.getProfileUrl());
	}

	public Account findById(String id) {
        LOGGER.debug("Finding user by id: {}", id);
        User user = mongoTemplate.findById(id, User.class);
        return new Account(user.getId(), user.getDisplayName(), user.getEmail(), user.getProfileUrl());
        
		//return jdbcTemplate.queryForObject(AccountMapper.SELECT_ACCOUNT + " where id = ?", accountMapper, id);
	}

	public Account findBySignin(String signin) throws SignInNotFoundException {
//		try {
//			return jdbcTemplate.queryForObject(accountQuery(signin), accountMapper, signin);
//		} catch (EmptyResultDataAccessException e) {
//			throw new SignInNotFoundException(signin);
//		}
        LOGGER.debug("Finding user by signin: {}", signin);
        return null;
	}

}