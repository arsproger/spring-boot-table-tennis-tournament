package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.MatchType;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    public List<List<Match>> buildTournamentGrid(List<String> participants) {
        if (participants.size() < 3 || participants.size() > 16) {
            throw new RuntimeException("Error!");
        }

        Collections.shuffle(participants);
        List<Match> firstRoundMatches = new ArrayList<>();
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Match> fourthRoundMatches = new ArrayList<>();
        List<Match> fifthRoundMatches = new ArrayList<>();

        int amountOfSecondRound = participants.size() <= 4
                ? 1 : (participants.size() + 7) / 8 * 2;

        for (int i = 0; i < participants.size() / 2; i++) {
            Match match = Match.builder()
                    .player1(participants.get(i * 2))
                    .player2(participants.get(i * 2 + 1))
                    .matchType(MatchType.FIRST_ROUND)
                    .build();
            firstRoundMatches.add(match);
            saveMatch(match);
        }

        if (participants.size() % 2 != 0) {
            Match match = Match.builder()
                    .player1(participants.get(participants.size() - 1))
                    .player2("None")
                    .winner(participants.get(participants.size() - 1))
                    .matchType(MatchType.FIRST_ROUND)
                    .build();
            firstRoundMatches.add(match);
            saveMatch(match);
        }

        saveMatches(firstRoundMatches);

        int i = 0;

        for (int round = 0; round < amountOfSecondRound; round++) {
            Match newMatch = Match.builder()
                    .player1(null)
                    .player2(null)
                    .matchType(MatchType.SECOND_ROUND)
                    .build();
            secondRoundMatches.add(newMatch);
            Long savedMatchId = saveMatch(newMatch);

            int count = 0;
            while (count < 2) {
                if (firstRoundMatches.get(i).getNextMatchId() == null) {
                    firstRoundMatches.get(i).setNextMatchId(savedMatchId);
                    firstRoundMatches.get(i).setNextMatchPlayerNumber(count);
                    count++;
                }
                i++;
            }
        }

        saveMatches(secondRoundMatches);

        int amountOfThirdRound = amountOfSecondRound == 1
                ? 0
                : amountOfSecondRound / 2;

        i = 0;

        for (int round = 0; round < amountOfThirdRound; round++) {
            Match newMatch = Match.builder()
                    .player1(null)
                    .player2(null)
                    .matchType(MatchType.THIRD_ROUND)
                    .build();
            thirdRoundMatches.add(newMatch);
            Long savedMatchId = saveMatch(newMatch);

            int count = 0;
            while (count < 2) {
                if (secondRoundMatches.get(i).getNextMatchId() == null) {
                    secondRoundMatches.get(i).setNextMatchId(savedMatchId);
                    secondRoundMatches.get(i).setNextMatchPlayerNumber(count);
                    count++;
                }
                i++;
            }
        }

        saveMatches(thirdRoundMatches);

        int amountOfFourthRound = amountOfThirdRound == 1
                ? 0
                : amountOfThirdRound / 2;

        i = 0;

        for (int round = 0; round < amountOfFourthRound; round++) {
            Match newMatch = Match.builder()
                    .player1(null)
                    .player2(null)
                    .matchType(MatchType.FOURTH_ROUND)
                    .build();
            fourthRoundMatches.add(newMatch);
            Long savedMatchId = saveMatch(newMatch);

            int count = 0;
            while (count < 2) {
                if (thirdRoundMatches.get(i).getNextMatchId() == null) {
                    thirdRoundMatches.get(i).setNextMatchId(savedMatchId);
                    thirdRoundMatches.get(i).setNextMatchPlayerNumber(count);
                    count++;
                }
                i++;
            }
        }

        saveMatches(fourthRoundMatches);

        return Arrays.asList(firstRoundMatches, secondRoundMatches, thirdRoundMatches, fourthRoundMatches, fifthRoundMatches);
    }

    public void saveMatches(List<Match> matches) {
        matchRepository.saveAll(matches);
    }

    public Long saveMatch(Match match) {
        match.setPlayer1Score(0);
        match.setPlayer2Score(0);
        return matchRepository.save(match).getId();
    }

    public Match getById(Long id) {
        return matchRepository.findById(id).orElseThrow(
                () -> new AppException("Match with id " + id + " not found!", HttpStatus.NOT_FOUND)
        );
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public void updateMatchAndScores(Long matchId, String winner, Integer player1Score, Integer player2Score) {
        Match match = getById(matchId);
        match.setWinner(winner);
        match.setPlayer1Score(player1Score);
        match.setPlayer2Score(player2Score);
        matchRepository.save(match);

        if (match.getNextMatchPlayerNumber() != null) {
            Match updatedMatch = getById(match.getNextMatchId());
            if (match.getNextMatchPlayerNumber() == 0) {
                updatedMatch.setPlayer1(winner);
            } else if (match.getNextMatchPlayerNumber() == 1) {
                updatedMatch.setPlayer2(winner);
            }
            matchRepository.save(updatedMatch);
        }
//        return matchId;
    }

//    public void updateScores(Long matchId, Integer player1Score, Integer player2Score) {
//        Match match = getById(matchId);
//        match.setPlayer1Score(player1Score);
//        match.setPlayer2Score(player2Score);
//        matchRepository.save(match);
//    }

}
