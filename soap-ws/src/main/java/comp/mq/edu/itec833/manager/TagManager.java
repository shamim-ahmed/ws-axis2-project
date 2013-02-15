package comp.mq.edu.itec833.manager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This class performs the database operations related to tags (keywords)
 * @author Shamim Ahmed
 *
 */
public class TagManager {
  private static final TagManager INSTANCE = new TagManager();
  private static final String TAG_SELECT_SQL = "SELECT tagNames FROM Tags where userId=?";
  private static final String TAG_INSERT_SQL = "INSERT INTO Tags(userId, tagNames) values (?, ?)";
  private static final String TAG_UPDATE_SQL = "UPDATE Tags SET tagNames=? WHERE userId=?";
  private static final String TAG_DELETE_SQL = "DELETE FROM Tags WHERE userId=?";
  private static final String SEPARATOR = ",";
  private static final String TAG_NAMES_COLUMN = "tagNames";

  public static TagManager getInstance() {
	return INSTANCE;
  }

  /**
   * Adds a particular tag to a user's profile information
   * @param tag the keyword
   * @param sessionId the session id corresponding to the currently logged in user
   * @return true if successful, false otherwise
   */
  public boolean addTag(String tag, String sessionId) {
	if (StringUtils.isBlank(tag) || StringUtils.isBlank(sessionId)) {
	  return false;
	}

	tag = tag.toLowerCase();

	if (tag.indexOf(SEPARATOR.charAt(0)) != -1) {
	  return false;
	}

	int userId = SessionManager.getInstance().getUserId(sessionId);

	if (userId == -1) {
	  return false;
	}

	boolean result = false;
	Connection connection = null;
	PreparedStatement queryStmt = null;
	PreparedStatement insertStmt = null;
	PreparedStatement updateStmt = null;
	ResultSet resultSet = null;

	try {
	  connection = DBConnectionManager.getConnection();
	  queryStmt = connection.prepareStatement(TAG_SELECT_SQL);
	  insertStmt = connection.prepareStatement(TAG_INSERT_SQL);
	  updateStmt = connection.prepareStatement(TAG_UPDATE_SQL);
	  queryStmt.setInt(1, userId);
	  resultSet = queryStmt.executeQuery();

	  if (resultSet.next()) {
		String tagNames = resultSet.getString(TAG_NAMES_COLUMN);

		if (!containsTag(tagNames, tag)) {
		  tagNames += SEPARATOR + tag;
		  updateStmt.setString(1, tagNames);
		  updateStmt.setInt(2, userId);
		  updateStmt.execute();
		  result = true;
		}
	  } else {
		insertStmt.setInt(1, userId);
		insertStmt.setString(2, tag);
		insertStmt.execute();
		result = true;
	  }
	} catch (SQLException ex) {
	  System.err.println(ex);
	} catch (NamingException ex) {
	  System.err.println(ex);
	} finally {
	  DbUtils.closeQuietly(resultSet);
	  DbUtils.closeQuietly(queryStmt);
	  DbUtils.closeQuietly(insertStmt);
	  DbUtils.closeQuietly(updateStmt);
	  DbUtils.closeQuietly(connection);
	}

	return result;
  }

  /**
   * Removes a particular tag to a user's profile information
   * @param tag the keyword
   * @param sessionId the session id corresponding to the currently logged in user
   * @return true if successful, false otherwise
   */
  public boolean removeTag(String tag, String sessionId) {
	if (StringUtils.isBlank(tag) || StringUtils.isBlank(sessionId)) {
	  return false;
	}

	tag = tag.toLowerCase();

	if (tag.indexOf(SEPARATOR.charAt(0)) != -1) {
	  return false;
	}

	int userId = SessionManager.getInstance().getUserId(sessionId);

	if (userId == -1) {
	  return false;
	}

	boolean result = false;
	Connection connection = null;
	PreparedStatement selectStmt = null;
	PreparedStatement updateStmt = null;
	PreparedStatement deleteStmt = null;
	ResultSet resultSet = null;

	try {
	  connection = DBConnectionManager.getConnection();
	  selectStmt = connection.prepareStatement(TAG_SELECT_SQL);
	  updateStmt = connection.prepareStatement(TAG_UPDATE_SQL);
	  deleteStmt = connection.prepareStatement(TAG_DELETE_SQL);
	  selectStmt.setInt(1, userId);
	  resultSet = selectStmt.executeQuery();

	  if (resultSet.next()) {
		String tagNames = resultSet.getString(TAG_NAMES_COLUMN);

		if (containsTag(tagNames, tag)) {
		  String updatedTagNames = removeTagFromString(tagNames, tag);

		  if (StringUtils.isNotBlank(updatedTagNames)) {
			updateStmt.setString(1, updatedTagNames);
			updateStmt.setInt(2, userId);
			updateStmt.execute();
			result = true;
		  } else {
			deleteStmt.setInt(1, userId);
			deleteStmt.execute();
			result = true;
		  }
		}
	  }
	} catch (SQLException ex) {
	  System.err.println(ex);
	} catch (NamingException ex) {
	  System.err.println(ex);
	} finally {
	  DbUtils.closeQuietly(resultSet);
	  DbUtils.closeQuietly(selectStmt);
	  DbUtils.closeQuietly(updateStmt);
	  DbUtils.closeQuietly(deleteStmt);
	  DbUtils.closeQuietly(connection);
	}

	return result;
  }

