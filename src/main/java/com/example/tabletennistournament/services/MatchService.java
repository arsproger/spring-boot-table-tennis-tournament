package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.MatchType;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final TournamentService tournamentService;

    public void buildTournamentGrid(Long tournamentId) {
        Tournament tournament = tournamentService.getById(tournamentId);
        List<String> participants = tournament.getUsers();
        Collections.shuffle(participants);
        List<Match> firstRoundMatches = new ArrayList<>();
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Match> fourthRoundMatches = new ArrayList<>();
        double participantsSize = participants.size();
        while (true) {
            if (participantsSize / 4.0 > 0.0) {
                participantsSize -= 4;
                Match match1 = Match.builder().matchType(MatchType.FIRST_ROUND).tournament(tournament).build();
                Match match2 = Match.builder().matchType(MatchType.FIRST_ROUND).tournament(tournament).build();
                firstRoundMatches.addAll(List.of(match1, match2));
                saveMatch(match1);
                saveMatch(match2);
            } else {
                break;
            }
        }
        int amountFirstRound = firstRoundMatches.size();
        int participantsSizeInt = participants.size();
        for (Match firstRound : firstRoundMatches) {
            if (participantsSizeInt > amountFirstRound) {
                firstRound.setPlayer1(participants.get(participantsSizeInt - 1));
                participantsSizeInt--;
                firstRound.setPlayer2(participants.get(participantsSizeInt - 1));
                participantsSizeInt--;
                amountFirstRound--;
            } else if (participantsSizeInt == amountFirstRound) {
                firstRound.setPlayer1(participants.get(participantsSizeInt - 1));
                participantsSizeInt--;
                amountFirstRound--;
            } else {
                break;
            }
        }

        int amountOfSecondRound = participants.size() <= 4
                ? 1 : (participants.size() + 7) / 8 * 2;

        saveMatches(firstRoundMatches);

        int i = 0;

        for (int round = 0; round < amountOfSecondRound; round++) {
            Match newMatch = Match.builder()
                    .player1(null)
                    .player2(null)
                    .matchType(MatchType.SECOND_ROUND)
                    .tournament(tournament)
                    .build();
            secondRoundMatches.add(newMatch);
            Long savedMatchId = saveMatch(newMatch);

            int count = 0;
            while (count < 2) {
                if (firstRoundMatches.size() <= i) {
                    break;
                }
                Match match = firstRoundMatches.get(i);
                if (match.getNextMatchId() == null) {
                    if (match.getPlayer2() == null) {
                        if (count == 0) {
                            newMatch.setPlayer1(match.getPlayer1());
                        } else if (count == 1) {
                            newMatch.setPlayer2(match.getPlayer1());
                        }
                        saveMatch(newMatch);
                    }
                    match.setNextMatchId(savedMatchId);
                    match.setNextMatchPlayerNumber(count);
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
                    .tournament(tournament)
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
                    .tournament(tournament)
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

    public List<Match> getMatchesByTournamentId(Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

}
