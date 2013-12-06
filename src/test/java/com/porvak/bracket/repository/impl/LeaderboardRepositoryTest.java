package com.porvak.bracket.repository.impl;

import com.porvak.bracket.config.ComponentConfig;
import com.porvak.bracket.config.EmbeddedDataConfig;
import com.porvak.bracket.config.TestSecurityConfig;
import com.porvak.bracket.domain.BracketConstants;
import com.porvak.bracket.repository.LeaderboardRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.springframework.data.domain.Sort.Direction.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedDataConfig.class, ComponentConfig.class, TestSecurityConfig.class})
@ActiveProfiles(profiles = "embedded")
public class LeaderboardRepositoryTest {
    
    @Inject
    private LeaderboardRepository leaderboardRepository;
    
    @Test @Ignore
    public void test(){
        leaderboardRepository.findByPoolId(BracketConstants.POOL_ID, new Sort(DESC, "totalScore"));

    }
}
