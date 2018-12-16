package pl.beny.nsai.game.battleship;

public class BattleshipPlayer {

    private BattleshipShips ships = new BattleshipShips();  //instance of BattleshipShips
    private BattleshipBoard board = new BattleshipBoard();  //instance of BattleshipBoard

    public BattleshipShips getShips() {
        return ships;
    }

    public void setShips(BattleshipShips ships) {
        this.ships = ships;
    }

    public BattleshipBoard getBoard() {
        return board;
    }

    public void setBoard(BattleshipBoard board) {
        this.board = board;
    }
}
