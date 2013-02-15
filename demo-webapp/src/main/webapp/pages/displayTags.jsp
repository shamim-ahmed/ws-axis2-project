<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<!doctype html>
<html>
<head>
  <title>Tag List</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <c:if test="${not empty sessionScope.userName}">
      <h3>Tag List for ${sessionScope.userName}</h3>
      <c:choose>
        <c:when test="${not empty requestScope.tagList}">
          <ul>
            <c:forEach var="tag" items="${requestScope.tagList}">
              <li><c:out value="${tag}"/></li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <p>You haven't selected any tag yet.</p>
        </c:otherwise>
      </c:choose>
    </c:if>
    
    <p class="homelink">Please go back to <a href="/${webappName}">Home page</</a></p>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>
