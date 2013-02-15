package comp.mq.edu.itec833.manager;

import java.util.HashMap;
import java.util.Map;

import comp.mq.edu.itec833.util.SessionIdGenerator;

/**
 * This class deals with session management. It keeps a Map that enables the programmer to find
 * the user Id corresponding to a particular session Id. The session Id is passed to the demo 
 * webapp when the user logs in and is returned as part of the SOAP header for subsequent requests.
 * 
 * This class is thread-safe.
 * 
 * @author Shamim Ahmed
 *
 */
public class SessionManager {
  private static final SessionManager INSTANCE = new SessionManager();
  private final Map<String, Integer> sessionMap = new HashMap<String, Integer>();

  /**
   * Accessor method for singleton object
   * @return the SessionManager singleton
   */
  public static SessionManager getInstance() {
	return INSTANCE;
  }
  
  /**
   * returns the session Id corresponding to a particular user when he logs in. 
   * saves the (sessionId, userId) pair in an internal map
   * @param userId the user id
   * @return the session id
   */
  public synchronized String createSession(int userId) {
	String sessionId = SessionIdGenerator.generate();

	while (sessionMap.containsKey(sessionId)) {
	  sessionId = SessionIdGenerator.generate();
	}

	sessionMap.put(sessionId, userId);
	return sessionId;
  }
  
  /**
   * removes the (sessionId, userId) mapping from the internal map when the user logs out
   * @param sessionId the session Id
   * @return true if successful, false otherwise
   */
  public synchronized boolean deleteSession(String sessionId) {
	boolean result = false;

	synchronized (this) {
	  result = sessionMap.containsKey(sessionId);
	  sessionMap.remove(sessionId);
	}

	return result;
  }
  
  /**
   * Checks if the sessionId is valid
   * @param sessionId the session Id
   * @return true if the map contains such an id, false otherwise
   */
  public synchronized boolean isLoggedIn(String sessionId) {
	return sessionMap.containsKey(sessionId);
  }
  
  /**
   * Checks if a particular user is logged in
   * @param userId the userId corresponding to the user
   * @return true if the user is logged in, false otherwise
   */
  public synchronized boolean isLoggedIn(Integer userId) {
	return sessionMap.containsValue(userId);
  }
  
  /**
   * returns the user Id corresponding to a session Id
   * @param sessionId the session id
   * @return a valid user id, or -1 if no such id can be found
   */
  public synchronized int getUserId(String sessionId) {
	int userId = -1;
	
	if (isLoggedIn(sessionId)) {
	  userId = sessionMap.get(sessionId);
	}
	
	return userId;
  }
  
  /**
   * retrieves the session Id corresponding to a particular userId
   * @param userId the user Id
   * @return the session Id, or an empty string if no such id can be found
   */
  public synchronized String getSessionId(int userId) {
	String result = "";
	
	for (Map.Entry<String, Integer> entry : sessionMap.entrySet()) {
	  if (entry.getValue().equals(userId)) {
		result = entry.getKey();
		break;
	  }
	}
	
	return result;
  }
  
  // private constructor to prevent instantiation
  private SessionManager() {
  }
}
