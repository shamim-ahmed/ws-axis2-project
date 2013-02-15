<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!doctype html>

<html>
<head>
  <title>Registration Form</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Registration Form</h3>
	  <s:form action="register" method="POST">
	    <s:textfield name="userName" label="User Name"/>
	    <s:password name="password" label="Password"/>
	    <s:submit/>
	  </s:form>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>
