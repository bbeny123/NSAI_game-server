package pl.beny.nsai.dto.battleship;

public abstract class AbstractBattleshipResponse {

    public interface BattleshipStatus {
        int PREPARING = 0;
        int BATTLE = 1;
        int WIN = 2;
        int DEFEAT = 3;
    }

    protected Integer gameStatus;

    public Integer getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Integer gameStatus) {
        this.gameStatus = gameStatus;
    }
}
