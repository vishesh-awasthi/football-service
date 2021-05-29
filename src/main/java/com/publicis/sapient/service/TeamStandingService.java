package com.publicis.sapient.service;

import com.publicis.sapient.model.TeamRanking;

public interface TeamStandingService {
    TeamRanking getTeamByLeagueId(String countryName, String leagueName, String teamName) throws Exception;
}
