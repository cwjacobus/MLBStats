package actions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

import dao.DAO;
import data.MLBBattingStats;
import data.MLBPitchingStats;
import init.MLBStatsDatabase;

public class GetPlayerStatsAction extends ActionSupport implements SessionAware {
	
	private static final long serialVersionUID = 1L;
	Map<String, Object> userSession;
	String playerType;
	String playerName;
	Integer mlbPlayerId;
	Integer teamId;
	Integer year;
	String teamMode;

	public String execute() throws Exception {
		ValueStack stack = ActionContext.getContext().getValueStack();
	    Map<String, Object> context = new HashMap<String, Object>();
	    
	    MLBStatsDatabase bowlPoolDB = (MLBStatsDatabase)ServletActionContext.getServletContext().getAttribute("Database");  
        Connection con = bowlPoolDB.getCon();
        DAO.setConnection(con);
        try {
        	DAO.pingDatabase();
        }
        catch (CommunicationsException ce) {
        	System.out.println("DB Connection timed out - Reconnect");
        	con = bowlPoolDB.reconnectAfterTimeout();
        	DAO.setConnection(con);
        }
        if (teamMode == null && playerType == null) {
        	context.put("errorMsg", "Player type is required!");
			stack.push(context);
			return "error";
        }
        if (teamMode == null && (playerName == null || playerName.trim().length() == 0) && mlbPlayerId == null) {
        	context.put("errorMsg", "Player name is required!");
			stack.push(context);
			return "error";
        }
        
        if (teamMode != null && (year == null || teamId == null)) {
        	context.put("errorMsg", "Year and team are required!");
			stack.push(context);
			return "error";
        }
        List<MLBBattingStats> mlbBattingStatsList = null;
        List<MLBPitchingStats> mlbPitchingStatsList = null;
        boolean batter = teamMode == null && playerType.equals("batter");
        if (batter) {
        	mlbBattingStatsList = mlbPlayerId == null ? DAO.getMLBBattingStatsList(playerName):DAO.getMLBBattingStatsList(mlbPlayerId);
			Map<Integer, MLBBattingStats> multipleBattersMap = new HashMap<>();
			mlbBattingStatsList.stream().forEach(entry -> multipleBattersMap.put(entry.getMlbPlayerId(), entry));
			if (multipleBattersMap.size() != 0 && multipleBattersMap.size() > 1) {
				List<MLBBattingStats> multipleBattersList = new ArrayList<>(multipleBattersMap.values());
				context.put("multipleBattersList", multipleBattersList);
			}
			else {
				context.put("mlbBattingStatsList", mlbBattingStatsList);
				context.put("batterName", mlbBattingStatsList.size() > 0 ? mlbBattingStatsList.get(0).getPlayerName(): null);
			}
			context.put("batter", batter);
		}
		else if (teamMode == null && !batter) { // pitcher
			mlbPitchingStatsList = mlbPlayerId == null ? DAO.getMLBPitchingStatsList(playerName):DAO.getMLBPitchingStatsList(mlbPlayerId);
			Map<Integer, MLBPitchingStats> multiplePitchersMap = new HashMap<>();
			mlbPitchingStatsList.stream().forEach(entry -> multiplePitchersMap.put(entry.getMlbPlayerId(), entry));
			if (multiplePitchersMap.size() != 0 && multiplePitchersMap.size() > 1) {
				List<MLBPitchingStats> multiplePitchersList = new ArrayList<>(multiplePitchersMap.values());
				context.put("multiplePitchersList", multiplePitchersList);
			}
			else {
				context.put("mlbPitchingStatsList", mlbPitchingStatsList);
				context.put("pitcherName", mlbPitchingStatsList.size() > 0 ? mlbPitchingStatsList.get(0).getPlayerName(): null);
			}
			context.put("batter", batter);
		}
		else { // team mode
			mlbPitchingStatsList = DAO.getTeamMLBPitchingStatsListByYear(year, teamId);
			context.put("mlbPitchingStatsList", mlbPitchingStatsList);
		}
        context.put("teamMode", teamMode);
	    stack.push(context);
	    return "success";
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

	public String getTeamMode() {
		return teamMode;
	}

	public void setTeamMode(String teamMode) {
		this.teamMode = teamMode;
	}

}
