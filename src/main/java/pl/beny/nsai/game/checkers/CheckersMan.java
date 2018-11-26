package pl.beny.nsai.game.checkers;

import pl.beny.nsai.game.checkers.Checkers.Side;

import static pl.beny.nsai.game.checkers.Checkers.Side.BLACK;
import static pl.beny.nsai.game.checkers.Checkers.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.MAN;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.QUEEN;

public class CheckersMan {

    public enum Type {
        MAN,
        QUEEN;

        Type() {
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

    public boolean oppositeSide(CheckersMan man) {
        return this.side != man.side;
    }

    public boolean whiteOrQueen() {
        return this.type == QUEEN || this.side == WHITE;
    }

    public boolean blackOrQueen() {
        return this.type == QUEEN || this.side == BLACK;
    }
}
