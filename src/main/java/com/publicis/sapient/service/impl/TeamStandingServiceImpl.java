package com.publicis.sapient.service.impl;

import com.publicis.sapient.exception.InvalidParamException;
import com.publicis.sapient.exception.ResourceNotFound;
import com.publicis.sapient.model.*;
import com.publicis.sapient.service.AbstractTeamService;
import com.publicis.sapient.service.ClientService;
import com.publicis.sapient.service.TeamStandingService;
import com.publicis.sapient.utils.ObjectMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TeamStandingServiceImpl extends AbstractTeamService implements TeamStandingService {

    private static final String GET_COUNTRIES = "get_countries";
    private static final String GET_LEAGUES = "get_leagues";
    private static final String GET_STANDINGS = "get_standings";

    private ClientService clientService;

    /**
     * Get the teams ranking for mentioned countryName, leagueName and teamName.
     *
     * @param countryName as Country Name
     * @param leagueName  as League Name
     * @param teamName    as Team Name
     * @return {@link TeamRanking}
     * @throws ResourceNotFound, InvalidParamException
     */
    public TeamRanking getTeamByLeagueId(String countryName, String leagueName, String teamName) throws ResourceNotFound, InvalidParamException {
        checkArguments(countryName, leagueName, teamName);
        Country country = getCountryByName(countryName);
        League league = getLeagueByLeagueNameAndCountryId(leagueName, country.getCountryId());
        return getTeamByLeagueId(league, country.getCountryId(), teamName);
    }

    /**
     * Get the country details with countryName.
     *
     * @param countryName as country name
     * @return {@link Country}
     * @throws ResourceNotFound
     */
    private Country getCountryByName(String countryName) throws ResourceNotFound {
        log.info("getting country with name :{}", countryName);
        ResponseEntity<String> response = clientService.getResponse(getBaseHeaders(GET_COUNTRIES));
        ApiResponse<Country> apiResponse = ObjectMapperUtils.parseResponse(response.getBody(), Country.class);
        checkApiResponse(apiResponse);
        List<Country> countries = apiResponse.getData();
        log.debug("country size {}", countries.size());
        return countries
                .stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Country not found with name : " + countryName));
    }

    /**
     * Gets the league details with league name and country id.
     *
     * @param leagueName as league name
     * @param countryId as country id
     * @return {@link League}
     * @throws ResourceNotFound
     */
    private League getLeagueByLeagueNameAndCountryId(String leagueName, String countryId) throws ResourceNotFound {
        log.info("getting league with name :{} and country id : {}", leagueName, countryId);
        HttpHeaders headers = getBaseHeaders(GET_LEAGUES);
        headers.add("country_id", countryId);
        ResponseEntity<String> response = clientService.getResponse(headers);
        ApiResponse apiResponse = ObjectMapperUtils.parseResponse(response.getBody(), League.class);
        List<League> leagues = apiResponse.getData();
        log.debug("league size {}", leagues.size());
        checkApiResponse(apiResponse);
        return leagues.stream()
                .filter(league -> league.getLeagueName().equalsIgnoreCase(leagueName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("League not found with name : " + leagueName));
    }

    /**
     * Gets the team details and structures the team ranking response.
     *
     * @param league as league
     * @param countryId as country Id
     * @param teamName as team name
     * @return {@link TeamRanking}
     * @throws ResourceNotFound
     */
    private TeamRanking getTeamByLeagueId(League league, String countryId, String teamName) throws ResourceNotFound {
        log.info("getting team with name :{} and league Id : {}", teamName, league.getLeagueId());
        HttpHeaders headers = getBaseHeaders(GET_STANDINGS);
        headers.add("league_id", league.getLeagueId());
        ResponseEntity<String> response = clientService.getResponse(headers);
        ApiResponse apiResponse = ObjectMapperUtils.parseResponse(response.getBody(), Team.class);
        checkApiResponse(apiResponse);
        List<Team> teams = apiResponse.getData();
        log.debug("teams size {}", teams.size());
        Team team = teams
                .stream()
                .filter(teamRanking -> teamRanking.getTeamName().equalsIgnoreCase(teamName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Team not found with name : " + teamName));
        return new TeamRanking(team, countryId);
    }

}
