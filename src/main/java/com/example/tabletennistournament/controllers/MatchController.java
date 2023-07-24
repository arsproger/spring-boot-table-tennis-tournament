package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.enums.MatchType;
import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.services.MatchService;
import com.example.tabletennistournament.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final TournamentService tournamentService;

    @ResponseBody
    @GetMapping("/all")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/tournament/{id}/{amountUsers}")
    public String showTournamentGrid(@PathVariable("id") Long tournamentId,
                                     @PathVariable Integer amountUsers,
                                     Model model) {
        List<Match> matches = matchService.getMatchesByTournamentId(tournamentId)
                .stream().sorted(Comparator.comparing(Match::getId)).toList();

        model.addAttribute("firstRound", matches
                .stream().filter(x -> x.getMatchType().equals(MatchType.FIRST_ROUND))
                .collect(Collectors.toList()));
        model.addAttribute("secondRound", matches
                .stream().filter(x -> x.getMatchType().equals(MatchType.SECOND_ROUND))
                .collect(Collectors.toList()));
        model.addAttribute("thirdRound", matches
                .stream().filter(x -> x.getMatchType().equals(MatchType.THIRD_ROUND))
                .collect(Collectors.toList()));
        model.addAttribute("fourthRound", matches
                .stream().filter(x -> x.getMatchType().equals(MatchType.FOURTH_ROUND))
                .collect(Collectors.toList()));

        model.addAttribute("amountMatches", matches.size());
        model.addAttribute("amountUsers", amountUsers);
        model.addAttribute("tournamentId", tournamentId);

        return "tournament";
    }

    @PatchMapping
    public void updateMatchAndScores(@RequestParam String matchId,
                                     @RequestParam Long winnerId,
                                     @RequestParam Integer player1Score,
                                     @RequestParam Integer player2Score) {
        matchService.updateMatchAndScores(Long.parseLong(matchId), winnerId, player1Score, player2Score);
    }

}
