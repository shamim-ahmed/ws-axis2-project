<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<div id="content">
  <h3>Welcome !</h3>
  <p>The portal service allows you to save a number of tags and then search for relevant content
  based on those tags.</p>
  
  <p>At present, content can be fetched from the following sources : </p>
  
  <ul>
    <li>The Guardian</li>
    <li>Youtube</li>
    <li>Twitter</li>
    <li>Flickr</li>
  </ul>
  
  <p>The application also allows you to fetch weather forecast from an external service.</p>
</div>
<div id="login">
  <h3>Login</h3>
  <form action="/${webappName}/demo/default/login.action" method="post" class="inputform">
    <label for="username">Username : </label><input type="text" name="userName"/><br/>
    <label for="password">Password : &nbsp;</label><input type="password" name="password"/><br/>
    <input type="submit" class="submit" value="Login"/>
    <c:if test="${not empty param.error and param.error == 'auth'}">
      <div class="loginErrorMessage">Authentication failure</div>
    </c:if>
    <div class="info">
      New user ? <a href="/${webappName}/demo/default/showRegisterForm.action">Register</a>
    </div>
  </form>
</div>