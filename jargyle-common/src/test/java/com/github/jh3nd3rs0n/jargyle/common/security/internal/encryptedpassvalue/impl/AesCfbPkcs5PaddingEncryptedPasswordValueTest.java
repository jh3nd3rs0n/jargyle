package com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpassvalue.impl;

import org.junit.Assert;
import org.junit.Test;

public class AesCfbPkcs5PaddingEncryptedPasswordValueTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException01() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                "D/Gq+9TP1pLYbNhOwBpdJA==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException02() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                        "dGNdMNGk2T2/UsPtquFnzg==," +
                        "ceVrk9XvghhylTYoJME5rA==," +
                        "sjrjs940djf0380==");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException03() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "??????????????????????????????," +
                        "dGNdMNGk2T2/UsPtquFnzg==," +
                        "iiaIaNs3tkQ=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException04() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                        "?????????????????????????," +
                        "iiaIaNs3tkQ=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException05() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                        "dGNdMNGk2T2/UsPtquFnzg==," +
                        "???????????????????????????");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException06() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "7MSMLoYMuv3IrBsduufa9j90najeru9023j09jadnfna0weGfg4=," +
                        "dGNdMNGk2T2/UsPtquFnzg==," +
                        "iiaIaNs3tkQ=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException07() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                        "D/Gq+9TP1pLYbNhOwjwsofsj3BpdJA==," +
                        "iiaIaNs3tkQ=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAesCfbPkcs5PaddingEncryptedPasswordValueStringForIllegalArgumentException08() {
        new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                        "dGNdMNGk2T2/UsPtquFnzg==," +
                        "ceVrk9XvghhylTYoJME5jq03jf0ewuwe02rA==");
    }

    @Test
    public void testEqualsObject01() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        Assert.assertEquals(s, s);
    }

    @Test
    public void testEqualsObject02() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        Assert.assertNotEquals(s, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = new AesCfbPkcs5PaddingEncryptedPasswordValue(
                "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
                                "TQmU6HevTlDEDr38Z/dByQ==," +
                                "PAFnak2eyRg=");
        Assert.assertNotEquals(s1, s2);
    }

    @Test
    public void testEqualsObject05() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        Assert.assertEquals(s1, s2);
    }

    @Test
    public void testGetPassword01() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Hello, World".toCharArray());
        Assert.assertArrayEquals(
                "Hello, World".toCharArray(), s1.getPassword());
    }

    @Test
    public void testGetPassword02() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        Assert.assertArrayEquals(
                "Hello, World".toCharArray(), s1.getPassword());
    }

    @Test
    public void testGetPassword03() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "The quick brown fox jumped over the lazy dog".toCharArray());
        Assert.assertArrayEquals(
                "The quick brown fox jumped over the lazy dog".toCharArray(), s1.getPassword());
    }

    @Test
    public void testGetPassword04() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
                                "TQmU6HevTlDEDr38Z/dByQ==," +
                                "PAFnak2eyRg=");
        Assert.assertArrayEquals(
                "The quick brown fox jumped over the lazy dog".toCharArray(), s1.getPassword());
    }

    @Test
    public void testGetPassword05() {
        System.setProperty(
                "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword",
                "donkeyhorsefruitcakepunch");
        try {
            AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                    new AesCfbPkcs5PaddingEncryptedPasswordValue(
                            "Hello, World".toCharArray());
            Assert.assertArrayEquals(
                    "Hello, World".toCharArray(), s1.getPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword");
        }
    }

    @Test
    public void testGetPassword06() {
        System.setProperty(
                "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword",
                "donkeyhorsefruitcakepunch");
        try {
            AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                    new AesCfbPkcs5PaddingEncryptedPasswordValue(
                            "/h797HfrZjn+ApDLK3UKjg==," +
                                    "SyQx5mq9EogmBaUhyhXy8g==," +
                                    "IcZLI5MWIkA=");
            Assert.assertArrayEquals(
                    "Hello, World".toCharArray(), s1.getPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword");
        }
    }

    @Test
    public void testGetPassword07() {
        System.setProperty(
                "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword",
                "donkeyhorsefruitcakepunch");
        try {
            AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                    new AesCfbPkcs5PaddingEncryptedPasswordValue(
                            "The quick brown fox jumped over the lazy dog".toCharArray());
            Assert.assertArrayEquals(
                    "The quick brown fox jumped over the lazy dog".toCharArray(), s1.getPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword");
        }
    }

    @Test
    public void testGetPassword08() {
        System.setProperty(
                "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword",
                "donkeyhorsefruitcakepunch");
        try {
            AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                    new AesCfbPkcs5PaddingEncryptedPasswordValue(
                            "wRLw0dI76rcVxicOLzPb+iKtaioHOHauyTfY46u1JpmDL2WQr5v4x+L42XtztfSp," +
                                    "dMDTKKqLiEmDLAYn4uu8Vg==," +
                                    "lKeQyPyCH9c=");
            Assert.assertArrayEquals(
                    "The quick brown fox jumped over the lazy dog".toCharArray(), s1.getPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword");
        }
    }

    @Test
    public void testHashCode01() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        Assert.assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testHashCode02() {
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                                "dGNdMNGk2T2/UsPtquFnzg==," +
                                "iiaIaNs3tkQ=");
        AesCfbPkcs5PaddingEncryptedPasswordValue s2 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(
                        "gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
                                "TQmU6HevTlDEDr38Z/dByQ==," +
                                "PAFnak2eyRg=");
        Assert.assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testToString01() {
        String string = "Yl8Da3RdK6x3aWr3P/Ss8A==," +
                "dGNdMNGk2T2/UsPtquFnzg==," +
                "iiaIaNs3tkQ=";
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(string);
        Assert.assertEquals(string, s1.toString());
    }

    @Test
    public void testToString02() {
        String string =
                "gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
                "TQmU6HevTlDEDr38Z/dByQ==," +
                "PAFnak2eyRg=";
        AesCfbPkcs5PaddingEncryptedPasswordValue s1 =
                new AesCfbPkcs5PaddingEncryptedPasswordValue(string);
        Assert.assertEquals(string, s1.toString());
    }

}