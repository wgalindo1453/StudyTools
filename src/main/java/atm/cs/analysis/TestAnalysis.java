package atm.cs.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestAnalysis {

  public static Map<String, String> kotlinFileKtLintResultMap = new HashMap<String, String>();

  public void compute(String configFile, String projectsFile) {
    try {
      System.out.println("inside compute");
      //load properties
      Properties prop = new Properties();
      prop.load(new FileInputStream(configFile));
      //get repositories location
      String repositoriesFolderName = prop.getProperty("repo_dest_full_path");
      //process projects file
      File inputFile = new File(projectsFile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      //new doc
      Document newDoc = dBuilder.newDocument();
      //new apps list
      List<Node> processedApps = new ArrayList<Node>();
      //iterate over apps in doc
      NodeList appList = doc.getElementsByTagName("application");
      int count = 0;
      for (int i = 0; i < appList.getLength(); ++i) {
        Node appNode = appList.item(i);
        if (appNode.getNodeType() == Node.ELEMENT_NODE) {
          Element appElement = (Element) appNode;
          int onGooglePlay = ProjectUtils.getGooglePlay(appElement);
          //if(onGooglePlay==0){
           // continue;
          //}
//          count++;
//          if(count>50){
//            return;
//          }
          //make node copy
          Node appNodeCopy = newDoc.importNode(appElement, true);
          //get repository name
          String githubRepoName = ProjectUtils.getRepoName(appElement);
          System.out.println("githubRepoName"+githubRepoName);
          //local repo name
          String localRepoName = ProjectUtils.getLocalRepoName(appElement);
          System.out.println("localRepoName: "+localRepoName);
          //app source folder
          String appSourceFolder = ProjectUtils.getAppSourceFolder(appElement);
          System.out.println("appSourceFolder: "+appSourceFolder);
          //compute app analysis folder
          String localRepoFullPath = repositoriesFolderName + File.separator + localRepoName;
          System.out.println(localRepoFullPath);
          String appAnalysisFolder = localRepoFullPath;
          if (!appSourceFolder.equals("")) {
            appAnalysisFolder = appAnalysisFolder + File.separator + appSourceFolder;
          }
          System.out.println("appAnalysisFolder = "+"localRepoFullPath"+localRepoFullPath);
          if(!localRepoName.equals("repository-81")){
            System.out.println("not repo 80 skip");
            continue;

          }

          int commitsNum1 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 1);
          int commitsNum2 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 2);
          int commitsNum3 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 3);
          int commitsNum4 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 4);
          int commitsNum5 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 5);
          int commitsNum6 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 6);
          int commitsNum7 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 7);
          int commitsNum8 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 8);
          int commitsNum9 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 9);
          int commitsNum10 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 10);
          //get loc
          System.out.println("commitsnum1 :"+commitsNum1);
          System.out.println("commitsnum2 :"+commitsNum2);
          System.out.println("commitsnum3 :"+commitsNum3);
          System.out.println("commitsnum4 :"+commitsNum4);
          System.out.println("commitsnum5 :"+commitsNum5);
          System.out.println("commitsnum6 :"+commitsNum6);
          System.out.println("commitsnum7 :"+commitsNum7);
          System.out.println("commitsnum8 :"+commitsNum8);
          System.out.println("commitsnum9 :"+commitsNum9);
          System.out.println("commitsnum10 :"+commitsNum10);
          int javaLoc = AnalysisUtils.getJavaLoc(prop, appAnalysisFolder);
          int kotlinLoc = AnalysisUtils.getKotlinLoc(prop, appAnalysisFolder);
          int xmlLoc = AnalysisUtils.getXMLLoc(prop, appAnalysisFolder);
          //java files
          Set<String> javaFiles = AnalysisUtils.getRelevantFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
          //kotlin files
          Set<String> kotlinFiles = AnalysisUtils.getRelevantFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
          //get info from ktlint on relevant files
          AnalysisUtils
              .runKtLintOnRelevantKtFiles(prop, appAnalysisFolder, kotlinFileKtLintResultMap);
          //test files
          Set<String> javaTestFiles = AnalysisUtils.getTestFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
          Set<String> kotlinTestFiles = AnalysisUtils.getTestFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
          Set<String> testFiles = new HashSet<String>();
          testFiles.addAll(javaTestFiles);
          testFiles.addAll(kotlinTestFiles);
          System.out.println("testFiles both .kt and .java");
          int commitsOfTestsInLastThreeMonths = AnalysisUtils
              .commitsOfTestsInTheLastNMonths(prop, localRepoFullPath, testFiles, 3);
          System.out.println("commitsOfTestInTheLastNmonths :"+commitsOfTestsInLastThreeMonths);
          int commitsOfTestsInLastSixMonths = AnalysisUtils
              .commitsOfTestsInTheLastNMonths(prop, localRepoFullPath, testFiles, 6);
          System.out.println("commitsOfTestInTheLastNmonths :"+commitsOfTestsInLastSixMonths);
          int commitsOfTestsInLastYear1 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 1);
          System.out.println("commits past 1 year:"+commitsOfTestsInLastYear1);
          int commitsOfTestsInLastYear2 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 2);
          System.out.println("commits past 2 years"+commitsOfTestsInLastYear2);
          int commitsOfTestsInLastYear3 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 3);
          System.out.println("commits past 3 years:"+commitsOfTestsInLastYear3);
          int commitsOfTestsInLastYear4 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 4);
          System.out.println("commits past 4 years:"+commitsOfTestsInLastYear4);
          int commitsOfTestsInLastYear5 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 5);
          System.out.println("commits past 5 years:"+commitsOfTestsInLastYear5);
          int commitsOfTestsInLastYear6 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 6);
          System.out.println("commits past 6 years :"+commitsOfTestsInLastYear6);
          int commitsOfTestsInLastYear7 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 7);
          System.out.println("commits 7 years:"+commitsOfTestsInLastYear7);
          int commitsOfTestsInLastYear8 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 8);
          System.out.println("commits past 8 years :"+commitsOfTestsInLastYear8);
          int commitsOfTestsInLastYear9 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 9);
          System.out.println("commits past 9 years :"+commitsOfTestsInLastYear9);
          int commitsOfTestsInLastYear10 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 10);
          System.out.println("commits past 10 years :"+commitsOfTestsInLastYear10);


          //jvm test files
          Set<String> javaJvmTestFiles = AnalysisUtils
              .getJvmTestFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
          Set<String> kotlinJvmTestFiles = AnalysisUtils
              .getJvmTestFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
          //device test files
          Set<String> javaDeviceTestFiles = AnalysisUtils
              .getDeviceTestFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
          Set<String> kotlinDeviceTestFiles = AnalysisUtils
              .getDeviceTestFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
          //robolectric test runners
          Set<String> robolectricTestRunners = new HashSet<String>();
          Set<String> robolectricTestRunnersFromJavaCode = AnalysisUtils.getRobolectricTestRunnersFromJavaCode(javaJvmTestFiles);
          Set<String> robolectricTestRunnersFromKotlinCode = AnalysisUtils
              .getRobolectricTestRunnersFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap);
          robolectricTestRunners.addAll(robolectricTestRunnersFromJavaCode);
          robolectricTestRunners.addAll(robolectricTestRunnersFromKotlinCode);
          //tests
          int testsNumFromJavaCode = AnalysisUtils.getTestsNumFromJavaCode(javaTestFiles);
          int testsNumFromKotlinCode = AnalysisUtils
              .getTestsNumFromKotlinCode(kotlinTestFiles, kotlinFileKtLintResultMap);
          //jvm tests
          int jvmTestsNumFromJavaCode = AnalysisUtils.getJvmTestsNumFromJavaCode(javaJvmTestFiles);
          int jvmTestsNumFromKotlinCode = AnalysisUtils
              .getJvmTestsNumFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap);
          //device tests
          int deviceTestsNumFromJavaCode = AnalysisUtils.getDeviceTestsNumFromJavaCode(javaDeviceTestFiles);
          int deviceTestsNumFromKotlinCode = AnalysisUtils
              .getDeviceTestsNumFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //unit tests
          int unitTestsNumFromJavaCode = AnalysisUtils
              .getUnitTestsNumFromJavaCode(javaJvmTestFiles, javaDeviceTestFiles);
          int unitTestsNumFromKotlinCode = AnalysisUtils
              .getUnitTestsNumFromKotlinCode(kotlinJvmTestFiles, kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //integration tests
          int integrationTestsNumFromJavaCode = AnalysisUtils.getIntegrationTestsNumFromJavaCode(javaDeviceTestFiles);
          int integrationTestsNumFromKotlinCode = AnalysisUtils
              .getIntegrationTestsNumFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //GUI tests
          int guiTestsNumFromJavaCode = AnalysisUtils.getGUITestsNumFromJavaCode(javaDeviceTestFiles);
          int guiTestsNumFromKotlinCode = AnalysisUtils
              .getGUITestsNumFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //robolectric tests num
          int robolectricTestsNumFromJavaCode = AnalysisUtils
              .getRobolectricTestsNumFromJavaCode(javaJvmTestFiles, robolectricTestRunners);
          int robolectricTestsNumFromKotlinCode = AnalysisUtils
              .getRobolectricTestsNumFromKotlinCode(prop, kotlinJvmTestFiles, robolectricTestRunners);
          //mockito mocks in jvm tests
          int mockitoMocksNumInJvmTestsFromJavaCode = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaJvmTestFiles).size();
          int mockitoMocksNumInJvmTestsFromKotlinCode = AnalysisUtils
              .getMockitoMockInfoFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap).size();
          //mockito mocks in device tests
          int mockitoMocksNumInDeviceTestsFromJavaCode = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaDeviceTestFiles).size();
          int mockitoMocksNumInDeviceTestsFromKotlinCode = AnalysisUtils
              .getMockitoMockInfoFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap).size();
          //mockito mocks in files
          int mockitoMocksNumInAppFilesFromJavaCode = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaFiles).size();
          int mockitoMocksNumInAppFilesFromKotlinCode = AnalysisUtils
              .getMockitoMockInfoFromKotlinCode(kotlinFiles, kotlinFileKtLintResultMap).size();
          //mock classes in jvm tests
          int mockClassesNumInJvmTestsFromJavaCode = AnalysisUtils.getMockClassesFromJavaCode(javaJvmTestFiles).size();
          int mockClassesNumInJvmTestsFromKotlinCode = AnalysisUtils
              .getMockClassesFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap).size();
          //mock classes in device tests
          int mockClassesNumInDeviceTestsFromJavaCode = AnalysisUtils.getMockClassesFromJavaCode(javaDeviceTestFiles).size();
          int mockClassesNumInDeviceTestsFromKotlinCode = AnalysisUtils
              .getMockClassesFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap).size();
          //robolectric shadows
          int robolectricShadowsNumFromJavaCode = AnalysisUtils.getRobolectricShadowsFromJavaCode(javaJvmTestFiles).size();
          int robolectricShadowsNumFromKotlinCode = AnalysisUtils
              .getRobolectricShadowsFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap).size();
          //methods in robolectric shadows
          int robolectricShadowMethodsNumFromJavaCode = AnalysisUtils.getRobolectricShadowMethodsNumFromJavaCode(javaJvmTestFiles);
          int robolectricShadowMethodsNumFromKotlinCode = AnalysisUtils.getRobolectricShadowMethodsNumFromJavaCode(kotlinJvmTestFiles);
          //dependencies
          String buildFile = AnalysisUtils.findBuildFile(appAnalysisFolder);
          //compute import data
          //java
          Set<String> importNames = new HashSet<String>();
          importNames.add(AnalysisUtils.JAVA_MOCKITO_DEPENDENCY);
          int javaTestFilesWithMockitoImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_POWERMOCK_DEPENDENCY);
          int javaTestFilesWithPowermockImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_DAGGER_DEPENDENCY);
          int javaTestFilesWithDaggerImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_EASYMOCK_DEPENDENCY);
          int javaTestFilesWithEasymockImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_OKHTTP_1_DEPENDENCY);
          importNames.add(AnalysisUtils.JAVA_OKHTTP_2_DEPENDENCY);
          int javaTestFilesWithOkhttpImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_WIREMOCK_DEPENDENCY);
          int javaTestFilesWithWiremockImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_ROBOLECTRIC_DEPENDENCY);
          int javaTestFilesWithRobolectricImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_ANDROIDTEST_DEPENDENCY);
          int javaTestFilesWithAndroidtestImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_MOCKK_DEPENDENCY);
          int javaTestFilesWithMockkImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_SPRINGFRAMEWORK_DEPENDENCY);
          int javaTestFilesWithSpringframeworkImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_RETROFIT_DEPENDENCY);
          int javaTestFilesWithRetrofitImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_MOCKITOKOTLIN_1_DEPENDENCY);
          importNames.add(AnalysisUtils.JAVA_MOCKITOKOTLIN_2_DEPENDENCY);
          int javaTestFilesWithMockitokotlinImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_MOCKSERVER_DEPENDENCY);
          int javaTestFilesWithMockserverImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //kotlin
          int kotlinTestFilesWithMockitoImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKITO_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithPowermockImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_POWERMOCKITO_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithDaggerImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_DAGGER_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithEasymockImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_EASYMOCK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithOkhttpImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_OKHTTP_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithWiremockImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_WIREMOCK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithRobolectricImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_ROBOLECTRIC_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithAndroidtestImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_ANDROID_TEST_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithMockkImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithSpringframeworkImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_SPRINGFRAMEWORK_MOCK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithRetrofitImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_RETROFIT_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithMockitokotlinImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithMockserverImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKSERVER_DEPENDENCY, kotlinFileKtLintResultMap);

          //print results
          System.out.print(githubRepoName);
          System.out.print("\t" + localRepoName);
          System.out.print("\t" + appAnalysisFolder);
          //print and add info to xml
          System.out.print("\t"+buildFile);
          ProjectUtils.addNewNode(newDoc, appNodeCopy, "buildFile", buildFile);
          System.out.print("\t" + commitsOfTestsInLastThreeMonths);
          System.out.print("\t" + commitsOfTestsInLastSixMonths);
          System.out.print("\t" + commitsOfTestsInLastYear1);
          System.out.print("\t" + commitsOfTestsInLastYear2);
          System.out.print("\t" + commitsOfTestsInLastYear3);
          System.out.print("\t" + commitsOfTestsInLastYear4);
          System.out.print("\t" + commitsOfTestsInLastYear5);
          System.out.print("\t" + commitsOfTestsInLastYear6);
          System.out.print("\t" + commitsOfTestsInLastYear7);
          System.out.print("\t" + commitsOfTestsInLastYear8);
          System.out.print("\t" + commitsOfTestsInLastYear9);
          System.out.print("\t" + commitsOfTestsInLastYear10);
          System.out.print("\t" + commitsNum1);
          System.out.print("\t" + commitsNum2);
          System.out.print("\t" + commitsNum3);
          System.out.print("\t" + commitsNum4);
          System.out.print("\t" + commitsNum5);
          System.out.print("\t" + commitsNum6);
          System.out.print("\t" + commitsNum7);
          System.out.print("\t" + commitsNum8);
          System.out.print("\t" + commitsNum9);
          System.out.print("\t" + commitsNum10);
          System.out.print("\t" + javaLoc);
          System.out.print("\t" + kotlinLoc);
          System.out.print("\t" + xmlLoc);
          //////////////////////////////////////AGGREGATED
          //tests
          System.out.print("\t" + (testsNumFromJavaCode+testsNumFromKotlinCode));
          //jvm tests
          System.out.print("\t" + (jvmTestsNumFromJavaCode+jvmTestsNumFromKotlinCode));
          //device tests
          System.out.print("\t" + (deviceTestsNumFromJavaCode+deviceTestsNumFromKotlinCode));
          //unit tests
          System.out.print("\t" + (unitTestsNumFromJavaCode+unitTestsNumFromKotlinCode));
          //integration tests
          System.out.print("\t" + (integrationTestsNumFromJavaCode+integrationTestsNumFromKotlinCode));
          //GUI tests
          System.out.print("\t" + (guiTestsNumFromJavaCode+guiTestsNumFromKotlinCode));
          //robolectric tests
          System.out.print("\t" + (robolectricTestsNumFromJavaCode+robolectricTestsNumFromKotlinCode));
          //mocks in tests
          System.out.print("\t" + (mockitoMocksNumInJvmTestsFromJavaCode+mockitoMocksNumInDeviceTestsFromJavaCode+
              mockitoMocksNumInJvmTestsFromKotlinCode+mockitoMocksNumInDeviceTestsFromKotlinCode));
          //mock classes
          System.out.print("\t" + (mockClassesNumInJvmTestsFromJavaCode+mockClassesNumInDeviceTestsFromJavaCode+
              mockClassesNumInDeviceTestsFromKotlinCode+mockClassesNumInJvmTestsFromKotlinCode));
          //mocks in app files
          System.out.print("\t" + (mockitoMocksNumInAppFilesFromJavaCode+mockitoMocksNumInAppFilesFromKotlinCode));
          //robolectric shadows
          System.out.print("\t" + (robolectricShadowsNumFromJavaCode+robolectricShadowsNumFromKotlinCode));
          //robolectric shadow methods
          System.out.print("\t" + (robolectricShadowMethodsNumFromJavaCode+robolectricShadowMethodsNumFromKotlinCode));
          //test dependencies
          System.out.print("\t"+(javaTestFilesWithMockitoImport+kotlinTestFilesWithMockitoImport));
          System.out.print("\t"+(javaTestFilesWithPowermockImport+kotlinTestFilesWithPowermockImport));
          System.out.print("\t"+(javaTestFilesWithDaggerImport+kotlinTestFilesWithDaggerImport));
          System.out.print("\t"+(javaTestFilesWithEasymockImport+kotlinTestFilesWithEasymockImport));
          System.out.print("\t"+(javaTestFilesWithOkhttpImport+kotlinTestFilesWithOkhttpImport));
          System.out.print("\t"+(javaTestFilesWithWiremockImport+kotlinTestFilesWithWiremockImport));
          System.out.print("\t"+(javaTestFilesWithRobolectricImport+kotlinTestFilesWithRobolectricImport));
          System.out.print("\t"+(javaTestFilesWithAndroidtestImport+kotlinTestFilesWithAndroidtestImport));
          System.out.print("\t"+(javaTestFilesWithMockkImport+kotlinTestFilesWithMockkImport));
          System.out.print("\t"+(javaTestFilesWithSpringframeworkImport+kotlinTestFilesWithSpringframeworkImport));
          System.out.print("\t"+(javaTestFilesWithRetrofitImport+kotlinTestFilesWithRetrofitImport));
          System.out.print("\t"+(javaTestFilesWithMockitokotlinImport+kotlinTestFilesWithMockitokotlinImport));
          System.out.print("\t"+(javaTestFilesWithMockserverImport+kotlinTestFilesWithMockserverImport));
          ////////////////////////////////////INDIVIDUALS
          //tests
          System.out.print("\t" + testsNumFromJavaCode);
          System.out.print("\t" + testsNumFromKotlinCode);
          //jvm tests
          System.out.print("\t" + jvmTestsNumFromJavaCode);
          System.out.print("\t" + jvmTestsNumFromKotlinCode);
          //device tests
          System.out.print("\t" + deviceTestsNumFromJavaCode);
          System.out.print("\t" + deviceTestsNumFromKotlinCode);
          //unit tests
          System.out.print("\t" + unitTestsNumFromJavaCode);
          System.out.print("\t" + unitTestsNumFromKotlinCode);
          //integration tests
          System.out.print("\t" + integrationTestsNumFromJavaCode);
          System.out.print("\t" + integrationTestsNumFromKotlinCode);
          //GUI tests
          System.out.print("\t" + guiTestsNumFromJavaCode);
          System.out.print("\t" + guiTestsNumFromKotlinCode);
          //robolectric tests
          System.out.print("\t" + robolectricTestsNumFromJavaCode);
          System.out.print("\t" + robolectricTestsNumFromKotlinCode);
          //mockito mocks in jvm tests
          System.out.print("\t" + mockitoMocksNumInJvmTestsFromJavaCode);
          System.out.print("\t" + mockitoMocksNumInJvmTestsFromKotlinCode);
          //mockito mocks in device tests
          System.out.print("\t" + mockitoMocksNumInDeviceTestsFromJavaCode);
          System.out.print("\t" + mockitoMocksNumInDeviceTestsFromKotlinCode);
          //mock classes in jvm tests
          System.out.print("\t" + mockClassesNumInJvmTestsFromJavaCode);
          System.out.print("\t" + mockClassesNumInJvmTestsFromKotlinCode);
          //mock classes in device tests
          System.out.print("\t" + mockClassesNumInDeviceTestsFromJavaCode);
          System.out.print("\t" + mockClassesNumInDeviceTestsFromKotlinCode);
          //mockito mocks in kotlin app
          System.out.print("\t" + mockitoMocksNumInAppFilesFromJavaCode);
          System.out.print("\t" + mockitoMocksNumInAppFilesFromKotlinCode);
          //robolectric shadows
          System.out.print("\t" + robolectricShadowsNumFromJavaCode);
          System.out.print("\t" + robolectricShadowsNumFromKotlinCode);
          //robolectric shadows
          System.out.print("\t" + robolectricShadowMethodsNumFromJavaCode);
          System.out.print("\t" + robolectricShadowMethodsNumFromKotlinCode);
          ////////////////////////TERMINATE ITERATION
          System.out.println();
          processedApps.add(appNodeCopy);
        }
      }
      System.out.println("processed apps:"+processedApps.size());
      Element appsElement = newDoc.createElement("apps");
      for(Node app:processedApps){
        appsElement.appendChild(app);
      }
      newDoc.appendChild(appsElement);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(newDoc);
      StreamResult file = new StreamResult(new File("out.xml"));
      transformer.transform(source, file);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void computeCommits(String configFile, String projectsFile) {
    try {
      //load properties
      Properties prop = new Properties();
      prop.load(new FileInputStream(configFile));
      //get repositories location
      String repositoriesFolderName = prop.getProperty("repo_dest_full_path");
      //process projects file
      File inputFile = new File(projectsFile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      //new doc
      Document newDoc = dBuilder.newDocument();
      //new apps list
      List<Node> processedApps = new ArrayList<Node>();
      //iterate over apps in doc
      NodeList appList = doc.getElementsByTagName("application");
      for (int i = 0; i < appList.getLength(); ++i) {
        Node appNode = appList.item(i);
        if (appNode.getNodeType() == Node.ELEMENT_NODE) {
          Element appElement = (Element) appNode;
          int onGooglePlay = ProjectUtils.getGooglePlay(appElement);
          if(onGooglePlay==0){
            continue;
          }
          //local repo name
          String localRepoName = ProjectUtils.getLocalRepoName(appElement);
          //compute app analysis folder
          String localRepoFullPath = repositoriesFolderName + File.separator + localRepoName;
          String commitID = AnalysisUtils
              .getNewestCommitBeforeAnalysisStarted(prop, localRepoFullPath);
          System.out.println(localRepoFullPath+" "+commitID);
        }
      }
      System.out.println("processed apps:"+processedApps.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void computeForAse2020Dataset(String configFile, String reposFileName, String mappingsFileName, String fdroidDataFileName) {
    try {
      //load properties
      Properties prop = new Properties();
      prop.load(new FileInputStream(configFile));
      //get repositories location
      String repositoriesFolderName = prop.getProperty("repo_dest_full_path");
      File reposFile = new File(reposFileName);
      String reposFileContent = FileUtils.readFileToString(reposFile, "utf-8");
      JSONObject reposJSON = new JSONObject(reposFileContent);
      File mappingsFile = new File(mappingsFileName);
      String mappingsFileContent = FileUtils.readFileToString(mappingsFile, "utf-8");
      JSONObject mappingsJSON = new JSONObject(mappingsFileContent);
      File inputFile = new File(fdroidDataFileName);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      int aseDatasetCount = 0;
      for(String repoName:reposJSON.keySet()){
        JSONObject repoJSON = reposJSON.getJSONObject(repoName);
        String repoFolderName = "";
        for(String repoNameFromMapping:mappingsJSON.keySet()){
          String repoFolderNameFromMapping = mappingsJSON.getString(repoNameFromMapping);
          if(repoNameFromMapping.equals(repoName)){
            repoFolderName = repoFolderNameFromMapping;
            break;
          }
        }
        if(repoFolderName.equals("")){
          continue;
        }
//        if(!repoFolderName.equals("repository-10216")){
//          continue;
//        }
        //System.out.println("PROCESSING:"+repoName+"#"+repoFolderName);
        boolean isConsistentSourceFolder = true;
        //get source folder based on tests
        int numTest = repoJSON.getInt("num_tests");
        String testSuffix = "";
        if(numTest>0){
          Set<String> suffixSet = new HashSet<String>();
          JSONArray testsJSONArray = repoJSON.getJSONArray("tests");
          for(int i=0; i<testsJSONArray.length(); ++i){
            String testFileName = testsJSONArray.getString(i);
            int index = testFileName.indexOf(File.separator+"test"+File.separator);
            if(index==-1){
              isConsistentSourceFolder = false;
              break;
            }
            String suffix = testFileName.substring(0, index);
            suffixSet.add(suffix);
          }
          if(suffixSet.size()!=1){
            isConsistentSourceFolder = false;
          }
          else{
            for(String suffix:suffixSet){
              testSuffix=suffix;
            }
          }
        }
        //get source folder based on android tests
        int numAndroidTest = repoJSON.getInt("num_androidTests");
        String androidTestSuffix = "";
        if(numAndroidTest>0){
          Set<String> suffixSet = new HashSet<String>();
          JSONArray androidTestsJSONArray = repoJSON.getJSONArray("androidTests");
          for(int i=0; i<androidTestsJSONArray.length(); ++i){
            String androidTestFileName = androidTestsJSONArray.getString(i);
            int index = androidTestFileName.indexOf(File.separator+"androidTest"+File.separator);
            if(index==-1){
              isConsistentSourceFolder = false;
              break;
            }
            String suffix = androidTestFileName.substring(0, index);
            suffixSet.add(suffix);
          }
          if(suffixSet.size()!=1){
            isConsistentSourceFolder = false;
          }
          else{
            for(String suffix:suffixSet){
              androidTestSuffix=suffix;
            }
          }
        }
        String sourceLocalFolder = "";
        //fix test location inconsistency
        if(repoName.equals("ykayacan/Backpackers")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("ashomokdev/ENumbers_Android")){
          sourceLocalFolder = "ENumbers/app/src";
        }
        else if(repoName.equals("k3b/APhotoManager")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("softwarelikeyou/BudgetMobile")){
          sourceLocalFolder = "Android/app/src";
        }
        else if(repoName.equals("oluwajobaadegboye/cloudnative")){
          sourceLocalFolder = "reactnative/android/app/src";
        }
        else if(repoName.equals("YaowenGuo/filemanager")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("lexica/lexica")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("kevinwshin/pebbleauth")){
          sourceLocalFolder = "google_authenticator_source/AuthenticatorApp/src";
        }
        else if(repoName.equals("BioID-GmbH/BWS-Android")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("nikitautiu/ubb")){
          sourceLocalFolder = "PDM/reactapp/android/app/src";
        }
        else if(repoName.equals("SilenceIM/Silence")){
          sourceLocalFolder = "";
        }
        else if(repoName.equals("costrella/jhipster2")){
          sourceLocalFolder = "android_app1/Cechini/app/src";
        }
        else if(repoName.equals("jcs0062/go-bees-2")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("saied89/DVDPrism")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("sundxing/ptbook")){
          sourceLocalFolder = "";
        }
        else if(repoName.equals("translation-cards/translation-cards")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("dhruv7goyal/calcu")){
          sourceLocalFolder = "android-app/src";
        }
        else if(repoName.equals("tanrabad/survey")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("cchao1024/insomnia")){
          sourceLocalFolder = "android/app/src";
        }
        else if(repoName.equals("OpenSRP/opensrp-client-chw")){
          sourceLocalFolder = "opensrp-chw/src";
        }
        else if(repoName.equals("casific/murmur")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("asadmshah/moviegur")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("etp404/frenchverbinator")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("kishorependyala/DumbCharades")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("ericlee123/ssbl")){
          sourceLocalFolder = "ssbl-android/app/src";
        }
        else if(repoName.equals("guliash/Calculator")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("IamTechknow/Terraview")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("murdly/tagop")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("naive4E4A55/test2")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("Ifsttar/NoiseCapture")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("natanverdes/LigaBaloncesto")){
          sourceLocalFolder = "SpringConAndroidOAuthProject/AndroidProject/app/src";
        }
        else if(repoName.equals("Waboodoo/HTTP-Shortcuts")){
          sourceLocalFolder = "HTTPShortcuts/app/src";
        }
        else if(repoName.equals("OakOnEll/DnDCharacter")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("uozuAho/YakBox")){
          sourceLocalFolder = "app/src";
        }
        else {
          if (!isConsistentSourceFolder) {
            System.out.println("NOT CONSISTENT:" + repoName + "#" + repoFolderName);
            continue;
          }
          if (numTest > 0 && numAndroidTest < 1) {
            sourceLocalFolder = testSuffix;
          } else if (numTest < 1 && numAndroidTest > 0) {
            sourceLocalFolder = androidTestSuffix;
          } else {
            if (!testSuffix.equals(androidTestSuffix)) {
              System.out.println("DOUBLE CHECK FOLDER:" + repoName + "#" + repoFolderName);
              continue;
            }
            else{
              sourceLocalFolder = testSuffix;
            }
          }
        }
        //System.out.println("SOURCE FOLDER:"+repoName+"#"+repoFolderName+"#"+sourceLocalFolder);
        String manifestLocation = repoJSON.getString("manifest_path");
        //fix manifest location and test location mistmatch
        if(repoName.equals("victoraldir/DublinBusAlarm")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("UberPay/uberpay-wallet")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("vexelon-dot-net/currencybg.app")){
          sourceLocalFolder = "src";
        }
        else if(repoName.equals("jhellingman/cebuano-dictionary-app")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("TheLogicMaster/Tower-Defense-Galaxy")){
          sourceLocalFolder = "android";
        }
        else if(repoName.equals("u3shadow/DontDistraction")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("openwalletGH/openwallet-android")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("rafal-adamek/booksplayground")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("MiPushFramework/MiPushTester")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("leavebody/SportsPartner")){
          sourceLocalFolder = "code/Android/Sports Partner/app";
        }
        else if(repoName.equals("ddopik/AttendOnB")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("DanceDeets/dancedeets-android")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("MFazio23/cbb-bracket-randomizer")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("greenaddress/GreenBits")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("sakhayadeep/Chat-Hub")){
          sourceLocalFolder = "android/app/src";
        }
        else if(repoName.equals("Marcdnd/BRLCoins-android")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("hyosang82/GeoLocation")){
          sourceLocalFolder = "Android/app/src";
        }
        else if(repoName.equals("hayeb/SDE")){
          sourceLocalFolder = "propr-app/app/src";
        }
        else if(repoName.equals("hartalex/hartweather-android")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("qianjun2017/order")){
          sourceLocalFolder = "app/app/src";
        }
        else if(repoName.equals("alleyway/PiggyBanker")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("begumerkal/PROJEMM1")){
          sourceLocalFolder = "android";
        }
        else if(repoName.equals("LKRcoin/lkrcoin-android")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("TheMHMoritz3/Trainer-App")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("jguerinet/MyMartlet")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("Vel-San/Getoffyourphone")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("saulmm/Material-Movies")){
          sourceLocalFolder = "HackVG/app/src";
        }
        else if(repoName.equals("BruceGiese/PerfectPosture")){
          sourceLocalFolder = "PerfectPosture/app/src";
        }
        else if(repoName.equals("frostwire/frostwire")){
          sourceLocalFolder = "android";
        }
        else if(repoName.equals("domjos1994/SchoolTools")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("domjos1994/SchoolTools")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("joaomneto/TitanCompanion")){
          sourceLocalFolder = "src";
        }
        else if(!manifestLocation.startsWith(sourceLocalFolder)){
          System.out.println("DOUBLE CHECK FOLDER BECAUSE OF MANIFEST:"+repoName);
          continue;
        }
        String analysisFolderName = "";
        if(sourceLocalFolder.equals("")){
          analysisFolderName = repositoriesFolderName + File.separator  + repoFolderName;
        }
        else {
          analysisFolderName = repositoriesFolderName + File.separator  + repoFolderName + File.separator + sourceLocalFolder;
        }
        File analysisFolderFile = new File(analysisFolderName);
        //skipping repos that do not exsist anymore
        if(repoName.equals("bocward/WordSensei")){
          continue;
        }
        else if(repoName.equals("vase4kin/TeamCityApp")){
          continue;
        }
        else if(repoName.equals("alisonthemonster/Presently")){
          continue;
        }
        else if(repoName.equals("Nyariki/Ogotera")){
          continue;
        }
        else if(repoName.equals("marcelljee/hacker-news-android-app")){
          continue;
        }
        else if(repoName.equals("jamesturner/urn-android")){
          continue;
        }
        else if(repoName.equals("aangurets/RMFnews")){
          continue;
        }
        else if(repoName.equals("marcellocamara/PGM")){
          continue;
        }
        else if(repoName.equals("alesat1215/ProductsfromErokhin")){
          continue;
        }
        else if(repoName.equals("kroney/OfflineBrowser")){
          continue;
        }
        else if(repoName.equals("ngeor/android-tictactoe")){
          continue;
        }
        else if(repoName.equals("ErikKarlsson/CI-Evaluation")){
          continue;
        }
        else if(repoName.equals("butich/edu.tatar.ru_android_client")){
          continue;
        }
        else if(repoName.equals("fylmr/hotelsignature")){
          continue;
        }
        else if(repoName.equals("sagoot/MyDiary")){
          continue;
        }
        else if(repoName.equals("j-bruel/J.Epicture")){
          continue;
        }
        else if(repoName.equals("rafal-adamek/booksplayground")){
          continue;
        }
        else if(repoName.equals("CurrencyConverterCalculator/androidCCC")){
          continue;
        }
        else if(repoName.equals("jorgegil96/All-NBA")){
          continue;
        }
        else if(repoName.equals("AbnerSHO/plen-PLENConnect_Android")){
          continue;
        }
        else if(repoName.equals("humanqp/Android-Fastlane")){
          continue;
        }
        else if(repoName.equals("jossiwolf/schnarchbox_android")){
          continue;
        }
        else if(repoName.equals("ZoidbergZero/Canora")){
          continue;
        }
        else if(repoName.equals("DimitriKatsoulis/PopularMovies")){
          continue;
        }
        else if(repoName.equals("PhoenixMing0912/SAPSupportNow")){
          continue;
        }
        else if(repoName.equals("NOS-Cash/NOSwalletAndroid")){
          continue;
        }
        else if(repoName.equals("mytoysgroup/AgilePokerCardsAndroid")){
          continue;
        }
        else if(repoName.equals("bmbodj/Test2")){
          continue;
        }
        else if(repoName.equals("rdall96/Kitchen-Tools")){
          continue;
        }
        else if(repoName.equals("jamesturner/android-dimmer-switch")){
          continue;
        }
        else if(!(analysisFolderFile.exists() && analysisFolderFile.isDirectory())){
          System.out.println("DOUBLE CHECK FOLDER BECAUSE IT DOES NOT EXIST:"+repoName+"#"+analysisFolderName);
          continue;
        }
        //iterate over apps in doc
        boolean alreadyProcessed = false;
        NodeList appList = doc.getElementsByTagName("application");
        for (int i = 0; i < appList.getLength(); ++i) {
          Node appNode = appList.item(i);
          if (appNode.getNodeType() == Node.ELEMENT_NODE) {
            Element appElement = (Element) appNode;
            String appNodeReponame = ProjectUtils.getRepoAuthorAndName(appElement);
            if (appNodeReponame.equals(repoName)) {
              alreadyProcessed = true;
              break;
            }
          }
        }
        if(alreadyProcessed){
          //System.out.println(repoName);
          continue;
        }
        aseDatasetCount++;
        //process ase dataset app
        //System.out.println("test suffix:"+testSuffix);
        //System.out.println("android test suffix:"+androidTestSuffix);

          String githubRepoName = "https://github.com/"+repoName+".git";
          String market = repoJSON.getString("market");
          //local repo name
          String localRepoName = repoFolderName;
          //compute app folder
          String localRepoFullPath = repositoriesFolderName + File.separator + localRepoName;
          //compute app analysis folder
          String appAnalysisFolder = analysisFolderName;

          //System.out.println("source folder:"+sourceLocalFolder);
          //System.out.println("app analysis:"+appAnalysisFolder);

          int commitsNum1 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 1);
          int commitsNum2 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 2);
          int commitsNum3 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 3);
          int commitsNum4 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 4);
          int commitsNum5 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 5);
          int commitsNum6 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 6);
          int commitsNum7 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 7);
          int commitsNum8 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 8);
          int commitsNum9 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 9);
          int commitsNum10 = AnalysisUtils.getCommitsNumNYearsAgo(prop, localRepoFullPath, 10);
          //get loc
          int javaLoc = AnalysisUtils.getJavaLoc(prop, appAnalysisFolder);
          int kotlinLoc = AnalysisUtils.getKotlinLoc(prop, appAnalysisFolder);
          int xmlLoc = AnalysisUtils.getXMLLoc(prop, appAnalysisFolder);
          //java files
          Set<String> javaFiles = AnalysisUtils.getRelevantFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
