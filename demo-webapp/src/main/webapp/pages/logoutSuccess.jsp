<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<!doctype html>
<html>
<head>
  <title>Logout Successful</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Logout was successful.</h3>
    <p class="homelink">Please go back to <a href="/${webappName}">Home page</a></p>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>

 