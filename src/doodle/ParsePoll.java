package doodle;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ucb.c5.utils.FileUtils;;

/**
 *
 * @author J. Christopher Anderson
 */
public class ParsePoll {
    private DateTimeFormatter formatter;

    public void initiate() {
        formatter = DateTimeFormatter.ofPattern("h:mm a");
    }

    public Map<Section, Set<String>> run(String filepath) throws Exception {
        String data = FileUtils.readFile(filepath);
        data = data.replaceAll("\"", "");
        String[] lines = data.split("\\r|\\r?\\n");

        //Read the line containing days of week
        String dayline = lines[4];
        if(!dayline.trim().startsWith("Mon")) {
            throw new Exception("Wrong line for day data");
        }

        //Read the line containing time periods
        String timeline = lines[5];
        if(!timeline.trim().startsWith("9")) {
            throw new Exception("Wrong line for time data");
        }

        //Create the Sections
        Map<Integer,Section> columnToSection = new HashMap<>();
        String[] dayTabs = dayline.split("\t");
        String[] timeTabs = timeline.split("\t");

        DayOfWeek currday = DayOfWeek.MONDAY;
        for(int col=1; col<timeTabs.length; col++) {
            //Figure out day for this column
            String daycell = "";
            if(col < dayTabs.length) {
                daycell = dayTabs[col];
            }
            if(!daycell.trim().isEmpty()) {
                if(daycell.startsWith("Mon")) {
                    currday = DayOfWeek.MONDAY;
                } else if(daycell.startsWith("Tue")) {
                    currday = DayOfWeek.TUESDAY;
                } else if(daycell.startsWith("Wed")) {
                    currday = DayOfWeek.WEDNESDAY;
                } else if(daycell.startsWith("Thu")) {
                    currday = DayOfWeek.THURSDAY;
                } else if(daycell.startsWith("Fri")) {
                    currday = DayOfWeek.FRIDAY;
                } else {
                    throw new Exception("Parsing error of day of week");
                }
            }

            //Figure out time for this column
            String timecell = timeTabs[col];
            String[] timesplit = timecell.split(" � ");

            LocalTime start = LocalTime.parse(timesplit[0], formatter);
            LocalTime end = LocalTime.parse(timesplit[1], formatter);

            //Create the Section and associate with column
            Section section = new Section(currday, start, end);
            columnToSection.put(col, section);
//            System.out.println(section);
        }

        //Read student lines, abggregate students available per section
        Map<Section, Set<String>> sectionToStudentsOK = new HashMap<>();
        int stdtindex = 6;
        while(true) {
            String stdtLine = lines[stdtindex];
            if(stdtLine.trim().startsWith("Count")) {
//                System.out.println("End students parsing at " + stdtindex);
                break;
            }

            //Process student data
            String[] stdtTabs = stdtLine.split("\t");
            String name = stdtTabs[0];
//            System.out.println("name " + name);
            for(int col=1; col<stdtTabs.length; col++) {
                String okCell = stdtTabs[col];
                if(okCell.equals("OK")) {
//                if(okCell.contains("OK")) {
                    Section currsec = columnToSection.get(col);
                    Set<String> names = sectionToStudentsOK.get(currsec);
                    if(names == null) {
                        names = new HashSet<>();
                    }
                    names.add(name);
                    sectionToStudentsOK.put(currsec, names);
                }
            }

            stdtindex++;
        }

        return sectionToStudentsOK;
    }

    public static void main(String[] args) throws Exception {
        ParsePoll parser = new ParsePoll();
        parser.initiate();
        String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data" + File.separator + "Doodle.txt";
        Map<Section, Set<String>> secToStudents = parser.run(filepath);

        for(Section sec : secToStudents.keySet()) {
            System.out.println(sec);
            Set<String> names = secToStudents.get(sec);
            for(String name : names) {
                System.out.println("\t" + name);
            }
        }
    }
}
