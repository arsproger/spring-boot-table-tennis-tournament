package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.MatchType;
import com.example.tabletennistournament.enums.TournamentType;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.Match;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.models.User;
import com.example.tabletennistournament.repositories.MatchRepository;
import com.example.tabletennistournament.repositories.TournamentRepository;
import com.example.tabletennistournament.repositories.UserRepository;
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
    private final UserService userService;
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;

    public void buildTournamentGrid(Tournament tournament) {
        List<User> participants = tournament.getUsers();
        if (participants.size() < 3 || participants.size() > 32) {
            throw new AppException("Число игроков должно быть от 3 до 32", HttpStatus.BAD_REQUEST);
        }
        Collections.shuffle(participants);
        List<Match> firstRoundMatches = new ArrayList<>();
        List<Match> secondRoundMatches = new ArrayList<>();
        List<Match> thirdRoundMatches = new ArrayList<>();
        List<Match> fourthRoundMatches = new ArrayList<>();
        double participantsSize = participants.size();

        int matchSize = 0;
        if (participantsSize > 2 && participantsSize <= 4) {
            matchSize = 2;
        } else if (participantsSize > 4 && participantsSize <= 8) {
            matchSize = 4;
        } else if (participantsSize > 8 && participantsSize <= 16) {
            matchSize = 8;
        } else if (participantsSize > 16 && participantsSize <= 32) {
            matchSize = 16;
        }

        while (participantsSize > matchSize) {
            for (int i = 0; i < matchSize; i++) {
                Match match = Match.builder().matchType(MatchType.FIRST_ROUND).tournament(tournament).build();
                firstRoundMatches.add(match);
                saveMatch(match);
            }
            participantsSize -= matchSize;
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

    public void finishTournament(Long tournamentId) {
        Tournament tournament = tournamentService.getById(tournamentId);
        List<Match> matches = getMatchesByTournamentId(tournamentId);
        MatchType maxMatchType;

        if (matches.size() == 3) {
            maxMatchType = MatchType.SECOND_ROUND;
        } else if (matches.size() == 7) {
            maxMatchType = MatchType.THIRD_ROUND;
        } else if (matches.size() == 15) {
            maxMatchType = MatchType.FOURTH_ROUND;
        } else if (matches.size() == 31) {
            maxMatchType = MatchType.FIFTH_ROUND;
        } else {
            throw new AppException("Ошибка при завершении турнира", HttpStatus.BAD_REQUEST);
        }

        for (Match match : matches) {
            User user = match.getWinner();
            if (user != null) {
                if (match.getMatchType().equals(maxMatchType)) {
                    user.setNumberOfMatchesWon(user.getNumberOfMatchesWon() + 1);
                    user.setNumberOfTournamentsWon(user.getNumberOfTournamentsWon() + 1);
                    userRepository.save(user);
                } else {
                    user.setNumberOfMatchesWon(user.getNumberOfMatchesWon() + 1);
                    userRepository.save(user);
                }
            }
        }

        tournament.setTournamentType(TournamentType.FINISH);
        tournamentRepository.save(tournament);
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

    public void updateMatchAndScores(Long matchId, Long winnerId, Integer player1Score, Integer player2Score) {
        User winner = userService.getById(winnerId);
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
    }

    public List<Match> getMatchesByTournamentId(Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

}
