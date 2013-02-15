<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<!doctype html>
<html>
<head>
  <title>The Portal Service Demo</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <c:choose>
      <c:when test="${not empty sessionScope.userName}">
        <jsp:include page="/pages/fragments/loggedInContent.jsp"/>
      </c:when>
      <c:otherwise>
        <jsp:include page="/pages/fragments/loggedOutContent.jsp"/>
      </c:otherwise>
    </c:choose>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>