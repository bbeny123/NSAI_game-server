package pl.beny.nsai.dto.battleship;

public class BattleshipFireResponse extends BattleshipStatusResponse {

    private Integer x;
    private Integer y;
    private Integer playerStatus;
    private Integer enemyStatus;

    public BattleshipFireResponse(Integer x, Integer y, Integer enemyStatus) {
        super(BattleshipStatus.BATTLE);
        this.x = x;
        this.y = y;
        this.enemyStatus = enemyStatus;
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
}
