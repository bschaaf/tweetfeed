package org.interview.model;

import org.junit.Assert;
import org.junit.Test;


public class UserTest {

    @Test
    public void createUserTest(){
        User user = new User("1","Thu Jul 02 20:18:19 +0000 2020","J. Luipaard", "Otorongo");
        Assert.assertEquals(user.getId(), "1");
        Assert.assertEquals(user.getCreatedAt(), Long.valueOf(1593721099));
        Assert.assertEquals(user.getName(), "J. Luipaard");
        Assert.assertEquals(user.getScreenName(), "Otorongo");
    }
}