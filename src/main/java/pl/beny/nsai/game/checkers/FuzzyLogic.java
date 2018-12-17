package pl.beny.nsai.game.checkers;

import net.sourceforge.jFuzzyLogic.FIS;

public class FuzzyLogic {
    static double getStageVal(double playerPieces, double cpuPieces, double currentDoneTurns) throws Exception {
        String fileName = "fcl/stage.fcl";
        FIS fis = FIS.load(fileName, true);

        if (fis == null) {
            throw new Exception("Can't load file: '\" + fileName + \"'\"");
        }

        // Set inputs
        fis.setVariable("playerPieces", playerPieces);
        fis.setVariable("cpuPieces", cpuPieces);
        fis.setVariable("turns", currentDoneTurns);

        // Evaluate
        fis.evaluate();

        return fis.getVariable("stage").getLatestDefuzzifiedValue();
    }
}
