package pl.beny.nsai.game;

import pl.beny.nsai.util.GamesException;

import java.time.LocalDateTime;
import java.util.List;

import static pl.beny.nsai.util.GamesException.GamesErrors.DIFFICULTY_INCORRECT;

//abstract game instance
public abstract class Game {

    private LocalDateTime lastActivity = LocalDateTime.now();   //last activity of player

    //starts the new game with given difficulty
    public void newGame(String difficultyLevel) {
        try {
            setDifficulty(difficultyLevel);
        } catch (Exception ex) {
            throw new GamesException(DIFFICULTY_INCORRECT);
        }
    }

    //method which should be executed after new game starts
    public void newGameAsync() {
    }

    public LocalDateTime lastActivity() {
        return lastActivity;
    }

    //update last activity of player to current time
    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    protected abstract void setDifficulty(String difficultyLevel);

    //call AI move
    public abstract List<Object> moveAI();

}
