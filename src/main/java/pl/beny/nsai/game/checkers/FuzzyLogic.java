package pl.beny.nsai.game.checkers;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 * This class provides wrapper for FuzzyLogic library.
 */
public class FuzzyLogic {

    /**
     * @param playerPieces     Number of player pawns.
     * @param cpuPieces        Number of computer pawns.
     * @param currentDoneTurns Number of done turns.
     * @return Value that determines stage of game.
     */
    static double getStageVal(double playerPieces, double cpuPieces, double currentDoneTurns) throws Exception {
        //Open file with rules
        String fileName = "fcl/stage.fcl";
        FIS fis = FIS.load(fileName, true);

        //Check if file is loaded
        if (fis == null) {
            throw new Exception("Can't load file: '\" + fileName + \"'\"");
        }

        // Set inputs
        fis.setVariable("playerPieces", playerPieces);
        fis.setVariable("cpuPieces", cpuPieces);
        fis.setVariable("turns", currentDoneTurns);

        // Evaluate value
        fis.evaluate();

        return fis.getVariable("stage").getLatestDefuzzifiedValue();
    }
}
