package doodle;

import org.ucb.manage140l.model.Section;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author J. Christopher Anderson
 */
public class CurrSecAnalysis {
    public void initiate() {

    }

    public Map<String, Set<Section>> run(Map<Section, Set<String>> secToStudents) throws Exception {
        //Gather up all names
        Set<String> allnames = new HashSet<>();
        for(Section sec : secToStudents.keySet()) {
            Set<String> names = secToStudents.get(sec);
            allnames.addAll(names);
        }

        //Prune out entries missing a GSI or PI and thus impossible to supervise
        Set<Section> tossers = new HashSet<>();
        for(Section sec : secToStudents.keySet()) {
            Set<String> names = secToStudents.get(sec);
            if(!names.contains("Instructor") && !names.contains("GSI1") && !names.contains("GSI2")) {
                tossers.add(sec);
            }
        }

        //Prune out sections not in the original schedule
        for(Section sec : secToStudents.keySet()) {
            Set<String> names = secToStudents.get(sec);
            if(!names.contains("curr_sec")) {
                tossers.add(sec);
            }
        }

        //Remove the tossers
        for(Section sec : tossers) {
            secToStudents.remove(sec);
        }

        //Gather up all students in sections from original schedule
        Set<String> originalSectionAvailNames = new HashSet<>();
        for(Section sec : secToStudents.keySet()) {
            Set<String> names = secToStudents.get(sec);
            originalSectionAvailNames.addAll(names);
        }

        //Determine the students that don't fit into original sections
        Set<String> unmatched = new HashSet<>();
        unmatched.addAll(allnames);
        unmatched.removeAll(originalSectionAvailNames);
        for(String name : unmatched) {
            System.out.println("not in sections: " + name);
        }

        //Put empty sets for all the names
        Map<String, Set<Section>> nameToSections = new HashMap<>();
        for(String name : allnames) {
            nameToSections.put(name, new HashSet<>());
        }

        //Scan through originally scheduled sections and assign
        for(Section sec : secToStudents.keySet()) {
            Set<String> names = secToStudents.get(sec);
            for(String name : names) {
                Set<Section> secset = nameToSections.get(name);
                secset.add(sec);
                nameToSections.put(name, secset);
            }
        }
        return nameToSections;
    }
}
