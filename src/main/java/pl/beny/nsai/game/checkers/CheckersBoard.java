package pl.beny.nsai.game.checkers;

import pl.beny.nsai.game.checkers.CheckersMan.Side;
import pl.beny.nsai.game.checkers.CheckersMan.Type;
import pl.beny.nsai.util.GamesException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.beny.nsai.game.checkers.CheckersMan.Side.BLACK;
import static pl.beny.nsai.game.checkers.CheckersMan.Side.WHITE;
import static pl.beny.nsai.game.checkers.CheckersMan.Type.*;
import static pl.beny.nsai.game.checkers.CheckersResult.Status.BLACK_TURN;
import static pl.beny.nsai.game.checkers.CheckersResult.Status.WHITE_TURN;
import static pl.beny.nsai.util.GamesException.GamesErrors.*;

//instance of CheckersBoard (1 per game)
public class CheckersBoard {
    //number of done turns
    private int turns = 0;

    //array of CheckersMan
    private CheckersMan[][] board = new CheckersMan[8][8];

    //initialize board with proper checkers
    public CheckersBoard() {
        for (int y = 0; y < 8; y++) {
            for (int x = (y + 1) % 2; x < 8; x += 2) {
                board(x, y, new CheckersMan(x, y, null, PROHIBITED));
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int x = y % 2; x < 8; x += 2) {
                board(x, y, new CheckersMan(x, y, BLACK, MAN));
            }
        }

        for (int y = 7; y > 4; y--) {
            for (int x = y % 2; x < 8; x += 2) {
                board(x, y, new CheckersMan(x, y, WHITE, MAN));
            }
        }
    }