//          System.out.println("Java Files");
//          for(String file:javaFiles){
//            System.out.println(file);
//          }
          //kotlin files
          Set<String> kotlinFiles = AnalysisUtils.getRelevantFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
//          System.out.println("Kotlin Files");
//          for(String file:kotlinFiles){
//            System.out.println(file);
//          }
          //get info from ktlint on relevant files
          AnalysisUtils
              .runKtLintOnRelevantKtFiles(prop, appAnalysisFolder, kotlinFileKtLintResultMap);
          //test files
          Set<String> javaTestFiles = AnalysisUtils.getTestFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
//          System.out.println("Java Test Files");
//          for(String file:javaTestFiles){
//            System.out.println(file);
//          }
          Set<String> kotlinTestFiles = AnalysisUtils.getTestFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
          Set<String> testFiles = new HashSet<String>();
          testFiles.addAll(javaTestFiles);
          testFiles.addAll(kotlinTestFiles);
          int commitsOfTestsInLastThreeMonths = AnalysisUtils
              .commitsOfTestsInTheLastNMonths(prop, localRepoFullPath, testFiles, 3);
          int commitsOfTestsInLastSixMonths = AnalysisUtils
              .commitsOfTestsInTheLastNMonths(prop, localRepoFullPath, testFiles, 6);
          int commitsOfTestsInLastYear1 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 1);
          int commitsOfTestsInLastYear2 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 2);
          int commitsOfTestsInLastYear3 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 3);
          int commitsOfTestsInLastYear4 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 4);
          int commitsOfTestsInLastYear5 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 5);
          int commitsOfTestsInLastYear6 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 6);
          int commitsOfTestsInLastYear7 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 7);
          int commitsOfTestsInLastYear8 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 8);
          int commitsOfTestsInLastYear9 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 9);
          int commitsOfTestsInLastYear10 = AnalysisUtils
              .commitsOfTestsInNthYearFromDatasetDate(prop, localRepoFullPath, testFiles, 10);
