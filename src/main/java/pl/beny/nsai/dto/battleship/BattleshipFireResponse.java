package pl.beny.nsai.dto.battleship;

public class BattleshipFireResponse extends BattleshipStatusResponse {

    public interface BattleshipFireTurn {
        int PLAYER_TURN = 0;
        int COMPUTER_TURN = 1;
    }

    private Integer x;
    private Integer y;
    private Integer playerStatus;
    private Integer enemyStatus;
    private Integer fireTurn;

    public BattleshipFireResponse(Integer x, Integer y, Integer enemyStatus, Integer fireTurn) {
        super(BattleshipStatus.BATTLE);
        this.x = x;
        this.y = y;
        this.enemyStatus = enemyStatus;
        this.fireTurn = fireTurn;
    }

    public BattleshipFireResponse(Integer playerStatus, Integer fireTurn) {
        super(BattleshipStatus.BATTLE);
        this.playerStatus = playerStatus;
        this.fireTurn = fireTurn;
    }

    public BattleshipFireResponse(Integer gameStatus) {
        super(gameStatus);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(Integer playerStatus) {
        this.playerStatus = playerStatus;
    }

    public Integer getEnemyStatus() {
        return enemyStatus;
    }

    public void setEnemyStatus(Integer enemyStatus) {
        this.enemyStatus = enemyStatus;
    }

    public Integer getFireTurn() {
        return fireTurn;
    }

    public void setFireTurn(Integer fireTurn) {
        this.fireTurn = fireTurn;
    }
}
