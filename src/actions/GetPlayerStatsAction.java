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
        if (playerType == null) {
        	return "success";  // change to return error
        }
        if ((playerName == null || playerName.trim().length() == 0) && mlbPlayerId == null) {
        	return "success";  // change to return error
        }
        boolean batter = playerType.equals("batter");
        List<MLBBattingStats> mlbBattingStatsList = null;
        List<MLBPitchingStats> mlbPitchingStatsList = null;
        if (batter) {
        	mlbBattingStatsList = mlbPlayerId == null ? DAO.getMLBBattingStatsList(playerName):DAO.getMLBBattingStatsList(mlbPlayerId);
        }
        else {
        	mlbPitchingStatsList = mlbPlayerId == null ? DAO.getMLBPitchingStatsList(playerName):DAO.getMLBPitchingStatsList(mlbPlayerId);
        }
		
		if (batter) {
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
		}
		else { // pitcher
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
		}
		context.put("batter", batter);
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

}
