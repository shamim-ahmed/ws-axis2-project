package comp.mq.edu.itec833.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;

/**
 * This class deals with database operations related to registration and login
 * @author Shamim Ahmed
 *
 */
public class AuthenticationManager {
  private static final AuthenticationManager INSTANCE = new AuthenticationManager();
  private static final String USERNAME_CHECK_SQL = "SELECT * FROM Users WHERE userName=?";
  private static final String REGISTER_SQL = "INSERT INTO Users (userName, password) values (?, ?)";
  private static final String LOGIN_SQL = "SELECT userId FROM Users WHERE userName=? AND password=?";

  /**
   * returns the singleton instance
   * @return the AuthenticationManager instance
   */
  public static AuthenticationManager getInstance() {
	return INSTANCE;
  }

  /**
   * Performs registration
   * @param userName the username
   * @param password the password
   * @return true if registration is successful, false otherwise
   */
  public boolean register(String userName, String password) {
	boolean result = false;
	
	Connection connection = null;
    PreparedStatement checkStmt = null;
    PreparedStatement registerStmt = null;
    ResultSet resultSet = null;
    	
	try {
	  connection = DBConnectionManager.getConnection();
	  checkStmt = connection.prepareStatement(USERNAME_CHECK_SQL);
	  registerStmt = connection.prepareStatement(REGISTER_SQL);
	  checkStmt.setString(1, userName);
	  resultSet = checkStmt.executeQuery();

	  // check if there is a user with the same username
	  if (resultSet.next()) {
		return result;
	  }

	  // insert the new user in database
	  registerStmt.setString(1, userName);
	  registerStmt.setString(2, password);
	  registerStmt.execute();
	  result = true;
	} catch (SQLException ex) {
	  System.err.println(ex);
	} catch (NamingException ex) {
	  System.err.println(ex);
	} finally {
	  DbUtils.closeQuietly(resultSet);
	  DbUtils.closeQuietly(checkStmt);
	  DbUtils.closeQuietly(registerStmt);
	  DbUtils.closeQuietly(connection);
	}

	return result;
  }

  /**
   * Performs login operation
   * @param userName the username
   * @param password the password
   * @return the sessionId corresponding to the logged in user, or an empty string if the authentication fails
   */
  public String login(String userName, String password) {
	String sessionId = "";
	Connection connection = null;
	PreparedStatement stmt = null;
	ResultSet resultSet = null;
	
	try {
	  connection = DBConnectionManager.getConnection(); 
	  stmt = connection.prepareStatement(LOGIN_SQL);
	  stmt.setString(1, userName);
	  stmt.setString(2, password);

	  resultSet = stmt.executeQuery();

	  if (resultSet.next()) {
		int userId = resultSet.getInt("userId");
		SessionManager sessionManager = SessionManager.getInstance();
		
		if (sessionManager.isLoggedIn(userId)) {
		  sessionId = sessionManager.getSessionId(userId);
		} else {
		  sessionId = sessionManager.createSession(userId);
		}
	  }
	} catch (SQLException ex) {
	  System.err.println(ex);
	} catch (NamingException ex) {
	  System.err.println(ex);
	} finally {
	  DbUtils.closeQuietly(resultSet);
	  DbUtils.closeQuietly(stmt);
	  DbUtils.closeQuietly(connection);
	}

	return sessionId;
  }
  
  /**
   * Performs logout operation
   * @param sessionId the session id corresponding to the logged in user
   * @return true if the operation is successful, false otherwise
   */
  public boolean logout(String sessionId) {
	return SessionManager.getInstance().deleteSession(sessionId);
  }

  // private constructor to prevent instantiation
  private AuthenticationManager() {
  }
}
