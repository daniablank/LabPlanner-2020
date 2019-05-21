package doodle;


import org.ucb.c5.utils.FileUtils;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * Created by Daniel on 4/5/2019.
 */
public class SerializeAvailability {

    public void initiate(){

    }

    public void run(Map<Section, Set<String>> availability, String filename) throws Exception{
        StringBuilder sb = new StringBuilder();

        for(Section sec : availability.keySet()){
            sb.append(sec.toString());
            for(String name : availability.get(sec)){
                sb.append("\t");
                sb.append(name);
            }
            sb.append("\n");
        }
        FileUtils.writeResourceFile(sb.toString(), System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data" + File.separator + filename);


    }
}
