package pl.beny.nsai.dto;

import javax.validation.constraints.NotEmpty;

public class GameRequest {

    private String difficulty;

    @NotEmpty
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
