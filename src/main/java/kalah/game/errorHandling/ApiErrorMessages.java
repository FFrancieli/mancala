package kalah.game.errorHandling;

import lombok.Getter;

@Getter
public enum ApiErrorMessages {
    GAME_NOT_FOUND("Cannot find game session with id %s", "NotFound"),
    SOWING_FROM_KALAH("Pit index must be between 0 and 5 or 7 and 12. Received value: %s", "AttemptToSowFromKalah"),
    INVALID_PIT_INDEX("Pit index must be between 0 and 5 or 7 and 12. Received value: %s", "InvalidPitIndex"),
    NOT_ASSIGNED_TO_PLAYER("Pit with id %d is not assigned to %s", "PitNotAssignedToPlayer"),
    SOWING_EMPTY_PIT("Cannot sow from empty pit. Pit on index %d has %d seeds", "InvalidMove");

    private final String message;
    private final String error;

    ApiErrorMessages(String message, String error) {
        this.error = error;
        this.message = message;
    }
}
