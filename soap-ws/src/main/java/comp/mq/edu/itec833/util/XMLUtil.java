package comp.mq.edu.itec833.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

/**
 * This class is a collection of various utility methods related to XML parsing
 * 
 * @author Shamim Ahmed
 * 
 */
public class XMLUtil {
  /**
   * evaluates the given xpath expression in the given context
   * @param xpathExpression the xpath expression
   * @param context the context of evaluating the xpath expression
   * @param namespaces required namespaces
   * @return the value of evaluating the xpath expression in the given context
   * @throws JDOMException if an error occurs while evaluating the xpath expression
   * @throws IllegalArgumentException if the document is null, or the xpath expression is blank
   */
  public static String getValue(String xpathExpression, Object context, Namespace... namespaces)
	  throws JDOMException {
	if (StringUtils.isBlank(xpathExpression)) {
	  throw new IllegalArgumentException("the xpath expression cannot be blank");
	}
	
	if (context == null) {
	  throw new IllegalArgumentException("context cannot be null");
	}

	XPath xpath = XPath.newInstance(xpathExpression);

	for (Namespace ns : namespaces) {
	  xpath.addNamespace(ns);
	}

	return xpath.valueOf(context);
  }
  
  /**
   * returns the result of evaluating the given xpath expression as a list
   * @param xpathExpression the given xpath expression
   * @param context the context in which the xpath expression has to be evaluated
   * @param namespaces the required namespaces
   * @return a <code>List</code> of <code>Element</code> objects
   * @throws JDOMException if a problem occurs while evaluating the xpath expression
   * @throws IllegalArgumentException if the xpath expression is blank, or the context is null
   */
  public static List<Element> getValueAsList(String xpathExpression, Object context, Namespace... namespaces)
	  throws JDOMException {
	if (StringUtils.isBlank(xpathExpression)) {
	  throw new IllegalArgumentException("The xpath expression cannot be blank");
	}
	
	if (context == null) {
	  throw new IllegalArgumentException("The context cannot be null");
	}
	
	XPath xpath = XPath.newInstance(xpathExpression);
	
	for (Namespace ns : namespaces) {
	  xpath.addNamespace(ns);
	}
	
	@SuppressWarnings("unchecked")
	List<Element> resultList = xpath.selectNodes(context); 
	return resultList;
  }
  
  // private constructor to prevent instantiation
  private XMLUtil() {
  }
}