    //initialize CheckersBoard with copy of give board
    private CheckersBoard(CheckersMan[][] board) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != null) {
                    this.board[x][y] = board[x][y].copy();
                }
            }
        }
    }

    //get copy of that CheckersBoard instance
    public CheckersBoard copy() {
        return new CheckersBoard(this.board);
    }

    //get CheckersMan for given x,y
    private CheckersMan board(int x, int y) {
        return board[y][x];
    }

    //get CheckersMan for given men.x and men.y
    private CheckersMan board(CheckersMan men) {
        return board[men.y][men.x];
    }

    //set CheckersMan for given x,y
    private void board(int x, int y, CheckersMan men) {
        board[y][x] = men;
    }

    //move checker from given x1, y1 to x2, y2
    public CheckersResult move(int x1, int y1, int x2, int y2, Side side) throws GamesException {
        return move(board(x1, y1), new CheckersMan(x2, y2), side);
    }

    //validates and executes move
    private CheckersResult move(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        valid(source, target, side);    //check if given move is valid

        List<CheckersMan> captureMoves = getCaptureMoves(source);   //get all move with capture for source checker
        CheckersResult result = new CheckersResult();

        //check if requested move equals to one of the captureMoves (if captureMoves not empty)
        if (!captureMoves.isEmpty() && captureMoves.stream().noneMatch(move -> target.x == move.x && target.y == move.y)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED);
        }

        if (!captureMoves.isEmpty()) {
            result.setStatus(side == WHITE ? BLACK_TURN : WHITE_TURN);  //set next turn for opposite side
            capture(source, target, result, side);  //move (with capture) checker from source to target
        } else {
            validMove(source, target, side);    //check if given non capture move is valid
            move(source, target, result);       //move checker from source to target
            result.setStatus(side == WHITE ? BLACK_TURN : WHITE_TURN);  //set next turn for opposite side
        }

        turns += 1;
        return result;
    }

    //checks if given move is valid
    private void valid(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        if (source == null || source.type == PROHIBITED) {                  //check if source is not null and source coordinates are not prohibited
            throw new GamesException(CHECKERS_NO_MAN);
        }
        if (side != source.side) {                                          //check if source checker belongs to requested side
            throw new GamesException(CHECKERS_OPPOSITE_MAN);
        }
        if (board(target) != null && board(target).type == PROHIBITED) {    //check if target is not null and target coordinates are not prohibited
            throw new GamesException(CHECKERS_POSITION_PROHIBITED);
        } else if (board(target) != null) {                                 //check if target is not null
            throw new GamesException(CHECKERS_POSITION_TAKEN);
        }
    }

    //checks if given non capture move is valid
    private void validMove(CheckersMan source, CheckersMan target, Side side) throws GamesException {
        //check if move "length" = 1 and if move in given direction is allowed for source checker
        if (Math.abs(target.x - source.x) != 1 || Math.abs(target.y - source.y) != 1 || (source.type != QUEEN && ((side == WHITE && target.y >= source.y) || (side == BLACK && target.y <= source.y)))) {
            throw new GamesException(CHECKERS_NOT_ALLOWED);
        }
    }

    //moves (with capture) checker from source to target
    private void capture(CheckersMan source, CheckersMan target, CheckersResult result, Side side) throws GamesException {
        result.setCaptured(capture(source, target));    //capture checker between source and target
        move(source, target, result);                   //move source to target

        //check if there is another capture for the moved checker
        List<CheckersMan> forceToCapture = getCaptureMoves(board(target));
        if (!forceToCapture.isEmpty()) {
            result.setForceToCapture(new CheckersMoves(board(target), forceToCapture));
            result.setStatus(side == WHITE ? WHITE_TURN : BLACK_TURN);
        }
    }

    //captures checker between source and target
    private CheckersMan capture(CheckersMan source, CheckersMan target) throws GamesException {
        CheckersMan captured = board((source.x + target.x) / 2, (source.y + target.y) / 2); //computer capture checker position

        if (captured == null) { //check if capture is not nu;;
            throw new GamesException(CHECKERS_ERROR);
        }

        board(captured.x, captured.y, null);    //set capture position as null (remove capture from board)
        return captured;
    }

    //move source to target
    private void move(CheckersMan source, CheckersMan target, CheckersResult result) {
        result.setSource(source.copy());
        board(source.x, source.y, null);
        board(target.x, target.y, source);
        source.move(target.x, target.y);
        result.setTarget(source.copy());
    }

    //get possible non capture or capture moves for all checkers which belongs to given side
    public List<CheckersMoves> getMoves(Side side, boolean captures) {
        List<CheckersMoves> moves = new ArrayList<>();
        getAllCheckers().forEach(c -> {
            if (c.side == side) {
                List<CheckersMan> men;
                if (captures) {
                    men = getCaptureMoves(c);
                } else {
                    men = getMoves(c);
                }
                if (!men.isEmpty()) {
                    moves.add(new CheckersMoves(c, men));
                }
            }
        });
        return moves;
    }

    //get all possible non capture moves for given CheckersMan
    private List<CheckersMan> getMoves(CheckersMan source) {
        List<CheckersMan> men = new ArrayList<>();

        if (source.whiteOrQueen() && source.x - 1 >= 0 && source.y - 1 >= 0 && board(source.x - 1, source.y - 1) == null) {
            men.add(new CheckersMan(source.x - 1, source.y - 1, source.side, source.type));
        }
        if (source.whiteOrQueen() && source.x + 1 <= 7 && source.y - 1 >= 0 && board(source.x + 1, source.y - 1) == null) {
            men.add(new CheckersMan(source.x + 1, source.y - 1, source.side, source.type));
        }
        if (source.blackOrQueen() && source.x - 1 >= 0 && source.y + 1 <= 7 && board(source.x - 1, source.y + 1) == null) {
            men.add(new CheckersMan(source.x - 1, source.y + 1, source.side, source.type));
        }
        if (source.blackOrQueen() && source.x + 1 <= 7 && source.y + 1 <= 7 && board(source.x + 1, source.y + 1) == null) {
            men.add(new CheckersMan(source.x + 1, source.y + 1, source.side, source.type));
        }

        return men;
    }

    //get all possible capture moves for given CheckersMan
    private List<CheckersMan> getCaptureMoves(CheckersMan source) {
        List<CheckersMan> men = new ArrayList<>();

        //UPPER LEFT
        if (source.whiteOrQueen() && source.x - 2 >= 0 && source.y - 2 >= 0 && board(source.x - 2, source.y - 2) == null) {
            CheckersMan man = board(source.x - 1, source.y - 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x - 2, source.y - 2, source.side, source.type));
            }
        }

        //UPPER RIGHT
        if (source.whiteOrQueen() && source.x + 2 <= 7 && source.y - 2 >= 0 && board(source.x + 2, source.y - 2) == null) {
            CheckersMan man = board(source.x + 1, source.y - 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x + 2, source.y - 2, source.side, source.type));
            }
        }

        //BOTTOM LEFT
        if (source.blackOrQueen() && source.x - 2 >= 0 && source.y + 2 <= 7 && board(source.x - 2, source.y + 2) == null) {
            CheckersMan man = board(source.x - 1, source.y + 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x - 2, source.y + 2, source.side, source.type));
            }
        }

        //BOTTOM RIGHT
        if (source.blackOrQueen() && source.x + 2 <= 7 && source.y + 2 <= 7 && board(source.x + 2, source.y + 2) == null) {
            CheckersMan man = board(source.x + 1, source.y + 1);
            if (man != null && source.side != man.side) {
                men.add(new CheckersMan(source.x + 2, source.y + 2, source.side, source.type));
            }
        }

        return men;
    }

    //checks if any move is possible for given side
    public boolean moveOrCapturePossible(Side side) {
        return !getMoves(side, false).isEmpty() || !getMoves(side, true).isEmpty();
    }

    //check if requested move is equals to one of the forced move
    public void checkForcedCapture(int x1, int y1, int x2, int y2, CheckersMoves forcedCapture) throws GamesException {
        if (forcedCapture.getSource().x != x1 || forcedCapture.getSource().y != y1 || forcedCapture.getMoves().stream().noneMatch(move -> move.x == x2 && move.y == y2)) {
            throw new GamesException(CHECKERS_CAPTURE_FORCED_STATE);
        }
    }

    //get all checkers on the board
    public List<CheckersMan> getAllCheckers() {
        List<CheckersMan> men = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = y % 2; x < 8; x += 2) {
                if (board(x, y) != null) {
                    men.add(board(x, y));
                }
            }
        }
        return men;
    }

    //get all checkers on the board of given side
    public List<CheckersMan> getCheckers(Side side) {
        return getAllCheckers().stream().filter(checker -> checker.side == side).collect(Collectors.toList());
    }

    //get all checkers on the board of given side and type
    public List<CheckersMan> getCheckers(Side side, Type type) {
        return getCheckers(side).stream().filter(checker -> checker.side == side && checker.type == type).collect(Collectors.toList());
    }


    //returns number of done turns
    public int getTurns() {
        return turns;
    }
}