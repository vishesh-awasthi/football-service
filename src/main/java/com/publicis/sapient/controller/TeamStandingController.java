package com.publicis.sapient.controller;

import com.publicis.sapient.model.TeamRanking;
import com.publicis.sapient.service.impl.TeamStandingServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Api(tags = "FootBall service")
@Slf4j
public class TeamStandingController {

    private final TeamStandingServiceImpl teamStandingService;

    @GetMapping("/standings")
    public ResponseEntity<TeamRanking> getTeamRanking(@ApiParam("Country Name") @RequestParam(defaultValue = "") String countryName,
                                                      @ApiParam("League Name") @RequestParam(defaultValue = "") String leagueName,
                                                      @ApiParam("Team Name") @RequestParam(defaultValue = "") String teamName) throws Exception {
        log.info("Getting team standing for {},{},{}", countryName, leagueName, teamName);
        return new ResponseEntity<>(teamStandingService.getTeamByLeagueId(countryName, leagueName, teamName), HttpStatus.OK);
    }
}
