package com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpassvalue.impl;

import org.junit.Assert;
import org.junit.Test;

public class AesCfbPkcs5PaddingEncryptedPasswordValueTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException01() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                "D/Gq+9TP1pLYbNhOwBpdJA==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException02() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                        "D/Gq+9TP1pLYbNhOwBpdJA==," +
                        "ceVrk9XvghhylTYoJME5rA==," +
                        "sjrjs940djf0380==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException03() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "??????????????????????????????," +
                        "D/Gq+9TP1pLYbNhOwBpdJA==," +
                        "ceVrk9XvghhylTYoJME5rA==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException04() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                        "?????????????????????????," +
                        "ceVrk9XvghhylTYoJME5rA==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException05() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                        "D/Gq+9TP1pLYbNhOwBpdJA==," +
                        "???????????????????????????");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException06() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsduufa9j90najeru9023j09jadnfna0weGfg4=," +
                        "D/Gq+9TP1pLYbNhOwBpdJA==," +
                        "ceVrk9XvghhylTYoJME5rA==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException07() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                        "D/Gq+9TP1pLYbNhOwjwsofsj3BpdJA==," +
                        "ceVrk9XvghhylTYoJME5rA==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException08() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                        "D/Gq+9TP1pLYbNhOwBpdJA==," +
                        "ceVrk9XvghhylTYoJME5jq03jf0ewuwe02rA==");
    }

    @Test
    public void testEqualsObject01() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        Assert.assertEquals(s, s);
    }

    @Test
    public void testEqualsObject02() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        Assert.assertNotEquals(s, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "wdiWY14abZlnDY0wimI6MnyusPM1AETPnG4I8dW1Epc=," +
                                "ILEQgndNi4JUJBVuKWyu4w==," +
                                "V6TzjtAZ7SGk/QNQHCip/Q==");
        Assert.assertNotEquals(s1, s2);
    }

    @Test
    public void testEqualsObject05() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void testGetPassword01() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        Assert.assertArrayEquals(
                "Hello, World".toCharArray(), s1.getPassword());
    }

    @Test
    public void testGetPassword02() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Hello, World".toCharArray());
        Assert.assertArrayEquals(
                "Hello, World".toCharArray(), s1.getPassword());
    }

    @Test
    public void testHashCode01() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        Assert.assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testHashCode02() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                                "ceVrk9XvghhylTYoJME5rA==");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "wdiWY14abZlnDY0wimI6MnyusPM1AETPnG4I8dW1Epc=," +
                                "ILEQgndNi4JUJBVuKWyu4w==," +
                                "V6TzjtAZ7SGk/QNQHCip/Q==");
        Assert.assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testToString() {
        String string = "7MSMLoYMuv3IrBsdu5YVi+iFMqVTVcGxl1CLp4aGfg4=," +
                "D/Gq+9TP1pLYbNhOwBpdJA==," +
                "ceVrk9XvghhylTYoJME5rA==";
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(string);
        Assert.assertEquals(string, s1.toString());
    }

}