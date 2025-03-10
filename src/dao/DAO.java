package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.MLBBattingStats;

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
		List<MLBBattingStats>mlbBattingStatsList = new ArrayList<MLBBattingStats>();
		try {
			// Get positions played by player
			Statement stmt1 = conn.createStatement();
			String sql1 = "select group_concat(POSITION) as pos, fs.YEAR as year FROM MLB_FIELDING_STATS fs, MLB_PLAYER p " + 
				"WHERE p.mlb_player_id = fs.mlb_player_id AND p.full_name LIKE '%" + playerName + "%'" + " GROUP BY fs.YEAR ORDER BY fs.YEAR";
			ResultSet rs = stmt1.executeQuery(sql1);
			HashMap<Integer, String> positionMap = new HashMap<>();
			while (rs.next()) {
				positionMap.put(rs.getInt("year"), rs.getString("pos"));
			}
			// Get player statistics
			Statement stmt2 = conn.createStatement();
			String sql2 = "SELECT p.FULL_NAME as name, p.PRIMARY_POSITION as pos, bs.YEAR as year, t.FULL_NAME as team, bs.AT_BATS as ab, bs.HITS as h, " + 
				"bs.HITS/bs.AT_BATS as ba, bs.DOUBLES as dbl, bs.TRIPLES as tpl, bs.HOME_RUNS as hr, bs.WALKS as bb, bs.STRIKEOUTS as k, bs.HIT_BY_PITCH as hbp, " +
				"bs.RUNS as r, bs.RBIS as rbis, bs.STOLEN_BASES as sb, bs.PLATE_APPEARANCES as pa, bs.CAUGHT_STEALING as cs " + 
				"FROM MLB_PLAYER p, MLB_BATTING_STATS bs, MLB_TEAM t " + 
				"WHERE p.mlb_player_id = bs.mlb_player_id AND p.full_name LIKE '%" + playerName + "%' AND " +
				"(bs.mlb_team_id = t.team_id AND t.first_year_played <= bs.year AND (t.last_year_played >= bs.year OR t.last_year_played IS NULL)) ORDER BY bs.year";
			rs = stmt2.executeQuery(sql2);
			MLBBattingStats mlbBattingStats;
			while (rs.next()) {
				int year = rs.getInt("year");
				String positionsPlayed = positionMap.get(year) != null ? positionMap.get(year) : rs.getString("pos");
				mlbBattingStats = new MLBBattingStats(rs.getString("name"), positionsPlayed, rs.getString("team"), year, 
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
}
