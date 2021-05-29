package com.publicis.sapient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamRanking {

    @JsonProperty("Country ID")
    private String countryId;
    @JsonProperty("Country Name")
    private String countryName;
    @JsonProperty("League ID")
    private String leagueId;
    @JsonProperty("League Name")
    private String leagueName;
    @JsonProperty("Team ID")
    private String teamId;
    @JsonProperty("Team Name")
    private String teamName;
    @JsonProperty("Overall League Position")
    private String overallLeaguePosition;

    public TeamRanking(Team team, String countryId) {
        this.countryId = countryId;
        this.countryName = team.getCountryName();
        this.leagueId = team.getLeagueId();
        this.leagueName = team.getLeagueName();
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.overallLeaguePosition = team.getOverallLeaguePosition();
    }
}
