# LabPlanner2020

Inputs: 
  Doodle poll data, to be put into Data/doodle.txt
  Construction Files, to be put into Data/ConstructionFiles
  Thread data, as a TSV file with each line corresponding to a specific thread, with the construction file product names for that thread.

The Doodle poll is parsed by ParsePoll.run(), and the raw output of ParsePoll is processed by ProcessPoll.run(), which yields an assignment from each section to the students attending it, using CurrSecAnalysis to generate the sections that will actually be populated. This assignment minimizes the standard deviation over all sections of students in each section (which are enumerated using CombinationIterable). The output of ProcessPoll can be written to a file using SerializeAvailability, and then parsed again using ParseAvailability.

BundleThreads.run() takes in the path to the construction files, and the path to the thread TSV. It reads in the construction files using ParseConstructionFile.run(), turning them into ConstructionFile objects. Then, using the product names in the threads TSV, it groups the construction files into a List of Lists, with each sublist representing a specific thread.

BundleSteps.run() takes in the output of BundleThreads, and outputs a List of Lists of Lists of Steps, corresponding to a grouping of the steps in each thread by the Operation in each of them. The first layer of indexing corresponds to the threads, the second to the Operation types, and the last gives us individual Step objects.

SheetGenerator is currently incomplete, but its function will be to take in the output of BundleSteps, and output a Sheet and a Semiprotocol corresponding to each of the innermost Lists in the output of BundleSteps. The main() method of SheetGenerator should write the sheets and semiprotocols to files of some kind, possibly Word documents. 

ScheduleWork is largely outdated, and was originally designed to assign Steps to times, using SessionGenerator to generate Sessions corresponding to occurrences of the provided Sections.
