package pl.beny.nsai.dto;

import javax.validation.constraints.NotEmpty;

//new game request
public class GameRequest {

    private String difficulty;  //game difficulty

    @NotEmpty
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
