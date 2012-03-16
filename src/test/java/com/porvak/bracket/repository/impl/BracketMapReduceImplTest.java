package com.porvak.bracket.repository.impl;

import com.porvak.bracket.config.ComponentConfig;
import com.porvak.bracket.config.EmbeddedDataConfig;
import com.porvak.bracket.config.TestSecurityConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {EmbeddedDataConfig.class, ComponentConfig.class, TestSecurityConfig.class})
@ActiveProfiles(profiles = "embedded")
public class BracketMapReduceImplTest {

    @Inject
    BracketMapReduceImpl bracketMapReduce;


    @Test @Ignore
    public void testGenerateLeaderboard() throws Exception {
        bracketMapReduce.generateLeaderboard();
    }
}
