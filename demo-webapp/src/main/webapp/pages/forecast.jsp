<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<c:set var="city">
  <s:property value="city"/> 
</c:set>

<c:set var="country">
  <s:property value="country"/> 
</c:set>

<!doctype html>
<html>
<head>
  <title>Weather Forecast</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Weather forecast for ${city}, ${country}</h3>
  
	  <c:if test="${not empty requestScope.forecastHtml}">
	    <c:out value="${requestScope.forecastHtml}" escapeXml="false"/>
	  </c:if>
	  
	  <p class="homelink">Please go back to <a href="/${webappName}">Home page</a></p>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>