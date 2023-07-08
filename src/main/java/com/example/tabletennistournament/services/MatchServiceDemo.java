//package com.example.tabletennistournament.services;
//
//import com.example.tabletennistournament.enums.MatchType;
//import com.example.tabletennistournament.models.Match;
//import com.example.tabletennistournament.models.Tournament;
//import com.example.tabletennistournament.repositories.MatchRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MatchServiceDemo {
//    private final MatchRepository matchRepository;
//    private final TournamentService tournamentService;
//
//    public void buildTournamentGrid(Long tournamentId) {
//        Tournament tournament = tournamentService.getById(tournamentId);
//        List<String> participants = tournament.getUsers();
//        Collections.shuffle(participants);
//        List<Match> firstRoundMatches = new ArrayList<>();
//        List<Match> secondRoundMatches = new ArrayList<>();
//        List<Match> thirdRoundMatches = new ArrayList<>();
//        List<Match> fourthRoundMatches = new ArrayList<>();
//        double participantsSize = participants.size();
//        while (true) {
//            if(participantsSize % 4.0 > 0.0) {
//                participantsSize -= 4;
//                Match match1 = Match.builder().matchType(MatchType.FIRST_ROUND).tournament(tournament).build();
//                Match match2 = Match.builder().matchType(MatchType.FIRST_ROUND).tournament(tournament).build();
//                firstRoundMatches.addAll(List.of(match1, match2));
//            } else {
//                break;
//            }
//        }
//        int amountFirstRound = firstRoundMatches.size();
//        int participantsSizeInt = participants.size() - 1;
//        for (Match firstRound : firstRoundMatches) {
//            if (participantsSizeInt > amountFirstRound) {
//                firstRound.setPlayer1(participants.get(participantsSizeInt));
//                participantsSizeInt--;
//                firstRound.setPlayer2(participants.get(participantsSizeInt));
//                participantsSizeInt--;
//                amountFirstRound--;
//            } else if (participantsSizeInt == amountFirstRound) {
//                firstRound.setPlayer1(participants.get(participantsSizeInt));
//                participantsSizeInt--;
//                amountFirstRound--;
//            } else {
//                break;
//            }
//        }
//
//        int amountOfSecondRound = participants.size() <= 4
//                ? 1 : (participants.size() + 7) / 8 * 2;
//
//        saveMatches(firstRoundMatches);
//    }
//
//    public void saveMatches(List<Match> matches) {
//        matchRepository.saveAll(matches);
//    }
//
//}
