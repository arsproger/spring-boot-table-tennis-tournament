package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.dtos.TournamentDto;
import com.example.tabletennistournament.mappers.TournamentMapper;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.services.MatchService;
import com.example.tabletennistournament.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tournament")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final TournamentMapper tournamentMapper;
    private final MatchService matchService;

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<TournamentDto>> getAll() {
        return new ResponseEntity<>(tournamentMapper.map(tournamentService.getAll()), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<TournamentDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(tournamentMapper.map(tournamentService.getById(id)), HttpStatus.OK);
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("tournament") Tournament tournament) {
        return "newTournament";
    }

    @PostMapping
    public String save(@ModelAttribute("tournament") TournamentDto tournament) {
        Long tournamentId = tournamentService.save(tournamentMapper.map(tournament));
        matchService.buildTournamentGrid(tournamentId);
        return "redirect:/matches/tournament/" + tournamentId;
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

}
