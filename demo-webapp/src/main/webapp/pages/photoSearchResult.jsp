<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<!doctype html>
<html>
<head>
  <title>Photo Search Result</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h1>Photo search result</h1>
	  <c:if test="${not empty requestScope.photoHtml}">
	    <div class="container">
	      <c:out value="${requestScope.photoHtml}" escapeXml="false"/>
	    </div>
	  </c:if>
	  
	  <p class="homelink">Please go back to <a href="/${webappName}">Home page</a></p>
  </div>
    
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>