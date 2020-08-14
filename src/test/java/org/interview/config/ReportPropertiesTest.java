package org.interview.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import  org.junit.Assert;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class ReportPropertiesTest {
 
    @Autowired
    private ReportProperties reportProperties;
 
    @Test
    public void whenSimplePropertyQueriedThenReturnsPropertyValue() 
      throws Exception {
        Assert.assertEquals("Incorrectly bound OutFile property", 
          "java-exercise-%t.json", reportProperties.getOut_file());
    }
    
}