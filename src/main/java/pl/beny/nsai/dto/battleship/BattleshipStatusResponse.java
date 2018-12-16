package pl.beny.nsai.dto.battleship;

public class BattleshipStatusResponse {

    public interface BattleshipStatus {
        int PREPARING = 0;
        int BATTLE = 1;
        int WIN = 2;
        int DEFEAT = 3;
    }

    public BattleshipStatusResponse(Integer gameStatus) {
        this.gameStatus = gameStatus;
    }

    protected Integer gameStatus;   //indicates phase of the game (values:  BattleshipStatus)

    public Integer getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Integer gameStatus) {
        this.gameStatus = gameStatus;
    }
}
