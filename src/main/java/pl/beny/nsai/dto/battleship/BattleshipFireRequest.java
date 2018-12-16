package pl.beny.nsai.dto.battleship;

import pl.beny.nsai.game.battleship.BattleshipBoard;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class BattleshipFireRequest {

    //fire coordinates
    private Integer x;
    private Integer y;

    public BattleshipFireRequest(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @NotNull
    @Min(0)
    @Max(BattleshipBoard.BOARD_SIZE)
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @NotNull
    @Min(0)
    @Max(BattleshipBoard.BOARD_SIZE)
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
