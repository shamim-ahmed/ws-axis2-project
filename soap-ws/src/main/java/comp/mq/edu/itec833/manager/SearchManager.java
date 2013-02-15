package comp.mq.edu.itec833.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import comp.mq.edu.itec833.content.FlickrSearchManager;
import comp.mq.edu.itec833.content.GuardianSearchManager;
import comp.mq.edu.itec833.content.TwitterSearchManager;
import comp.mq.edu.itec833.content.YouTubeSearchManager;
import comp.mq.edu.itec833.util.ContentType;
import comp.mq.edu.itec833.util.DateConverter;
import comp.mq.edu.itec833.util.XMLUtil;

/**
 * This class initiates a search using the API of an external content provider. It also parses the XML result and
 * restructures it to a standard format that the client can understand.
 * 
 * @author Poopak Alaeifar
 *
 */
public class SearchManager {
  private static final SearchManager INSTANCE = new SearchManager();
  private static final Namespace ATOM_NAMESPACE = Namespace.getNamespace("a", "http://www.w3.org/2005/Atom");

  public static SearchManager getInstance() {
	return INSTANCE;
  }

  /**
   * Initiate a tweet search
   * @param maxPerTag max number of results per keyword
   * @param sessionId the sessonId of the currently logged in user
   * @return the XML 
   */
  public String getTweets(int maxPerTag, String sessionId) {
	String mergedResult = "";
	List<String> tagList = TagManager.getInstance().getTagsAsList(sessionId);

	Document resultDocument = new Document();
	Element resultRootElement = new Element("results");
	resultDocument.setRootElement(resultRootElement);
	ByteArrayInputStream in = null;
	ByteArrayOutputStream out = null;
	
	try {
	  for (String tag : tagList) {
		Element groupElement = new Element("group");
		groupElement.setAttribute("tag", tag);
		resultRootElement.addContent(groupElement);

		String result = TwitterSearchManager.getInstance().search(tag, maxPerTag);
		Document doc = getXmlDocument(result);
		
		if (doc == null) {
		  continue;
		}
		
		List<Element> entryList = XMLUtil.getValueAsList("/a:feed/a:entry", doc.getRootElement(), ATOM_NAMESPACE);

		for (Element entry : entryList) {
		  String title = XMLUtil.getValue("a:title", entry, ATOM_NAMESPACE);
		  String tweetUrl = XMLUtil.getValue("a:link[@rel='alternate']/@href", entry, ATOM_NAMESPACE);
		  String publishDate = XMLUtil.getValue("a:published", entry, ATOM_NAMESPACE);
		  String authorName = XMLUtil.getValue("a:author/a:name", entry, ATOM_NAMESPACE);
		  String authorUrl = XMLUtil.getValue("a:author/a:uri", entry, ATOM_NAMESPACE);
		  String authorPicUrl = XMLUtil.getValue("a:link[@rel='image']/@href", entry, ATOM_NAMESPACE);
		  
		  // the elements of the result tree
		  Element tweetElement = new Element("tweet");

		  Element titleElement = new Element("title");
		  titleElement.addContent(title);
		  tweetElement.addContent(titleElement);

		  Element urlElement = new Element("url");
		  urlElement.addContent(tweetUrl);
		  tweetElement.addContent(urlElement);
		  
		  Element publishDateElement = new Element("publishDate");
		  publishDateElement.addContent(DateConverter.convertDate(publishDate, ContentType.TWEET));
		  tweetElement.addContent(publishDateElement);
		  
		  Element authorElement = new Element("author");
		  Element authorNameElement = new Element("authorName");
		  authorNameElement.addContent(authorName);
		  Element authorUrlElement = new Element("authorUrl");
		  authorUrlElement.addContent(authorUrl);
		  Element authorPicUrlElement = new Element("authorPicUrl");
		  authorPicUrlElement.addContent(authorPicUrl);
		  authorElement.addContent(authorNameElement);
		  authorElement.addContent(authorUrlElement);
		  authorElement.addContent(authorPicUrlElement);
		  tweetElement.addContent(authorElement);
		  
		  groupElement.addContent(tweetElement);
		}
	  }

	  XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	  out = new ByteArrayOutputStream();
	  outputter.output(resultDocument, out);
	  mergedResult = out.toString();
	} catch (IOException ex) {
	  System.err.println(ex);
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	  IOUtils.closeQuietly(out);
	}
	
	return mergedResult;
  }

