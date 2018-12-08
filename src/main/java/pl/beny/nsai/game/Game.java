package pl.beny.nsai.game;

import java.time.LocalDateTime;

public interface Game {

    LocalDateTime getLastActivity();

    void setLastActivity(LocalDateTime dateTime);

}
