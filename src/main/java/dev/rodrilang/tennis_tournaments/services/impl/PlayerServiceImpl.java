package dev.rodrilang.tennis_tournaments.services.impl;


import dev.rodrilang.tennis_tournaments.dtos.request.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.exceptions.DeletedPlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.DuplicatePlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.PlayerNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.rodrilang.tennis_tournaments.repositories.PlayerRepository;
import dev.rodrilang.tennis_tournaments.services.PlayerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;


    @Override
    public PlayerResponseDto create(PlayerRequestDto playerRequestDto) {

        this.verifyDni(playerRequestDto.dni());
        return playerMapper.toDto(playerRepository.save(playerMapper.toEntity(playerRequestDto)));
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @Override
    public List<PlayerResponseDto> getAll() {
        return playerRepository.findAllByDeletedFalse()
                .stream()
                .map(playerMapper::toDto)
                .toList();
    }

    @Override
    public PlayerResponseDto update(String dni, PlayerRequestDto playerRequestDto) {

        Player existingPlayer = this.findEntityByDni(dni);

        playerMapper.updatePlayerFromDto(playerRequestDto, existingPlayer);

        return playerMapper.toDto(playerRepository.save(existingPlayer));
    }


    @Override
    public void softDelete(String dni) {

        Player foundPlayer = this.findEntityByDni(dni);

        foundPlayer.setDeleted(true);
        playerRepository.save(foundPlayer);
    }

    @Override
    public PlayerResponseDto restoreDeletedPlayer(String dni) {

        Player player = playerRepository.findByDniAndDeleted(dni, true)
                .orElseThrow(() -> new PlayerNotFoundException("No eliminated player was found with the DNI:" + dni));

        player.setDeleted(false);
        playerRepository.save(player);

        return playerMapper.toDto(player);
    }

    @Override
    public PlayerResponseDto getByDni(String dni) {
        return playerMapper.toDto(playerRepository.findByDniAndDeleted(dni, false)
                .orElseThrow(() -> new PlayerNotFoundException(dni)));
    }

    @Override
    public PlayerResponseDto adjustPoints(String dni, int delta) {
        Player player = this.findEntityByDni(dni);

        int newPoints = player.getPoints() + delta;
        if (newPoints < 0) {
            throw new IllegalArgumentException("The score cannot be negative");
        }
        player.setPoints(newPoints);
        playerRepository.save(player);

        return playerMapper.toDto(player);
    }

    @Override
    public List<PlayerResponseDto> getPlayersOrderedByPoints() {
        return List.of();
    }

    @Override
    public List<PlayerResponseDto> getPlayersByTournamentId(Long tournamentId) {
        return List.of();
    }


    private void verifyDni(String dni) {

        if (Boolean.TRUE.equals(playerRepository.existsByDniAndDeleted(dni, false))) {
            throw new DuplicatePlayerException(dni);
        }

        if (Boolean.TRUE.equals(playerRepository.existsByDniAndDeleted(dni, true))) {
            throw new DeletedPlayerException(dni);
        }
    }

    private Player findEntityByDni(String dni) {

        return playerRepository.findByDniAndDeleted(dni, false)
                .orElseThrow(() -> new PlayerNotFoundException(dni));
    }


/*

    private Integer getMatchesWon(Integer id) throws TournamentNotFoundException {
        List<Match> matchesByPlayer = getMatchesByPlayer(id);
        Integer matchesWon = 0;

        for (Match m : matchesByPlayer) {
            int idWinner = 0;
            try {
                idWinner = getWinner(m).getIdPlayer();
            } catch (IncompleteMatchException e) {
                System.out.println("The match has not finished or the result was not loaded.");
            }

            if (idWinner == id) {
                matchesWon++;
            }
        }
        return matchesWon;
    }


    public Player getWinner(Match match) throws IncompleteMatchException {
        if (match.getResult() == null) {
            throw new IncompleteMatchException("The match has not finished or the result was not loaded.");
        }

        // Check if player one has won two sets
        if (match.getResult().getSetsWonPlayerOne() == 2) {
            return match.getPlayerOne();
        }
        // Check if player two has won two sets
        else if (match.getResult().getSetsWonPlayerTwo() == 2) {
            return match.getPlayerTwo();
        }

        // If there is no defined winner...
        throw new IncompleteMatchException("There is no defined winner.");
    }

    public List<Match> getMatchesByPlayer(Integer idPlayer) throws FileProcessingException, TournamentNotFoundException {

        List<Match> playerMatches = new ArrayList<>();
        for (Tournament tournament : tournamentService.getAllTournaments())
            for (Round round : tournament.getRounds()) {
                for (Match match : round.getMatches()) {
                    if (match.getPlayerOne().getIdPlayer().equals(idPlayer) || match.getPlayerTwo().getIdPlayer().equals(idPlayer)) {
                        playerMatches.add(match);
                    }
                }
            }
        return playerMatches;
    }

    public String showStatsByPlayer(Integer id) throws IncompleteMatchException, PlayerNotFoundException, TournamentNotFoundException {
        Player player = findPlayerById(id);

        int matchesPlayed = getMatchesByPlayer(id).size();
        int matchesWon = getMatchesWon(id);
        int matchesLost = matchesPlayed - matchesWon;
        double percentageWon = (matchesPlayed > 0) ? ((double) matchesWon / matchesPlayed) * 100 : 0;
        int totalPoints = player.getPoints();

        int maxNameLength = 24;

        String formattedName = String.format("%s %s", player.getName(), player.getLastName());
        if (formattedName.length() > maxNameLength) {
            formattedName = formattedName.substring(0, maxNameLength - 3) + "...";
        }

        int padding = maxNameLength - formattedName.length();

        String formattedPercentage = String.format("%.2f%%", percentageWon);

        return String.format("\n" +
                """
                        -------------------------------------
                        |          Player Statistics        |
                        -------------------------------------
                        | Name: %s%s    |
                        |                                   |
                        | Matches Played      : %-11d |
                        | Matches Won         : %-11d |
                        | Matches Lost        : %-11d |
                        | Won/Lost Percentage : %-11s |
                        | Total Points        : %-11d |
                        -------------------------------------
                        """, formattedName, " ".repeat(padding), matchesPlayed, matchesWon, matchesLost, formattedPercentage, totalPoints
        );
    }

    private List<Player> getPlayerRankings() throws PlayerNotFoundException {
        List<Player> players = getAllPlayers();

        players.sort((p1, p2) -> p2.getPoints().compareTo(p1.getPoints()));

        return players;
    }

    public String showPlayerRankings() throws PlayerNotFoundException {
        List<Player> rankedPlayerList = getPlayerRankings();

        final int NAME_COLUMN_WIDTH = 25;
        final int POINTS_COLUMN_WIDTH = 6;

        StringBuilder rankingStr = new StringBuilder();

        rankingStr.append("\n" +
                """
                        --------------------------------------------
                        |                  Ranking                 |
                        --------------------------------------------
                        | Pos | Name                      | Points |
                        --------------------------------------------
                        """);

        for (int i = 0; i < rankedPlayerList.size(); i++) {
            Player player = rankedPlayerList.get(i);

            rankingStr.append(String.format("| %-4d| %-" + NAME_COLUMN_WIDTH + "s | %-" + POINTS_COLUMN_WIDTH + "d |\n",
                    (i + 1), player.getName() + " " + player.getLastName(), player.getPoints()));
        }

        rankingStr.append("--------------------------------------------\n");

        return rankingStr.toString();
    }


 */
}
