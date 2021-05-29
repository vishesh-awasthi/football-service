package com.publicis.sapient.controller;

import com.publicis.sapient.model.TeamRanking;
import com.publicis.sapient.service.TeamStandingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TeamStandingController {

    private final TeamStandingServiceImpl teamStandingService;

    @GetMapping("/standings")
    public ResponseEntity<TeamRanking> getTeamRanking(@RequestParam(defaultValue = "") String countryName,
                                                      @RequestParam(defaultValue = "") String leagueName,
                                                      @RequestParam(defaultValue = "") String teamName) throws Exception {
        return new ResponseEntity<>(teamStandingService.getTeamByLeagueId(countryName, leagueName, teamName), HttpStatus.OK);
    }
}
