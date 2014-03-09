package io.linger;

/**
 * Created: 18 December 2007
 *
 * @author: Java Port By: James Ratcliff, falazar@arlut.utexas.edu
 * ftp://ftp.arlut.utexas.edu/pub/java_hashes/Sha256Crypt.java
 * Redistribution and use in source and binary form are permitted
 * provided that distributions retain this entire copyright notice
 * and comment. Neither the name of the University nor the names of
 * its contributors may be used to endorse or promote products
 * derived from this software without specific prior written
 * permission. THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

import java.security.MessageDigest;
import android.util.Log;

/**
 * <p>This class defines a method, {@link
 * Sha256Crypt#Sha256_crypt(java.lang.String, java.lang.String, int)
 * Sha256_crypt()}, which takes a password and a salt string and
 * generates a Sha256 encrypted password entry.</p>
 *
 * <p>This class implements the new generation, scalable, SHA256-based
 * Unix 'crypt' algorithm developed by a group of engineers from Red
 * Hat, Sun, IBM, and HP for common use in the Unix and Linux
 * /etc/shadow files.</p>
 *
 * <p>The Linux glibc library (starting at version 2.7) includes
 * support for validating passwords hashed using this algorithm.</p>
 *
 * <p>The algorithm itself was released into the Public Domain by
 * Ulrich Drepper &lt;drepper@redhat.com&gt;.  A discussion of the
 * rationale and development of this algorithm is at</p>
 *
 * <p>http://people.redhat.com/drepper/sha-crypt.html</p>
 *
 * <p>and the specification and a sample C language implementation is
 * at</p>
 *
 * <p>http://people.redhat.com/drepper/SHA-crypt.txt</p>
 */

