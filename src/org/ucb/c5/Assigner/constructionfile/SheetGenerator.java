package org.ucb.c5.Assigner.constructionfile;

import javafx.util.Pair;
import org.ucb.constructionFileModel.ConstructionFile;
import org.ucb.constructionFileModel.Step;
import org.ucb.manage140l.model.Section;
import org.ucb.manage140l.model.Session;
import org.ucb.manage140l.utils.FileUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Daniel on 5/1/2019.
 */
public class SheetGenerator {

    public void initiate(){

    }

    //returns a list of pairs of sheets and semiprotocols for a particular operation and thread.
    public  List<List<Pair<String, String>>> run(List<List<List<Step>>> threads){
        List<List<Pair<String, String>>> sheetsAndSemiprotocols = new ArrayList<>();
        for(List<List<Step>> thread : threads){
            sheetsAndSemiprotocols.add(new ArrayList<>());
            for(List<Step> stepsForOperation : thread){
                StringBuilder sheet = new StringBuilder();
                String semiprotocol = "dummy"; //TODO: convert the steps to a Semiprotocol
                for(Step step : stepsForOperation){
                    sheet.append(step.get_str() + "\n"); //TODO: make the sheet more human readable
                }
                sheetsAndSemiprotocols.get(sheetsAndSemiprotocols.size() - 1).add(new Pair<>(sheet.toString(), semiprotocol));
            }
        }
        return sheetsAndSemiprotocols;
        //TODO: possibly reason about an inventory object?
    }

    public static void main(String[] args) throws Exception{
        //Use ParseAvailability, BundleThreads and BundleSteps to generate input, then run on it.
        //write the outputs to files? Possibly use RTF file? Figure out how to use those?
        //concatenate sheets to same file, semiprotocols stay as flatfiles
        String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data";
        String CFdir = filepath + File.separator + "ConstructionFiles";
        String tsvpath = filepath + File.separator + "threads.txt";

        BundleThreads bundler = new BundleThreads();
        BundleSteps stepBundler = new BundleSteps();
        SheetGenerator sheetGenerator = new SheetGenerator();
        bundler.initiate();
        stepBundler.initiate();
        sheetGenerator.initiate();
        List<List<ConstructionFile>> threads = bundler.run(CFdir, tsvpath);
        List<List<List<Step>>> stepsSorted = stepBundler.run(threads);
        List<List<Pair<String, String>>> sheetsAndSemiprotocols = sheetGenerator.run(stepsSorted);
        Integer i = 0;
        for(List<Pair<String, String>> thread : sheetsAndSemiprotocols){
            for(Pair<String, String> operation : thread){
                String sheet = operation.getKey();
                String semiprotocol = operation.getValue();
                FileUtils.writeFile(semiprotocol, filepath + File.separator + "semiprotocol" + i.toString());
                //TODO: Write sheets to word file, possibly using https://www.tutorialspoint.com/apache_poi_word/apache_poi_word_document.htm.
            }
        }

    }

}
