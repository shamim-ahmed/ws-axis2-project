package comp.mq.edu.itec833.manager;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class is used to retrieve database connections configured via JNDI
 * @author Shamim Ahmed
 *
 */
public class DBConnectionManager {
  private static final String JNDI_RESOURCE_NAME = "java:/comp/env/jdbc/itec833";

  /**
   * static method for retrieving database connection 
   * @return the database connection
   * @throws NamingException
   * @throws SQLException
   */
  public static Connection getConnection() throws NamingException, SQLException {
	Context initContext = new InitialContext();	
	DataSource dataSource = (DataSource) initContext.lookup(JNDI_RESOURCE_NAME);
	Connection connection = dataSource.getConnection();
	return connection;
  }

  // private constructor to prevent instantiation
  private DBConnectionManager() {
  }
}
