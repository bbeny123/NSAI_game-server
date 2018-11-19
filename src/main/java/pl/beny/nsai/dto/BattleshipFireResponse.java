package pl.beny.nsai.dto;

public class BattleshipFireResponse extends AbstractBattleshipResponse {

    private Integer x;
    private Integer y;
    private Integer playerStatus;
    private Integer enemyStatus;

    public BattleshipFireResponse(Integer x, Integer y, Integer enemyStatus) {
        this.x = x;
        this.y = y;
        this.enemyStatus = enemyStatus;
        this.gameStatus = BattleshipStatus.BATTLE;
    }

    public BattleshipFireResponse(Integer gameStatus) {
        this.gameStatus = gameStatus;
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
