package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.MAN;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.QUEEN;

public class CheckersMan {

    public enum Type {
        MAN,
        QUEEN,
        PROHIBITED;

        Type() {
        }
    }

    public enum Side {
        WHITE,
        BLACK;

        Side() {
        }
    }

    public int x;
    public int y;
    public Side side = WHITE;
    public Type type = MAN;

    public CheckersMan(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CheckersMan(int x, int y, Side side, Type type) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.type = type;
    }

    public boolean whiteOrQueen() {
        return this.type == QUEEN || this.side == WHITE;
    }

    public boolean blackOrQueen() {
        return this.type == QUEEN || this.side == BLACK;
    }

    public void move(int x, int y) {
        if (y == 0 || y == 7) {
            this.type = QUEEN;
        }
        this.x = x;
        this.y = y;
    }

    public CheckersMan copy() {
        return new CheckersMan(this.x, this.y, this.side, this.type);
    }
}
