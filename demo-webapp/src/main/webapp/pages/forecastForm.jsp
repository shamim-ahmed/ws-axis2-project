<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!doctype html>
<html>
<head>
  <title>Forecast Form</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Forecast Form</h3>  
	  <s:form action="fetchForecast" method="POST">
	    <s:textfield name="city" label="City"/>
	    <s:textfield name="country" label="Country"/>
	    <s:submit/>
	  </s:form> 
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>