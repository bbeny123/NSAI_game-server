package pl.beny.nsai.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CheckersRequest {

    private Integer x1;
    private Integer y1;
    private Integer x2;
    private Integer y2;

    @NotNull
    @Min(0)
    @Max(7)
    public Integer getX1() {
        return x1;
    }

    public void setX1(Integer x1) {
        this.x1 = x1;
    }

    @NotNull
    @Min(0)
    @Max(7)
    public Integer getY1() {
        return y1;
    }

    public void setY1(Integer y1) {
        this.y1 = y1;
    }

    @NotNull
    @Min(0)
    @Max(7)
    public Integer getX2() {
        return x2;
    }

    public void setX2(Integer x2) {
        this.x2 = x2;
    }

    @NotNull
    @Min(0)
    @Max(7)
    public Integer getY2() {
        return y2;
    }

    public void setY2(Integer y2) {
        this.y2 = y2;
    }

}
