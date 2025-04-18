package data;

public class MLBBattingStats {
	private Integer mlbPlayerId;
	private String playerName;
	private String pos;
	private String teamName;
	private Integer year;
	private int atBats;
	private int hits;
	private double battingAverage;
	private int doubles;
	private int triples;
	private int homeRuns;
	private int walks;
	private int strikeOuts;
	private int hitByPitch;
	private int runs;
	private int rbis;
	private int stolenBases;
	private int plateAppearances;
	private int caughtStealing;

	public MLBBattingStats(Integer mlbPlayerId, String playerName, String pos, String teamName, Integer year, int atBats, int hits,
			double battingAverage, int doubles, int triples, int homeRuns, int walks, int strikeOuts, int hitByPitch,
			int runs, int rbis, int stolenBases, int plateAppearances, int caughtStealing) {
		this.mlbPlayerId = mlbPlayerId;
		this.pos = pos;
		this.playerName = playerName;
		this.teamName = teamName;
		this.year = year;
		this.atBats = atBats;
		this.hits = hits;
		this.battingAverage = battingAverage;
		this.doubles = doubles;
		this.triples = triples;
		this.homeRuns = homeRuns;
		this.walks = walks;
		this.strikeOuts = strikeOuts;
		this.hitByPitch = hitByPitch;
		this.runs = runs;
		this.rbis = rbis;
		this.stolenBases = stolenBases;
		this.plateAppearances = plateAppearances;
		this.caughtStealing = caughtStealing;
	}
	
	public Integer getMlbPlayerId() {
		return mlbPlayerId;
	}

	public void setMlbPlayerId(Integer mlbPlayerId) {
		this.mlbPlayerId = mlbPlayerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public int getAtBats() {
		return atBats;
	}

	public void setAtBats(int atBats) {
		this.atBats = atBats;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public double getBattingAverage() {
		return battingAverage;
	}

	public void setBattingAverage(double battingAverage) {
		this.battingAverage = battingAverage;
	}

	public int getDoubles() {
		return doubles;
	}

	public void setDoubles(int doubles) {
		this.doubles = doubles;
	}

	public int getTriples() {
		return triples;
	}

	public void setTriples(int triples) {
		this.triples = triples;
	}

	public int getHomeRuns() {
		return homeRuns;
	}

	public void setHomeRuns(int homeRuns) {
		this.homeRuns = homeRuns;
	}

	public int getWalks() {
		return walks;
	}

	public void setWalks(int walks) {
		this.walks = walks;
	}

	public int getStrikeOuts() {
		return strikeOuts;
	}

	public void setStrikeOuts(int strikeOuts) {
		this.strikeOuts = strikeOuts;
	}

	public int getHitByPitch() {
		return hitByPitch;
	}

	public void setHitByPitch(int hitByPitch) {
		this.hitByPitch = hitByPitch;
	}

	public int getRuns() {
		return runs;
	}
	
	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getRbis() {
		return rbis;
	}

	public void setRbis(int rbis) {
		this.rbis = rbis;
	}

	public int getStolenBases() {
		return stolenBases;
	}

	public void setStolenBases(int stolenBases) {
		this.stolenBases = stolenBases;
	}

	public int getPlateAppearances() {
		return plateAppearances;
	}

	public void setPlateAppearances(int plateAppearances) {
		this.plateAppearances = plateAppearances;
	}

	public int getCaughtStealing() {
		return caughtStealing;
	}

	public void setCaughtStealing(int caughtStealing) {
		this.caughtStealing = caughtStealing;
	}

	@Override
	public String toString() {
		return "playerName=" + playerName + " teamName=" + teamName;
	}
	
	@Override
    public boolean equals(Object obj) {
        if(obj instanceof MLBBattingStats)
        {
        	MLBBattingStats temp = (MLBBattingStats) obj;
            if (this.mlbPlayerId.intValue() == temp.mlbPlayerId.intValue() && this.year.intValue() == temp.year.intValue())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.mlbPlayerId.hashCode() + this.year.hashCode());        
    }
}
