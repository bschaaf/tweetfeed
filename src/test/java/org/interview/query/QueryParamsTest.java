package org.interview.query;

import org.junit.Assert;
import org.junit.Test;

public class QueryParamsTest {

    @Test
    public void createQueryParamsTest(){
        String[] args = new String[0];
        
        QueryParams qParams = new QueryParams(args);
        Assert.assertEquals("bieber", qParams.getTrack());
        Assert.assertEquals("bieber", qParams.getBody().get("track").get(0));

        args = new String[] {"Trump", "Biden"};

        qParams = new QueryParams(args);
        Assert.assertEquals("Trump,Biden", qParams.getTrack());
        Assert.assertEquals("Trump,Biden", qParams.getBody().get("track").get(0));
    }   
}