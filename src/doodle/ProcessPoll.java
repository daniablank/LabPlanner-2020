package doodle;

import org.ucb.c5.utils.CombinationIterable;

import java.io.File;
import java.util.*;

/**
 * Created by Daniel on 3/2/2019.
 */
public class ProcessPoll {

    public void initiate(){}

    public Map<Section,Set<String>> run(Map<Section, Set<String>> secToStudents, Set<String> compnames) throws Exception{
        CurrSecAnalysis analyzer = new CurrSecAnalysis();
        analyzer.initiate();
        Map<String, Set<Section>> nameToSections = analyzer.run(secToStudents);



        //Invert the map, disregard the computational students
        Map<Section, Set<String>> sectionToNames = new HashMap<>();

        //Fill in all the sections with curr_sec
        Set<Section> currsecs = nameToSections.get("curr_sec");
        for (Section sec : currsecs) {
            sectionToNames.put(sec, new HashSet<>());
        }

        List<Map<Section, Set<String>>> enumeration = new ArrayList<>();
        enumeration.add(sectionToNames);

        for (String name : nameToSections.keySet()) {
            if (compnames.contains(name)) {
                continue;
            }
            if (name.equals("curr_sec")) {
                continue;
            }
            if (name.startsWith("GSI")) {
                continue;
            }
            if (name.startsWith("Instructor")) {
                continue;
            }
            Set<Section> secs = nameToSections.get(name);
            List<Map<Section, Set<String>>> nextEnumeration = new ArrayList<>();
            for(Map<Section, Set<String>> prev : enumeration){
                Map<Section, Set<String>> template = new HashMap<>();
                for(Section sec : prev.keySet()){
                    template.put(sec, new HashSet<>(prev.get(sec)));
                }
                for(List<Section> choice : new CombinationIterable<Section>(secs, 3)){
                    Map<Section, Set<String>> attempt = new HashMap<>();
                    for(Section sec : template.keySet()){
                        attempt.put(sec, new HashSet<>(template.get(sec)));
                    }
                    for(Section sec : choice){
                        Set<String> names = attempt.get(sec);
                        names.add(name);
                        attempt.put(sec, names);
                    }
                    nextEnumeration.add(attempt);
                }
            }
            enumeration = nextEnumeration;
        }

        Map<Section, Set<String>> winner = enumeration.get(0);
        double bestScore = stddev(winner);

        for(Map<Section, Set<String>> cand : enumeration){
            double score = stddev(cand);
            if(score < bestScore){
                bestScore = score;
                winner = cand;
            }
        }


        return winner;
    }

    private double stddev(Map<Section, Set<String>> cand){
        double mean = 0;
        for(Set<String> names : cand.values()){
            mean += names.size()/(cand.size() + 0.0);
        }
        double variance = 0;
        for(Set<String> names : cand.values()){
            variance += Math.pow((names.size() - mean), 2)/(cand.size() - 1);
        }
        return Math.sqrt(variance);
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
        compnames.add("Lance Azizi");
        compnames.add("Zack");
        Map<Section, Set<String>> processedPoll = pollProcessor.run(secToStudents, compnames);

        for(Section sec : processedPoll.keySet()) {
            System.out.println(sec);
            Set<String> names = processedPoll.get(sec);
            for(String name : names) {
                System.out.println("\t" + name);
            }
        }
    }

}
