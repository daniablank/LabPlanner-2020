package org.ucb.c5.Assigner.constructionfile;

import doodle.ParsePoll;
import doodle.ProcessPoll;
import doodle.Section;
import doodle.Session;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Created by Daniel on 3/2/2019.
 */
public class SessionGenerator {


    public SessionGenerator(){
    }

    public void initiate(){


    }

    public List<Session> run(Set<Section> sectionSet, LocalDateTime start, LocalDateTime end){
        List<Section> sections = new ArrayList<>();
        LocalDateTime firstDay = start;
        LocalDateTime lastDay = end;
        DayOfWeek today = firstDay.getDayOfWeek();
        LocalTime nowTime = LocalTime.of(firstDay.getHour(), firstDay.getMinute());
        while(sections.size() < sectionSet.size()) {
            boolean foundToday = false;
            LocalTime champ = LocalTime.MAX;
            Section winner = null;
            for (Section sec : sectionSet) {
                if(sec.getDay().equals(today)){
                    foundToday = true;
                    if(nowTime.isBefore(sec.getStart())){
                        if(sec.getStart().isBefore(champ)){
                            champ = sec.getStart();
                            winner = sec;
                        }
                    }
                }
            }
            if(!foundToday){
                today = today.plus(1);
            }
            else if(winner != null){
                sectionSet.remove(winner);
                nowTime = winner.getStart();
                sections.add(winner);
            }

        }
        List<Session> sessions = new ArrayList<>();
        LocalDateTime currDay = firstDay;
        int index = 0;
        while(currDay.isBefore(lastDay)) {
            Section nextSec = sections.get(index);
            DayOfWeek dayOfSess = nextSec.getDay();
            LocalTime startTime = nextSec.getStart();
            if(! dayOfSess.equals(currDay.getDayOfWeek())) {
                currDay = currDay.with(TemporalAdjusters.next(dayOfSess));
            }
            currDay = currDay.withMinute(startTime.getMinute());
            currDay = currDay.withHour(startTime.getHour());
            index++;
            if (index >= sections.size()) {
                index = 0;
            }
            sessions.add( new Session(firstDay, nextSec));
        }
        return sessions;
    }

    public static void main(String[] args) throws Exception{
        ParsePoll parser = new ParsePoll();
        parser.initiate();
        String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data" + File.separator + "Doodle.txt";
        Map<Section, Set<String>> secToStudents = parser.run(filepath);
        ProcessPoll pollProcessor = new ProcessPoll();
        pollProcessor.initiate();
        Set<String> compnames = new HashSet<>();
        compnames.add("Nathan Wong");
        compnames.add("Daniel Blank");
        compnames.add("Kelly Huang");
        compnames.add("Arjun Chandran");
        compnames.add("Zack");
        Map<Section, Set<String>> processedPoll = pollProcessor.run(secToStudents, compnames);

        SessionGenerator sessGen = new SessionGenerator();
        sessGen.initiate();

        List<Session> sessions = sessGen.run(processedPoll.keySet(), LocalDateTime.of(2019, Month.JANUARY, 22, 0, 0), LocalDateTime.of(2019, Month.MAY, 15, 0, 0) );

        System.out.println(sessions);

    }

}
