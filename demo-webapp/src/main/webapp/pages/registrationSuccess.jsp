<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<<custom:getWebappName var="webappName"/>

<!doctype html>
<html>
<head>
  <title>Registration Successful</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>You have successfully registered.</h3>
    <p class="homelink">Please go back to <a href="/${webappName}">Home page</a></p>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>