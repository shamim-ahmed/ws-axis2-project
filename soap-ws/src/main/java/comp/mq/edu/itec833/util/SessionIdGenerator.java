package comp.mq.edu.itec833.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class generates a random string of hexadecimal numbers which is used as session id
 * @author Shamim Ahmed
 *
 */
public class SessionIdGenerator {
  private static final SecureRandom random = new SecureRandom();
  private static final int BIT_LENGTH = 128;
  private static final int RADIX = 16;
  
  /**
   * generates a session id
   * @return the session id containing hexadecimal digits
   */
  public static String generate() {
	BigInteger value = new BigInteger(BIT_LENGTH, random);
	return value.toString(RADIX);
  }
  
  // private constructor to prevent instantiation
  private SessionIdGenerator() {
  }
}
