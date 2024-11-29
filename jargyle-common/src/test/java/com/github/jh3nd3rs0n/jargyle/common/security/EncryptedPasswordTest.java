package com.github.jh3nd3rs0n.jargyle.common.security;

import org.junit.Assert;
import org.junit.Test;

public class EncryptedPasswordTest {

	@Test
	public void testEqualsObject01() {
		EncryptedPassword e = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Assert.assertEquals(e, e);
	}

	@Test
	public void testEqualsObject02() {
		EncryptedPassword e = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Assert.assertNotEquals(e, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		EncryptedPassword e1 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		EncryptedPassword e2 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
						"TQmU6HevTlDEDr38Z/dByQ==," +
						"PAFnak2eyRg=");
		Assert.assertNotEquals(e1, e2);
	}

	@Test
	public void testEqualsObject05() {
		EncryptedPassword e1 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		EncryptedPassword e2 = new EncryptedPassword(
				new TestEncryptedPasswordSpec("TestEncryptedPassword"),
				new TestEncryptedPasswordValue(
						"Hello, World".toCharArray()));
		Assert.assertNotEquals(e1, e2);
	}

	@Test
	public void testEqualsObject06() {
		EncryptedPassword e1 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		EncryptedPassword e2 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Assert.assertEquals(e1, e2);
	}

	@Test
	public void testGetPassword01() {
		char[] password = "opensesame".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		Assert.assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword02() {
		char[] password = "mission%3Aimpossible".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		Assert.assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword03() {
		char[] password = "safeDriversSave40%25".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		Assert.assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword04() {
		EncryptedPassword e = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Assert.assertArrayEquals("Hello, World".toCharArray(), e.getPassword());
	}

	@Test
	public void testGetPassword05() {
		EncryptedPassword e = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
						"TQmU6HevTlDEDr38Z/dByQ==," +
						"PAFnak2eyRg=");
		Assert.assertArrayEquals("The quick brown fox jumped over the lazy dog".toCharArray(), e.getPassword());
	}

	@Test
	public void testHashCode01() {
		EncryptedPassword e1 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		EncryptedPassword e2 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Assert.assertEquals(e1.hashCode(), e2.hashCode());
	}

	@Test
	public void testHashCode02() {
		EncryptedPassword e1 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		EncryptedPassword e2 = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
						"TQmU6HevTlDEDr38Z/dByQ==," +
						"PAFnak2eyRg=");
		Assert.assertNotEquals(e1.hashCode(), e2.hashCode());
	}

	@Test
	public void testNewInstanceCharArray() {
		Assert.assertNotNull(EncryptedPassword.newInstance(
				"Hello, World".toCharArray()));
	}

	@Test
	public void testNewInstanceFromString() {
		Assert.assertNotNull(EncryptedPassword.newInstanceFrom(
				"AesCfbPkcs5PaddingEncryptedPassword:" +
						"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ="));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceFromStringForIllegalArgumentException01() {
		EncryptedPassword.newInstanceFrom("no_password");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceFromStringForIllegalArgumentException02() {
		EncryptedPassword.newInstanceFrom("PlainTextPassword:password");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceFromStringForIllegalArgumentException03() {
		EncryptedPassword.newInstanceFrom(
				"AesCfbPkcs5PaddingEncryptedPassword:???????????????");
	}

	@Test
	public void testNewInstanceStringString() {
		Assert.assertNotNull(EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ="));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceStringStringForIllegalArgumentException01() {
		EncryptedPassword.newInstance("PlainTextPassword", "password");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewInstanceStringStringForIllegalArgumentException02() {
		EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"???????????????????????");
	}

	@Test
	public void testToString01() {
		EncryptedPassword e = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=");
		Assert.assertEquals(
				"AesCfbPkcs5PaddingEncryptedPassword:" +
						"Yl8Da3RdK6x3aWr3P/Ss8A==," +
						"dGNdMNGk2T2/UsPtquFnzg==," +
						"iiaIaNs3tkQ=",
				e.toString());
	}

	@Test
	public void testToString02() {
		EncryptedPassword e = EncryptedPassword.newInstance(
				"AesCfbPkcs5PaddingEncryptedPassword",
				"gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
						"TQmU6HevTlDEDr38Z/dByQ==," +
						"PAFnak2eyRg=");
		Assert.assertEquals(
				"AesCfbPkcs5PaddingEncryptedPassword:" +
						"gxhAQ/Wppz5qq2KqpJLdI+0XoHED8KctcmCVvxeiL0vof9Qg4z3E81HJ6qBEt8/x," +
						"TQmU6HevTlDEDr38Z/dByQ==," +
						"PAFnak2eyRg=",
				e.toString());
	}

}