  /**
   * Retrieves a list of tags from the user's profile information
   * @param sessionId the session id corresponding to the currently logged in user
   * @return a comma-separated list of tags
   */
  public String getTags(String sessionId) {
	String tagNames = "";

	if (StringUtils.isBlank(sessionId)) {
	  return tagNames;
	}

	int userId = SessionManager.getInstance().getUserId(sessionId);

	if (userId == -1) {
	  return tagNames;
	}

	tagNames = getTagsFromDB(userId);

	return tagNames;
  }

  private String getTagsFromDB(int userId) {
	String tagNames = "";
	Connection connection = null;
	PreparedStatement stmt = null;
	ResultSet resultSet = null;

	try {
	  connection = DBConnectionManager.getConnection();
	  stmt = connection.prepareStatement(TAG_SELECT_SQL);
	  stmt.setInt(1, userId);
	  resultSet = stmt.executeQuery();

	  if (resultSet.next()) {
		tagNames = resultSet.getString(TAG_NAMES_COLUMN);
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

	return tagNames;
  }

  /**
   * Retrieves tags as a List of strings
   * @param sessionId the session id of the currently logged in user
   * @return a list of strings representing the keywords
   */
  public List<String> getTagsAsList(String sessionId) {
	List<String> resultList = Collections.emptyList();

	if (StringUtils.isBlank(sessionId)) {
	  return resultList;
	}

	int userId = SessionManager.getInstance().getUserId(sessionId);

	if (userId == -1) {
	  return resultList;
	}

	String tagNames = getTagsFromDB(userId);

	if (StringUtils.isNotBlank(tagNames)) {
	  resultList = new ArrayList<String>();

	  StringTokenizer tokenizer = new StringTokenizer(tagNames, SEPARATOR);

	  while (tokenizer.hasMoreTokens()) {
		resultList.add(tokenizer.nextToken());
	  }
	}

	return resultList;
  }

  /**
   * Returns the list of tags in XML format
   * @param sessionId the session id of the currently logged in user
   * @return the XML corresponding to the tags chosen by the user
   */
  public String getTagsAsXml(String sessionId) {
	String resultXml = "";

	List<String> tagList = getTagsAsList(sessionId);

	Document document = new Document();
	Element rootElement = new Element("tags");
	document.setRootElement(rootElement);

	for (String tag : tagList) {
	  Element tagElement = new Element("tag");
	  tagElement.setText(tag);
	  rootElement.addContent(tagElement);
	}

	ByteArrayOutputStream out = null;

	try {
	  XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	  out = new ByteArrayOutputStream();
	  outputter.output(document, out);
	  resultXml = out.toString();
	} catch (IOException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(out);
	}

	return resultXml;
  }

  /**
   * Checks if a particular tag is contained in the current tag list
   * @param tagNames a comma-separated list of current tags
   * @param tag the new keyword
   * @return true if the tag is already present, false otherwise
   */
  private boolean containsTag(String tagNames, String tag) {
	boolean result = false;
	StringTokenizer tokenizer = new StringTokenizer(tagNames, SEPARATOR);

	while (tokenizer.hasMoreTokens()) {
	  String token = tokenizer.nextToken();
	  if (token.equals(tag)) {
		result = true;
		break;
	  }
	}

	return result;
  }

  /**
   * removes a tag from a comma-separated list of tags
   * @param tagNames a comma-separated list of tags
   * @param tag the keyword
   * @return the result string after removal
   */
  private String removeTagFromString(String tagNames, String tag) {
	StringTokenizer tokenizer = new StringTokenizer(tagNames, SEPARATOR);
	StringBuilder result = new StringBuilder();

	while (tokenizer.hasMoreTokens()) {
	  String token = tokenizer.nextToken();

	  if (!token.equals(tag)) {
		result.append(token).append(SEPARATOR);
	  }
	}

	String str = result.toString();

	if (StringUtils.isNotBlank(str)) {
	  str = str.substring(0, str.length() - 1);
	}

	return str;
  }

  // private constructor to prevent instantiation
  private TagManager() {
  }
}