  /**
   * Initiate a video search
   * @param maxPerTag max number of results per keyword
   * @param sessionId the sessionId of the currently logged in user
   * @return an XML string representing the result
   */
  public String getVideos(int maxPerTag, String sessionId) {
	String mergedResult = "";
	List<String> tagList = TagManager.getInstance().getTagsAsList(sessionId);
	Document resultDocument = new Document();
	Element resultRootElement = new Element("results");
	resultDocument.setRootElement(resultRootElement);
	
	ByteArrayInputStream in = null;
	ByteArrayOutputStream out = null;

	try {
	  for (String tag : tagList) {
		Element groupElement = new Element("group");
		groupElement.setAttribute("tag", tag);
		resultRootElement.addContent(groupElement);

		String result = YouTubeSearchManager.getInstance().search(tag, maxPerTag);
		Document doc = getXmlDocument(result);
		
		if (doc == null) {
		  continue;
		}
		
		List<Element> entryList = XMLUtil.getValueAsList("/a:feed/a:entry", doc.getRootElement(), ATOM_NAMESPACE);

		for (Element entry : entryList) {
		  String title = XMLUtil.getValue("a:title[@type='text']", entry, ATOM_NAMESPACE);
		  String link = XMLUtil.getValue("a:link[@rel='alternate']/@href", entry, ATOM_NAMESPACE);
		  String publishDate = XMLUtil.getValue("a:published", entry, ATOM_NAMESPACE);
		  String idUrl = XMLUtil.getValue("a:id", entry, ATOM_NAMESPACE);
		  String id = idUrl.substring(idUrl.lastIndexOf("/") + 1);
		  String authorName = XMLUtil.getValue("a:author/a:name", entry, ATOM_NAMESPACE);
		  String authorUrl = XMLUtil.getValue("a:author/a:uri", entry, ATOM_NAMESPACE);

		  Element videoElement = new Element("video");

		  Element titleElement = new Element("title");
		  titleElement.addContent(title);
		  videoElement.addContent(titleElement);
		  
		  Element idElement = new Element("id");
		  idElement.addContent(id);
		  videoElement.addContent(idElement);

		  Element urlElement = new Element("url");
		  urlElement.addContent(link);
		  videoElement.addContent(urlElement);
		  
		  Element publishDateElement = new Element("publishDate");
		  publishDateElement.addContent(DateConverter.convertDate(publishDate, ContentType.VIDEO));
		  videoElement.addContent(publishDateElement);
		  
		  Element authorElement = new Element("author");
		  Element authorNameElement = new Element("authorName");
		  authorNameElement.addContent(authorName);
		  Element authorUrlElement = new Element("authorUrl");
		  authorUrlElement.addContent(authorUrl);
		  authorElement.addContent(authorNameElement);
		  authorElement.addContent(authorUrlElement);
		  videoElement.addContent(authorElement);
		  
		  groupElement.addContent(videoElement);
		}
	  }

	  XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	  out = new ByteArrayOutputStream();
	  outputter.output(resultDocument, out);
	  mergedResult = out.toString();
	} catch (IOException ex) {
	  System.err.println(ex);
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	  IOUtils.closeQuietly(out);
	}
	
	return mergedResult;
  }

  /**
   * Initiates a news item search
   * @param maxPerTag maximum number of items per keyword
   * @param sessionId the session id corresponding to the currently logged in user
   * @return an XML string representing the result
   */
  public String getNews(int maxPerTag, String sessionId) {
	String mergedResult = "";
	List<String> tagList = TagManager.getInstance().getTagsAsList(sessionId);
	Document resultDocument = new Document();
	Element resultRootElement = new Element("results");
	resultDocument.setRootElement(resultRootElement);
	
	ByteArrayInputStream in = null;
	ByteArrayOutputStream out = null;
	
	try {
	  for (String tag : tagList) {
		Element groupElement = new Element("group");
		groupElement.setAttribute("tag", tag);
		resultRootElement.addContent(groupElement);

		String searchResult = GuardianSearchManager.getInstance().search(tag, maxPerTag);
		Document doc = getXmlDocument(searchResult);
		
		if (doc == null) {
		  continue;
		}
		
		List<Element> contentList = XMLUtil.getValueAsList("/response/results/content", doc.getRootElement());

		for (Element content : contentList) {
		  Element newsElement = new Element("news");

		  Element titleElement = new Element("title");
		  titleElement.addContent(content.getAttributeValue("web-title"));
		  newsElement.addContent(titleElement);

		  Element urlElement = new Element("url");
		  urlElement.addContent(content.getAttributeValue("web-url"));
		  newsElement.addContent(urlElement);
		  
		  Element publishDateElement = new Element("publishDate");
		  String publishDate = content.getAttributeValue("web-publication-date");
		  publishDateElement.addContent(DateConverter.convertDate(publishDate, ContentType.NEWS));
		  newsElement.addContent(publishDateElement);

		  groupElement.addContent(newsElement);
		}
	  }

	  XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	  out = new ByteArrayOutputStream();
	  outputter.output(resultDocument, out);
	  mergedResult = out.toString();
	} catch (IOException ex) {
	  System.err.println(ex);
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	  IOUtils.closeQuietly(out);
	}

	return mergedResult;
  }
  