//          System.out.println("Kotlin Test Files");
//          for(String file:kotlinTestFiles){
//            System.out.println(file);
//          }
          //jvm test files
          Set<String> javaJvmTestFiles = AnalysisUtils
              .getJvmTestFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
//          System.out.println("Java JVM Test Files");
//          for(String file:javaJvmTestFiles){
//            System.out.println(file);
//          }
          Set<String> kotlinJvmTestFiles = AnalysisUtils
              .getJvmTestFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
//          System.out.println("Kotlin JVM Test Files");
//          for(String file:kotlinJvmTestFiles){
//            System.out.println(file);
//          }
          //device test files
          Set<String> javaDeviceTestFiles = AnalysisUtils
              .getDeviceTestFiles(appAnalysisFolder, AnalysisUtils.JAVA_EXTENSION);
//          System.out.println("Java Device Test Files");
//          for(String file:javaDeviceTestFiles){
//            System.out.println(file);
//          }
          Set<String> kotlinDeviceTestFiles = AnalysisUtils
              .getDeviceTestFiles(appAnalysisFolder, AnalysisUtils.KOTLIN_EXTENSION);
//          System.out.println("Kotlin Device Test Files");
//          for(String file:kotlinDeviceTestFiles){
//            System.out.println(file);
//          }
          //robolectric test runners
          Set<String> robolectricTestRunners = new HashSet<String>();
          Set<String> robolectricTestRunnersFromJavaCode = AnalysisUtils.getRobolectricTestRunnersFromJavaCode(javaJvmTestFiles);
          Set<String> robolectricTestRunnersFromKotlinCode = AnalysisUtils
              .getRobolectricTestRunnersFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap);
          robolectricTestRunners.addAll(robolectricTestRunnersFromJavaCode);
          robolectricTestRunners.addAll(robolectricTestRunnersFromKotlinCode);
          //tests
          int testsNumFromJavaCode = AnalysisUtils.getTestsNumFromJavaCode(javaTestFiles);
          int testsNumFromKotlinCode = AnalysisUtils
              .getTestsNumFromKotlinCode(kotlinTestFiles, kotlinFileKtLintResultMap);
          //jvm tests
          int jvmTestsNumFromJavaCode = AnalysisUtils.getJvmTestsNumFromJavaCode(javaJvmTestFiles);
          int jvmTestsNumFromKotlinCode = AnalysisUtils
              .getJvmTestsNumFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap);
          //device tests
          int deviceTestsNumFromJavaCode = AnalysisUtils.getDeviceTestsNumFromJavaCode(javaDeviceTestFiles);
          int deviceTestsNumFromKotlinCode = AnalysisUtils
              .getDeviceTestsNumFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //unit tests
          int unitTestsNumFromJavaCode = AnalysisUtils
              .getUnitTestsNumFromJavaCode(javaJvmTestFiles, javaDeviceTestFiles);
          int unitTestsNumFromKotlinCode = AnalysisUtils
              .getUnitTestsNumFromKotlinCode(kotlinJvmTestFiles, kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //integration tests
          int integrationTestsNumFromJavaCode = AnalysisUtils.getIntegrationTestsNumFromJavaCode(javaDeviceTestFiles);
          int integrationTestsNumFromKotlinCode = AnalysisUtils
              .getIntegrationTestsNumFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //GUI tests
          int guiTestsNumFromJavaCode = AnalysisUtils.getGUITestsNumFromJavaCode(javaDeviceTestFiles);
          int guiTestsNumFromKotlinCode = AnalysisUtils
              .getGUITestsNumFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
          //robolectric tests num
          int robolectricTestsNumFromJavaCode = AnalysisUtils
              .getRobolectricTestsNumFromJavaCode(javaJvmTestFiles, robolectricTestRunners);
          int robolectricTestsNumFromKotlinCode = AnalysisUtils
              .getRobolectricTestsNumFromKotlinCode(prop, kotlinJvmTestFiles, robolectricTestRunners);
          //mockito mocks in jvm tests
          int mockitoMocksNumInJvmTestsFromJavaCode = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaJvmTestFiles).size();
          int mockitoMocksNumInJvmTestsFromKotlinCode = AnalysisUtils
              .getMockitoMockInfoFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap).size();
          //mockito mocks in device tests
          int mockitoMocksNumInDeviceTestsFromJavaCode = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaDeviceTestFiles).size();
          int mockitoMocksNumInDeviceTestsFromKotlinCode = AnalysisUtils
              .getMockitoMockInfoFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap).size();
          //mockito mocks in files
          int mockitoMocksNumInAppFilesFromJavaCode = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaFiles).size();
          int mockitoMocksNumInAppFilesFromKotlinCode = AnalysisUtils
              .getMockitoMockInfoFromKotlinCode(kotlinFiles, kotlinFileKtLintResultMap).size();
          //mock classes in jvm tests
          int mockClassesNumInJvmTestsFromJavaCode = AnalysisUtils.getMockClassesFromJavaCode(javaJvmTestFiles).size();
          int mockClassesNumInJvmTestsFromKotlinCode = AnalysisUtils
              .getMockClassesFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap).size();
          //mock classes in device tests
          int mockClassesNumInDeviceTestsFromJavaCode = AnalysisUtils.getMockClassesFromJavaCode(javaDeviceTestFiles).size();
          int mockClassesNumInDeviceTestsFromKotlinCode = AnalysisUtils
              .getMockClassesFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap).size();
          //robolectric shadows
          int robolectricShadowsNumFromJavaCode = AnalysisUtils.getRobolectricShadowsFromJavaCode(javaJvmTestFiles).size();
          int robolectricShadowsNumFromKotlinCode = AnalysisUtils
              .getRobolectricShadowsFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap).size();
          //methods in robolectric shadows
          int robolectricShadowMethodsNumFromJavaCode = AnalysisUtils.getRobolectricShadowMethodsNumFromJavaCode(javaJvmTestFiles);
          int robolectricShadowMethodsNumFromKotlinCode = AnalysisUtils.getRobolectricShadowMethodsNumFromJavaCode(kotlinJvmTestFiles);
          //dependencies
          String buildFile = AnalysisUtils.findBuildFile(appAnalysisFolder);
          //compute import data
          //java
          Set<String> importNames = new HashSet<String>();
          importNames.add(AnalysisUtils.JAVA_MOCKITO_DEPENDENCY);
          int javaTestFilesWithMockitoImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_POWERMOCK_DEPENDENCY);
          int javaTestFilesWithPowermockImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_DAGGER_DEPENDENCY);
          int javaTestFilesWithDaggerImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_EASYMOCK_DEPENDENCY);
          int javaTestFilesWithEasymockImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_OKHTTP_1_DEPENDENCY);
          importNames.add(AnalysisUtils.JAVA_OKHTTP_2_DEPENDENCY);
          int javaTestFilesWithOkhttpImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_WIREMOCK_DEPENDENCY);
          int javaTestFilesWithWiremockImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_ROBOLECTRIC_DEPENDENCY);
          int javaTestFilesWithRobolectricImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_ANDROIDTEST_DEPENDENCY);
          int javaTestFilesWithAndroidtestImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_MOCKK_DEPENDENCY);
          int javaTestFilesWithMockkImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_SPRINGFRAMEWORK_DEPENDENCY);
          int javaTestFilesWithSpringframeworkImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_RETROFIT_DEPENDENCY);
          int javaTestFilesWithRetrofitImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_MOCKITOKOTLIN_1_DEPENDENCY);
          importNames.add(AnalysisUtils.JAVA_MOCKITOKOTLIN_2_DEPENDENCY);
          int javaTestFilesWithMockitokotlinImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //
          importNames.add(AnalysisUtils.JAVA_MOCKSERVER_DEPENDENCY);
          int javaTestFilesWithMockserverImport = AnalysisUtils
              .countImportsInJavaFiles(javaTestFiles, importNames);
          importNames.clear();
          //kotlin
          int kotlinTestFilesWithMockitoImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKITO_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithPowermockImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_POWERMOCKITO_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithDaggerImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_DAGGER_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithEasymockImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_EASYMOCK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithOkhttpImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_OKHTTP_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithWiremockImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_WIREMOCK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithRobolectricImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_ROBOLECTRIC_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithAndroidtestImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_ANDROID_TEST_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithMockkImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithSpringframeworkImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_SPRINGFRAMEWORK_MOCK_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithRetrofitImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_RETROFIT_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithMockitokotlinImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKITO_KOTLIN_DEPENDENCY, kotlinFileKtLintResultMap);
          int kotlinTestFilesWithMockserverImport = AnalysisUtils
              .countImportsInKotlinFiles(kotlinTestFiles, AnalysisUtils.KOTLIN_MOCKSERVER_DEPENDENCY, kotlinFileKtLintResultMap);

          //print results
          System.out.print(githubRepoName);
          System.out.print("\t" + market);
          System.out.print("\t" + localRepoName);
          System.out.print("\t" + appAnalysisFolder);
          //print and add info to xml
          System.out.print("\t"+buildFile);
          System.out.print("\t" + commitsOfTestsInLastThreeMonths);
          System.out.print("\t" + commitsOfTestsInLastSixMonths);
          System.out.print("\t" + commitsOfTestsInLastYear1);
          System.out.print("\t" + commitsOfTestsInLastYear2);
          System.out.print("\t" + commitsOfTestsInLastYear3);
          System.out.print("\t" + commitsOfTestsInLastYear4);
          System.out.print("\t" + commitsOfTestsInLastYear5);
          System.out.print("\t" + commitsOfTestsInLastYear6);
          System.out.print("\t" + commitsOfTestsInLastYear7);
          System.out.print("\t" + commitsOfTestsInLastYear8);
          System.out.print("\t" + commitsOfTestsInLastYear9);
          System.out.print("\t" + commitsOfTestsInLastYear10);
          System.out.print("\t" + commitsNum1);
          System.out.print("\t" + commitsNum2);
          System.out.print("\t" + commitsNum3);
          System.out.print("\t" + commitsNum4);
          System.out.print("\t" + commitsNum5);
          System.out.print("\t" + commitsNum6);
          System.out.print("\t" + commitsNum7);
          System.out.print("\t" + commitsNum8);
          System.out.print("\t" + commitsNum9);
          System.out.print("\t" + commitsNum10);
          System.out.print("\t" + javaLoc);
          System.out.print("\t" + kotlinLoc);
          System.out.print("\t" + xmlLoc);
          //////////////////////////////////////AGGREGATED
          //tests
          System.out.print("\t" + (testsNumFromJavaCode+testsNumFromKotlinCode));
          //jvm tests
          System.out.print("\t" + (jvmTestsNumFromJavaCode+jvmTestsNumFromKotlinCode));
          //device tests
          System.out.print("\t" + (deviceTestsNumFromJavaCode+deviceTestsNumFromKotlinCode));
          //unit tests
          System.out.print("\t" + (unitTestsNumFromJavaCode+unitTestsNumFromKotlinCode));
          //integration tests
          System.out.print("\t" + (integrationTestsNumFromJavaCode+integrationTestsNumFromKotlinCode));
          //GUI tests
          System.out.print("\t" + (guiTestsNumFromJavaCode+guiTestsNumFromKotlinCode));
          //robolectric tests
          System.out.print("\t" + (robolectricTestsNumFromJavaCode+robolectricTestsNumFromKotlinCode));
          //mocks in tests
          System.out.print("\t" + (mockitoMocksNumInJvmTestsFromJavaCode+mockitoMocksNumInDeviceTestsFromJavaCode+
              mockitoMocksNumInJvmTestsFromKotlinCode+mockitoMocksNumInDeviceTestsFromKotlinCode));
          //mock classes
          System.out.print("\t" + (mockClassesNumInJvmTestsFromJavaCode+mockClassesNumInJvmTestsFromKotlinCode+
              mockClassesNumInDeviceTestsFromJavaCode+mockClassesNumInDeviceTestsFromKotlinCode));
          //mocks in app files
          System.out.print("\t" + (mockitoMocksNumInAppFilesFromJavaCode+mockitoMocksNumInAppFilesFromKotlinCode));
          //robolectric shadows
          System.out.print("\t" + (robolectricShadowsNumFromJavaCode+robolectricShadowsNumFromKotlinCode));
          //robolectric shadow methods
          System.out.print("\t" + (robolectricShadowMethodsNumFromJavaCode+robolectricShadowMethodsNumFromKotlinCode));
          //test dependencies
          System.out.print("\t"+(javaTestFilesWithMockitoImport+kotlinTestFilesWithMockitoImport));
          System.out.print("\t"+(javaTestFilesWithPowermockImport+kotlinTestFilesWithPowermockImport));
          System.out.print("\t"+(javaTestFilesWithDaggerImport+kotlinTestFilesWithDaggerImport));
          System.out.print("\t"+(javaTestFilesWithEasymockImport+kotlinTestFilesWithEasymockImport));
          System.out.print("\t"+(javaTestFilesWithOkhttpImport+kotlinTestFilesWithOkhttpImport));
          System.out.print("\t"+(javaTestFilesWithWiremockImport+kotlinTestFilesWithWiremockImport));
          System.out.print("\t"+(javaTestFilesWithRobolectricImport+kotlinTestFilesWithRobolectricImport));
          System.out.print("\t"+(javaTestFilesWithAndroidtestImport+kotlinTestFilesWithAndroidtestImport));
          System.out.print("\t"+(javaTestFilesWithMockkImport+kotlinTestFilesWithMockkImport));
          System.out.print("\t"+(javaTestFilesWithSpringframeworkImport+kotlinTestFilesWithSpringframeworkImport));
          System.out.print("\t"+(javaTestFilesWithRetrofitImport+kotlinTestFilesWithRetrofitImport));
          System.out.print("\t"+(javaTestFilesWithMockitokotlinImport+kotlinTestFilesWithMockitokotlinImport));
          System.out.print("\t"+(javaTestFilesWithMockserverImport+kotlinTestFilesWithMockserverImport));
          ////////////////////////////////////INDIVIDUALS
          //tests
          System.out.print("\t" + testsNumFromJavaCode);
          System.out.print("\t" + testsNumFromKotlinCode);
          //jvm tests
          System.out.print("\t" + jvmTestsNumFromJavaCode);
          System.out.print("\t" + jvmTestsNumFromKotlinCode);
          //device tests
          System.out.print("\t" + deviceTestsNumFromJavaCode);
          System.out.print("\t" + deviceTestsNumFromKotlinCode);
          //unit tests
          System.out.print("\t" + unitTestsNumFromJavaCode);
          System.out.print("\t" + unitTestsNumFromKotlinCode);
          //integration tests
          System.out.print("\t" + integrationTestsNumFromJavaCode);
          System.out.print("\t" + integrationTestsNumFromKotlinCode);
          //GUI tests
          System.out.print("\t" + guiTestsNumFromJavaCode);
          System.out.print("\t" + guiTestsNumFromKotlinCode);
          //robolectric tests
          System.out.print("\t" + robolectricTestsNumFromJavaCode);
          System.out.print("\t" + robolectricTestsNumFromKotlinCode);
          //mockito mocks in jvm tests
          System.out.print("\t" + mockitoMocksNumInJvmTestsFromJavaCode);
          System.out.print("\t" + mockitoMocksNumInJvmTestsFromKotlinCode);
          //mockito mocks in device tests
          System.out.print("\t" + mockitoMocksNumInDeviceTestsFromJavaCode);
          System.out.print("\t" + mockitoMocksNumInDeviceTestsFromKotlinCode);
          //mock classes in jvm tests
          System.out.print("\t" + mockClassesNumInJvmTestsFromJavaCode);
          System.out.print("\t" + mockClassesNumInJvmTestsFromKotlinCode);
          //mock classes in device tests
          System.out.print("\t" + mockClassesNumInDeviceTestsFromJavaCode);
          System.out.print("\t" + mockClassesNumInDeviceTestsFromKotlinCode);
          //mockito mocks in kotlin app
          System.out.print("\t" + mockitoMocksNumInAppFilesFromJavaCode);
          System.out.print("\t" + mockitoMocksNumInAppFilesFromKotlinCode);
          //robolectric shadows
          System.out.print("\t" + robolectricShadowsNumFromJavaCode);
          System.out.print("\t" + robolectricShadowsNumFromKotlinCode);
          //robolectric shadows
          System.out.print("\t" + robolectricShadowMethodsNumFromJavaCode);
          System.out.print("\t" + robolectricShadowMethodsNumFromKotlinCode);
          ////////////////////////TERMINATE ITERATION
          System.out.println();

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void computeCommitsForAse2020Dataset(String configFile, String reposFileName, String mappingsFileName, String fdroidDataFileName) {
    try {
      //load properties
      Properties prop = new Properties();
      prop.load(new FileInputStream(configFile));
      //get repositories location
      String repositoriesFolderName = prop.getProperty("repo_dest_full_path");
      File reposFile = new File(reposFileName);
      String reposFileContent = FileUtils.readFileToString(reposFile, "utf-8");
      JSONObject reposJSON = new JSONObject(reposFileContent);
      File mappingsFile = new File(mappingsFileName);
      String mappingsFileContent = FileUtils.readFileToString(mappingsFile, "utf-8");
      JSONObject mappingsJSON = new JSONObject(mappingsFileContent);
      File inputFile = new File(fdroidDataFileName);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      int aseDatasetCount = 0;
      for(String repoName:reposJSON.keySet()){
        JSONObject repoJSON = reposJSON.getJSONObject(repoName);
        String repoFolderName = "";
        for(String repoNameFromMapping:mappingsJSON.keySet()){
          String repoFolderNameFromMapping = mappingsJSON.getString(repoNameFromMapping);
          if(repoNameFromMapping.equals(repoName)){
            repoFolderName = repoFolderNameFromMapping;
            break;
          }
        }
        if(repoFolderName.equals("")){
          continue;
        }
        //System.out.println("PROCESSING:"+repoName+"#"+repoFolderName);
        boolean isConsistentSourceFolder = true;
        //get source folder based on tests
        int numTest = repoJSON.getInt("num_tests");
        String testSuffix = "";
        if(numTest>0){
          Set<String> suffixSet = new HashSet<String>();
          JSONArray testsJSONArray = repoJSON.getJSONArray("tests");
          for(int i=0; i<testsJSONArray.length(); ++i){
            String testFileName = testsJSONArray.getString(i);
            int index = testFileName.indexOf(File.separator+"test"+File.separator);
            if(index==-1){
              isConsistentSourceFolder = false;
              break;
            }
            String suffix = testFileName.substring(0, index);
            suffixSet.add(suffix);
          }
          if(suffixSet.size()!=1){
            isConsistentSourceFolder = false;
          }
          else{
            for(String suffix:suffixSet){
              testSuffix=suffix;
            }
          }
        }
        //get source folder based on android tests
        int numAndroidTest = repoJSON.getInt("num_androidTests");
        String androidTestSuffix = "";
        if(numAndroidTest>0){
          Set<String> suffixSet = new HashSet<String>();
          JSONArray androidTestsJSONArray = repoJSON.getJSONArray("androidTests");
          for(int i=0; i<androidTestsJSONArray.length(); ++i){
            String androidTestFileName = androidTestsJSONArray.getString(i);
            int index = androidTestFileName.indexOf(File.separator+"androidTest"+File.separator);
            if(index==-1){
              isConsistentSourceFolder = false;
              break;
            }
            String suffix = androidTestFileName.substring(0, index);
            suffixSet.add(suffix);
          }
          if(suffixSet.size()!=1){
            isConsistentSourceFolder = false;
          }
          else{
            for(String suffix:suffixSet){
              androidTestSuffix=suffix;
            }
          }
        }
        String sourceLocalFolder = "";
        //fix test location inconsistency
        if(repoName.equals("ykayacan/Backpackers")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("ashomokdev/ENumbers_Android")){
          sourceLocalFolder = "ENumbers/app/src";
        }
        else if(repoName.equals("k3b/APhotoManager")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("softwarelikeyou/BudgetMobile")){
          sourceLocalFolder = "Android/app/src";
        }
        else if(repoName.equals("oluwajobaadegboye/cloudnative")){
          sourceLocalFolder = "reactnative/android/app/src";
        }
        else if(repoName.equals("YaowenGuo/filemanager")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("lexica/lexica")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("kevinwshin/pebbleauth")){
          sourceLocalFolder = "google_authenticator_source/AuthenticatorApp/src";
        }
        else if(repoName.equals("BioID-GmbH/BWS-Android")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("nikitautiu/ubb")){
          sourceLocalFolder = "PDM/reactapp/android/app/src";
        }
        else if(repoName.equals("SilenceIM/Silence")){
          sourceLocalFolder = "";
        }
        else if(repoName.equals("costrella/jhipster2")){
          sourceLocalFolder = "android_app1/Cechini/app/src";
        }
        else if(repoName.equals("jcs0062/go-bees-2")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("saied89/DVDPrism")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("sundxing/ptbook")){
          sourceLocalFolder = "";
        }
        else if(repoName.equals("translation-cards/translation-cards")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("dhruv7goyal/calcu")){
          sourceLocalFolder = "android-app/src";
        }
        else if(repoName.equals("tanrabad/survey")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("cchao1024/insomnia")){
          sourceLocalFolder = "android/app/src";
        }
        else if(repoName.equals("OpenSRP/opensrp-client-chw")){
          sourceLocalFolder = "opensrp-chw/src";
        }
        else if(repoName.equals("casific/murmur")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("asadmshah/moviegur")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("etp404/frenchverbinator")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("kishorependyala/DumbCharades")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("ericlee123/ssbl")){
          sourceLocalFolder = "ssbl-android/app/src";
        }
        else if(repoName.equals("guliash/Calculator")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("IamTechknow/Terraview")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("murdly/tagop")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("naive4E4A55/test2")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("Ifsttar/NoiseCapture")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("natanverdes/LigaBaloncesto")){
          sourceLocalFolder = "SpringConAndroidOAuthProject/AndroidProject/app/src";
        }
        else if(repoName.equals("Waboodoo/HTTP-Shortcuts")){
          sourceLocalFolder = "HTTPShortcuts/app/src";
        }
        else if(repoName.equals("OakOnEll/DnDCharacter")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("uozuAho/YakBox")){
          sourceLocalFolder = "app/src";
        }
        else {
          if (!isConsistentSourceFolder) {
            System.out.println("NOT CONSISTENT:" + repoName + "#" + repoFolderName);
            continue;
          }
          if (numTest > 0 && numAndroidTest < 1) {
            sourceLocalFolder = testSuffix;
          } else if (numTest < 1 && numAndroidTest > 0) {
            sourceLocalFolder = androidTestSuffix;
          } else {
            if (!testSuffix.equals(androidTestSuffix)) {
              System.out.println("DOUBLE CHECK FOLDER:" + repoName + "#" + repoFolderName);
              continue;
            }
          }
        }
        //System.out.println("SOURCE FOLDER:"+repoName+"#"+repoFolderName+"#"+sourceLocalFolder);
        String manifestLocation = repoJSON.getString("manifest_path");
        //fix manifest location and test location mistmatch
        if(repoName.equals("victoraldir/DublinBusAlarm")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("UberPay/uberpay-wallet")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("vexelon-dot-net/currencybg.app")){
          sourceLocalFolder = "src";
        }
        else if(repoName.equals("jhellingman/cebuano-dictionary-app")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("TheLogicMaster/Tower-Defense-Galaxy")){
          sourceLocalFolder = "android";
        }
        else if(repoName.equals("u3shadow/DontDistraction")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("openwalletGH/openwallet-android")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("rafal-adamek/booksplayground")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("MiPushFramework/MiPushTester")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("leavebody/SportsPartner")){
          sourceLocalFolder = "code/Android/Sports Partner/app";
        }
        else if(repoName.equals("ddopik/AttendOnB")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("DanceDeets/dancedeets-android")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("MFazio23/cbb-bracket-randomizer")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("greenaddress/GreenBits")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("sakhayadeep/Chat-Hub")){
          sourceLocalFolder = "android/app/src";
        }
        else if(repoName.equals("Marcdnd/BRLCoins-android")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("hyosang82/GeoLocation")){
          sourceLocalFolder = "Android/app/src";
        }
        else if(repoName.equals("hayeb/SDE")){
          sourceLocalFolder = "propr-app/app/src";
        }
        else if(repoName.equals("hartalex/hartweather-android")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("qianjun2017/order")){
          sourceLocalFolder = "app/app/src";
        }
        else if(repoName.equals("alleyway/PiggyBanker")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("begumerkal/PROJEMM1")){
          sourceLocalFolder = "android";
        }
        else if(repoName.equals("LKRcoin/lkrcoin-android")){
          sourceLocalFolder = "wallet/src";
        }
        else if(repoName.equals("TheMHMoritz3/Trainer-App")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("jguerinet/MyMartlet")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("Vel-San/Getoffyourphone")){
          sourceLocalFolder = "app/src";
        }
        else if(repoName.equals("saulmm/Material-Movies")){
          sourceLocalFolder = "HackVG/app/src";
        }
        else if(repoName.equals("BruceGiese/PerfectPosture")){
          sourceLocalFolder = "PerfectPosture/app/src";
        }
        else if(repoName.equals("frostwire/frostwire")){
          sourceLocalFolder = "android";
        }
        else if(!manifestLocation.startsWith(sourceLocalFolder)){
          System.out.println("DOUBLE CHECK FOLDER BECAUSE OF MANIFEST:"+repoName);
          continue;
        }
        String analysisFolderName = "";
        if(sourceLocalFolder.equals("")){
          analysisFolderName = repositoriesFolderName + File.separator  + repoFolderName;
        }
        else {
          analysisFolderName = repositoriesFolderName + File.separator  + repoFolderName + File.separator + sourceLocalFolder;
        }
        File analysisFolderFile = new File(analysisFolderName);
        //skipping repos that do not exsist anymore
        if(repoName.equals("bocward/WordSensei")){
          continue;
        }
        else if(repoName.equals("Nyariki/Ogotera")){
          continue;
        }
        else if(repoName.equals("marcelljee/hacker-news-android-app")){
          continue;
        }
        else if(repoName.equals("jamesturner/urn-android")){
          continue;
        }
        else if(repoName.equals("aangurets/RMFnews")){
          continue;
        }
        else if(repoName.equals("marcellocamara/PGM")){
          continue;
        }
        else if(repoName.equals("alesat1215/ProductsfromErokhin")){
          continue;
        }
        else if(repoName.equals("kroney/OfflineBrowser")){
          continue;
        }
        else if(repoName.equals("ngeor/android-tictactoe")){
          continue;
        }
        else if(repoName.equals("ErikKarlsson/CI-Evaluation")){
          continue;
        }
        else if(repoName.equals("butich/edu.tatar.ru_android_client")){
          continue;
        }
        else if(repoName.equals("fylmr/hotelsignature")){
          continue;
        }
        else if(repoName.equals("sagoot/MyDiary")){
          continue;
        }
        else if(repoName.equals("j-bruel/J.Epicture")){
          continue;
        }
        else if(repoName.equals("rafal-adamek/booksplayground")){
          continue;
        }
        else if(repoName.equals("CurrencyConverterCalculator/androidCCC")){
          continue;
        }
        else if(repoName.equals("jorgegil96/All-NBA")){
          continue;
        }
        else if(repoName.equals("AbnerSHO/plen-PLENConnect_Android")){
          continue;
        }
        else if(repoName.equals("humanqp/Android-Fastlane")){
          continue;
        }
        else if(repoName.equals("jossiwolf/schnarchbox_android")){
          continue;
        }
        else if(repoName.equals("ZoidbergZero/Canora")){
          continue;
        }
        else if(repoName.equals("DimitriKatsoulis/PopularMovies")){
          continue;
        }
        else if(repoName.equals("PhoenixMing0912/SAPSupportNow")){
          continue;
        }
        else if(repoName.equals("NOS-Cash/NOSwalletAndroid")){
          continue;
        }
        else if(repoName.equals("mytoysgroup/AgilePokerCardsAndroid")){
          continue;
        }
        else if(repoName.equals("bmbodj/Test2")){
          continue;
        }
        else if(repoName.equals("rdall96/Kitchen-Tools")){
          continue;
        }
        else if(repoName.equals("jamesturner/android-dimmer-switch")){
          continue;
        }
        else if(!(analysisFolderFile.exists() && analysisFolderFile.isDirectory())){
          System.out.println("DOUBLE CHECK FOLDER BECAUSE IT DOES NOT EXIST:"+repoName+"#"+analysisFolderName);
          continue;
        }
        //iterate over apps in doc
        boolean alreadyProcessed = false;
        NodeList appList = doc.getElementsByTagName("application");
        for (int i = 0; i < appList.getLength(); ++i) {
          Node appNode = appList.item(i);
          if (appNode.getNodeType() == Node.ELEMENT_NODE) {
            Element appElement = (Element) appNode;
            String appNodeReponame = ProjectUtils.getRepoAuthorAndName(appElement);
            if (appNodeReponame.equals(repoName)) {
              alreadyProcessed = true;
              break;
            }
          }
        }
        if(alreadyProcessed){
          //System.out.println("ALREADY PROCESSED:"+repoName+"#"+analysisFolderName);
          continue;
        }
        aseDatasetCount++;
        //process ase dataset app

        String githubRepoName = "https://github.com/"+repoName+".git";
        String market = repoJSON.getString("market");
        //local repo name
        String localRepoName = repoFolderName;
        //compute app folder
        String localRepoFullPath = repositoriesFolderName + File.separator + localRepoName;
        String commitID = AnalysisUtils.getNewestCommitBeforeAnalysisStarted(prop, localRepoFullPath);
        System.out.println(localRepoFullPath+" "+commitID);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
