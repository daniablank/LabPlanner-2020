package org.ucb.manage140l.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author J. Christopher Anderson
 */
public class Section {
    private final DayOfWeek day;
    private final LocalTime start;
    private final LocalTime end;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");;

    public Section(DayOfWeek day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return day.toString() + "  " + start.format(formatter) + " - " +  end.format(formatter);
    }
}
