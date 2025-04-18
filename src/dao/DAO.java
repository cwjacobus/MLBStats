package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.MLBBattingStats;
import data.MLBPitchingStats;
import data.MLBSeasonLeaderStats;
import data.MLBTeam;

public class DAO {
	public static Connection conn;
	
	public static void pingDatabase() throws SQLException {
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM mlb_world_series";
		stmt.executeQuery(sql);
	}
		
	public static void setConnection(Connection connection) {
		conn = connection;
	}
	
	public static List<MLBBattingStats> getMLBBattingStatsList(String playerName) {
		return getMLBBattingStatsList(playerName, null);
	}
	
	public static List<MLBBattingStats> getMLBBattingStatsList(Integer playerId) {
		return getMLBBattingStatsList(null, playerId);
	}
	
	private static List<MLBBattingStats> getMLBBattingStatsList(String playerName, Integer playerId) {
		List<MLBBattingStats>mlbBattingStatsList = new ArrayList<MLBBattingStats>();
		try {
			// Get positions played by player
			Statement stmt1 = conn.createStatement();
			String sql1 = playerId == null ? "select group_concat(POSITION) as pos, fs.YEAR as year FROM MLB_FIELDING_STATS fs, MLB_PLAYER p " + 
				"WHERE p.MLB_PLAYER_ID = fs.MLB_PLAYER_ID AND p.FULL_NAME LIKE '%" + playerName + "%'" + " GROUP BY fs.YEAR ORDER BY fs.YEAR" 
				:
				"select group_concat(POSITION) as pos, fs.YEAR as year FROM MLB_FIELDING_STATS fs, MLB_PLAYER p " + 
				"WHERE p.MLB_PLAYER_ID = fs.MLB_PLAYER_ID AND p.MLB_PLAYER_ID = " + playerId + " GROUP BY fs.YEAR ORDER BY fs.YEAR";
			ResultSet rs = stmt1.executeQuery(sql1);
			HashMap<Integer, String> positionMap = new HashMap<>();
			while (rs.next()) {
				positionMap.put(rs.getInt("year"), rs.getString("pos"));
			}
			// Get player statistics
			Statement stmt2 = conn.createStatement();
			String sql2 = "SELECT p.MLB_PLAYER_ID as id, p.FULL_NAME as name, p.PRIMARY_POSITION as pos, bs.YEAR as year, t.FULL_NAME as team, bs.AT_BATS as ab, bs.HITS as h, " + 
				"bs.HITS/bs.AT_BATS as ba, bs.DOUBLES as dbl, bs.TRIPLES as tpl, bs.HOME_RUNS as hr, bs.WALKS as bb, bs.STRIKEOUTS as k, bs.HIT_BY_PITCH as hbp, " +
				"bs.RUNS as r, bs.RBIS as rbis, bs.STOLEN_BASES as sb, bs.PLATE_APPEARANCES as pa, bs.CAUGHT_STEALING as cs " + 
				"FROM MLB_PLAYER p, MLB_BATTING_STATS bs, MLB_TEAM t " +  
				"WHERE p.MLB_PLAYER_ID = bs.MLB_PLAYER_ID AND " + (playerId == null ? "p.FULL_NAME LIKE '%" + playerName + "%'" : "p.MLB_PLAYER_ID = " + playerId) +
				" AND (bs.MLB_TEAM_ID = t.TEAM_ID AND t.FIRST_YEAR_PLAYED <= bs.YEAR AND (t.LAST_YEAR_PLAYED >= bs.YEAR OR t.LAST_YEAR_PLAYED IS NULL)) ORDER BY bs.YEAR";
			rs = stmt2.executeQuery(sql2);
			MLBBattingStats mlbBattingStats;
			while (rs.next()) {
				int year = rs.getInt("year");
				String positionsPlayed = positionMap.get(year) != null ? positionMap.get(year) : rs.getString("pos");
				mlbBattingStats = new MLBBattingStats(rs.getInt("id"), rs.getString("name"), positionsPlayed, rs.getString("team"), year, 
					rs.getInt("ab"),  rs.getInt("h"), rs.getDouble("ba"), rs.getInt("dbl"), rs.getInt("tpl"), rs.getInt("hr"), rs.getInt("bb"), 
					rs.getInt("k"), rs.getInt("hbp"), rs.getInt("r"), rs.getInt("rbis"), rs.getInt("sb"), rs.getInt("pa"), rs.getInt("cs"));
				mlbBattingStatsList.add(mlbBattingStats);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return mlbBattingStatsList;
	}
	
	public static List<MLBPitchingStats> getMLBPitchingStatsList(String playerName) {
		return getMLBPitchingStatsList(playerName, null);
	}
	
	public static List<MLBPitchingStats> getMLBPitchingStatsList(Integer playerId) {
		return getMLBPitchingStatsList(null, playerId);
	}
	
	private static List<MLBPitchingStats> getMLBPitchingStatsList(String playerName, Integer playerId) {
		List<MLBPitchingStats>mlbPitchingStatsList = new ArrayList<MLBPitchingStats>();
		try {
			// Get player statistics
			Statement stmt = conn.createStatement();
			String sql = "SELECT p.MLB_PLAYER_ID as id, p.FULL_NAME as name, p.PRIMARY_POSITION as pos, ps.YEAR as year, t.FULL_NAME as team, " + 
				"ps.INNINGS_PITCHED as ip, ps.WALKS as bb, ps.STRIKEOUTS as k, ps.RUNS_ALLOWED as r, ps.EARNED_RUNS_ALLOWED as er, " + 
				"ps.HOME_RUNS_ALLOWED as hr, ps.STOLEN_BASES_ALLOWED as sb, ps.HIT_BATTERS as hb, ps.HITS_ALLOWED as h, ps.HOLDS as holds, " + 
				"ps.SAVES as s, ps.BLOWN_SAVES as bs, ps.GAMES_STARTED as gs, ps.BALKS as bk, ps.WILD_PITCHES as wp, " + 
				"ps.SAC_FLIES as sf, ps.BATTERS_FACED as bf, ps.WINS as w, ps.LOSSES as l " + 
				"FROM MLB_PLAYER p, MLB_PITCHING_STATS ps, MLB_TEAM t " +  
				"WHERE p.MLB_PLAYER_ID = ps.MLB_PLAYER_ID AND " + (playerId == null ? "p.FULL_NAME LIKE '%" + playerName + "%'" : "p.MLB_PLAYER_ID = " + playerId) +
				" AND (ps.MLB_TEAM_ID = t.TEAM_ID AND t.FIRST_YEAR_PLAYED <= ps.YEAR AND (t.LAST_YEAR_PLAYED >= ps.YEAR OR t.LAST_YEAR_PLAYED IS NULL)) ORDER BY ps.YEAR";
			ResultSet  rs = stmt.executeQuery(sql);
			MLBPitchingStats mlbPitchingStats;
			while (rs.next()) {
				int year = rs.getInt("year");
				mlbPitchingStats = new MLBPitchingStats(rs.getInt("id"), rs.getString("name"), rs.getString("pos"), rs.getString("team"), year, 
					rs.getDouble("ip"), rs.getInt("er"), rs.getInt("r"), rs.getInt("bb"), rs.getInt("k"), rs.getInt("hr"), 
					rs.getInt("sb"), rs.getInt("hb"), rs.getInt("h"), rs.getInt("holds"), rs.getInt("s"), rs.getInt("bs"), 
					rs.getInt("gs"), rs.getInt("bk"), rs.getInt("wp"),rs.getInt("sf"), rs.getInt("bf"), rs.getInt("w"), rs.getInt("l"));
				mlbPitchingStatsList.add(mlbPitchingStats);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return mlbPitchingStatsList;
	}
	
	public static List<MLBSeasonLeaderStats> getSeasonLeaderMLBStatsListByYear(Integer year, String stat, Integer limit, String league, boolean batter) {
		List<MLBSeasonLeaderStats>mlbSeasonLeaderStatsList = new ArrayList<>();
		try {
			String origStat = stat;
			boolean battAvg = stat.equalsIgnoreCase("BATTING_AVERAGE");
			boolean era = stat.equalsIgnoreCase("EARNED_RUN_AVERAGE");
			if (battAvg) {
				stat = "SUM(s.HITS)/SUM(s.AT_BATS)";
			}
			else if (era) {
				stat = "SUM(s.EARNED_RUNS_ALLOWED)*9/(FLOOR(SUM(s.INNINGS_PITCHED))+(MOD(SUM(s.INNINGS_PITCHED),1)*3.33))";  // Innings pitched must be converted from X.1, X.2
			}
			else {
					stat = "SUM(s." + stat + ")";
			}
			// Get player statistics
			String leagueSQL = league != null && league.length() > 0 ? " AND t.league = '" + league + "'": "";
			Statement stmt2 = conn.createStatement();
			String sql2 = "SELECT p.MLB_PLAYER_ID as id, p.FULL_NAME as name, t.SHORT_NAME as team, " + stat + " as stat " + 
				"FROM MLB_PLAYER p, " + (batter ? "MLB_BATTING_STATS":"MLB_PITCHING_STATS") +  " s, MLB_TEAM t " +  
				"WHERE p.MLB_PLAYER_ID = s.MLB_PLAYER_ID " +
				" AND (s.MLB_TEAM_ID = t.TEAM_ID AND t.FIRST_YEAR_PLAYED <= s.YEAR AND (t.LAST_YEAR_PLAYED >= s.YEAR OR t.LAST_YEAR_PLAYED IS NULL)) AND " + 
				" s.YEAR = " + year + leagueSQL + " GROUP BY s.MLB_PLAYER_ID ORDER BY " + stat + (era ? "" : " DESC") + " LIMIT " + limit;
			ResultSet rs = stmt2.executeQuery(sql2);
			MLBSeasonLeaderStats mlbSeasonLeaderStats;
			boolean doubleStatType = stat.equalsIgnoreCase("s.INNINGS_PITCHED") || battAvg || era;
			while (rs.next()) {
				mlbSeasonLeaderStats = new MLBSeasonLeaderStats(rs.getInt("id"), rs.getString("name"), rs.getString("team"), year, origStat, 
					(!doubleStatType ? rs.getInt("stat"):null), (doubleStatType ? rs.getDouble("stat"): null));
				mlbSeasonLeaderStatsList.add(mlbSeasonLeaderStats);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return mlbSeasonLeaderStatsList;
	}
	
	public static List<MLBBattingStats> getTeamMLBBattingStatsListByYear(Integer year, Integer teamId) {
		List<MLBBattingStats>mlbBattingStatsList = new ArrayList<MLBBattingStats>();
		try {
			// Get player statistics
			Statement stmt2 = conn.createStatement();
			String sql2 = "SELECT p.MLB_PLAYER_ID as id, p.FULL_NAME as name, p.PRIMARY_POSITION as pos, bs.YEAR as year, t.FULL_NAME as team, bs.AT_BATS as ab, bs.HITS as h, " + 
				"bs.HITS/bs.AT_BATS as ba, bs.DOUBLES as dbl, bs.TRIPLES as tpl, bs.HOME_RUNS as hr, bs.WALKS as bb, bs.STRIKEOUTS as k, bs.HIT_BY_PITCH as hbp, " +
				"bs.RUNS as r, bs.RBIS as rbis, bs.STOLEN_BASES as sb, bs.PLATE_APPEARANCES as pa, bs.CAUGHT_STEALING as cs " + 
				"FROM MLB_PLAYER p, MLB_BATTING_STATS bs, MLB_TEAM t " +  
				"WHERE p.MLB_PLAYER_ID = bs.MLB_PLAYER_ID " +
				" AND (bs.MLB_TEAM_ID = t.TEAM_ID AND t.FIRST_YEAR_PLAYED <= bs.YEAR AND (t.LAST_YEAR_PLAYED >= bs.YEAR OR t.LAST_YEAR_PLAYED IS NULL)) AND " + 
				"bs.MLB_TEAM_ID = " + teamId + " AND bs.YEAR = " + year + " ORDER BY p.FULL_NAME";
			ResultSet rs = stmt2.executeQuery(sql2);
			MLBBattingStats mlbBattingStats;
			while (rs.next()) {
				//String positionsPlayed = positionMap.get(year) != null ? positionMap.get(year) : rs.getString("pos");
				mlbBattingStats = new MLBBattingStats(rs.getInt("id"), rs.getString("name"), rs.getString("pos"), rs.getString("team"), year, 
					rs.getInt("ab"),  rs.getInt("h"), rs.getDouble("ba"), rs.getInt("dbl"), rs.getInt("tpl"), rs.getInt("hr"), rs.getInt("bb"), 
					rs.getInt("k"), rs.getInt("hbp"), rs.getInt("r"), rs.getInt("rbis"), rs.getInt("sb"), rs.getInt("pa"), rs.getInt("cs"));
				mlbBattingStatsList.add(mlbBattingStats);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return mlbBattingStatsList;
	}
	
	public static List<MLBPitchingStats> getTeamMLBPitchingStatsListByYear(Integer year, Integer teamId) {
		List<MLBPitchingStats>mlbPitchingStatsList = new ArrayList<MLBPitchingStats>();
		try {
			// Get player statistics
			Statement stmt = conn.createStatement();
			String sql = "SELECT p.MLB_PLAYER_ID as id, p.FULL_NAME as name, p.PRIMARY_POSITION as pos, ps.YEAR as year, t.FULL_NAME as team, " + 
				"ps.INNINGS_PITCHED as ip, ps.WALKS as bb, ps.STRIKEOUTS as k, ps.RUNS_ALLOWED as r, ps.EARNED_RUNS_ALLOWED as er, " + 
				"ps.HOME_RUNS_ALLOWED as hr, ps.STOLEN_BASES_ALLOWED as sb, ps.HIT_BATTERS as hb, ps.HITS_ALLOWED as h, ps.HOLDS as holds, " + 
				"ps.SAVES as s, ps.BLOWN_SAVES as bs, ps.GAMES_STARTED as gs, ps.BALKS as bk, ps.WILD_PITCHES as wp, " + 
				"ps.SAC_FLIES as sf, ps.BATTERS_FACED as bf, ps.WINS as w, ps.LOSSES as l " + 
				"FROM MLB_PLAYER p, MLB_PITCHING_STATS ps, MLB_TEAM t " +  
				"WHERE p.MLB_PLAYER_ID = ps.MLB_PLAYER_ID " + 
				"AND (ps.MLB_TEAM_ID = t.TEAM_ID AND t.FIRST_YEAR_PLAYED <= ps.YEAR AND (t.LAST_YEAR_PLAYED >= ps.YEAR OR t.LAST_YEAR_PLAYED IS NULL)) AND " + 
				"ps.MLB_TEAM_ID = " + teamId + " AND ps.YEAR = " + year + " ORDER BY p.FULL_NAME";
			ResultSet  rs = stmt.executeQuery(sql);
			MLBPitchingStats mlbPitchingStats;
			while (rs.next()) {
				mlbPitchingStats = new MLBPitchingStats(rs.getInt("id"), rs.getString("name"), rs.getString("pos"), rs.getString("team"), year, 
					rs.getDouble("ip"), rs.getInt("er"), rs.getInt("r"), rs.getInt("bb"), rs.getInt("k"), rs.getInt("hr"), 
					rs.getInt("sb"), rs.getInt("hb"), rs.getInt("h"), rs.getInt("holds"), rs.getInt("s"), rs.getInt("bs"), 
					rs.getInt("gs"), rs.getInt("bk"), rs.getInt("wp"),rs.getInt("sf"), rs.getInt("bf"), rs.getInt("w"), rs.getInt("l"));
				mlbPitchingStatsList.add(mlbPitchingStats);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return mlbPitchingStatsList;
	}
	
	public static List<MLBTeam> getMLBTeamListByYear(Integer year) {
		List<MLBTeam> mlbTeamList = new ArrayList<>();
		
		try {
			// Get positions played by player
			Statement stmt = conn.createStatement();
			String sql = "select * from MLB_TEAM where FIRST_YEAR_PLAYED <= " + year + " AND (LAST_YEAR_PLAYED >= " + year + " OR LAST_YEAR_PLAYED is NULL)";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				mlbTeamList.add(new MLBTeam(rs.getInt("TEAM_ID"), rs.getInt("MLB_FRANCHISE_ID"), rs.getString("FULL_NAME"), rs.getString("SHORT_NAME"), 
					rs.getString("LEAGUE"), rs.getInt("FIRST_YEAR_PLAYED"), rs.getInt("LAST_YEAR_PLAYED")));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mlbTeamList;	
	}
	
	public static ArrayList<MLBTeam> getAllMLBTeamsList() {
		ArrayList<MLBTeam> allTeams = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM MLB_TEAM ORDER BY SHORT_NAME, FIRST_YEAR_PLAYED";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MLBTeam team = new MLBTeam(rs.getInt("TEAM_ID"), rs.getInt("MLB_FRANCHISE_ID"), rs.getString("FULL_NAME"), rs.getString("SHORT_NAME"), 
					rs.getString("LEAGUE"), rs.getInt("FIRST_YEAR_PLAYED"), rs.getInt("LAST_YEAR_PLAYED"));
				allTeams.add(team);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allTeams;
	}
}
