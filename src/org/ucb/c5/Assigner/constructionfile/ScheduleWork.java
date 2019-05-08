package org.ucb.c5.Assigner.constructionfile;

import java.io.File;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.time.LocalDateTime;

import doodle.ParsePoll;
import doodle.ProcessPoll;
import org.ucb.constructionFileModel.*;
import doodle.Section;
import doodle.Session;

/**
 * Created by Daniel on 3/2/2019.
 */
public class ScheduleWork {

    private SessionGenerator sessGen;

    private Map<Operation, Integer> minsToWait;

    public void initiate(){
        sessGen = new SessionGenerator();
        minsToWait = new HashMap<>();
        minsToWait.put(Operation.pcr, 240);
        minsToWait.put(Operation.cleanup, 0);
        minsToWait.put(Operation.digest, 60);
        minsToWait.put(Operation.ligate, 30);
        minsToWait.put(Operation.transform, 16 * 60);
        minsToWait.put(Operation.miniprep, 0);
        minsToWait.put(Operation.inoculate, 12 * 60);
    }

    public Map<Session, List<Operation>> run(List<List<ConstructionFile>> threads, Map<Section, Set<String>> availability, LocalDateTime start, LocalDateTime end){ //multiple operations run per day
        //TODO: Not just an Operation; make an Assignment obj or similar.
        //TODO: possibly change this to the SheetGenerator class?
        List<List<ConstructionFile>> remainingThreads = new ArrayList<>(threads);
        Map<Section, Integer> numStudentsLeft = new HashMap<>();
        for(Section sec : availability.keySet()){
            numStudentsLeft.put(sec, availability.get(sec).size());
        }
        Map<Session, List<Operation>> schedule = new HashMap<>();
        Map<Session, Integer> studentsAtTime = new HashMap<>();
        sessGen.initiate();
        List<Session> sessions = sessGen.run(availability.keySet(), start, end);
        for(Session sess : sessions){
            studentsAtTime.put(sess, numStudentsLeft.get(sess.getSection()));
        }
        for(List<ConstructionFile> thread : remainingThreads){
            int minsLeft = 0;
            int index = 0;
            Session prev = null;
            for(ConstructionFile cf : thread){
                for(Step step : cf.getSteps()){
                    Operation op = step.getOperation();
                    Session next = sessions.get(index);
                    if(prev != null) {
                        minsLeft -= prev.getStartTime().until(next.getStartTime(), ChronoUnit.MINUTES);
                    }
                    if(minsLeft <= 0){
                        if(studentsAtTime.get(next) <= 0){
                            prev = next;
                            continue;
                        }
                        else{
                            studentsAtTime.put(next, studentsAtTime.get(next) - 1);
                            minsLeft = minsToWait.get(step.getOperation());
                            if(!(schedule.containsKey(next)) ){
                                schedule.put(next, new ArrayList<>());
                            }
                            schedule.get(next).add(step.getOperation());
                        }
                    }
                    else{
                        prev = next;
                        continue;
                    }

                }
            }
        }
        return schedule;
    }

    public static void main(String[] args) throws Exception{
        String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data";
        String CFdir = filepath + File.separator + "ConstructionFiles";
        String tsvpath = filepath + "threads.tsv"; //TODO: populate these with example data

        BundleThreads bundler = new BundleThreads();
        bundler.initiate();
        List<List<ConstructionFile>> threads = bundler.run(CFdir, tsvpath);

        ParsePoll parser = new ParsePoll();
        parser.initiate();
        Map<Section, Set<String>> secToStudents = parser.run(filepath + "Doodle.txt");
        ProcessPoll pollProcessor = new ProcessPoll();
        pollProcessor.initiate();
        Set<String> compnames = new HashSet<>();
        compnames.add("Nathan Wong");
        compnames.add("Daniel Blank");
        compnames.add("Kelly Huang");
        compnames.add("Arjun Chandran");
        compnames.add("Zack");
        Map<Section, Set<String>> processedPoll = pollProcessor.run(secToStudents, compnames);

        ScheduleWork scheduler = new ScheduleWork();

        scheduler.initiate();

        Map<Session, List<Operation>> sched = scheduler.run(threads, processedPoll, LocalDateTime.of(2019, Month.JANUARY, 22, 0, 0), LocalDateTime.of(2019, Month.MAY, 15, 0, 0));

        for(Session sess : sched.keySet()){
            System.out.println(sess);
            for(Operation op : sched.get(sess)){
                System.out.print(op);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
