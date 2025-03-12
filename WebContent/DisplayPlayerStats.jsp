<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>MLB Player Stats</title>
</head>
<body>
	<h1>MLB Player Stats</h1>
	<c:choose>
	<c:when test="${!(multipleBattersList == null && multiplePitchersList == null)}">
		Multiple players found:
		<c:if test="${batter == 'true'}">
			<ul>
			<s:iterator value="multipleBattersList" var="player">
  				<li><a href='/MLBStats/getPlayerStats?mlbPlayerId=${player.mlbPlayerId}&playerType=batter'><s:property value="#player.playerName"/></a></li>	
			</s:iterator>
			</ul>
		</c:if>
		<c:if test="${batter == 'false'}">
			<ul>
			<s:iterator value="multiplePitchersList" var="pitcher">
  				<li><a href='/MLBStats/getPlayerStats?mlbPlayerId=${pitcher.mlbPlayerId}&playerType=pitcher'><s:property value="#pitcher.playerName"/></a></li>	
			</s:iterator>
			</ul>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${batter == 'true' && batterName != null && batterName != ''}">
				<h3>${batterName}</h3>
				<table>
				<tr><th>year</th><th>pos</th><th>team</th><th>ab</th><th>h</th><th>ba</th><th>dbl</th><th>tpl</th><th>hr</th><th>bb</th><th>k</th>
				<th>hbp</th><th>r</th><th>rbi</th><th>sb</th><th>pa</th><th>cs</th></tr>
				<s:iterator value="mlbBattingStatsList" var="mlbBattingStats">
					<tr>
					<td><s:property value="#mlbBattingStats.year"/></td>
					<td><s:property value="#mlbBattingStats.pos"/></td>
					<td><s:property value="#mlbBattingStats.teamName"/></td>
					<td><s:property value="#mlbBattingStats.atBats"/></td>
					<td><s:property value="#mlbBattingStats.hits"/></td>
					<td><s:property value="#mlbBattingStats.battingAverage"/></td>
					<td><s:property value="#mlbBattingStats.doubles"/></td>
					<td><s:property value="#mlbBattingStats.triples"/></td>
					<td><s:property value="#mlbBattingStats.homeRuns"/></td>
					<td><s:property value="#mlbBattingStats.walks"/></td>
					<td><s:property value="#mlbBattingStats.strikeOuts"/></td>
					<td><s:property value="#mlbBattingStats.hitByPitch"/></td>
					<td><s:property value="#mlbBattingStats.runs"/></td>
					<td><s:property value="#mlbBattingStats.rbis"/></td>
					<td><s:property value="#mlbBattingStats.stolenBases"/></td>
					<td><s:property value="#mlbBattingStats.plateAppearances"/></td>
					<td><s:property value="#mlbBattingStats.caughtStealing"/></td>
					</tr>
				</s:iterator>
				</table>
			</c:when>
			<c:when test="${batter == 'false' && pitcherName != null && pitcherName != ''}">
				<h3>${pitcherName}</h3>
			</c:when>
			<c:otherwise>
				No player found
			</c:otherwise>
		</c:choose>
	</c:otherwise>
	</c:choose>
</body>
</html>