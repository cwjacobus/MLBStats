<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<title>MLB Player Stats</title>
</head>
<body>
	<h1>MLB Player Stats</h1>
	<c:choose>
	<c:when test="${!(multipleBattersList == null && multiplePitchersList == null)}">
		Multiple players found:
		<c:if test="${multipleBattersList != null}">
			<ul>
			<c:forEach items="${multipleBattersList}" var="player">
  				<li><a href='/MLBStats/getPlayerStats?mlbPlayerId=${player.mlbPlayerId}&playerType=batter'>${player.playerName}</a></li>	
			</c:forEach>
			</ul>
		</c:if>
		<c:if test="${multiplePitchersList != null}">
			<ul>
			<c:forEach items="${multiplePitchersList}" var="pitcher">
  				<li><a href='/MLBStats/getPlayerStats?mlbPlayerId=${pitcher.mlbPlayerId}&playerType=pitcher'>${pitcher.playerName}</a></li>	
			</c:forEach>
			</ul>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:if test="${(fn:length(sessionScope.mlbBattingStatsList) > 0) && (teamId != null || playerType == 'batter')}">
			<c:choose>
				<c:when test="${teamDisplayName != null}">
					<h3>${year} ${teamDisplayName}</h3>
				</c:when>
				<c:otherwise>
					<h3>${sessionScope.mlbBattingStatsList[0].playerName}</h3>
				</c:otherwise>
			</c:choose>
			<table>
			<tr>
			<c:choose>
				<c:when test = "${teamDisplayName != null}">
					<th>name</th>
					<c:set var="sortURL" value="/MLBStats/getPlayerStats?year=${year}&teamId=${teamId}&teamMode=true&battingSortType="/>
				</c:when>
				<c:otherwise>
					<th>year</th><th>team</th>
					<c:set var="sortURL" value="/MLBStats/getPlayerStats?mlbPlayerId=${sessionScope.mlbBattingStatsList[0].mlbPlayerId}&playerType=batter&battingSortType="/>
				</c:otherwise>
			</c:choose>
			<th><a href='${sortURL}AB'>ab</a></th>
			<th><a href='${sortURL}H'>h</a></th>
			<th><a href='${sortURL}BA'>ba</a></th>
			<th><a href='${sortURL}DB'>db</a></th>
			<th><a href='${sortURL}TR'>tr</a></th>
			<th><a href='${sortURL}HR'>hr</a></th>
			<th><a href='${sortURL}BB'>bb</a></th>
			<th><a href='${sortURL}K'>k</a></th>
			<th><a href='${sortURL}HBP'>hbp</a></th>
			<th><a href='${sortURL}R'>r</a></th>
			<th><a href='${sortURL}RBI'>rbi</a></th>
			<th><a href='${sortURL}SB'>sb</a></th>
			<th><a href='${sortURL}PA'>pa</a></th>
			<th><a href='${sortURL}CS'>cs</a></th>
			<th>pos</th></tr>
			<c:forEach var="mlbBattingStats" items="${sessionScope.mlbBattingStatsList}" >
				<tr>
				<c:choose>
					<c:when test = "${teamDisplayName != null}">
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
			<br><br>
		</c:if>
		<c:if test="${(fn:length(sessionScope.mlbPitchingStatsList) > 0) && (teamId != null || playerType == 'pitcher')}">
			<c:if test="${teamDisplayName == null}">
				<h3>${sessionScope.mlbPitchingStatsList[0].playerName}</h3>
			</c:if>
			<table>
			<tr>
			<c:choose>
				<c:when test = "${teamDisplayName != null}">
					<th>name</th>
					<c:set var="sortURL" value="/MLBStats/getPlayerStats?year=${year}&teamId=${teamId}&teamMode=true&pitchingSortType="/>
				</c:when>
				<c:otherwise>
					<th>year</th><th>team</th>
					<c:set var="sortURL" value="/MLBStats/getPlayerStats?mlbPlayerId=${sessionScope.mlbPitchingStatsList[0].mlbPlayerId}&playerType=pitcher&pitchingSortType="/>
				</c:otherwise>
			</c:choose>
			<th><a href='${sortURL}IP'>ip</a></th>
			<th><a href='${sortURL}R'>r</a></th>
			<th><a href='${sortURL}ER'>er</a></th>
			<th><a href='${sortURL}ERA'>era</a></th>
			<th><a href='${sortURL}K'>k</a></th>
			<th><a href='${sortURL}HR'>hr</a></th>
			<th><a href='${sortURL}SB'>sb</a></th>
			<th><a href='${sortURL}HB'>hb</a></th>
			<th><a href='${sortURL}H'>h</a></th>
			<th><a href='${sortURL}HD'>hd</a></th>
			<th><a href='${sortURL}SV'>sv</a></th>
			<th><a href='${sortURL}BS'>bs</a></th>
			<th><a href='${sortURL}GS'>gs</a></th>
			<th><a href='${sortURL}BK'>bk</a></th>
			<th><a href='${sortURL}WP'>wp</a></th>
			<th><a href='${sortURL}SF'>sf</a></th>
			<th><a href='${sortURL}BF'>bf</a></th>
			<th><a href='${sortURL}W'>w</a></th>
			<th><a href='${sortURL}L'>l</a></th>
			</tr>
			<c:forEach var="mlbPitchingStats" items="${sessionScope.mlbPitchingStatsList}" >
				<tr>
				<c:choose>
					<c:when test = "${teamDisplayName != null}">
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