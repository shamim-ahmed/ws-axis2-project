<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<c:set var="tag">
  <s:property value="tag"/>
</c:set>

<!doctype html>
<html>
<head>
  <title>Tag Added Successfully</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Tag ${tag} was added successfully.</h3>
    <p class="homelink">Please go back to <a href="/${webappName}">Home Page</a></p>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>