package com.publicis.sapient.service;

import com.publicis.sapient.exception.InvalidParamException;
import com.publicis.sapient.exception.ResourceNotFound;
import com.publicis.sapient.model.*;
import com.publicis.sapient.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class TeamStandingServiceImpl extends AbstractTeamService implements TeamStandingService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String GET_COUNTRIES = "get_countries";
    private static final String GET_LEAGUES = "get_leagues";
    private static final String GET_STANDINGS = "get_standings";

    @Value("${api.base.url}")
    String baseUrl;

    @Value("${api.secret.key}")
    String apiSecretKey;

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

    private Country getCountryByName(String countryName) throws ResourceNotFound {
        ResponseEntity<String> response = getResponse(getBaseHeaders(GET_COUNTRIES, apiSecretKey));
        ApiResponse<Country> apiResponse = ObjectMapperUtils.parseResponse(response.getBody(), Country.class);
        checkApiResponse(apiResponse);
        List<Country> countries = apiResponse.getData();
        return countries
                .stream()
                .filter(country -> country.getCountryName().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Country not found with name : " + countryName));
    }

    private League getLeagueByLeagueNameAndCountryId(String leagueName, String countryId) throws ResourceNotFound {
        HttpHeaders headers = getBaseHeaders(GET_LEAGUES, apiSecretKey);
        headers.add("country_id", countryId);
        ResponseEntity<String> response = getResponse(headers);
        ApiResponse apiResponse = ObjectMapperUtils.parseResponse(response.getBody(), League.class);
        List<League> leagues = apiResponse.getData();
        checkApiResponse(apiResponse);
        return leagues.stream()
                .filter(league -> league.getLeagueName().equalsIgnoreCase(leagueName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("League not found with name : " + leagueName));
    }

    private TeamRanking getTeamByLeagueId(League league, String countryId, String teamName) throws ResourceNotFound {
        HttpHeaders headers = getBaseHeaders(GET_STANDINGS, apiSecretKey);
        headers.add("league_id", league.getLeagueId());
        ResponseEntity<String> response = getResponse(headers);
        ApiResponse apiResponse = ObjectMapperUtils.parseResponse(response.getBody(), Team.class);
        checkApiResponse(apiResponse);
        List<Team> teams = apiResponse.getData();
        Team team = teams
                .stream()
                .filter(teamRanking -> teamRanking.getTeamName().equalsIgnoreCase(teamName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Team not found with name : " + teamName));
        return new TeamRanking(team, countryId);
    }

    private ResponseEntity<String> getResponse(HttpHeaders headers) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl).queryParams(headers).build().toUri();
        return REST_TEMPLATE.getForEntity(uri, String.class);
    }

}
