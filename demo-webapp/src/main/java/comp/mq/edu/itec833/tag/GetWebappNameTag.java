package comp.mq.edu.itec833.tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * A jsp tag that retrieves webapp name from JSP file
 * @author Shamim Ahmed
 *
 */
public class GetWebappNameTag extends SimpleTagSupport {
  private String var;

  public String getVar() {
	return var;
  }

  public void setVar(String var) {
	this.var = var;
  }

  @Override
  public void doTag() {
	PageContext pageContext = (PageContext) getJspContext();
	String webappName = pageContext.getServletContext().getServletContextName();
	pageContext.setAttribute(var, webappName);

  }
}
