package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.dtos.TournamentDto;
import com.example.tabletennistournament.enums.MatchType;
import com.example.tabletennistournament.mappers.TournamentMapper;
import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.security.DetailsUser;
import com.example.tabletennistournament.services.MatchService;
import com.example.tabletennistournament.services.TournamentService;
import com.example.tabletennistournament.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;
    private final MatchService matchService;
    private final UserService userService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tournaments", tournamentMapper.map(
                tournamentService.getAll().stream()
                        .sorted(Comparator.comparing(Tournament::getId).reversed()).toList()));
        return "tournaments";
    }

    @GetMapping("/{id}")
    public String showTournamentGrid(@PathVariable("id") Long tournamentId,
                                     @AuthenticationPrincipal DetailsUser user, Model model) {
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
        model.addAttribute("amountUsers", tournamentService.getById(tournamentId).getUserCount());
        model.addAttribute("tournament", tournamentService.getById(tournamentId));
        model.addAttribute("userId", user.getUser().getId());

        return "tournament";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("tournament") TournamentDto tournament, Model model) {
        model.addAttribute("users", userService.getAll());
        return "newTournament";
    }

    @PostMapping
    public String save(@ModelAttribute("tournament") TournamentDto tournamentDto,
                       @AuthenticationPrincipal DetailsUser user) {
        Long tournamentId = tournamentService.save(tournamentMapper.map(tournamentDto), user.getUser().getId());
        Tournament tournament = tournamentService.getById(tournamentId);
        matchService.buildTournamentGrid(tournament);
        return "redirect:/tournament/" + tournamentId;
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(tournamentService.deleteById(id), HttpStatus.NO_CONTENT);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<Long> updateById(@PathVariable Long id, @RequestBody TournamentDto tournament) {
        return new ResponseEntity<>(tournamentService.updateById(id, tournamentMapper.map(tournament)), HttpStatus.OK);
    }

    @PatchMapping("/finish/{tournamentId}")
    public String finish(@PathVariable Long tournamentId) {
        matchService.finishTournament(tournamentId);
        return "redirect:/tournament";
    }

}
