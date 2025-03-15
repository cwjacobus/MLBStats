<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>

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
			<c:forEach items="${multipleBattersList}" var="player">
  				<li><a href='/MLBStats/getPlayerStats?mlbPlayerId=${player.mlbPlayerId}&playerType=batter'>${player.playerName}</a></li>	
			</c:forEach>
			</ul>
		</c:if>
		<c:if test="${batter == 'false'}">
			<ul>
			<c:forEach items="${multiplePitchersList}" var="pitcher">
  				<li><a href='/MLBStats/getPlayerStats?mlbPlayerId=${pitcher.mlbPlayerId}&playerType=pitcher'>${pitcher.playerName}</a></li>	
			</c:forEach>
			</ul>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:if test="${teamMode == 'true' || (batter == 'true' && batterName != null && batterName != '')}">
			<c:choose>
			<c:when test="${teamMode == 'true'}">
				<h3>${year} TBD Team Name</h3>
			</c:when>
			<c:otherwise>
				<h3>${batterName}</h3>
			</c:otherwise>
			</c:choose>
			<table>
			<tr>
			<c:choose>
			<c:when test = "${teamMode == 'true'}">
				<th>name</th>
			</c:when>
			<c:otherwise>
				<th>year</th><th>team</th>
			</c:otherwise>
			</c:choose>
			<th>ab</th><th>h</th><th>ba</th><th>dbl</th><th>tpl</th><th>hr</th><th>bb</th><th>k</th><th>hbp</th><th>r</th><th>rbi</th>
			<th>sb</th><th>pa</th><th>cs</th><th>pos</th></tr>
			<c:forEach var="mlbBattingStats" items="${mlbBattingStatsList}" >
				<tr>
				<c:choose>
				<c:when test = "${teamMode == 'true'}">
					<td>${mlbBattingStats.playerName}</td>
				</c:when>
				<c:otherwise>
					<td>${mlbBattingStats.year}</td>
					<td>${mlbBattingStats.teamName}</td>
				</c:otherwise>
				</c:choose>
				<td>${mlbBattingStats.atBats}</td>
				<td>${mlbBattingStats.hits}</td>
				<td><fmt:formatNumber type="number" pattern=".###" value="${mlbBattingStats.battingAverage}" minFractionDigits="3"/></td>
				<td>${mlbBattingStats.doubles}</td>
				<td>${mlbBattingStats.triples}</td>
				<td>${mlbBattingStats.homeRuns}</td>
				<td>${mlbBattingStats.walks}</td>
				<td>${mlbBattingStats.strikeOuts}</td>
				<td>${mlbBattingStats.hitByPitch}</td>
				<td>${mlbBattingStats.runs}</td>
				<td>${mlbBattingStats.rbis}</td>
				<td>${mlbBattingStats.stolenBases}</td>
				<td>${mlbBattingStats.plateAppearances}</td>
				<td>${mlbBattingStats.caughtStealing}</td>
				<td>${mlbBattingStats.pos}</td>
				</tr>
			</c:forEach>
			</table>
		</c:if>
		<c:if test="${teamMode == 'true' || (batter == 'false' && pitcherName != null && pitcherName != '')}">
			<c:if test="${teamMode == 'false'}">
				<h3>${pitcherName}</h3>
			</c:if>
			<table>
			<tr>
			<c:choose>
			<c:when test = "${teamMode == 'true'}">
				<th>name</th>
			</c:when>
			<c:otherwise>
				<th>year</th><th>team</th>
			</c:otherwise>
			</c:choose>
			<th>ip</th><th>r</th><th>er</th><th>era</th><th>k</th><th>hr</th><th>sb</th><th>hb</th><th>h</th>
			<th>hd</th><th>sv</th><th>bs</th><th>gs</th><th>bk</th><th>wp</th><th>sf</th><th>bf</th><th>w</th><th>l</th></tr>
			<c:forEach var="mlbPitchingStats" items="${mlbPitchingStatsList}" >
				<tr>
				<c:choose>
				<c:when test = "${teamMode == 'true'}">
					<td>${mlbPitchingStats.playerName}</td>
				</c:when>
				<c:otherwise>
					<td>${mlbPitchingStats.year}</td>
					<td>${mlbPitchingStats.teamName}</td>
				</c:otherwise>
				</c:choose>
				<td>${mlbPitchingStats.inningsPitched}</td>
				<td>${mlbPitchingStats.runsAllowed}</td>
				<td>${mlbPitchingStats.earnedRunsAllowed}</td>
				<td><fmt:formatNumber type="number" pattern="#.##" value="${mlbPitchingStats.earnedRunAverage}" minFractionDigits="2"/></td>
				<td>${mlbPitchingStats.strikeouts}</td>
				<td>${mlbPitchingStats.homeRunsAllowed}</td>
				<td>${mlbPitchingStats.stolenBasesAllowed}</td>
				<td>${mlbPitchingStats.hitBatters}</td>
				<td>${mlbPitchingStats.hitsAllowed}</td>
				<td>${mlbPitchingStats.holds}</td>
				<td>${mlbPitchingStats.saves}</td>
				<td>${mlbPitchingStats.blownSaves}</td>
				<td>${mlbPitchingStats.gamesStarted}</td>
				<td>${mlbPitchingStats.balks}</td>
				<td>${mlbPitchingStats.wildPitches}</td>
				<td>${mlbPitchingStats.sacrificeFlies}</td>
				<td>${mlbPitchingStats.battersFaced}</td>
				<td>${mlbPitchingStats.wins}</td>
				<td>${mlbPitchingStats.losses}</td>
				</tr>
			</c:forEach>
			</table>
		</c:if>
	</c:otherwise>
	</c:choose>
</body>
</html>