  /**
   * Initiates a photo search
   * @param maxPerTag maximum number of items per keyword
   * @param sessionId the session id corresponding to the currently logged in user
   * @return an XML string representing the result
   */
  public String getPhotos(int maxPerTag, String sessionId) {
	String mergedResult = "";
	List<String> tagList = TagManager.getInstance().getTagsAsList(sessionId);
	Document resultDocument = new Document();
	Element resultRootElement = new Element("results");
	resultDocument.setRootElement(resultRootElement);
	
	ByteArrayInputStream in = null;
	ByteArrayOutputStream out = null;
	
	try {
	  for (String tag : tagList) {
		Element groupElement = new Element("group");
		groupElement.setAttribute("tag", tag);
		resultRootElement.addContent(groupElement);

		String searchResult = FlickrSearchManager.getInstance().search(tag, maxPerTag);
		Document doc = getXmlDocument(searchResult);
		
		if (doc == null) {
		  continue;
		}
		
		List<Element> photoList = XMLUtil.getValueAsList("/rsp/photos/photo", doc.getRootElement());

		for (Element photo : photoList) {
		  Element photoElement = new Element("photo");
		  groupElement.addContent(photoElement);

		  Element titleElement = new Element("title");
		  titleElement.addContent(photo.getAttributeValue("title"));
		  photoElement.addContent(titleElement);

		  Element urlElement = new Element("url");
		  String farm = photo.getAttributeValue("farm");
		  String server = photo.getAttributeValue("server");
		  String id = photo.getAttributeValue("id");
		  String secret = photo.getAttributeValue("secret");
		  String url = String.format("http://farm%s.staticflickr.com/%s/%s_%s.jpg", farm, server, id, secret);
		  urlElement.addContent(url);
		  photoElement.addContent(urlElement);
		  
		  String owner = photo.getAttributeValue("owner");
		  String userSearchXml = FlickrSearchManager.getInstance().userSearch(owner);
		  Document userDoc = getXmlDocument(userSearchXml);
		  
		  if (userDoc != null) {
			Element userDocRoot = userDoc.getRootElement();
			String authorName = XMLUtil.getValue("/rsp/person/username", userDocRoot);
			String authorUrl = XMLUtil.getValue("/rsp/person/profileurl", userDocRoot);
			
			Element authorElement = new Element("author");
			Element authorNameElement = new Element("authorName");
			authorNameElement.addContent(authorName);
			Element authorUrlElement = new Element("authorUrl");
			authorUrlElement.addContent(authorUrl);
			authorElement.addContent(authorNameElement);
			authorElement.addContent(authorUrlElement);
			photoElement.addContent(authorElement);
		  }
		}
	  }

	  XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	  out = new ByteArrayOutputStream();
	  outputter.output(resultDocument, out);
	  mergedResult = out.toString();
	} catch (IOException ex) {
	  System.err.println(ex);
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	  IOUtils.closeQuietly(out);
	}

	return mergedResult;
  }
  
  /**
   * constructs an XML document from an XML string
   * @param xmlString the XML string
   * @return an XML Document
   */
  private Document getXmlDocument(String xmlString) {
	Document doc = null;
	Reader in = null;
	
	try {
	  in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(xmlString.getBytes()),
		                      Charset.forName("UTF-8")));
	  SAXBuilder builder = new SAXBuilder();
	  doc = builder.build(in);
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} catch (IOException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	}
	
	return doc;
  }

  // private constructor to prevent instantiation
  private SearchManager() {
  }
}
