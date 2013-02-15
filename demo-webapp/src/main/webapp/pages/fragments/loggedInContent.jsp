<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="http://www.example.com/custom-tags" %>

<custom:getWebappName var="webappName"/>

<div id="content">  
  <h3>Available Options</h3>
  <ul>
    <li><a href="/${webappName}/demo/default/displayTags.action">Display tags</a></li>
    <li><a href="/${webappName}/demo/default/showAddTagForm.action">Add a tag</a></li>
    <li><a href="/${webappName}/demo/default/showRemoveTagForm.action">Remove a tag</a></li>
    <li><a href="/${webappName}/demo/default/showNewsSearchForm.action">Fetch News from the Guardian</a></li>
    <li><a href="/${webappName}/demo/default/showVideoSearchForm.action">Fetch Videos from Youtube</a></li>
    <li><a href="/${webappName}/demo/default/showTweetSearchForm.action">Fetch Tweets</a></li>
    <li><a href="/${webappName}/demo/default/showPhotoSearchForm.action">Fetch Photos from Flickr</a></li>
    <li><a href="/${webappName}/demo/default/showForecastForm.action">Fetch weather forecast</a></li>          
  </ul>
</div>
<div id="login">
  <h3>Welcome, ${sessionScope.userName}</h3>
  <form action="/${webappName}/demo/default/logout.action" method="post" class="inputform">
    <input type="submit" value="Logout"/>
  </form>
</div>