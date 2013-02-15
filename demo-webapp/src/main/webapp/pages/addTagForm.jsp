<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!doctype html>
<html>
<head>
  <title>Add a Tag</title>
  <jsp:include page="/pages/fragments/headResources.jsp"/>
</head>
<body>
  <jsp:include page="/pages/fragments/header.jsp"/>
  
  <div id="main">
    <h3>Add a Tag</h3>
    <s:form action="addTag">
      <s:textfield name="tag" label="Tag"/>
      <s:submit/>
    </s:form>
  </div>
  
  <jsp:include page="/pages/fragments/footer.jsp"/>
</body>
</html>
