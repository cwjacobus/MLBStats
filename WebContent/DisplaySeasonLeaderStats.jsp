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
	<h3>${headerDisplayText}</h3>
	<table>
	<c:forEach var="mlbSeasonLeaderStats" items="${mlbSeasonLeaderStatsList}" >
		<tr>
		<td>${mlbSeasonLeaderStats.playerName}</td><td>${mlbSeasonLeaderStats.teamName}</td>
		<c:choose>
			<c:when test="${mlbSeasonLeaderStats.intStat != null}">
				<td>${mlbSeasonLeaderStats.intStat}</td>
			</c:when>
			<c:when test="${mlbSeasonLeaderStats.statType == 'EARNED_RUN_AVERAGE'}">
				<td><fmt:formatNumber type="number" maxIntegerDigits="1" minFractionDigits="3" value="${mlbSeasonLeaderStats.doubleStat}" /></td>
			</c:when>
			<c:when test="${mlbSeasonLeaderStats.statType == 'BATTING_AVERAGE'}">
				<td><fmt:formatNumber type="number" maxIntegerDigits="0" minFractionDigits="3" value="${mlbSeasonLeaderStats.doubleStat}" /></td>
			</c:when>
			<c:otherwise> <!-- INNINGS_PITCHED -->
				<td>${mlbSeasonLeaderStats.doubleStat}</td>
			</c:otherwise> 	
		</c:choose>
		</tr>
	</c:forEach>
	</table>
</body>
</html>