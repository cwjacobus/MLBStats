<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<title>Season Leaders</title>
<style>
	body {
  		font-family: Arial, serif;
  		background-color: lightgrey;
  		color: darkred;
	}
	th, a {
  		color: black;
	}
</style>
</head>
<body>
	<h1>Season Leaders</h1>
	
	<table>
	<c:forEach var="mlbSeasonLeaderStats" items="${mlbSeasonLeaderStatsList}" >
		<tr><td>${mlbSeasonLeaderStats.playerName}</td><td>${mlbSeasonLeaderStats.teamName}</td><td>${mlbSeasonLeaderStats.intStat}</td></tr>
	</c:forEach>
	</table>
</body>
</html>