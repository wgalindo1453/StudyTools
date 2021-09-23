package atm.cs.analysis;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import atm.cs.analysis.data.Commit;
import atm.cs.analysis.data.MockInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class AnalysisUtils {

  //example tests to filter out
  public static final String EXAMPLE_JVM_TEST = "ExampleUnitTest";
  public static final String EXAMPLE_DEVICE_TEST = "ExampleInstrumentedTest";
  //extensions
  public static final String JAVA_EXTENSION = ".java";
  public static final String KOTLIN_EXTENSION = ".kt";
  //mockito
  public static final String MOCKITO_MOCK_API = "mock";
  public static final String MOCKITO_MOCKSTATIC_API = "mockStatic";
  public static final String MOCKITO_SPY_API = "spy";
  public static final String MOCKITO_MOCK_ANNOTATION = "Mock";
  public static final String MOCKITO_SPY_ANNOTATION = "Spy";
  //frameworks
  public static final String MOCKITO_FRAMEWORK = "mockito";
  public static final String MOCKITO_FRAMEWORK_UPPER_INITIAL = "Mockito";
  public static final String MOCKITO_KOTLIN_FRAMEWORK = "mockito-kotlin";
  public static final String POWERMOCKITO_FRAMEWORK_UPPER_INITIAL = "PowerMockito";
  //date
  public static final int DATASET_YEAR = 2021;//2020;
  public static final int DATASET_MONTH = 8;//8;
  public static final int DATASET_DAY =24; //1;
  //test folders considered
  public static final String JVM_TESTS_FOLDER = "test";
  public static final String DEVICE_TESTS_FOLDER = "androidTest";
  //robolectric runner
  public static final String ROBOLECTRIC_TEST_RUNNER_CLASS = "RobolectricTestRunner";
  //test annotations
  public static final String TEST_ANNOTATION = "Test";
  public static final String UI_THREAD_TEST_ANNOTATION = "UiThreadTest";
  public static final String SMALL_TEST_ANNOTATION = "SmallTest";
  public static final String MEDIUM_TEST_ANNOTATION = "MediumTest";
  public static final String LARGE_TEST_ANNOTATION = "LargeTest";
  //shadow annotations
  public static final String SHADOW_ANNOTATION = "Implements";
  public static final String SHADOW_METHOD_ANNOTATION = "Implementation";
  //kotlin rules
  public static final String TEST_RULE = "#TEST#";
  public static final String ROBOLECTRIC_TEST_RUNNER_RULE = "#ROBOLECTRIC_TEST_RUNNER#";
  public static final String ROBOLECTRIC_TEST_RULE = "#ROBOLECTRIC_TEST#";
  public static final String ROBOLECTRIC_SHADOW_RULE = "#ROBOLECTRIC_SHADOW#";
  public static final String ROBOLECTRIC_SHADOW_METHOD_RULE = "#ROBOLECTRIC_SHADOW_METHOD#";
  public static final String MOCKITO_MOCK_RULE = "#MOCKITO_MOCK#";
  public static final String MOCKITO_MOCKSTATIC_RULE = "#MOCKITO_MOCKSTATIC#";
  public static final String MOCKITO_SPY_RULE = "#MOCKITO_SPY#";
  public static final String MOCKITO_MOCK_ANNOTATION_RULE = "#MOCKITO_MOCK_ANNOTATION#";
  public static final String MOCKITO_SPY_ANNOTATION_RULE = "#MOCKITO_SPY_ANNOTATION#";
  public static final String JVM_UNIT_TEST_RULE = "#JVM_UNIT_TEST#";
  public static final String JVM_TEST_RULE = "#JVM_TEST#";
  public static final String DEVICE_UNIT_TEST_RULE = "#DEVICE_UNIT_TEST#";
  public static final String DEVICE_TEST_RULE = "#DEVICE_TEST#";
  public static final String DEVICE_INTEGRATION_TEST_RULE = "#DEVICE_INTEGRATION_TEST#";
  public static final String DEVICE_GUI_TEST_RULE = "#DEVICE_GUI_TEST#";
  public static final String KTLINT_PARAM_FILE = File.separator+"tmp"+File.separator+"ktlint_params.txt";
  public static final String KOTLIN_MOCKITO_DEPENDENCY = "#MOCKITO_DEPENDENCY#";
  public static final String KOTLIN_POWERMOCKITO_DEPENDENCY = "#POWERMOCKITO_DEPENDENCY#";
  public static final String KOTLIN_DAGGER_DEPENDENCY = "#DAGGER_DEPENDENCY#";
  public static final String KOTLIN_EASYMOCK_DEPENDENCY = "#EASYMOCK_DEPENDENCY#";
  public static final String KOTLIN_OKHTTP_DEPENDENCY = "#OKHTTP_DEPENDENCY#";
  public static final String KOTLIN_WIREMOCK_DEPENDENCY = "#WIREMOCK_DEPENDENCY#";
  public static final String KOTLIN_ROBOLECTRIC_DEPENDENCY = "#ROBOLECTRIC_DEPENDENCY#";
  public static final String KOTLIN_ANDROID_TEST_DEPENDENCY = "#ANDROID_TEST_DEPENDENCY#";
  public static final String KOTLIN_MOCKK_DEPENDENCY = "#MOCKK_DEPENDENCY#";
  public static final String KOTLIN_SPRINGFRAMEWORK_MOCK_DEPENDENCY = "#SPRINGFRAMEWORK_MOCK_DEPENDENCY#";
  public static final String KOTLIN_RETROFIT_DEPENDENCY= "#RETROFIT_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_KOTLIN_DEPENDENCY = "#MOCKITO_KOTLIN_DEPENDENCY#";
  public static final String KOTLIN_MOCKSERVER_DEPENDENCY = "#MOCKSERVER_DEPENDENCY#";
  public static final String CLASS_TEST_DOUBLE_RULE = "#TEST_DOUBLE_WORD#";
  public static final String CLASS_TEST_RULE = "#TEST_WORD#";
  public static final String JAVA_MOCKITO_DEPENDENCY = "org.mockito";
  public static final String JAVA_MOCKITO_SPECIFIC_DEPENDENCY = "org.mockito.Mockito.*";
  public static final String JAVA_MOCKITO_MOCK_SPECIFIC_DEPENDENCY = "org.mockito.Mockito.mock";
  public static final String JAVA_MOCKITO_SPY_SPECIFIC_DEPENDENCY = "org.mockito.Mockito.spy";
  public static final String JAVA_MOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY = "org.mockito.Mockito.mockStatic";
  public static final String JAVA_MOCKITO_ANNOTATION_SPECIFIC_DEPENDENCY = "org.mockito.*";
  public static final String JAVA_MOCKITO_ANNOTATION_MOCK_SPECIFIC_DEPENDENCY = "org.mockito.Mock";
  public static final String JAVA_MOCKITO_ANNOTATION_SPY_SPECIFIC_DEPENDENCY = "org.mockito.Spy";


  public static final String JAVA_POWERMOCK_DEPENDENCY = "org.powermock";
  public static final String JAVA_DAGGER_DEPENDENCY = "dagger.";
  public static final String JAVA_EASYMOCK_DEPENDENCY = "org.easymock";
  public static final String JAVA_OKHTTP_1_DEPENDENCY = "com.squareup.okhttp";
  public static final String JAVA_OKHTTP_2_DEPENDENCY = "okhttp3.mockwebserver";
  public static final String JAVA_WIREMOCK_DEPENDENCY = "com.github.tomakehurst.wiremock";
  public static final String JAVA_ROBOLECTRIC_DEPENDENCY = "org.robolectric";
  public static final String JAVA_ANDROIDTEST_DEPENDENCY = "android.test.mock";
  public static final String JAVA_MOCKK_DEPENDENCY = "io.mockk";
  public static final String JAVA_SPRINGFRAMEWORK_DEPENDENCY = "org.springframework.mock.web";
  public static final String JAVA_RETROFIT_DEPENDENCY = "retrofit2.mock";
  public static final String JAVA_MOCKITOKOTLIN_1_DEPENDENCY = "com.nhaarman.mockito_kotlin";
  public static final String JAVA_MOCKITOKOTLIN_2_DEPENDENCY = "com.nhaarman.mockitokotlin2";
  public static final String JAVA_MOCKSERVER_DEPENDENCY = "org.mockserver";

  //mockito
  public static final String KOTLIN_MOCKITO_CALL_ON_CLASS = "#MOCKITO_CALL_ON_CLASS#";
  //power mockito
  public static final String KOTLIN_POWERMOCKITO_CALL_ON_CLASS = "#POWERMOCKITO_CALL_ON_CLASS#";
  //mockito kotlin
  public static final String KOTLIN_MOCKITO_KOTLIN_NO_ARGUMENTS_CALL = "#MOCKITO_KOTLIN_NO_ARGUMENTS_CALL#";
  public static final String KOTLIN_MOCKITO_KOTLIN_TYPE_CALL = "#MOCKITO_KOTLIN_TYPE_CALL#";

  //mockito in java code
  public static final String JAVA_POWERMOCKITO_SPECIFIC_DEPENDENCY = "org.powermock.api.mockito.PowerMockito.*";
  public static final String JAVA_POWERMOCKITO_MOCK_SPECIFIC_DEPENDENCY = "org.powermock.api.mockito.PowerMockito.mock";
  public static final String JAVA_POWERMOCKITO_SPY_SPECIFIC_DEPENDENCY = "org.powermock.api.mockito.PowerMockito.spy";
  public static final String JAVA_POWERMOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY = "org.powermock.api.mockito.PowerMockito.mockStatic";
  //mockito in kotlin code
  public static final String KOTLIN_MOCKITO_MOCK_SPECIFIC_DEPENDENCY = "#MOCKITO_MOCK_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_SPY_SPECIFIC_DEPENDENCY = "#MOCKITO_SPY_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY = "#MOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_ANNOTATION_MOCK_SPECIFIC_DEPENDENCY = "#MOCKITO_ANNOTATION_MOCK_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_ANNOTATION_SPY_SPECIFIC_DEPENDENCY = "#MOCKITO_ANNOTATION_SPY_SPECIFIC_DEPENDENCY#";
  //mockito kotlin in kotlin code
  public static final String KOTLIN_MOCKITO_KOTLIN_MOCK_SPECIFIC_DEPENDENCY = "#MOCKITO_KOTLIN_MOCK_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_KOTLIN_SPY_SPECIFIC_DEPENDENCY = "#MOCKITO_KOTLIN_SPY_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_MOCKITO_KOTLIN_MOCKSTATIC_SPECIFIC_DEPENDENCY = "#MOCKITO_KOTLIN_MOCKSTATIC_SPECIFIC_DEPENDENCY#";
  //power mockito in kotlin code
  public static final String KOTLIN_POWERMOCKITO_MOCK_SPECIFIC_DEPENDENCY = "#POWERMOCKITO_MOCK_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_POWERMOCKITO_SPY_SPECIFIC_DEPENDENCY = "#POWERMOCKITO_SPY_SPECIFIC_DEPENDENCY#";
  public static final String KOTLIN_POWERMOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY = "#POWERMOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY#";

  public static final String DUMMY_WORD = "dummy";
  public static final String STUB_WORD = "stub";
  public static final String MOCK_WORD = "mock";
  public static final String SPY_WORD = "spy";
  public static final String FAKE_WORD = "fake";
  public static final String MOCKITO_WORD = "mockito";
  public static final String TEST_WORD = "test";
  public static final String TESTS_WORD = "tests";

  public static Node getDeclaringClassNode(Node node) {
    Node result = null;
    List<Node> workList = new ArrayList<Node>();
    if (node.getParentNode().isPresent()) {
      Node parentNode = node.getParentNode().get();
      if (parentNode instanceof ClassOrInterfaceDeclaration) {
        result = parentNode;
        return result;
      }
      workList.add(parentNode);
    }
    while (!workList.isEmpty()) {
      Node currNode = workList.remove(0);
      if (currNode.getParentNode().isPresent()) {
        Node parentNode = currNode.getParentNode().get();
        if (parentNode instanceof ClassOrInterfaceDeclaration) {
          result = parentNode;
          return result;
        }
        workList.add(parentNode);
      }
    }
    return result;
  }

  public static String getNameOfDeclaringClassNode(Node node) {
    String result = "";
    List<Node> workList = new ArrayList<Node>();
    if (node.getParentNode().isPresent()) {
      Node parentNode = node.getParentNode().get();
      if (parentNode instanceof ClassOrInterfaceDeclaration) {
        result = ((ClassOrInterfaceDeclaration) parentNode).getNameAsString();
        return result;
      }
      workList.add(parentNode);
    }
    while (!workList.isEmpty()) {
      Node currNode = workList.remove(0);
      if (currNode.getParentNode().isPresent()) {
        Node parentNode = currNode.getParentNode().get();
        if (parentNode instanceof ClassOrInterfaceDeclaration) {
          result = ((ClassOrInterfaceDeclaration) parentNode).getNameAsString();
          return result;
        }
        workList.add(parentNode);
      }
    }
    return result;
  }

  public static String getNameOfContainingMethodNode(Node node){
    String result = "";
    Node current = node;
    while(current!=null){
      if(current instanceof MethodDeclaration) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) current;
        result = methodDeclaration.getNameAsString();
        break;
      }
      else{
        if(current.getParentNode().isPresent()){
          current = current.getParentNode().get();
        }
        else{
          current = null;
        }
      }
    }
    return result;
  }



  public static int getCommitsNumNYearsAgo(Properties prop, String repoAnalysisFolder, int n){
    int result=0;
    LocalDateTime startBound = LocalDateTime.of(AnalysisUtils.DATASET_YEAR, AnalysisUtils.DATASET_MONTH, AnalysisUtils.DATASET_DAY, 0, 0);
    LocalDateTime upperBound = startBound.minusYears(n-1);
    LocalDateTime lowerBound = startBound.minusYears(n);
    try {
      //get commits
      String command = prop.getProperty("python_bin_full_path") + " " +
          prop.getProperty("python_check_commits_script_full_path") + " " +
          prop.getProperty("config_full_path") + " " + repoAnalysisFolder;
      Process process = Runtime.getRuntime().exec(command);
      System.out.println("command is :"+command);
      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      Set<String> commits = new HashSet<String>();
      String line = "";
      while ((line = input.readLine()) != null) {
        line = line.replace(System.lineSeparator(), "");
        commits.add(line);
      }
      input.close();
      List<LocalDateTime> commitDates = new ArrayList<LocalDateTime>();
      for (String commit : commits) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        Date date = sdf.parse(commit);
        LocalDateTime commitDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        commitDates.add(commitDate);
      }
      //sort commits
      Collections.sort(commitDates, new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime ld1, LocalDateTime ld2) {
          if (ld1.isAfter(ld2)) {
            return -1;
          } else if (ld1.isBefore((ld2))){
            return 1;
          }
          else {
            return 0;
          }
        }
      });
      //count commits
      for (LocalDateTime commitDate : commitDates){
        if (commitDate.isAfter(lowerBound) && commitDate.isBefore(upperBound)) {
          result++;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  public static String getNewestCommitBeforeAnalysisStarted(Properties prop, String repoAnalysisFolder){
    String result="ISSUE";
    int upperBoundYear = AnalysisUtils.DATASET_YEAR;
    int upperBoundMonth = AnalysisUtils.DATASET_MONTH;
    int upperBoundDay = AnalysisUtils.DATASET_DAY;
    LocalDateTime upperBound = LocalDateTime.of(upperBoundYear, upperBoundMonth, upperBoundDay, 0, 0);
    try {
      //get commits
      String command = prop.getProperty("python_bin_full_path") + " " +
          prop.getProperty("python_check_commits_with_hash_script_full_path") + " " +
          prop.getProperty("config_full_path") + " " + repoAnalysisFolder;
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      Set<String> commitLines = new HashSet<String>();
      String line = "";
      while ((line = input.readLine()) != null) {
        line = line.replace(System.lineSeparator(), "");
        commitLines.add(line);
      }
      input.close();
      List<Commit> commits = new ArrayList<Commit>();
      for (String commitLine : commitLines) {
        String commitInfo[] = commitLine.split("   ");
        String commitDateString = commitInfo[0];
        String commitID= commitInfo[1];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        Date date = sdf.parse(commitDateString);
        LocalDateTime commitDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        commits.add(new Commit(commitID, commitDate));
      }
      //sort commits
      Collections.sort(commits, new Comparator<Commit>() {
        @Override
        public int compare(Commit c1, Commit c2) {
          if (c1.getCommitDate().isAfter(c2.getCommitDate())) {
            return -1;
          } else if (c1.getCommitDate().isBefore((c2.getCommitDate()))){
            return 1;
          }
          else {
            return 0;
          }
        }
      });
      //count commits
      for (Commit commit : commits){
        if (commit.getCommitDate().isBefore(upperBound)) {
          result=commit.getCommitID();
          return result;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  public static int commitsOfTestsInNthYearFromDatasetDate(Properties prop, String repoAnalysisFolder, Set<String> fileNames, int n){
    Set<String> validCommits = new HashSet<String>();
    LocalDateTime startBound = LocalDateTime.of(AnalysisUtils.DATASET_YEAR, AnalysisUtils.DATASET_MONTH, AnalysisUtils.DATASET_DAY, 0, 0);
    LocalDateTime upperBound = startBound.minusYears(n-1);
    LocalDateTime lowerBound = startBound.minusYears(n);
    try {
      for(String fileName:fileNames) {
        //get commits
        String command = prop.getProperty("python_bin_full_path") + " " +
            prop.getProperty("python_check_commits_script_for_file_full_path") + " " +
            prop.getProperty("config_full_path") + " " + repoAnalysisFolder + " "+ fileName;
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        Set<String> commitLines = new HashSet<String>();
        String line = "";
        while ((line = input.readLine()) != null) {
          line = line.replace(System.lineSeparator(), "");
          commitLines.add(line);
        }
        input.close();
        List<Commit> commits = new ArrayList<Commit>();
        for (String commitLine : commitLines) {
          String commitInfo[] = commitLine.split("   ");
          String commitDateString = commitInfo[0];
          String commitID= commitInfo[1];
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
          Date date = sdf.parse(commitDateString);
          LocalDateTime commitDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
          commits.add(new Commit(commitID, commitDate));
        }
        //sort commits
        Collections.sort(commits, new Comparator<Commit>() {
          @Override
          public int compare(Commit c1, Commit c2) {
            if (c1.getCommitDate().isAfter(c2.getCommitDate())) {
              return -1;
            } else if (c1.getCommitDate().isBefore((c2.getCommitDate()))){
              return 1;
            }
            else {
              return 0;
            }
          }
        });
        //check range of commit
        for (Commit commit : commits) {
          if (commit.getCommitDate().isAfter(lowerBound) && commit.getCommitDate().isBefore(upperBound)) {
            validCommits.add(commit.getCommitID());
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return validCommits.size();
  }

  public static int commitsOfTestsInTheLastNMonths(Properties prop, String repoAnalysisFolder, Set<String> fileNames, int n){
    Set<String> validCommits = new HashSet<String>();
    int upperBoundYear = AnalysisUtils.DATASET_YEAR;
    int upperBoundMonth = AnalysisUtils.DATASET_MONTH;
    int upperBoundDay = AnalysisUtils.DATASET_DAY;
    LocalDateTime upperBound = LocalDateTime.of(upperBoundYear, upperBoundMonth, upperBoundDay, 0, 0);
    LocalDateTime lowerBound = upperBound.minusMonths(n);
    try {
      for(String fileName:fileNames) {
        //get commits
        String command = prop.getProperty("python_bin_full_path") + " " +
            prop.getProperty("python_check_commits_script_for_file_full_path") + " " +
            prop.getProperty("config_full_path") + " " + repoAnalysisFolder + " "+ fileName;
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        Set<String> commitLines = new HashSet<String>();
        String line = "";
        while ((line = input.readLine()) != null) {
          line = line.replace(System.lineSeparator(), "");
          commitLines.add(line);
        }
        input.close();
        List<Commit> commits = new ArrayList<Commit>();
        for (String commitLine : commitLines) {
          String commitInfo[] = commitLine.split("   ");
          String commitDateString = commitInfo[0];
          String commitID= commitInfo[1];
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
          Date date = sdf.parse(commitDateString);
          LocalDateTime commitDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
          commits.add(new Commit(commitID, commitDate));
        }
        //sort commits
        Collections.sort(commits, new Comparator<Commit>() {
          @Override
          public int compare(Commit c1, Commit c2) {
            if (c1.getCommitDate().isAfter(c2.getCommitDate())) {
              return -1;
            } else if (c1.getCommitDate().isBefore((c2.getCommitDate()))){
              return 1;
            }
            else {
              return 0;
            }
          }
        });
        //check range of commit
        for (Commit commit : commits) {
          if (commit.getCommitDate().isAfter(lowerBound) && commit.getCommitDate().isBefore(upperBound)) {
            validCommits.add(commit.getCommitID());
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return validCommits.size();
  }

  public static Set<String> getRelevantFiles(String folderName, String extension) {
    Set<String> result = new HashSet<String>();
    List<String> workList = new ArrayList<String>();
    workList.add(folderName);
    while (!workList.isEmpty()) {
      String currFileName = workList.remove(0);
      File currFile = new File(currFileName);
      if (currFile.isDirectory()) {
        String fileNameArray[] = currFile.list();
        for (String fileName : fileNameArray) {
          String fullFileName = currFileName + File.separator + fileName;
          workList.add(fullFileName);
        }
      } else {
        if (currFileName.endsWith(extension)) {
          if(currFileName.endsWith(AnalysisUtils.EXAMPLE_JVM_TEST+extension) || currFileName.endsWith(
              AnalysisUtils.EXAMPLE_DEVICE_TEST+extension)){
            continue;
          }
          result.add(currFileName);
        }
      }
    }
    return result;
  }

  public static void runKtLintOnRelevantKtFiles(Properties prop, String folderName, Map<String, String> kotlinFileKtLintResultMap) {
    List<String> workList = new ArrayList<String>();
    workList.add(folderName);
    while (!workList.isEmpty()) {
      String currFileName = workList.remove(0);
      File currFile = new File(currFileName);
      if (currFile.isDirectory()) {
        String fileNameArray[] = currFile.list();
        for (String fileName : fileNameArray) {
          String fullFileName = currFileName + File.separator + fileName;
          workList.add(fullFileName);
        }
      } else {
        if (currFileName.endsWith(AnalysisUtils.KOTLIN_EXTENSION)) {
          //System.out.println("Analyzing:"+currFileName);
          if(currFileName.endsWith(AnalysisUtils.EXAMPLE_JVM_TEST+ AnalysisUtils.KOTLIN_EXTENSION) || currFileName.endsWith(
              AnalysisUtils.EXAMPLE_DEVICE_TEST+ AnalysisUtils.KOTLIN_EXTENSION)){
            continue;
          }
          try {
            String command = prop.getProperty("python_bin_full_path") + " " +
                prop.getProperty("python_run_ktlint_script_full_path") + " " +
                prop.getProperty("config_full_path") + " " +
                currFileName;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Set<String> ktLines = new HashSet<String>();
            String line = "";
            while ((line = input.readLine()) != null) {
              line = line.replace(System.lineSeparator(), "");
              ktLines.add(line);
            }
            input.close();
            String mapContent = "";
            for(String ktLine : ktLines){
              mapContent = mapContent + ktLine + "\n";
            }
            if(mapContent.endsWith("\n")){
              mapContent = mapContent.substring(0, mapContent.length()-1);
            }
            kotlinFileKtLintResultMap.put(currFileName, mapContent);
          }
          catch(Exception e){
            e.printStackTrace();
            System.exit(1);
          }
        }
      }
    }
  }

  public static String findBuildFile(String appAnalysisFolder){
    String result = "";
    String builFilePath = appAnalysisFolder.substring(0, appAnalysisFolder.lastIndexOf(File.separator)) + File.separator + "build.gradle";
    if(new File(builFilePath).exists()){
      result = builFilePath;
      return result;
    }
    else{
      builFilePath = appAnalysisFolder.substring(0, appAnalysisFolder.lastIndexOf(File.separator)) + File.separator + "build.gradle.kts";
      if(new File(builFilePath).exists()){
        result = builFilePath;
        return result;
      }
    }
    return result;
  }

  public static Set<String> getJvmTestFiles(String folderName, String extension) {
    System.out.println("inside getJVMTESTFILES folderName : "+folderName);
    Set<String> result = new HashSet<String>();
    List<String> workList = new ArrayList<String>();
    workList.add(folderName);
    while (!workList.isEmpty()) {
      String currFileName = workList.remove(0);
      File currFile = new File(currFileName);
      if (currFile.isDirectory()) {
        String fileNameArray[] = currFile.list();
        for (String fileName : fileNameArray) {
          String fullFileName = currFileName + File.separator + fileName;
          workList.add(fullFileName);
        }
      } else {
        if (currFileName.endsWith(extension)) {
          if(currFileName.endsWith(AnalysisUtils.EXAMPLE_JVM_TEST+extension)){
            continue;
          }
          String unitTest = folderName+File.separator+JVM_TESTS_FOLDER+File.separator;
          if (currFileName.startsWith(unitTest)) {
            result.add(currFileName);
          }
        }
      }
    }
    return result;
  }

  public static Set<String> getDeviceTestFiles(String folderName, String extension) {
    Set<String> result = new HashSet<String>();
    List<String> workList = new ArrayList<String>();
    workList.add(folderName);
    while (!workList.isEmpty()) {
      String currFileName = workList.remove(0);
      File currFile = new File(currFileName);
      if (currFile.isDirectory()) {
        String fileNameArray[] = currFile.list();
        for (String fileName : fileNameArray) {
          String fullFileName = currFileName + File.separator + fileName;
          workList.add(fullFileName);
        }
      } else {
        if (currFileName.endsWith(extension)) {
          if(currFileName.endsWith(AnalysisUtils.EXAMPLE_DEVICE_TEST+extension)){
            continue;
          }
          String instrumentationTest = folderName+File.separator+DEVICE_TESTS_FOLDER+File.separator;
          if (currFileName.contains(instrumentationTest)) {
            result.add(currFileName);
          }
        }
      }
    }
    return result;
  }

  public static Set<String> getTestFiles(String folderName, String extension) {
    Set<String> result = new HashSet<String>();
    result.addAll(getJvmTestFiles(folderName, extension));
    result.addAll(getDeviceTestFiles(folderName, extension));
    return result;
  }

  public static int getTestsNumFromJavaCode(Set<String> testFiles) {
    int result = 0;
    for (String fileName : testFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
              methodDeclaration.getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(UI_THREAD_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(SMALL_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(MEDIUM_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(LARGE_TEST_ANNOTATION)) {
              result++;
              //break to count the test only once
              break;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getTestsNumFromKotlinCode(Set<String> testFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : testFiles) {
      //run klint on file and process output
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(TEST_RULE)){
            result++;
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }

  //check for java mock class
  //1) file name contains the word mock but not mockito
  //2) file does not have mockito import
  //3) file name does not end with test or tests
  public static int getMockClassesNumFromJavaCode(Set<String> testFiles) {
    int result = 0;
    for (String fileName : testFiles) {
      boolean containsMockitoImport = false;
      boolean containsMockWordAndDoesNotContainMockitoWord = false;
      boolean endsWithTestWordOrTestsWord = false;
      try {
        //check imports
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<ImportDeclaration> importDeclarations = cu.findAll(ImportDeclaration.class);
        for (ImportDeclaration importDeclaration : importDeclarations) {
          if (importDeclaration.getName().asString().startsWith(JAVA_MOCKITO_DEPENDENCY)) {
            containsMockitoImport = true;
            break;
          }
        }
        String fileNameLower = fileName.replace(".java","").toLowerCase();
        int separatorIndex = fileNameLower.lastIndexOf(File.separator);
        if(separatorIndex!=-1){
          fileNameLower = fileNameLower.substring(separatorIndex+1);
        }
        if(fileNameLower.contains(MOCK_WORD) && !fileNameLower.contains(MOCKITO_WORD)){
          containsMockWordAndDoesNotContainMockitoWord = true;
        }
        if(fileNameLower.endsWith(TEST_WORD) || fileNameLower.endsWith(TESTS_WORD)){
          endsWithTestWordOrTestsWord = true;
        }
        if(!containsMockitoImport && containsMockWordAndDoesNotContainMockitoWord && !endsWithTestWordOrTestsWord){
          result++;
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

//  //check for ktolin mock class
//  //1) file name contains the word mock but not mockito
//  //2) file does not have mockito or mockito kotlin import
//  //3) file name does not end with test or tests
//  public static int getMockClassesNumFromKotlinCode(Set<String> testFiles, Map<String, String> kotlinFileKtLintResultMap) {
//    int result = 0;
//    for (String fileName : testFiles) {
//      boolean containsMockitoOrMockitoKotlinImport = false;
//      boolean containsMockWordAndDoesNotContainMockitoWord = false;
//      boolean endsWithTestWordOrTestsWord = false;
//
//      try {
//        //check content against ktlin on file and process output
//        String mapContent = kotlinFileKtLintResultMap.get(fileName);
//        String[] ktLines = mapContent.split("\n");
//        for(String ktLine : ktLines){
//          if(ktLine.contains(KOTLIN_MOCKITO_DEPENDENCY) || ktLine.contains(KOTLIN_MOCKITO_KOTLIN_DEPENDENCY)){
//            containsMockitoOrMockitoKotlinImport = true;
//            break;
//          }
//        }
//        String fileNameLower = fileName.replace(".kt","").toLowerCase();
//        int separatorIndex = fileNameLower.lastIndexOf(File.separator);
//        if(separatorIndex!=-1){
//          fileNameLower = fileNameLower.substring(separatorIndex+1);
//        }
//        if(fileNameLower.contains(MOCK_WORD) && !fileNameLower.contains(MOCKITO_WORD)){
//          containsMockWordAndDoesNotContainMockitoWord = true;
//        }
//        if(fileNameLower.endsWith(TEST_WORD) || fileNameLower.endsWith(TESTS_WORD)){
//          endsWithTestWordOrTestsWord = true;
//        }
//        if(!containsMockitoOrMockitoKotlinImport && containsMockWordAndDoesNotContainMockitoWord && !endsWithTestWordOrTestsWord){
//          result++;
//        }
//      }
//      catch(Exception e){
//        e.printStackTrace();
//        System.exit(1);
//      }
//    }
//    return result;
//  }

  //jvm tests
  public static int getJvmTestsNumFromJavaCode(Set<String> jvmTestFiles) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
              methodDeclaration.getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION)) {
              result++;
              //break to count only once
              break;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getJvmTestsNumFromKotlinCode(Set<String> jvmTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      //run klint on file and process output
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(JVM_TEST_RULE)){
            result++;
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }

  //device tests
  public static int getDeviceTestsNumFromJavaCode(Set<String> deviceTestFiles) {
    int result = 0;
    for (String fileName : deviceTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
              methodDeclaration.getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(UI_THREAD_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(SMALL_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(MEDIUM_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(LARGE_TEST_ANNOTATION)) {
              result++;
              //break to count only once
              break;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getDeviceTestsNumFromKotlinCode(Set<String> deviceTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : deviceTestFiles) {
      //run klint on file and process output
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(DEVICE_TEST_RULE)){
            result++;
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }


  //unit tests
  //TEST_ANNOTATION in JVM_TESTS_FOLDER
  //UI_THREAD_TEST_ANNOTATION in DEVICE_TESTS_FOLDER
  //SMALL_TEST_ANNOTATION in DEVICE_TESTS_FOLDER
  public static int getUnitTestsNumFromJavaCode(Set<String> jvmTestFiles, Set<String> deviceTestFiles) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
              methodDeclaration.getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION)) {
              result++;
              //break to count only once
              break;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    for (String fileName : deviceTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          boolean isUnitTest = false;
          com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
              methodDeclaration.getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(UI_THREAD_TEST_ANNOTATION) ||
                methodAnnotation.getNameAsString().equals(SMALL_TEST_ANNOTATION)) {
              isUnitTest=true;
              //break to count only once
              break;
            }
          }
          if(isUnitTest){
            result++;
            continue;
          }
          boolean hasTestAnnotation = false;
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION)) {
              hasTestAnnotation=true;
              //break to count only once
              break;
            }
          }
          if(!hasTestAnnotation){
            continue;
          }
          Node classNode = AnalysisUtils.getDeclaringClassNode(methodDeclaration);
          if(classNode instanceof ClassOrInterfaceDeclaration){
            boolean inClassWithSmallTestAnnotation = false;
            com.github.javaparser.ast.NodeList<AnnotationExpr> classAnnotations = ((ClassOrInterfaceDeclaration) classNode).getAnnotations();
            for (AnnotationExpr methodAnnotation : methodAnnotations) {
              if (methodAnnotation.getNameAsString().equals(SMALL_TEST_ANNOTATION)){
                inClassWithSmallTestAnnotation = true;
                break;
              }
            }
            if(inClassWithSmallTestAnnotation){
              result++;
              continue;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }


  public static int getUnitTestsNumFromKotlinCode(Set<String> jvmTestFiles, Set<String> deviceTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(JVM_UNIT_TEST_RULE)){
            result++;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    for (String fileName : deviceTestFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(DEVICE_UNIT_TEST_RULE)){
            result++;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static boolean isIntegrationOrGUITest(Node node){
    boolean isIntegrationOrGUITest = false;
    Node current = node;
    while(current!=null){
      if(current instanceof MethodDeclaration) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) current;
        com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations = methodDeclaration.getAnnotations();
        boolean hasTestAnnotation = false;
        boolean hasUiThreadAnnotation = false;
        boolean hasSmallTestAnnotation = false;
        boolean hasMediumTestAnnotation = false;
        boolean hasLargeTestAnnotation = false;
        for (AnnotationExpr methodAnnotation : methodAnnotations) {
          if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION)){
            hasTestAnnotation = true;
          }
          else if (methodAnnotation.getNameAsString().equals(UI_THREAD_TEST_ANNOTATION)){
            hasUiThreadAnnotation = true;
          }
          else if (methodAnnotation.getNameAsString().equals(SMALL_TEST_ANNOTATION)){
            hasSmallTestAnnotation = true;
          }
          else if (methodAnnotation.getNameAsString().equals(MEDIUM_TEST_ANNOTATION)){
            hasMediumTestAnnotation = true;
          }
          else if (methodAnnotation.getNameAsString().equals(LARGE_TEST_ANNOTATION)){
            hasLargeTestAnnotation = true;
          }
        }
        boolean inClassWithSmallTestAnnotation = false;
        Node classNode = AnalysisUtils.getDeclaringClassNode(methodDeclaration);
        if(classNode instanceof ClassOrInterfaceDeclaration){
          com.github.javaparser.ast.NodeList<AnnotationExpr> classAnnotations = ((ClassOrInterfaceDeclaration) classNode).getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(SMALL_TEST_ANNOTATION)){
              inClassWithSmallTestAnnotation = true;
              break;
            }
          }
        }

        if((hasTestAnnotation && !hasUiThreadAnnotation && !hasSmallTestAnnotation && !inClassWithSmallTestAnnotation) || hasMediumTestAnnotation || hasLargeTestAnnotation){
          isIntegrationOrGUITest=true;
          break;
        }
        break;
      }
      else{
        if(current.getParentNode().isPresent()){
          current = current.getParentNode().get();
        }
        else{
          current = null;
        }
      }
    }
    return isIntegrationOrGUITest;
  }

  //get if a method declaration is an espresso test
  public static boolean isEspressoTest(Node methodDeclaration){
    boolean result = false;
    List<MethodCallExpr> methodCallExprs = methodDeclaration.findAll(MethodCallExpr.class);
    for (MethodCallExpr methodCallExpr : methodCallExprs) {
      //onView
      if (methodCallExpr.getNameAsString().equals("onView")) {
        if(methodCallExpr.getParentNode().isPresent()) {
          String methodName = AnalysisUtils.getNameOfContainingMethodNode(methodCallExpr);
          String declaringClassName = AnalysisUtils.getNameOfDeclaringClassNode(methodCallExpr);
          boolean isInsideIntegrationTest = isIntegrationOrGUITest(methodCallExpr);
          if(!methodName.equals("") && !declaringClassName.equals("") && isInsideIntegrationTest){
            result = true;
            return result;
          }
        }
      }
      //onData
      else if (methodCallExpr.getNameAsString().equals("onData")) {
        if(methodCallExpr.getParentNode().isPresent()) {
          String methodName = AnalysisUtils.getNameOfContainingMethodNode(methodCallExpr);
          String declaringClassName = AnalysisUtils.getNameOfDeclaringClassNode(methodCallExpr);
          boolean isInsideIntegrationTest = isIntegrationOrGUITest(methodCallExpr);
          if(!methodName.equals("") && !declaringClassName.equals("") && isInsideIntegrationTest){
            result = true;
            return result;
          }
        }
      }
    }
    return result;
  }

  public static boolean isGUITest(Node methodDeclaration){
    boolean result = false;
    if(isEspressoTest(methodDeclaration)){
      result = true;
    }
    return result;
  }

  //integration tests
  //TEST_ANNOTATION and not UI_THREAD_TEST_ANNOTATION and not SMALL_TEST_ANNOTATION in DEVICE_TESTS_FOLDER
  //MEDIUM_TEST_ANNOTATION in DEVICE_TESTS_FOLDER
  //LARGE_TEST_ANNOTATION in DEVICE_TESTS_FOLDER
  public static int getIntegrationTestsNumFromJavaCode(Set<String> deviceTestFiles) {
    int result = 0;
    for (String fileName : deviceTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          //do not count gui tests as integration tests
          if(isGUITest(methodDeclaration)){
            continue;
          }
          if(isIntegrationOrGUITest(methodDeclaration)){
            result++;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getIntegrationTestsNumFromKotlinCode(Set<String> deviceTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : deviceTestFiles) {
      //run klint on file and process output
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(DEVICE_INTEGRATION_TEST_RULE)){
            result++;
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }

  //find GUI tests num
  public static int getGUITestsNumFromJavaCode(Set<String> deviceTestFiles) {
    int result = 0;
    for (String fileName : deviceTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          if(isIntegrationOrGUITest(methodDeclaration) && isGUITest(methodDeclaration)){
            result++;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getGUITestsNumFromKotlinCode(Set<String> deviceTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : deviceTestFiles) {
      //run klint on file and process output
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(DEVICE_GUI_TEST_RULE)){
            result++;
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }

  public static Set<String> getRobolectricTestRunnersFromJavaCode(Set<String> testFiles) {
    Set<String> results = new HashSet<String>();
    results.add(ROBOLECTRIC_TEST_RUNNER_CLASS);
    for (String fileName : testFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<ClassOrInterfaceDeclaration> classDeclarations =
            cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
          for (ClassOrInterfaceType classType : classDeclaration.getExtendedTypes()) {
            if (classType.getNameAsString().equals(ROBOLECTRIC_TEST_RUNNER_CLASS)) {
              results.add(classDeclaration.getNameAsString());
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return results;
  }

  public static Set<String> getRobolectricTestRunnersFromKotlinCode(Set<String> testFiles, Map<String, String> kotlinFileKtLintResultMap) {
    Set<String> results = new HashSet<String>();
    results.add(ROBOLECTRIC_TEST_RUNNER_CLASS);
    for (String fileName : testFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(ROBOLECTRIC_TEST_RUNNER_RULE)){
            String[] ktLineArray = ktLine.split("#");
            String robolectricTestRunner = ktLineArray[2];
            results.add(robolectricTestRunner);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return results;
  }

  public static int getRobolectricTestsNumFromJavaCode(Set<String> jvmTestFiles, Set<String> robolectricTestRunners) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
              methodDeclaration.getAnnotations();
          for (AnnotationExpr methodAnnotation : methodAnnotations) {
            if (methodAnnotation.getNameAsString().equals(TEST_ANNOTATION)) {
              Node classDeclarationNode =
                  AnalysisUtils.getDeclaringClassNode(methodDeclaration);
              if (classDeclarationNode != null) {
                ClassOrInterfaceDeclaration classDeclaration =
                    (ClassOrInterfaceDeclaration) classDeclarationNode;
                boolean isRobolectric = false;
                com.github.javaparser.ast.NodeList<AnnotationExpr> classAnnotations =
                    classDeclaration.getAnnotations();
                for (AnnotationExpr classAnnotation : classAnnotations) {
                  if (classAnnotation.getNameAsString().equals("RunWith")) {
                    for (String robolectricTestRunner : robolectricTestRunners) {
                      if (classAnnotation.toString().contains(robolectricTestRunner+".class")) {
                        isRobolectric = true;
                        break;
                      }
                    }
                    if (isRobolectric) {
                      break;
                    }
                  }
                }
                if (isRobolectric) {
                  result++;
                  //avoid counting twice
                  break;
                }
              } else {
                System.out.println("Could not find ClassOrInterfaceDeclaration for "+fileName+" in findRobolectricTestNum");
                System.exit(1);
              }
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getRobolectricTestsNumFromKotlinCode(Properties prop, Set<String> jvmTestFiles, Set<String> robolectricTestRunners) {
    int result = 0;
    try {
      File dest = new File(KTLINT_PARAM_FILE);
      Files.deleteIfExists(dest.toPath());
      dest.createNewFile();
      for(String robolectricTestRunner:robolectricTestRunners){
        String fileLine = robolectricTestRunner+"\n";
        Files.write(dest.toPath(), fileLine.getBytes(), StandardOpenOption.APPEND);
      }
      for (String fileName : jvmTestFiles) {
        //run klint on file and process output
        try {
          String command = prop.getProperty("python_bin_full_path") + " " +
              prop.getProperty("python_run_ktlint_script_full_path") + " " +
              prop.getProperty("config_full_path") + " " +
              fileName;
          Process process = Runtime.getRuntime().exec(command);
          BufferedReader input =
              new BufferedReader(new InputStreamReader(process.getInputStream()));
          Set<String> ktLines = new HashSet<String>();
          String line = "";
          while ((line = input.readLine()) != null) {
            line = line.replace(System.lineSeparator(), "");
            ktLines.add(line);
          }
          input.close();
          for (String ktLine : ktLines) {
            if (ktLine.contains(ROBOLECTRIC_TEST_RULE)) {
              result++;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
      Files.deleteIfExists(dest.toPath());
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  public static int getMockitoMocksNumFromJavaCode(Set<String> testFiles) {
    int result = 0;
    for (String testFile : testFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(testFile));
        //find mock
        List<MethodCallExpr> methodCallExprs = cu.findAll(MethodCallExpr.class);
        for (MethodCallExpr methodCallExpr : methodCallExprs) {
          //mockito mock
          if (methodCallExpr.getNameAsString().equals(AnalysisUtils.MOCKITO_MOCK_API)) {
            result++;
          }
          else if (methodCallExpr.getNameAsString().equals(AnalysisUtils.MOCKITO_MOCKSTATIC_API)) {
            result++;
          }
          //mockito spy
          else if (methodCallExpr.getNameAsString().equals(AnalysisUtils.MOCKITO_SPY_API)) {
            result++;
          }
        }
        List<FieldDeclaration> fieldDeclarations = cu.findAll(FieldDeclaration.class);
        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
          boolean isMock = false;
          boolean isSpy = false;
          for (AnnotationExpr fieldAnnotation : fieldDeclaration.getAnnotations()) {
            //mock annotation
            if (fieldAnnotation.getNameAsString().equals(AnalysisUtils.MOCKITO_MOCK_ANNOTATION)) {
              isMock = true;
            }
            //mock annotation
            else if (fieldAnnotation.getNameAsString().equals(AnalysisUtils.MOCKITO_SPY_ANNOTATION)) {
              isSpy = true;
            }
          }
          if(isMock &&  isSpy){
            System.out.println("Field:"+fieldDeclaration.toString()+" in "+testFile+" is both Mock and Spy");
            System.exit(1);
          }
          else if (isMock || isSpy){
            result++;
          }
        }

      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

//  //find mockito mocks
//  public static int getMockitoMocksNumFromKotlinCode(Set<String> testFiles, Map<String, String> kotlinFileKtLintResultMap) {
//    int result = 0;
//    for (String fileName : testFiles) {
//      //run klint on file and process output
//      try {
//        String mapContent = kotlinFileKtLintResultMap.get(fileName);
//        String[] ktLines = mapContent.split("\n");
//        for(String ktLine : ktLines){
//          if(ktLine.contains(MOCKITO_MOCK_RULE) ||
//              ktLine.contains(MOCKITO_MOCKSTATIC_RULE) ||
//              ktLine.contains(MOCKITO_SPY_RULE) ||
//              ktLine.contains(MOCKITO_MOCK_ANNOTATION_RULE) ||
//              ktLine.contains(MOCKITO_SPY_ANNOTATION_RULE)){
//            result++;
//          }
//        }
//      }
//      catch(Exception e){
//        e.printStackTrace();
//        System.exit(1);
//      }
//    }
//    return result;
//  }

  //robolectric shadows
  public static int getRobolectricShadowsNumFromJavaCode(Set<String> jvmTestFiles) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<ClassOrInterfaceDeclaration> classDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> classAnnotations =
              classDeclaration.getAnnotations();
          for (AnnotationExpr classAnnotation : classAnnotations) {
            if (classAnnotation.getNameAsString().equals(SHADOW_ANNOTATION)) {
              result++;
              //break to count only once
              break;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  //robolectric shadows
  public static List<MockInfo> getRobolectricShadowsFromJavaCode(Set<String> jvmTestFiles) {
    List<MockInfo> result = new ArrayList<MockInfo>();
    for (String fileName : jvmTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<ClassOrInterfaceDeclaration> classDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
          com.github.javaparser.ast.NodeList<AnnotationExpr> classAnnotations =
              classDeclaration.getAnnotations();
          for (AnnotationExpr classAnnotation : classAnnotations) {
            if (classAnnotation.getNameAsString().equals(SHADOW_ANNOTATION)) {
              MockInfo mockInfo = new MockInfo(fileName, -1, "", "");
              result.add(mockInfo);
              //break to count only once
              break;
            }
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getRobolectricShadowsNumFromKotlinCode(Set<String> jvmTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(SHADOW_ANNOTATION)){
            result++;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static List<MockInfo> getRobolectricShadowsFromKotlinCode(Set<String> jvmTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    List<MockInfo> result = new ArrayList<MockInfo>();
    for (String fileName : jvmTestFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(SHADOW_ANNOTATION)){
            MockInfo mockInfo = new MockInfo(fileName, -1, "", "");
            result.add(mockInfo);
            break;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }


  //robolectric shadows
  public static int getRobolectricShadowMethodsNumFromJavaCode(Set<String> jvmTestFiles) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<ClassOrInterfaceDeclaration> classDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
        for (ClassOrInterfaceDeclaration classDeclaration : classDeclarations) {
          boolean isShadow = false;
          com.github.javaparser.ast.NodeList<AnnotationExpr> classAnnotations =
              classDeclaration.getAnnotations();
          for (AnnotationExpr classAnnotation : classAnnotations) {
            if (classAnnotation.getNameAsString().equals(SHADOW_ANNOTATION)) {
              isShadow=true;
              //break to count only once
              break;
            }
          }
          if(isShadow){
            List<MethodDeclaration> methodDeclarations = classDeclaration.findAll(MethodDeclaration.class);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
              com.github.javaparser.ast.NodeList<AnnotationExpr> methodAnnotations =
                  methodDeclaration.getAnnotations();
              for (AnnotationExpr methodAnnotation : methodAnnotations) {
                if (methodAnnotation.getNameAsString().equals(SHADOW_METHOD_ANNOTATION)) {
                  result++;
                  //break to count only once
                  break;
                }
              }
            }
          }
        }

      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getRobolectricShadowMethodsNumFromKotlinCode(Set<String> jvmTestFiles, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : jvmTestFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(SHADOW_METHOD_ANNOTATION)){
            result++;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static int getJavaLoc(Properties prop, String appFolder) {
    int result = 0;
    try {
      String command = prop.getProperty("python_bin_full_path") + " " +
          prop.getProperty("python_run_cloc_script_full_path") + " " +
          prop.getProperty("config_full_path") + " " +
          appFolder;
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader input =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      Set<String> outputLines = new HashSet<String>();
      String line = "";
      while ((line = input.readLine()) != null) {
        line = line.replace(System.lineSeparator(), "");
        outputLines.add(line);
      }
      input.close();
      for (String outputLine : outputLines) {
        if (outputLine.startsWith("Java ")) {
          String loc = outputLine.substring(outputLine.lastIndexOf(" ") + 1);
          result = Integer.parseInt(loc);
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  public static int getKotlinLoc(Properties prop, String appFolder) {
    int result = 0;
    try {
      String command = prop.getProperty("python_bin_full_path") + " " +
          prop.getProperty("python_run_cloc_script_full_path") + " " +
          prop.getProperty("config_full_path") + " " +
          appFolder;
      System.out.println("command inside getKotlinLoc"+command);
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader input =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      Set<String> outputLines = new HashSet<String>();
      String line = "";
      while ((line = input.readLine()) != null) {
        line = line.replace(System.lineSeparator(), "");
        outputLines.add(line);
      }
      input.close();
      for (String outputLine : outputLines) {
        if (outputLine.startsWith("Kotlin ")) {
          String loc = outputLine.substring(outputLine.lastIndexOf(" ") + 1);
          result = Integer.parseInt(loc);
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  public static int getXMLLoc(Properties prop, String appFolder) {
    int result = 0;
    try {
      String command = prop.getProperty("python_bin_full_path") + " " +
          prop.getProperty("python_run_cloc_script_full_path") + " " +
          prop.getProperty("config_full_path") + " " +
          appFolder;
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader input =
          new BufferedReader(new InputStreamReader(process.getInputStream()));
      Set<String> outputLines = new HashSet<String>();
      String line = "";
      while ((line = input.readLine()) != null) {
        line = line.replace(System.lineSeparator(), "");
        outputLines.add(line);
      }
      input.close();
      for (String outputLine : outputLines) {
        if (outputLine.startsWith("XML ")) {
          String loc = outputLine.substring(outputLine.lastIndexOf(" ") + 1);
          result = Integer.parseInt(loc);
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return result;
  }

  //count the number of files with a specific import
  public static int countImportsInJavaFiles(Set<String> testFiles, Set<String> importNames){
    int result = 0;
    for (String fileName : testFiles) {
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<ImportDeclaration> importDeclarations = cu.findAll(ImportDeclaration.class);
        for (ImportDeclaration importDeclaration : importDeclarations) {
          boolean found = false;
          for(String importName:importNames){
            if (importDeclaration.getName().asString().startsWith(importName)) {
              found = true;
              break;
            }
          }
          if(found) {
            result++;
            break;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static boolean hasExactImportInJavaFile(String fileName, Set<String> importNames){
    boolean result = false;
      try {
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        //find tests
        List<ImportDeclaration> importDeclarations = cu.findAll(ImportDeclaration.class);
        for (ImportDeclaration importDeclaration : importDeclarations) {
          boolean found = false;
          for(String importName:importNames){
            if (importDeclaration.getName().asString().equals(importName)) {
              found = true;
              break;
            }
          }
          if(found) {
            result=true;
            return result;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    return result;
  }

  public static int countImportsInKotlinFiles(Set<String> testFiles, String importName, Map<String, String> kotlinFileKtLintResultMap) {
    int result = 0;
    for (String fileName : testFiles) {
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(importName)){
            result++;
            break;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }


  public static boolean hasImportsInKotlinFiles(String fileName, Set<String> importNames, Map<String, String> kotlinFileKtLintResultMap) {
    boolean result = false;
      try {
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          boolean found = false;
          for(String importName:importNames) {
            if (ktLine.contains(importName)) {
              found = true;
              break;
            }
          }
          if(found){
            result = true;
            return true;
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    return result;
  }

  public static boolean isCall(String fileName, String callLineNum, String framework, Map<String, String> kotlinFileKtLintResultMap) {
    boolean result = false;
    try {
      String mapContent = kotlinFileKtLintResultMap.get(fileName);
      String[] ktLines = mapContent.split("\n");
      for(String ktLine : ktLines){
        if (ktLine.contains(framework)) {
          String lineParts[] = ktLine.split("#");
          String lineNum = lineParts[2];
          if(lineNum.equals(callLineNum)){
            result = true;
            return result;
          }
        }
      }
    } catch (ParseProblemException ppe) {
      //nothing we can do about this
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  public static List<MockInfo> getMockitoMockInfoFromJavaCode(Set<String> testFiles) {
    List<MockInfo> result = new ArrayList<MockInfo>();
    for (String testFile : testFiles) {
      try {

        Set<String> mockitoSpecificImports = new HashSet<String>();
        mockitoSpecificImports.add(AnalysisUtils.JAVA_MOCKITO_SPECIFIC_DEPENDENCY);
        boolean hasMockitoSpecificImports = hasExactImportInJavaFile(testFile, mockitoSpecificImports);

        Set<String> mockitoMockSpecificImports = new HashSet<String>();
        mockitoMockSpecificImports.add(AnalysisUtils.JAVA_MOCKITO_MOCK_SPECIFIC_DEPENDENCY);
        boolean hasMockitoMockSpecificImports = hasExactImportInJavaFile(testFile, mockitoMockSpecificImports);

        Set<String> mockitoSpySpecificImports = new HashSet<String>();
        mockitoSpySpecificImports.add(AnalysisUtils.JAVA_MOCKITO_SPY_SPECIFIC_DEPENDENCY);
        boolean hasMockitoSpySpecificImports = hasExactImportInJavaFile(testFile, mockitoSpySpecificImports);

        Set<String> mockitoMockStaticSpecificImports = new HashSet<String>();
        mockitoMockStaticSpecificImports.add(AnalysisUtils.JAVA_MOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY);
        boolean hasMockitoMockstaticSpecificImports = hasExactImportInJavaFile(testFile, mockitoMockStaticSpecificImports);

        Set<String> mockitoAnnotationSpecificImports = new HashSet<String>();
        mockitoAnnotationSpecificImports.add(AnalysisUtils.JAVA_MOCKITO_ANNOTATION_SPECIFIC_DEPENDENCY);
        boolean hasMockitoAnnotationSpecificImports = hasExactImportInJavaFile(testFile, mockitoAnnotationSpecificImports);

        Set<String> mockitoAnnotationMockSpecificImports = new HashSet<String>();
        mockitoAnnotationMockSpecificImports.add(AnalysisUtils.JAVA_MOCKITO_ANNOTATION_MOCK_SPECIFIC_DEPENDENCY);
        boolean hasMockitoAnnotationMockSpecificImports = hasExactImportInJavaFile(testFile, mockitoAnnotationMockSpecificImports);

        Set<String> mockitoAnnotationSpySpecificImports = new HashSet<String>();
        mockitoAnnotationSpySpecificImports.add(AnalysisUtils.JAVA_MOCKITO_ANNOTATION_SPY_SPECIFIC_DEPENDENCY);
        boolean hasMockitoAnnotationSpySpecificImports = hasExactImportInJavaFile(testFile, mockitoAnnotationSpySpecificImports);

        Set<String> powermockitoSpecificImports = new HashSet<String>();
        powermockitoSpecificImports.add(AnalysisUtils.JAVA_POWERMOCKITO_SPECIFIC_DEPENDENCY);
        boolean hasPowermockitoSpecificImports = hasExactImportInJavaFile(testFile, powermockitoSpecificImports);

        Set<String> powermockitoMockSpecificImports = new HashSet<String>();
        powermockitoMockSpecificImports.add(AnalysisUtils.JAVA_POWERMOCKITO_MOCK_SPECIFIC_DEPENDENCY);
        boolean hasPowermockitoMockSpecificImports = hasExactImportInJavaFile(testFile, powermockitoMockSpecificImports);

        Set<String> powermockitoMockStaticSpecificImports = new HashSet<String>();
        powermockitoMockStaticSpecificImports.add(AnalysisUtils.JAVA_POWERMOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY);
        boolean hasPowermockitoMockStaticSpecificImports = hasExactImportInJavaFile(testFile, powermockitoMockStaticSpecificImports);

        Set<String> powermockitoSpySpecificImports = new HashSet<String>();
        powermockitoSpySpecificImports.add(AnalysisUtils.JAVA_POWERMOCKITO_SPY_SPECIFIC_DEPENDENCY);
        boolean hasPowermockitoSpySpecificImports = hasExactImportInJavaFile(testFile, powermockitoSpySpecificImports);


        CompilationUnit cu = StaticJavaParser.parse(new File(testFile));
        //find mock
        List<MethodCallExpr> methodCallExprs = cu.findAll(MethodCallExpr.class);
        for (MethodCallExpr methodCallExpr : methodCallExprs) {
          //mockito mock
          if (methodCallExpr.getNameAsString().equals(AnalysisUtils.MOCKITO_MOCK_API)) {
            boolean onMockitoClass = false;
            if(methodCallExpr.getScope().isPresent()){
              String className = methodCallExpr.getScope().get().toString();
              if(className.equals(AnalysisUtils.MOCKITO_FRAMEWORK_UPPER_INITIAL) || className.endsWith("."+
                  AnalysisUtils.MOCKITO_FRAMEWORK_UPPER_INITIAL)){
                onMockitoClass = true;
              }
            }
            boolean onPowerMockitoClass = false;
            if(methodCallExpr.getScope().isPresent()){
              String className = methodCallExpr.getScope().get().toString();
              if(className.equals(POWERMOCKITO_FRAMEWORK_UPPER_INITIAL) || className.endsWith("."+POWERMOCKITO_FRAMEWORK_UPPER_INITIAL)){
                onPowerMockitoClass = true;
              }
            }
            int lineNumber = -1;
            if(methodCallExpr.getRange().isPresent()){
              lineNumber = methodCallExpr.getRange().get().begin.line;
            }
            if(hasMockitoMockSpecificImports || hasMockitoSpecificImports || onMockitoClass) {
              MockInfo mockInfo =
                  new MockInfo(testFile, lineNumber, AnalysisUtils.MOCKITO_MOCK_API, AnalysisUtils.MOCKITO_FRAMEWORK);
              result.add(mockInfo);
            }
            else{
                if(hasPowermockitoMockSpecificImports || hasPowermockitoSpecificImports || onPowerMockitoClass){
                  //System.out.println("CHECK POWERMOCK IMPORT:" + testFile + "#" + lineNumber);
                }
                else {
                  //System.out.println("CHECK MISSING IMPORT:" + testFile + "#" + lineNumber);
                  //System.exit(1);
                }
            }
          }
          else if (methodCallExpr.getNameAsString().equals(AnalysisUtils.MOCKITO_MOCKSTATIC_API)) {
            boolean onMockitoClass = false;
            if(methodCallExpr.getScope().isPresent()){
              String className = methodCallExpr.getScope().get().toString();
              if(className.equals(AnalysisUtils.MOCKITO_FRAMEWORK_UPPER_INITIAL) || className.endsWith("."+
                  AnalysisUtils.MOCKITO_FRAMEWORK_UPPER_INITIAL)){
                onMockitoClass = true;
              }
            }
            boolean onPowerMockitoClass = false;
            if(methodCallExpr.getScope().isPresent()){
              String className = methodCallExpr.getScope().get().toString();
              if(className.equals(POWERMOCKITO_FRAMEWORK_UPPER_INITIAL) || className.endsWith("."+POWERMOCKITO_FRAMEWORK_UPPER_INITIAL)){
                onPowerMockitoClass = true;
              }
            }
            int lineNumber = -1;
            if(methodCallExpr.getRange().isPresent()){
              lineNumber = methodCallExpr.getRange().get().begin.line;
            }
            if(hasMockitoMockstaticSpecificImports || hasMockitoSpecificImports || onMockitoClass) {
              MockInfo mockInfo = new MockInfo(testFile, lineNumber, AnalysisUtils.MOCKITO_MOCKSTATIC_API, AnalysisUtils.MOCKITO_FRAMEWORK);
              result.add(mockInfo);
            }
            else{
              if(hasPowermockitoMockStaticSpecificImports || hasPowermockitoSpecificImports || onPowerMockitoClass){
                //System.out.println("CHECK POWERMOCK IMPORT:" + testFile + "#" + lineNumber);
              }
              else {
                //System.out.println("CHECK MISSING IMPORT:" + testFile + "#" + lineNumber);
                //System.exit(1);
              }
            }
          }
          //mockito spy
          else if (methodCallExpr.getNameAsString().equals(AnalysisUtils.MOCKITO_SPY_API)) {
            boolean onMockitoClass = false;
            if(methodCallExpr.getScope().isPresent()){
              String className = methodCallExpr.getScope().get().toString();
              if(className.equals(AnalysisUtils.MOCKITO_FRAMEWORK_UPPER_INITIAL) || className.endsWith("."+
                  AnalysisUtils.MOCKITO_FRAMEWORK_UPPER_INITIAL)){
                onMockitoClass = true;
              }
            }
            boolean onPowerMockitoClass = false;
            if(methodCallExpr.getScope().isPresent()){
              String className = methodCallExpr.getScope().get().toString();
              if(className.equals(POWERMOCKITO_FRAMEWORK_UPPER_INITIAL) || className.endsWith("."+POWERMOCKITO_FRAMEWORK_UPPER_INITIAL)){
                onPowerMockitoClass = true;
              }
            }
            int lineNumber = -1;
            if(methodCallExpr.getRange().isPresent()){
              lineNumber = methodCallExpr.getRange().get().begin.line;
            }
            if(hasMockitoSpySpecificImports || hasMockitoSpecificImports || onMockitoClass) {
              MockInfo mockInfo = new MockInfo(testFile, lineNumber, AnalysisUtils.MOCKITO_SPY_API, AnalysisUtils.MOCKITO_FRAMEWORK);
              result.add(mockInfo);
            }
            else{
              if(hasPowermockitoSpySpecificImports || hasPowermockitoSpecificImports || onPowerMockitoClass){
                //System.out.println("CHECK POWERMOCK IMPORT:" + testFile + "#" + lineNumber);
              }
              else {
                //System.out.println("CHECK MISSING IMPORT:" + testFile + "#" + lineNumber);
                //System.exit(1);
              }
            }
          }
        }
        List<FieldDeclaration> fieldDeclarations = cu.findAll(FieldDeclaration.class);
        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
          boolean isMock = false;
          boolean isSpy = false;
          MockInfo mockInfo = null;
          for (AnnotationExpr fieldAnnotation : fieldDeclaration.getAnnotations()) {
            //mock annotation
            if (fieldAnnotation.getNameAsString().equals(AnalysisUtils.MOCKITO_MOCK_ANNOTATION)) {
              isMock = true;
              int lineNumber = -1;
              if(fieldAnnotation.getRange().isPresent()){
                lineNumber = fieldAnnotation.getRange().get().begin.line;
              }
              if(hasMockitoAnnotationMockSpecificImports || hasMockitoAnnotationSpecificImports) {
                mockInfo =
                    new MockInfo(testFile, lineNumber, AnalysisUtils.MOCKITO_MOCK_ANNOTATION, AnalysisUtils.MOCKITO_FRAMEWORK);
              }
              else{
                //System.out.println("CHECK MISSING IMPORT:"+testFile+"#"+lineNumber);
                //System.exit(1);
                isMock = false;
              }
            }
            //mock annotation
            else if (fieldAnnotation.getNameAsString().equals(AnalysisUtils.MOCKITO_SPY_ANNOTATION)) {
              isSpy = true;
              int lineNumber = -1;
              if(fieldAnnotation.getRange().isPresent()){
                lineNumber = fieldAnnotation.getRange().get().begin.line;
              }
              if(hasMockitoAnnotationSpySpecificImports || hasMockitoAnnotationSpecificImports) {
                mockInfo = new MockInfo(testFile, lineNumber, AnalysisUtils.MOCKITO_SPY_ANNOTATION, AnalysisUtils.MOCKITO_FRAMEWORK);
              }
              else{
                //System.out.println("CHECK MISSING IMPORT:"+testFile+"#"+lineNumber);
                //System.exit(1);
                isSpy = false;
              }
            }
          }
          if(isMock &&  isSpy){
            System.out.println("Field:"+fieldDeclaration.toString()+" in "+testFile+" is both Mock and Spy");
            System.exit(1);
          }
          else if (isMock || isSpy){
            result.add(mockInfo);
          }
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static String getMockFramework(String fileName, String lineNumber, String call, Map<String, String> kotlinFileKtLintResultMap){
    String framework = "";
    boolean isMockitoClassCall = isCall(fileName, lineNumber, AnalysisUtils.KOTLIN_MOCKITO_CALL_ON_CLASS, kotlinFileKtLintResultMap);
    boolean isPowerMockitoClassCall = isCall(fileName, lineNumber, AnalysisUtils.KOTLIN_POWERMOCKITO_CALL_ON_CLASS, kotlinFileKtLintResultMap);
    boolean isMockitoKotlinNoArgumentsCall = isCall(fileName, lineNumber, AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_NO_ARGUMENTS_CALL, kotlinFileKtLintResultMap);
    boolean isMockitoKotlinTypeCall = isCall(fileName, lineNumber, AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_TYPE_CALL, kotlinFileKtLintResultMap);
    //mockito
    Set<String> mockitoMockImport = new HashSet<String>();
    mockitoMockImport.add(AnalysisUtils.KOTLIN_MOCKITO_MOCK_SPECIFIC_DEPENDENCY);
    boolean isMockitoMockSpecific = hasImportsInKotlinFiles(fileName, mockitoMockImport, kotlinFileKtLintResultMap);
    Set<String> mockitoSpyImport = new HashSet<String>();
    mockitoSpyImport.add(AnalysisUtils.KOTLIN_MOCKITO_SPY_SPECIFIC_DEPENDENCY);
    boolean isMockitoSpySpecific = hasImportsInKotlinFiles(fileName, mockitoSpyImport, kotlinFileKtLintResultMap);
    Set<String> mockitoMockStaticImport = new HashSet<String>();
    mockitoMockStaticImport.add(AnalysisUtils.KOTLIN_MOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY);
    boolean isMockitoMockStaticSpecific = hasImportsInKotlinFiles(fileName, mockitoMockStaticImport, kotlinFileKtLintResultMap);
    //mockito kotlin
    Set<String> mockitoKotlinMockImport = new HashSet<String>();
    mockitoKotlinMockImport.add(AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_MOCK_SPECIFIC_DEPENDENCY);
    boolean isMockitoKotlinMockSpecific = hasImportsInKotlinFiles(fileName, mockitoKotlinMockImport, kotlinFileKtLintResultMap);
    Set<String> mockitoKotlinSpyImport = new HashSet<String>();
    mockitoKotlinSpyImport.add(AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_SPY_SPECIFIC_DEPENDENCY);
    boolean isMockitoKotlinSpySpecific = hasImportsInKotlinFiles(fileName, mockitoKotlinSpyImport, kotlinFileKtLintResultMap);
    Set<String> mockitoKolinMockStaticImport = new HashSet<String>();
    mockitoKolinMockStaticImport.add(AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_MOCKSTATIC_SPECIFIC_DEPENDENCY);
    boolean isMockitoKotlinMockStaticSpecific = hasImportsInKotlinFiles(fileName, mockitoKolinMockStaticImport, kotlinFileKtLintResultMap);
    //power mockito
    Set<String> powerMockitoMockImport = new HashSet<String>();
    powerMockitoMockImport.add(AnalysisUtils.KOTLIN_POWERMOCKITO_MOCK_SPECIFIC_DEPENDENCY);
    boolean isPowerMockitoMockSpecific = hasImportsInKotlinFiles(fileName, powerMockitoMockImport, kotlinFileKtLintResultMap);
    Set<String> powerMockitoSpyImport = new HashSet<String>();
    powerMockitoSpyImport.add(AnalysisUtils.KOTLIN_POWERMOCKITO_SPY_SPECIFIC_DEPENDENCY);
    boolean isPowerMockitoSpySpecific = hasImportsInKotlinFiles(fileName, powerMockitoSpyImport, kotlinFileKtLintResultMap);
    Set<String> powerMockitoMockStaticImport = new HashSet<String>();
    powerMockitoMockStaticImport.add(AnalysisUtils.KOTLIN_POWERMOCKITO_MOCKSTATIC_SPECIFIC_DEPENDENCY);
    boolean isPowerMockitoMockStaticSpecific = hasImportsInKotlinFiles(fileName, powerMockitoMockStaticImport, kotlinFileKtLintResultMap);
    //classify
    if(call.equals(AnalysisUtils.MOCKITO_MOCK_API)){
      if(isMockitoClassCall){
        framework = MOCKITO_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinNoArgumentsCall && isMockitoKotlinMockSpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinTypeCall && isMockitoKotlinMockSpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
//      else if(isMockitoMockSpecific && !isMockitoKotlinMockSpecific && !isPowerMockitoMockSpecific){
//        framework = MOCKITO_FRAMEWORK;
//        return framework;
//      }
//      else if(isMockitoKotlinMockSpecific && !isMockitoMockSpecific && !isPowerMockitoMockSpecific){
//        framework = MOCKITO_KOTLIN_FRAMEWORK;
//        return framework;
//      }
      else if(isMockitoMockSpecific){
        framework = MOCKITO_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinMockSpecific && !isMockitoMockSpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
      else{
        if((isPowerMockitoMockSpecific && !isMockitoMockSpecific && !isMockitoKotlinMockSpecific) || isPowerMockitoClassCall) {
          framework = "";
          return framework;
        }
        else{
          //System.out.println("Cannot recognize#"+call+"#"+fileName+"#"+lineNumber);
          //System.exit(1);
        }
        return framework;
      }
    }
    else if(call.equals(AnalysisUtils.MOCKITO_SPY_API)){
      if(isMockitoClassCall){
        framework = MOCKITO_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinNoArgumentsCall && isMockitoKotlinSpySpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinTypeCall && isMockitoKotlinSpySpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
//      else if(isMockitoSpySpecific && !isMockitoKotlinSpySpecific && !isPowerMockitoSpySpecific){
//        framework = MOCKITO_FRAMEWORK;
//        return framework;
//      }
//      else if(isMockitoKotlinSpySpecific && !isMockitoSpySpecific && !isPowerMockitoSpySpecific){
//        framework = MOCKITO_KOTLIN_FRAMEWORK;
//        return framework;
//      }
      else if(isMockitoSpySpecific){
        framework = MOCKITO_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinSpySpecific && !isMockitoSpySpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
      else{
        if((isPowerMockitoSpySpecific && !isMockitoSpySpecific && !isMockitoKotlinSpySpecific) || isPowerMockitoClassCall) {
          framework = "";
          return framework;
        }
        else{
          //System.out.println("Cannot recognize#"+call+"#"+fileName+"#"+lineNumber);
          //System.exit(1);
        }
        return framework;
      }
    }
    else if(call.equals(AnalysisUtils.MOCKITO_MOCKSTATIC_API)){
      if(isMockitoClassCall){
        framework = MOCKITO_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinNoArgumentsCall && isMockitoKotlinMockStaticSpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinTypeCall && isMockitoKotlinMockStaticSpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
//      else if(isMockitoMockStaticSpecific && !isMockitoKotlinMockStaticSpecific && !isPowerMockitoMockStaticSpecific){
//        framework = MOCKITO_FRAMEWORK;
//        return framework;
//      }
//      else if(isMockitoKotlinMockStaticSpecific && !isMockitoMockStaticSpecific && !isPowerMockitoMockStaticSpecific){
//        framework = MOCKITO_KOTLIN_FRAMEWORK;
//        return framework;
//      }
      else if(isMockitoMockStaticSpecific){
        framework = MOCKITO_FRAMEWORK;
        return framework;
      }
      else if(isMockitoKotlinSpySpecific && !isMockitoMockStaticSpecific){
        framework = MOCKITO_KOTLIN_FRAMEWORK;
        return framework;
      }
      else{
        if((isPowerMockitoMockStaticSpecific && !isMockitoMockStaticSpecific && !isMockitoKotlinMockStaticSpecific) || isPowerMockitoClassCall) {
          framework = "";
          return framework;
        }
        else{
          //System.out.println("Cannot recognize#"+call+"#"+fileName+"#"+lineNumber);
          //System.exit(1);
        }
        return framework;
      }
    }
    //System.out.println("Strange call#"+call+"#"+fileName+"#"+lineNumber);
    //System.exit(1);
    return framework;
  }

  //find mockito mocks
  public static List<MockInfo> getMockitoMockInfoFromKotlinCode(Set<String> testFiles, Map<String, String> kotlinFileKtLintResultMap) {
    List<MockInfo> result = new ArrayList<MockInfo>();
    for (String fileName : testFiles) {
      //run klint on file and process output
      try {
        Set<String> mockitoAnnotationMockImports = new HashSet<String>();
        mockitoAnnotationMockImports.add(AnalysisUtils.KOTLIN_MOCKITO_ANNOTATION_MOCK_SPECIFIC_DEPENDENCY);
        boolean hasMockitoAnnotationMockImports = hasImportsInKotlinFiles(fileName, mockitoAnnotationMockImports, kotlinFileKtLintResultMap);
        Set<String> mockitoAnnotationSpyImports = new HashSet<String>();
        mockitoAnnotationSpyImports.add(AnalysisUtils.KOTLIN_MOCKITO_ANNOTATION_SPY_SPECIFIC_DEPENDENCY);
        boolean hasMockitoAnnotationSpyImports = hasImportsInKotlinFiles(fileName, mockitoAnnotationSpyImports, kotlinFileKtLintResultMap);
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(AnalysisUtils.MOCKITO_MOCK_RULE)){
            String lineParts[] = ktLine.split("#");
            String lineNumber = lineParts[2];
            String mockFramework = getMockFramework(fileName, lineNumber, MOCKITO_MOCK_API, kotlinFileKtLintResultMap);
            if(mockFramework.equals("")){
              continue;
            }
            MockInfo mockInfo =
                new MockInfo(fileName, Integer.parseInt(lineNumber), MOCKITO_MOCK_API, mockFramework);
            result.add(mockInfo);
          }
          else if(ktLine.contains(AnalysisUtils.MOCKITO_MOCKSTATIC_RULE)){
            String lineParts[] = ktLine.split("#");
            String lineNumber = lineParts[2];
            String mockFramework = getMockFramework(fileName, lineNumber, MOCKITO_MOCKSTATIC_API, kotlinFileKtLintResultMap);
            //mf: fixme
            if(mockFramework.equals("")){
              continue;
            }
            MockInfo mockInfo = new MockInfo(fileName, Integer.parseInt(lineNumber), MOCKITO_MOCKSTATIC_API, mockFramework);
            result.add(mockInfo);
          }
          else if(ktLine.contains(AnalysisUtils.MOCKITO_SPY_RULE)){
            String lineParts[] = ktLine.split("#");
            String lineNumber = lineParts[2];
            String mockFramework = getMockFramework(fileName, lineNumber, MOCKITO_SPY_API, kotlinFileKtLintResultMap);
            //mf: fixme
            if(mockFramework.equals("")){
              continue;
            }
            MockInfo mockInfo = new MockInfo(fileName, Integer.parseInt(lineNumber), MOCKITO_SPY_API, mockFramework);
            result.add(mockInfo);
          }
          else if(ktLine.contains(AnalysisUtils.MOCKITO_MOCK_ANNOTATION_RULE)){
            String lineParts[] = ktLine.split("#");
            String lineNumber = lineParts[2];
            String mockFramework = "";
            if (hasMockitoAnnotationMockImports) {
              mockFramework = AnalysisUtils.MOCKITO_FRAMEWORK;
            }
            else{
              //System.out.println("CHECK MISSING IMPORT:"+ktLine);
              //System.exit(1);
              continue;
            }
            MockInfo mockInfo = new MockInfo(fileName, Integer.parseInt(lineNumber), MOCKITO_MOCK_ANNOTATION, mockFramework);
            result.add(mockInfo);
          }
          else if(ktLine.contains(AnalysisUtils.MOCKITO_SPY_ANNOTATION_RULE)){
            String lineParts[] = ktLine.split("#");
            String lineNumber = lineParts[2];
            String mockFramework = "";
            if (hasMockitoAnnotationSpyImports) {
              mockFramework = AnalysisUtils.MOCKITO_FRAMEWORK;
            }
            else{
              //System.out.println("CHECK MISSING IMPORT:"+ktLine);
              //System.exit(1);
              continue;
            }
            MockInfo mockInfo = new MockInfo(fileName, Integer.parseInt(lineNumber), MOCKITO_SPY_ANNOTATION, mockFramework);
            result.add(mockInfo);
          }
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }

//  public static final String JAVA_POWERMOCK_DEPENDENCY = "org.powermock";
//  public static final String JAVA_DAGGER_DEPENDENCY = "dagger.";
//  public static final String JAVA_EASYMOCK_DEPENDENCY = "org.easymock";
//  public static final String JAVA_OKHTTP_1_DEPENDENCY = "com.squareup.okhttp";
//  public static final String JAVA_OKHTTP_2_DEPENDENCY = "okhttp3.mockwebserver";
//  public static final String JAVA_WIREMOCK_DEPENDENCY = "com.github.tomakehurst.wiremock";
//  public static final String JAVA_ROBOLECTRIC_DEPENDENCY = "org.robolectric";
//  public static final String JAVA_ANDROIDTEST_DEPENDENCY = "android.test.mock";
//  public static final String JAVA_MOCKK_DEPENDENCY = "io.mockk";
//  public static final String JAVA_SPRINGFRAMEWORK_DEPENDENCY = "org.springframework.mock.web";
//  public static final String JAVA_RETROFIT_DEPENDENCY = "retrofit2.mock";
//  public static final String JAVA_MOCKITOKOTLIN_1_DEPENDENCY = "com.nhaarman.mockito_kotlin";
//  public static final String JAVA_MOCKITOKOTLIN_2_DEPENDENCY = "com.nhaarman.mockitokotlin2";
//  public static final String JAVA_MOCKSERVER_DEPENDENCY = "org.mockserver";

  //check for java mock class
  //1) file name contains the word mock but not mockito
  //2) file does not have mockito import
  //3) file name does not end with test or tests
  public static List<MockInfo> getMockClassesFromJavaCode(Set<String> testFiles) {
    List<MockInfo> result = new ArrayList<MockInfo>();
    for (String fileName : testFiles) {
      boolean containsMockingFrameworkImport = false;
      boolean containsTestDoubleWord = false;
      boolean endsWithTestWordOrTestsWord = false;
      try {
        //check imports
        CompilationUnit cu = StaticJavaParser.parse(new File(fileName));
        List<ImportDeclaration> importDeclarations = cu.findAll(ImportDeclaration.class);
        for (ImportDeclaration importDeclaration : importDeclarations) {
          if (importDeclaration.getName().asString().startsWith(JAVA_MOCKITO_DEPENDENCY)
          || importDeclaration.getName().asString().startsWith(JAVA_EASYMOCK_DEPENDENCY)
          || importDeclaration.getName().asString().startsWith(JAVA_POWERMOCK_DEPENDENCY)) {
            containsMockingFrameworkImport = true;
            break;
          }
        }
        List<ClassOrInterfaceDeclaration> classDeclarations = cu.findAll(ClassOrInterfaceDeclaration.class);
        for(ClassOrInterfaceDeclaration cd:classDeclarations){
          String className = cd.getNameAsString();
          String classNameLower = className.toLowerCase();
          if(classNameLower.contains(DUMMY_WORD) && !classNameLower.contains(MOCKITO_WORD)){
            containsTestDoubleWord = true;
          }
          else if(classNameLower.contains(STUB_WORD) && !classNameLower.contains(MOCKITO_WORD)){
            containsTestDoubleWord = true;
          }
          else if(classNameLower.contains(MOCK_WORD) && !classNameLower.contains(MOCKITO_WORD)){
            containsTestDoubleWord = true;
          }
          else if(classNameLower.contains(SPY_WORD) && !classNameLower.contains(MOCKITO_WORD)){
            containsTestDoubleWord = true;
          }
          else if(classNameLower.contains(FAKE_WORD) && !classNameLower.contains(MOCKITO_WORD)){
            containsTestDoubleWord = true;
          }
          if(classNameLower.endsWith(TEST_WORD) || classNameLower.endsWith(TESTS_WORD)){
            endsWithTestWordOrTestsWord = true;
          }
        }
        if(!containsMockingFrameworkImport && containsTestDoubleWord && !endsWithTestWordOrTestsWord){
          MockInfo mockInfo = new MockInfo(fileName, -1, "", "");
          result.add(mockInfo);
        }
      } catch (ParseProblemException ppe) {
        //nothing we can do about this
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  //check this is ok
  //check for ktolin mock class
  //1) file name contains the word mock but not mockito
  //2) file does not have mockito or mockito kotlin import
  //3) file name does not end with test or tests
  public static List<MockInfo> getMockClassesFromKotlinCode(Set<String> testFiles, Map<String, String> kotlinFileKtLintResultMap) {
    List<MockInfo> result = new ArrayList<MockInfo>();
    for (String fileName : testFiles) {
      boolean containsMockitoOrMockitoKotlinImport = false;
      boolean containsTestDoubleWord = false;
      boolean endsWithTestWordOrTestsWord = false;

      try {
        //check content against ktlin on file and process output
        String mapContent = kotlinFileKtLintResultMap.get(fileName);
        String[] ktLines = mapContent.split("\n");
        for(String ktLine : ktLines){
          if(ktLine.contains(KOTLIN_MOCKITO_DEPENDENCY)
              || ktLine.contains(KOTLIN_EASYMOCK_DEPENDENCY)
              || ktLine.contains(KOTLIN_POWERMOCKITO_DEPENDENCY)
              || ktLine.contains(KOTLIN_MOCKK_DEPENDENCY)
              || ktLine.contains(KOTLIN_MOCKITO_KOTLIN_DEPENDENCY)){
            containsMockitoOrMockitoKotlinImport = true;
            break;
          }
          if(ktLine.contains(CLASS_TEST_DOUBLE_RULE)){
            containsTestDoubleWord = true;
          }
          if(ktLine.contains(CLASS_TEST_RULE)){
            endsWithTestWordOrTestsWord = true;
            break;
          }
        }
        if(!containsMockitoOrMockitoKotlinImport && containsTestDoubleWord && !endsWithTestWordOrTestsWord){
          MockInfo mockInfo = new MockInfo(fileName, -1, "", "");
          result.add(mockInfo);
        }
      }
      catch(Exception e){
        e.printStackTrace();
        System.exit(1);
      }
    }
    return result;
  }

}
