package pl.beny.nsai.game;

import pl.beny.nsai.util.GamesException;

import java.time.LocalDateTime;

import static pl.beny.nsai.util.GamesException.GamesErrors.DIFFICULTY_INCORRECT;

public abstract class Game {

    private LocalDateTime lastActivity = LocalDateTime.now();

    public LocalDateTime lastActivity() {
        return lastActivity;
    }

    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    public void setDifficultyLevel(String difficultyLevel) throws GamesException {
        try {
            setDifficulty(difficultyLevel);
        } catch (Exception ex) {
            throw new GamesException(DIFFICULTY_INCORRECT);
        }
    }

    protected abstract void setDifficulty(String difficultyLevel);

}
