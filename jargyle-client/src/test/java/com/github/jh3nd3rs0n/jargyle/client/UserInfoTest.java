package com.github.jh3nd3rs0n.jargyle.client;

import org.junit.Assert;
import org.junit.Test;

public class UserInfoTest {

    @Test
    public void testNewInstanceString01() {
        Assert.assertNotNull(UserInfo.newInstance("Aladdin:opensesame"));
    }

    @Test
    public void testNewInstanceString02() {
        Assert.assertNotNull(UserInfo.newInstance("Jasmine:mission%3Aimpossible"));
    }

    @Test
    public void testNewInstanceString03() {
        Assert.assertNotNull(UserInfo.newInstance("Abu:safeDriversSave40%25"));
    }

    @Test
    public void testNewInstanceString04() {
        Assert.assertNotNull(UserInfo.newInstance("Jafar:opensesame"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringForIllegalArgumentException01() {
        UserInfo.newInstance("%");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringForIllegalArgumentException02() {
        UserInfo.newInstance("%A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringForIllegalArgumentException03() {
        UserInfo.newInstance("%Az");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringForIllegalArgumentException04() {
        UserInfo.newInstance("abcdef%ghijkl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringForIllegalArgumentException05() {
        UserInfo.newInstance("abcdef%0ghijkl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceStringForIllegalArgumentException06() {
        UserInfo.newInstance(" abc def ghi jkl ");
    }

    @Test
    public void testEqualsObject01() {
        UserInfo userInfo = UserInfo.newInstance("Aladdin:opensesame");
        Assert.assertEquals(userInfo, userInfo);
    }

    @Test
    public void testEqualsObject02() {
        UserInfo userInfo = UserInfo.newInstance("Aladdin:opensesame");
        Assert.assertNotEquals(userInfo, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = UserInfo.newInstance("Aladdin:opensesame");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        UserInfo userInfo1 = UserInfo.newInstance("Aladdin:opensesame");
        UserInfo userInfo2 = UserInfo.newInstance("Jafar:opensesame");
        Assert.assertNotEquals(userInfo1, userInfo2);
    }

    @Test
    public void testEqualsObject05() {
        UserInfo userInfo1 = UserInfo.newInstance("Aladdin:opensesame");
        UserInfo userInfo2 = UserInfo.newInstance("Aladdin:opensesame");
        Assert.assertEquals(userInfo1, userInfo2);
    }

    @Test
    public void testHashCode01() {
        UserInfo userInfo1 = UserInfo.newInstance("Aladdin:opensesame");
        UserInfo userInfo2 = UserInfo.newInstance("Jafar:opensesame");
        Assert.assertNotEquals(userInfo1.hashCode(), userInfo2.hashCode());
    }

    @Test
    public void testHashCode02() {
        UserInfo userInfo1 = UserInfo.newInstance("Aladdin:opensesame");
        UserInfo userInfo2 = UserInfo.newInstance("Aladdin:opensesame");
        Assert.assertEquals(userInfo1.hashCode(), userInfo2.hashCode());
    }

}