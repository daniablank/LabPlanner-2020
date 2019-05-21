package org.ucb.c5.Assigner.constructionfile;

import org.ucb.constructionFileModel.ConstructionFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.utils.*;

/**
 * Created by Daniel on 2/22/2019.
 */
public class BundleThreads {
    //list of lists of lists of steps? (thread->operation->step)
    ParseConstructionFile parser;

    public void initiate(){
        parser = new ParseConstructionFile();
        parser.initiate();
    }


    public List<List<ConstructionFile>> run(String cfpath, String tsvpath) throws Exception{
        File CFdir = new File(cfpath);
        List<ConstructionFile> allCFs = new ArrayList<>();
        File[] CFfiles = CFdir.listFiles();
        for(int i = 0; i < CFfiles.length; i++) {
            File CFfile = CFfiles[i];
            String text = FileUtils.readFile(CFfile.getAbsolutePath());
            allCFs.add(parser.run(text));
        }
        List<List<ConstructionFile>> threadOutput = new ArrayList<>();
        String threadData = FileUtils.readFile(tsvpath);
        String[] threads = threadData.split("\\r|\\r?\\n");
        for(String thread : threads){
            threadOutput.add(new ArrayList<>());
            String[] cfs = thread.split("\t");
            for(String cf : cfs){ //Using Product name for the moment; Should I have made a Map for this?
                for(ConstructionFile file : allCFs){
                    if(file.getPdtName().equals(cf)){
                        threadOutput.get(threadOutput.size() - 1).add(file);
                    }
                }
            }
        }
        return  threadOutput;
    }


    public static void main(String[] args) throws Exception{
        String filepath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "org" + File.separator + "ucb" +
                File.separator + "c5" + File.separator + "Data";
        String CFdir = filepath + File.separator + "ConstructionFiles";
        String tsvpath = filepath + File.separator + "threads.txt";

        BundleThreads bundler = new BundleThreads();
        bundler.initiate();
        List<List<ConstructionFile>> threads = bundler.run(CFdir, tsvpath);

        for(List<ConstructionFile> thread : threads){
            for(ConstructionFile cf : thread){
                System.out.print(cf.getPdtName());
                System.out.print("\t");
            }
            System.out.println();
        }

    }

}
