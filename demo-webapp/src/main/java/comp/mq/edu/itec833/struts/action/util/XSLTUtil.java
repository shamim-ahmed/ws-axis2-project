package comp.mq.edu.itec833.struts.action.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import comp.mq.edu.itec833.util.ContentType;

/**
 * This class is related to XSLT conversion
 * @author Shamim Ahmed
 *
 */
public class XSLTUtil {
  private static final String PATH_TO_NEWS_XSLT = "/comp/mq/edu/itec833/struts/action/util/news.xsl";
  private static final String PATH_TO_TWEET_XSLT = "/comp/mq/edu/itec833/struts/action/util/tweet.xsl";
  private static final String PATH_TO_VIDEO_XSLT = "/comp/mq/edu/itec833/struts/action/util/video.xsl";
  private static final String PATH_TO_FORECAST_XSLT = "/comp/mq/edu/itec833/struts/action/util/forecast.xsl";
  private static final String PATH_TO_PHOTO_XSLT = "/comp/mq/edu/itec833/struts/action/util/flickr.xsl";

  /**
   * transforms the input XML to HTML using the right style sheet
   * @param inputXml the input XML
   * @param type the type of 
   * @return the result HTML
   */
  public static String transform(String inputXml, ContentType type) {
	String result = "";
	
	switch (type) {
	  case NEWS:
		result = applyTransformation(inputXml, PATH_TO_NEWS_XSLT);
		break;
	  case TWEET:
		result = applyTransformation(inputXml, PATH_TO_TWEET_XSLT);
		break;
	  case VIDEO:
		result = applyTransformation(inputXml, PATH_TO_VIDEO_XSLT);
		break;
	  case FORECAST:
		result = applyTransformation(inputXml, PATH_TO_FORECAST_XSLT);
		break;
	  case PHOTO:
		result = applyTransformation(inputXml, PATH_TO_PHOTO_XSLT);
		break;
	}
	
	return result;
  }

  private static String applyTransformation(String inputXml, String pathToXsl) {
	String htmlString = "";

	if (StringUtils.isBlank(inputXml)) {
	  return htmlString;
	}

	InputStream xslInputStream = null;
    InputStream in = null;
    ByteArrayOutputStream out = null;
    
	try {
	  xslInputStream = XSLTUtil.class.getResourceAsStream(pathToXsl);
	  in = new ByteArrayInputStream(inputXml.getBytes());
	  out = new ByteArrayOutputStream();
	  TransformerFactory factory = TransformerFactory.newInstance();
	  Transformer transformer = factory.newTransformer(new StreamSource(xslInputStream));

	  Source xmlSource = new StreamSource(in);
	  Result outputTarget = new StreamResult(out);
	  transformer.transform(xmlSource, outputTarget);
	  htmlString = out.toString();
	} catch (TransformerException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(xslInputStream);
	  IOUtils.closeQuietly(in);
	  IOUtils.closeQuietly(out);
	}

	return htmlString;
  }

  // private constructor to prevent instantiation
  private XSLTUtil() {
  }
} 