public final class Sha256Crypt
{
	static private final String sha256_salt_prefix = "$5$";
	static private final String sha256_rounds_prefix = "rounds=";
	static private final int ROUNDS_DEFAULT = 110000;
	static private final int SALT_LENGTH = 16;
	static private final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static private final String SALTCHARS = itoa64;
//	static private final String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	static private MessageDigest getSHA256()
	{
		try
		{
			return MessageDigest.getInstance("SHA-256");
		}
		catch (java.security.NoSuchAlgorithmException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static String generateSalt()
	{
		java.util.Random randgen = new java.util.Random();
		StringBuilder saltBuf = new StringBuilder();
		
		while (saltBuf.length() < SALT_LENGTH)
		{
			int index = (int) (randgen.nextFloat() * SALTCHARS.length());
			saltBuf.append(SALTCHARS.substring(index, index+1));
		}
		
		Log.v("Testing", "Generated salt: " + saltBuf.toString());
		return saltBuf.toString();
	}
	
	/**
	 * <p>This method actually generates an Sha256 crypted password hash
	 * from a plaintext password and a salt.</p>
	 *
	 * <p>The resulting string will be in the form
	 * '$5$&lt;rounds=n&gt;$&lt;salt&gt;$&lt;hashed mess&gt;</p>
	 *
	 * @param keyStr Plaintext password
	 *
	 * @param saltStr An encoded salt/rounds which will be consulted to determine the salt
	 * and round count, if not null
	 *
	 * @param roundsCount If this value is not 0, this many rounds will
	 * used to generate the hash text.
	 *
	 * @return The Sha256 Unix Crypt hash text for the keyStr
	 */

	public static final String Sha256_crypt(String keyStr, String saltStr)
	{
		MessageDigest ctx = getSHA256();
		MessageDigest alt_ctx = getSHA256();
		byte[] alt_result;
		byte[] temp_result;
		byte[] p_bytes = null;
		byte[] s_bytes = null;
		int cnt, cnt2;
		StringBuilder buffer;

		/* -- */
		if (saltStr != null)
		{
			if (saltStr.startsWith(sha256_salt_prefix))
			{
				saltStr = saltStr.substring(sha256_salt_prefix.length());
			}
		}

		byte[] key = keyStr.getBytes();
		byte[] salt = saltStr.getBytes();

		ctx.reset();
		ctx.update(key, 0, key.length);
		ctx.update(salt, 0, salt.length);

		alt_ctx.reset();
		alt_ctx.update(key, 0, key.length);
		alt_ctx.update(salt, 0, salt.length);
		alt_ctx.update(key, 0, key.length);

		alt_result = alt_ctx.digest();

		for (cnt = key.length; cnt > 32; cnt -= 32)
		{
			ctx.update(alt_result, 0, 32);
		}

		ctx.update(alt_result, 0, cnt);

		for (cnt = key.length; cnt > 0; cnt >>= 1)
		{
			if ((cnt & 1) != 0)
			{
				ctx.update(alt_result, 0, 32);
			}
			else
			{
				ctx.update(key, 0, key.length);
			}
		}

		alt_result = ctx.digest();

		alt_ctx.reset();

		for (cnt = 0; cnt < key.length; ++cnt)
		{
			alt_ctx.update(key, 0, key.length);
		}

		temp_result = alt_ctx.digest();

		p_bytes = new byte[key.length];

		for (cnt2 = 0, cnt = p_bytes.length; cnt >= 32; cnt -= 32)
		{
			System.arraycopy(temp_result, 0, p_bytes, cnt2, 32);
			cnt2 += 32;
		}

		System.arraycopy(temp_result, 0, p_bytes, cnt2, cnt);

		alt_ctx.reset();

		for (cnt = 0; cnt < 16 + (alt_result[0]&0xFF); ++cnt)
		{
			alt_ctx.update(salt, 0, salt.length);
		}

		temp_result = alt_ctx.digest();

		s_bytes = new byte[salt.length];

		for (cnt2 = 0, cnt = s_bytes.length; cnt >= 32; cnt -= 32)
		{
			System.arraycopy(temp_result, 0, s_bytes, cnt2, 32);
			cnt2 += 32;
		}

		System.arraycopy(temp_result, 0, s_bytes, cnt2, cnt);

		/* Repeatedly run the collected hash value through SHA256 to 
		 * burn CPU cycles.  */
		for (cnt = 0; cnt < ROUNDS_DEFAULT; ++cnt)
		{
			ctx.reset();

			if ((cnt & 1) != 0)
			{
				ctx.update(p_bytes, 0, key.length);
			}
			else
			{
				ctx.update (alt_result, 0, 32);
			}

			if (cnt % 3 != 0)
			{
				ctx.update(s_bytes, 0, salt.length);
			}

			if (cnt % 7 != 0)
			{
				ctx.update(p_bytes, 0, key.length);
			}

			if ((cnt & 1) != 0)
			{
				ctx.update(alt_result, 0, 32);
			}
			else
			{
				ctx.update(p_bytes, 0, key.length);
			}

			alt_result = ctx.digest();
		}

		buffer = new StringBuilder(sha256_salt_prefix);
		buffer.append(sha256_rounds_prefix);
		buffer.append(ROUNDS_DEFAULT);
		buffer.append("$");

		buffer.append(saltStr);
		buffer.append("$");

		buffer.append(b64_from_24bit (alt_result[0],  alt_result[10], alt_result[20], 4));
		buffer.append(b64_from_24bit (alt_result[21], alt_result[1],  alt_result[11], 4));
		buffer.append(b64_from_24bit (alt_result[12], alt_result[22], alt_result[2],  4));
		buffer.append(b64_from_24bit (alt_result[3],  alt_result[13], alt_result[23], 4));
		buffer.append(b64_from_24bit (alt_result[24], alt_result[4],  alt_result[14], 4));
		buffer.append(b64_from_24bit (alt_result[15], alt_result[25], alt_result[5],  4));
		buffer.append(b64_from_24bit (alt_result[6],  alt_result[16], alt_result[26], 4));
		buffer.append(b64_from_24bit (alt_result[27], alt_result[7],  alt_result[17], 4));
		buffer.append(b64_from_24bit (alt_result[18], alt_result[28], alt_result[8],  4));
		buffer.append(b64_from_24bit (alt_result[9],  alt_result[19], alt_result[29], 4));
		buffer.append(b64_from_24bit ((byte)0x00,     alt_result[31],  alt_result[30], 3));

		/* Clear the buffer for the intermediate result so that people
    	attaching to processes or reading core dumps cannot get any
    	information. */
		ctx.reset();
		Log.v("Testing", buffer.toString());
		return buffer.toString();
	}

	private static final String b64_from_24bit(byte B2, byte B1, byte B0, int size)
	{
		int v = ((((int) B2) & 0xFF) << 16) | ((((int) B1) & 0xFF) << 8) | ((int)B0 & 0xff);

		StringBuilder result = new StringBuilder();

		while (--size >= 0)
		{
			result.append(itoa64.charAt((int) (v & 0x3f)));
			v >>>= 6;
		}

		return result.toString();
	}
}