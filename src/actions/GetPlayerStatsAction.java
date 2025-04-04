package actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import dao.DAO;
import data.MLBBattingStats;
import data.MLBPitchingStats;
import data.MLBTeam;

public class GetPlayerStatsAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	Map<String, Object> userSession;
	String playerType;
	String playerName;
	Integer mlbPlayerId;
	Integer teamId;
	Integer year;
	String battingSortType;
	String pitchingSortType;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		ValueStack stack = ActionContext.getContext().getValueStack();
	    Map<String, Object> context = new HashMap<String, Object>();
	    if (userSession == null || userSession.size() == 0) {
			context.put("errorMsg", "Session has expired!");
			stack.push(context);
			return "error";
		}
	    if (teamId == null  && playerType == null) {
        	context.put("errorMsg", "Player type is required!");
			stack.push(context);
			return "error";
        }
        if (teamId == null && (playerName == null || playerName.trim().length() == 0) && mlbPlayerId == null) {
        	context.put("errorMsg", "Player name is required!");
			stack.push(context);
			return "error";
        }
        
        if (teamId != null && (year == null || teamId == null)) {
        	context.put("errorMsg", "Year and team are required!");
			stack.push(context);
			return "error";
        }
        
		List<MLBBattingStats> mlbBattingStatsList = (List<MLBBattingStats>)userSession.get("mlbBattingStatsList");
        List<MLBPitchingStats> mlbPitchingStatsList = (List<MLBPitchingStats>)userSession.get("mlbPitchingStatsList");
        boolean batter = teamId == null && playerType.equals("batter");
        if (batter) {
        	if (battingSortType == null) {
        		mlbBattingStatsList = mlbPlayerId == null ? DAO.getMLBBattingStatsList(playerName):DAO.getMLBBattingStatsList(mlbPlayerId);
        	}
			Map<Integer, MLBBattingStats> multipleBattersMap = new HashMap<>();
			mlbBattingStatsList.stream().forEach(entry -> multipleBattersMap.put(entry.getMlbPlayerId(), entry));
			if (multipleBattersMap.size() > 1) {
				List<MLBBattingStats> multipleBattersList = new ArrayList<>(multipleBattersMap.values());
				context.put("multipleBattersList", multipleBattersList);
			}
			else if (multipleBattersMap.size() == 1) {
				if (battingSortType != null) {
					sortBattingStatsList(mlbBattingStatsList, battingSortType);
				}
				userSession.put("mlbBattingStatsList", mlbBattingStatsList);
				context.put("playerType", playerType);
			}
			else {
				context.put("errorMsg", "No stats found for: " + playerName);
				stack.push(context);
				return "error";
			}
		}
		else if (teamId == null && !batter) { // pitcher
			if (pitchingSortType == null) {
				mlbPitchingStatsList = mlbPlayerId == null ? DAO.getMLBPitchingStatsList(playerName):DAO.getMLBPitchingStatsList(mlbPlayerId);
			}
			Map<Integer, MLBPitchingStats> multiplePitchersMap = new HashMap<>();
			mlbPitchingStatsList.stream().forEach(entry -> multiplePitchersMap.put(entry.getMlbPlayerId(), entry));
			if (multiplePitchersMap.size() > 1) {
				List<MLBPitchingStats> multiplePitchersList = new ArrayList<>(multiplePitchersMap.values());
				context.put("multiplePitchersList", multiplePitchersList);
			}
			else if (multiplePitchersMap.size() == 1) {
				if (pitchingSortType != null) {
					sortPitchingStatsList(mlbPitchingStatsList, pitchingSortType);
				}
				userSession.put("mlbPitchingStatsList", mlbPitchingStatsList);
				context.put("playerType", playerType);
			}
			else {
				context.put("errorMsg", "No stats found for: " + playerName);
				stack.push(context);
				return "error";
			}
		}
		else { // team mode
			if (pitchingSortType == null && battingSortType == null) {
				mlbPitchingStatsList = DAO.getTeamMLBPitchingStatsListByYear(year, teamId);
				mlbBattingStatsList = DAO.getTeamMLBBattingStatsListByYear(year, teamId);
			}
			if (mlbPitchingStatsList.size() == 0) {
				context.put("errorMsg", "No pitching stats found for: " + year + "" + "TBD Team Name") ;
				stack.push(context);
				return "error";
			}
			else if ((mlbBattingStatsList.size() == 0)) {	
				context.put("errorMsg", "No batting stats found for: " + year + "" + "TBD Team Name");
				stack.push(context);
				return "error";
			}
			if (battingSortType != null) {
				sortBattingStatsList(mlbBattingStatsList, battingSortType);
			}
			if (pitchingSortType != null) {
				sortPitchingStatsList(mlbPitchingStatsList, pitchingSortType);
			}
			userSession.put("mlbPitchingStatsList", mlbPitchingStatsList);
			userSession.put("mlbBattingStatsList", mlbBattingStatsList);
			ArrayList<MLBTeam> allMLBTeamsList = (ArrayList<MLBTeam>)userSession.get("allMLBTeamsList");
			String teamDisplayName = null;
			Optional<MLBTeam> teamMatch = 
					allMLBTeamsList
				.stream()
				.filter((p) -> (p.getTeamId() == teamId.intValue() && p.getFirstYearPlayed().intValue() <= year.intValue()
					 && (p.getLastYearPlayed() == 0 || p.getLastYearPlayed().intValue() >= year.intValue())))
				.findAny();
			if (teamMatch.isPresent()) {
				teamDisplayName = teamMatch.get().getFullTeamName();
			}
			context.put("teamDisplayName", teamDisplayName);
			context.put("teamId", teamId);
		}
	    stack.push(context);
	    return "success";
	}
	
	public void sortBattingStatsList(List<MLBBattingStats> battersList, String type) 
    { 
			Collections.sort(battersList, new Comparator<MLBBattingStats>() { 
            public int compare(MLBBattingStats o1, MLBBattingStats o2) { 
            	if (type.equals("SB")) {
            		if (o1.getStolenBases() == o2.getStolenBases()) {
            			return 0;
            		}
            		return (o1.getStolenBases() >= o2.getStolenBases() ? -1 : 1); 
            	}
            	else if (type.equals("H")){
            		if (o1.getHits() == o2.getHits()) {
            			return 0;
            		}
            		return (o1.getHits() > o2.getHits() ? -1 : 1);
            	}
            	else if (type.equals("HR")){
            		if (o1.getHomeRuns() == o2.getHomeRuns()) {
            			return 0;
            		}
            		return (o1.getHomeRuns() > o2.getHomeRuns() ? -1 : 1);
            	}
            	else if (type.equals("RBI")){
            		if (o1.getRbis() == o2.getRbis()) {
            			return 0;
            		}
            		return (o1.getRbis() > o2.getRbis() ? -1 : 1);
            	}
            	else if (type.equals("PA")){
            		if (o1.getPlateAppearances() == o2.getPlateAppearances()) {
            			return 0;
            		}
            		return (o1.getPlateAppearances() > o2.getPlateAppearances() ? -1 : 1);
            	}
            	else if (type.equals("BA")){
            		if (o1.getBattingAverage() == o2.getBattingAverage()) {
            			return 0;
            		}
            		return (o1.getBattingAverage() > o2.getBattingAverage() ? -1 : 1);
            	}
            	else if (type.equals("AB")){
            		if (o1.getAtBats() == o2.getAtBats()) {
            			return 0;
            		}
            		return (o1.getAtBats() > o2.getAtBats() ? -1 : 1);
            	}
            	else if (type.equals("DB")){
            		if (o1.getDoubles() == o2.getDoubles()) {
            			return 0;
            		}
            		return (o1.getDoubles() > o2.getDoubles() ? -1 : 1);
            	}
            	else if (type.equals("TR")){
            		if (o1.getTriples() == o2.getTriples()) {
            			return 0;
            		}
            		return (o1.getTriples() > o2.getTriples() ? -1 : 1);
            	}
            	else if (type.equals("BB")){
            		if (o1.getWalks() == o2.getWalks()) {
            			return 0;
            		}
            		return (o1.getWalks() > o2.getWalks() ? -1 : 1);
            	}
            	else if (type.equals("K")){
            		if (o1.getStrikeOuts() == o2.getStrikeOuts()) {
            			return 0;
            		}
            		return (o1.getStrikeOuts() > o2.getStrikeOuts() ? -1 : 1);
            	}
            	else if (type.equals("HBP")){
            		if (o1.getHitByPitch() == o2.getHitByPitch()) {
            			return 0;
            		}
            		return (o1.getHitByPitch() > o2.getHitByPitch() ? -1 : 1);
            	}
            	else if (type.equals("R")){
            		if (o1.getRuns() == o2.getRuns()) {
            			return 0;
            		}
            		return (o1.getRuns() > o2.getRuns() ? -1 : 1);
            	}
            	else if (type.equals("CS")){
            		if (o1.getCaughtStealing() == o2.getCaughtStealing()) {
            			return 0;
            		}
            		return (o1.getCaughtStealing() > o2.getCaughtStealing() ? -1 : 1);
            	}
            	else {
            		return 0;
            	}
            } 
        }); 
    }
	
	public void sortPitchingStatsList(List<MLBPitchingStats> battersList, String type) 
    { 
			Collections.sort(battersList, new Comparator<MLBPitchingStats>() { 
            public int compare(MLBPitchingStats o1, MLBPitchingStats o2) { 
            	if (type.equals("GS")) {
            		if (o1.getGamesStarted() == o2.getGamesStarted()) {
            			return 0;
            		}
            		return (o1.getGamesStarted() >= o2.getGamesStarted() ? -1 : 1); 
            	}
            	else if (type.equals("SV")){
            		if (o1.getSaves() == o2.getSaves()) {
            			return 0;
            		}
            		return (o1.getSaves() > o2.getSaves() ? -1 : 1);
            	}
            	else if (type.equals("HD")){
            		if (o1.getHolds() == o2.getHolds()) {
            			return 0;
            		}
            		return (o1.getHolds() > o2.getHolds() ? -1 : 1);
            	}
            	else if (type.equals("IP")){
            		if (o1.getInningsPitched() == o2.getInningsPitched()) {
            			return 0;
            		}
            		return (o1.getInningsPitched() > o2.getInningsPitched() ? -1 : 1);
            	}
            	else if (type.equals("R")) {
            		if (o1.getRunsAllowed() == o2.getRunsAllowed()) {
            			return 0;
            		}
            		return (o1.getRunsAllowed() >= o2.getRunsAllowed() ? -1 : 1); 
            	}
            	else if (type.equals("ER")){
            		if (o1.getEarnedRunsAllowed() == o2.getEarnedRunsAllowed()) {
            			return 0;
            		}
            		return (o1.getEarnedRunsAllowed() > o2.getEarnedRunsAllowed() ? -1 : 1);
            	}
            	else if (type.equals("ERA")){  // ERA only stat sorted L to H
            		if (o1.getEarnedRunAverage() == o2.getEarnedRunAverage()) {
            			return 0;
            		}
            		return (o1.getEarnedRunAverage() < o2.getEarnedRunAverage() ? -1 : 1);
            	}
            	else if (type.equals("K")){
            		if (o1.getStrikeouts() == o2.getStrikeouts()) {
            			return 0;
            		}
            		return (o1.getStrikeouts() > o2.getStrikeouts() ? -1 : 1);
            	}
            	else if (type.equals("HR")) {
            		if (o1.getHomeRunsAllowed() == o2.getHomeRunsAllowed()) {
            			return 0;
            		}
            		return (o1.getHomeRunsAllowed() >= o2.getHomeRunsAllowed() ? -1 : 1); 
            	}
            	else if (type.equals("SB")){
            		if (o1.getStolenBasesAllowed() == o2.getStolenBasesAllowed()) {
            			return 0;
            		}
            		return (o1.getStolenBasesAllowed() > o2.getStolenBasesAllowed() ? -1 : 1);
            	}
            	else if (type.equals("HB")){
            		if (o1.getHitBatters() == o2.getHitBatters()) {
            			return 0;
            		}
            		return (o1.getHitBatters() > o2.getHitBatters() ? -1 : 1);
            	}
            	else if (type.equals("H")){
            		if (o1.getHitsAllowed() == o2.getHitsAllowed()) {
            			return 0;
            		}
            		return (o1.getHitsAllowed() > o2.getHitsAllowed() ? -1 : 1);
            	}
            	else if (type.equals("BS")){
            		if (o1.getBlownSaves() == o2.getBlownSaves()) {
            			return 0;
            		}
            		return (o1.getBlownSaves() > o2.getBlownSaves() ? -1 : 1);
            	}
            	else if (type.equals("BK")){
            		if (o1.getBalks() == o2.getBalks()) {
            			return 0;
            		}
            		return (o1.getBalks() > o2.getBalks() ? -1 : 1);
            	}
            	else if (type.equals("WP")){
            		if (o1.getWildPitches() == o2.getWildPitches()) {
            			return 0;
            		}
            		return (o1.getWildPitches() > o2.getWildPitches() ? -1 : 1);
            	}
            	else if (type.equals("SF")){
            		if (o1.getSacrificeFlies() == o2.getSacrificeFlies()) {
            			return 0;
            		}
            		return (o1.getSacrificeFlies() > o2.getSacrificeFlies() ? -1 : 1);
            	}
            	else if (type.equals("BF")){
            		if (o1.getBattersFaced() == o2.getBattersFaced()) {
            			return 0;
            		}
            		return (o1.getBattersFaced() > o2.getBattersFaced() ? -1 : 1);
            	}
            	else if (type.equals("W")){
            		if (o1.getWins() == o2.getWins()) {
            			return 0;
            		}
            		return (o1.getWins() > o2.getWins() ? -1 : 1);
            	}
            	else if (type.equals("L")){
            		if (o1.getLosses() == o2.getLosses()) {
            			return 0;
            		}
            		return (o1.getLosses() > o2.getLosses() ? -1 : 1);
            	}
            	else {
            		return 0;
            	}
            } 
        }); 
    }
	
	@Override
    public void setSession(Map<String, Object> sessionMap) {
        this.userSession = sessionMap;
    }

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getMlbPlayerId() {
		return mlbPlayerId;
	}

	public void setMlbPlayerId(Integer mlbPlayerId) {
		this.mlbPlayerId = mlbPlayerId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getBattingSortType() {
		return battingSortType;
	}

	public void setBattingSortType(String battingSortType) {
		this.battingSortType = battingSortType;
	}

	public String getPitchingSortType() {
		return pitchingSortType;
	}

	public void setPitchingSortType(String pitchingSortType) {
		this.pitchingSortType = pitchingSortType;
	}

}
