package kalah.game.service;

import kalah.game.models.CreateNewGamePayload;
import kalah.game.models.Game;
import kalah.game.models.Pit;
import kalah.game.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    public static final int SEEDS_PER_PIT = 6;
    public static final String FIRST_PLAYER_NAME = "firstPlayer";
    public static final String SECOND_PLAYER_NAME = "secondPlayer";

    @Captor
    ArgumentCaptor<Game> gameArgumentCaptor;

    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(gameRepository);
    }

    @Test
    void persistsNewGame() {
        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME);

        gameService.startGame(payload, SEEDS_PER_PIT);

        verify(gameRepository).save(gameArgumentCaptor.capture());

        Game gameToBePersisted = gameArgumentCaptor.getValue();

        assertThat(gameToBePersisted.getFirstPlayer().getName()).isEqualTo(FIRST_PLAYER_NAME);
        assertThat(gameToBePersisted.getSecondPlayer().getName()).isEqualTo(SECOND_PLAYER_NAME);

        Pit firstPitInTheBoard = gameToBePersisted.getBoard().getPits().get(0);
        assertThat(firstPitInTheBoard.getAmountOfSeeds()).isEqualTo(SEEDS_PER_PIT);
    }

    @Test
    void returnsCreatedGame() {
        Game game = new Game(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME, SEEDS_PER_PIT);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        CreateNewGamePayload payload = new CreateNewGamePayload(FIRST_PLAYER_NAME, SECOND_PLAYER_NAME);

        Game createdGame = gameService.startGame(payload, SEEDS_PER_PIT);

        assertThat(createdGame).isEqualTo(game);
    }
}
