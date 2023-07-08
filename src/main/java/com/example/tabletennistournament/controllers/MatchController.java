package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.enums.MatchType;
import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @ResponseBody
    @PostMapping("/generate")
    public List<List<Match>> generateTournamentGrid() {
        List<String> participants = generateParticipants();
        return matchService.buildTournamentGrid(participants);
    }

    @ResponseBody
    @GetMapping("/all")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/tournament")
    public String showTournamentGrid(Model model) {
        List<Match> matches = matchService.getAllMatches()
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

        return "test2";
    }

    @PatchMapping
    public void updateMatchAndScores(@RequestParam String matchId,
                       @RequestParam String winner,
                       @RequestParam Integer player1Score,
                       @RequestParam Integer player2Score) {
        matchService.updateMatchAndScores(Long.parseLong(matchId), winner, player1Score, player2Score);
    }

//    @PatchMapping("/scores")
//    public void updateScores(@RequestParam String matchId,
//                             @RequestParam Integer player1Score,
//                             @RequestParam Integer player2Score) {
//        matchService.updateScores(Long.parseLong(matchId), player1Score, player2Score);
//    }

    private List<String> generateParticipants() {
        List<String> participants = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            participants.add("Participant " + i);
        }
        return participants;
    }

}
