<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!doctype html>

<html>
<head>
  <title>Tweet Search</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Tweet search</h3>
	  <p>All search results will be based on the tags that you have already specified</p>
	  <s:form action="searchTweets">
	    <s:textfield name="max" label="Max no of results per tag"/>
	    <s:submit/>
	  </s:form>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>