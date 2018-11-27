package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.*;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.*;

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

    public void move(CheckersMan target) {
        if (target.y == 0 || target.y == 7) {
            this.type = QUEEN;
        }
        this.x = target.x;
        this.y = target.y;
    }

    public CheckersMan copy() {
        return new CheckersMan(this.x, this.y, this.side, this.type);
    }
}
