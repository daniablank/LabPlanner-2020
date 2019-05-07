package org.ucb.manage140l.model;

import java.time.LocalDateTime;

/**
 * Created by Daniel on 3/7/2019.
 */
public class Session {

    private final LocalDateTime startTime;
    private final Section section;

    public Session(LocalDateTime start, Section sec){
        this.section = sec;
        this.startTime = start;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Section getSection() {
        return section;
    }
}
