package pl.beny.nsai.game;

import pl.beny.nsai.util.GamesException;

import java.time.LocalDateTime;
import java.util.List;

import static pl.beny.nsai.util.GamesException.GamesErrors.DIFFICULTY_INCORRECT;

public abstract class Game {

    private LocalDateTime lastActivity = LocalDateTime.now();

    public void newGame(String difficultyLevel) {
        try {
            setDifficulty(difficultyLevel);
        } catch (Exception ex) {
            throw new GamesException(DIFFICULTY_INCORRECT);
        }
    }

    public void newGameAsync() {
    }

    public LocalDateTime lastActivity() {
        return lastActivity;
    }

    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    protected abstract void setDifficulty(String difficultyLevel);

    public abstract List<Object> moveAI();

}
