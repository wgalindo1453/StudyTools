package atm.cs.analysis;

public class Main {
  public static void main(String args[]) {
    if (args[0].equals("tanalysis")) {
      if (args.length != 3) {
        System.out.println("usage: java -jar file.jar tanalysis config.txt archive.xml");

        System.exit(1);

      }
      System.out.println("hello");
      System.out.println("args: "+args[0]+" "+args[1]+" "+args[2]);
      TestAnalysis testAnalysis = new TestAnalysis();
      testAnalysis.compute(args[1], args[2]);
    }
    else if (args[0].equals("canalysis")) {
      if (args.length != 3) {
        System.out.println("usage: java -jar file.jar canalysis config.txt archive.xml");
        System.exit(1);
      }
      TestAnalysis testAnalysis = new TestAnalysis();
      testAnalysis.computeCommits(args[1], args[2]);
    }
    else if (args[0].equals("taseanalysis")) {
      if (args.length != 5) {
        System.out.println("usage: java -jar file.jar taseanalysis config.txt repos.json mappings.json archive.xml");
        System.exit(1);
      }
      TestAnalysis testAnalysis = new TestAnalysis();
      testAnalysis.computeForAse2020Dataset(args[1], args[2], args[3], args[4]);
    }
    else if (args[0].equals("caseanalysis")) {
      if (args.length != 5) {
        System.out.println("usage: java -jar file.jar caseanalysis config.txt repos.json mappings.json archive.xml");
        System.exit(1);
      }
      TestAnalysis testAnalysis = new TestAnalysis();
      testAnalysis.computeCommitsForAse2020Dataset(args[1], args[2], args[3], args[4]);
    }
    else if (args[0].equals("pstatistics")) {
      if (args.length != 3) {
        System.out.println("usage: java -jar file.jar pstatistics config.txt archive.xml");
        System.exit(1);
      }
      PrintStatistics pStatistics = new PrintStatistics();
      pStatistics.printStatistics(args[1], args[2]);
    }
    else if (args[0].equals("pmocks")) {
      if (args.length != 7) {
        //String configFile, String githubRepoName, String localRepoName, String appAnalysisFolderName, String commitID, String resultFolderName
        System.out.println("usage: java -jar file.jar pmocks config.txt github_repo_name local_repo_name repo_analysis_directory commit_id result_folder");
        System.exit(1);
      }
      PrintMocks pMocks = new PrintMocks();
      pMocks.printMocks(args[1], args[2], args[3], args[4], args[5], args[6]);
    }
    //extract mocked types using soot
    else if (args[0].equals("emt")) {
      if (args.length != 2) {
        //String configFile, String githubRepoName, String localRepoName, String appAnalysisFolderName, String commitID, String resultFolderName
        System.out.println("usage: java -jar file.jar emt class_folders");
        System.exit(1);
      }
      ExtractMockedTypes emt = new ExtractMockedTypes();
      emt.extractMockedTypes(args[1]);
    }
  }
}
