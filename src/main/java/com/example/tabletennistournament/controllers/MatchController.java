package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @ResponseBody
    @GetMapping("/all")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @PatchMapping
    public void updateMatchAndScores(@RequestParam String matchId,
                                     @RequestParam Long winnerId,
                                     @RequestParam Integer player1Score,
                                     @RequestParam Integer player2Score) {
        matchService.updateMatchAndScores(Long.parseLong(matchId), winnerId, player1Score, player2Score);
    }

}
