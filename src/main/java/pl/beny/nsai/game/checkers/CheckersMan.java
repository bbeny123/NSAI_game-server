package pl.beny.nsai.game.checkers;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.MAN;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.QUEEN;

//contains CheckersMan data
public class CheckersMan {

    //checker type
    public enum Type {
        MAN,
        QUEEN,
        PROHIBITED;

        Type() {
        }
    }

    //checker side
    public enum Side {
        WHITE,
        BLACK;

        Side() {
        }
    }

    public int x;               //checker x position
    public int y;               //checker y position
    public Side side = WHITE;   //checker side
    public Type type = MAN;     //checker type

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

    //indicates if checker is white or queen
    public boolean whiteOrQueen() {
        return this.type == QUEEN || this.side == WHITE;
    }

    //indicates if checker is black or queen
    public boolean blackOrQueen() {
        return this.type == QUEEN || this.side == BLACK;
    }

    //change checker position to given x,y
    public void move(int x, int y) {
        if (y == 0 || y == 7) {
            this.type = QUEEN;
        }
        this.x = x;
        this.y = y;
    }

    //get copy of this checker
    public CheckersMan copy() {
        return new CheckersMan(this.x, this.y, this.side, this.type);
    }

    @Override
    public String toString() {
        return "CheckersMan{" +
                "x=" + x +
                ", y=" + y +
                ", side=" + side +
                ", type=" + type +
                '}';
    }
}
