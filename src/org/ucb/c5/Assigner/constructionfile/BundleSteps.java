package org.ucb.c5.Assigner.constructionfile;

import org.ucb.constructionFileModel.ConstructionFile;
import org.ucb.constructionFileModel.Operation;
import org.ucb.constructionFileModel.Step;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/22/2019.
 */
public class BundleSteps {

    public void initiate(){

    }

    public List<List<List<Step>>> run(List<List<ConstructionFile>> threads){
        List<List<List<Step>>> result = new ArrayList<>();
        for(List<ConstructionFile> thread : threads){
            List<List<Step>> threadList = new ArrayList<>();

            for(Operation op : Operation.values()){
                List<Step> allInstances = new ArrayList<>();
                for(ConstructionFile cf : thread){
                    for(Step step : cf.getSteps()){
                        if(step.getOperation().equals(op)){
                            allInstances.add(step);
                        }
                    }
                }
                if(allInstances.size() > 0){
                    threadList.add(allInstances);
                }
            }

            result.add(threadList);
        }

        return result;
        //For each thread, there is a list of lists of steps, with each list of steps containing all the instances of a step with a particular Operation.
    }

    public static void main(String[] args) throws Exception{
        String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data";
        String CFdir = filepath + File.separator + "ConstructionFiles";
        String tsvpath = filepath + File.separator + "threads.txt";

        BundleThreads bundler = new BundleThreads();
        BundleSteps stepBundler = new BundleSteps();
        bundler.initiate();
        stepBundler.initiate();
        List<List<ConstructionFile>> threads = bundler.run(CFdir, tsvpath);
        List<List<List<Step>>> stepsSorted = stepBundler.run(threads);

        for(List<List<Step>> threadList : stepsSorted){
            System.out.println();
            for(List<Step> steps: threadList){
                System.out.println("\t");
                for(Step step : steps){
                    System.out.print(step.get_str() + "  ");
                }
            }
        }

    }

}
