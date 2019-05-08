package doodle;

import org.ucb.c5.utils.FileUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import java.util.Map;

/**
 * Created by Daniel on 4/5/2019.
 */
public class ParseAvailability {

    public void initiate(){
        days = new HashMap<>();
        for(DayOfWeek day : DayOfWeek.values()){
            days.put(day.toString(), day);
        }
    }

    private Map<String, DayOfWeek> days;

    public Map<Section, Set<String>> run(String filepath) throws Exception{
        Map<Section, Set<String>> availability = new HashMap<>();
        String data = FileUtils.readFile(filepath);
        String[] sections = data.split("\\r|\\r?\\n");
        for(String sectiondata : sections){
            String[] secAndStudents = sectiondata.split("\t");
            String section = secAndStudents[0];
            String[] dayAndTimes = section.split("  ");
            String dayOfWeek = dayAndTimes[0];
            DayOfWeek secDay = days.get(dayOfWeek);
            String[] startAndEnd = dayAndTimes[0].split(" - ");
            LocalTime start = LocalTime.parse(startAndEnd[0], DateTimeFormatter.ofPattern("h:mm a") );
            LocalTime end = LocalTime.parse(startAndEnd[1], DateTimeFormatter.ofPattern("h:mm a") );
            Section sec = new Section(secDay, start, end);
            Set<String> studentSet = new HashSet<>();
            for(int i = 1; i < secAndStudents.length; i++){
                studentSet.add(secAndStudents[i]);
            }
            availability.put(sec, studentSet);
        }
        return availability;
    }

}
