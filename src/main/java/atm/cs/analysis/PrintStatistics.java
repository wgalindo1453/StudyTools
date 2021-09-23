package atm.cs.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PrintStatistics {

  public void printStatistics(String configFile, String projectsFile) {
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
          Node appNodeCopy = newDoc.importNode(appElement, true);
          //get repository name
          String githubRepoName = ProjectUtils.getRepoName(appElement);
          System.out.print(githubRepoName);
          //local repo name
          String localRepoName = ProjectUtils.getStastisticByName(appElement, "repoName");
          System.out.print("\t" + localRepoName);
          //local analysis folder
          String appAnalysisFolder = ProjectUtils
              .getStastisticByName(appElement, "appAnalysisFolder");
          System.out.print("\t" + appAnalysisFolder);
          //commits
          int commitsNum1 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+1));
          System.out.print("\t" + commitsNum1);
          int commitsNum2 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+2));
          System.out.print("\t" + commitsNum2);
          int commitsNum3 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+3));
          System.out.print("\t" + commitsNum3);
          int commitsNum4 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+4));
          System.out.print("\t" + commitsNum4);
          int commitsNum5 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+5));
          System.out.print("\t" + commitsNum5);
          int commitsNum6 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+6));
          System.out.print("\t" + commitsNum6);
          int commitsNum7 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+7));
          System.out.print("\t" + commitsNum7);
          int commitsNum8 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+8));
          System.out.print("\t" + commitsNum8);
          int commitsNum9 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+9));
          System.out.print("\t" + commitsNum9);
          int commitsNum10 = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "numCommits"+10));
          System.out.print("\t" + commitsNum10);
          //stars
          int stars = ProjectUtils.getStars(appElement);
          System.out.print("\t" + stars);
          //loc
          int javaLoc = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "javaLoc"));
          System.out.print("\t" + javaLoc);
          int kotlinLoc = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "kotlinLoc"));
          System.out.print("\t" + kotlinLoc);
          int xmlLoc = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "xmlLoc"));
          System.out.print("\t" + xmlLoc);
          ///////////////TOTALS
          //tests
          int tests = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "tests"));
          System.out.print("\t" + tests);
          //jvm tests
          int jvmTests = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "jvmTests"));
          System.out.print("\t" + jvmTests);
          //device tests
          int deviceTests = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "deviceTests"));
          System.out.print("\t" + deviceTests);
          //unit tests
          int unitTests = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "unitTests"));
          System.out.print("\t" + unitTests);
          //integration tests
          int integrationTests = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "integrationTests"));
          System.out.print("\t" + integrationTests);
          //GUI tests
          int guiTests = Integer.parseInt(ProjectUtils.getStastisticByName(appElement, "guiTests"));
          System.out.print("\t" + guiTests);
          //robolectric tests
          int robolectricTests = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricTests"));
          System.out.print("\t" + robolectricTests);
          //mocks in tests
          int mockitoInTestFiles = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "mockitoInTestFiles"));
          System.out.print("\t" + mockitoInTestFiles);
          //mocks in app files
          int mockitoInAppFiles = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "mockitoInAppFiles"));
          System.out.print("\t" + mockitoInAppFiles);
          //robolectric shadows
          int robolectricShadows = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricShadows"));
          System.out.print("\t" + robolectricShadows);
          //robolectric shadow methods
          int robolectricShadowMethods = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricShadowMethods"));
          System.out.print("\t" + robolectricShadowMethods);
          //////////////////////////////////////INDIVIDUALS
          //tests
          int testsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "testsNumFromJavaCode"));
          System.out.print("\t" + testsNumFromJavaCode);
          int testsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "testsNumFromKotlinCode"));
          System.out.print("\t" + testsNumFromKotlinCode);
          //jvm tests
          int jvmTestsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "jvmTestsNumFromJavaCode"));
          System.out.print("\t" + jvmTestsNumFromJavaCode);
          int jvmTestsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "jvmTestsNumFromKotlinCode"));
          System.out.print("\t" + jvmTestsNumFromKotlinCode);
          //device tests
          int deviceTestsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "deviceTestsNumFromJavaCode"));
          System.out.print("\t" + deviceTestsNumFromJavaCode);
          int deviceTestsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "deviceTestsNumFromKotlinCode"));
          System.out.print("\t" + deviceTestsNumFromKotlinCode);
          //unit tests
          int unitTestsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "unitTestsNumFromJavaCode"));
          System.out.print("\t" + unitTestsNumFromJavaCode);
          int unitTestsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "unitTestsNumFromKotlinCode"));
          System.out.print("\t" + unitTestsNumFromKotlinCode);
          //integration tests
          int integrationTestsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "integrationTestsNumFromJavaCode"));
          System.out.print("\t" + integrationTestsNumFromJavaCode);
          int integrationTestsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "integrationTestsNumFromKotlinCode"));
          System.out.print("\t" + integrationTestsNumFromKotlinCode);
          //GUI tests
          int guiTestsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "guiTestsNumFromJavaCode"));
          System.out.print("\t" + guiTestsNumFromJavaCode);
          int guiTestsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "guiTestsNumFromKotlinCode"));
          System.out.print("\t" + guiTestsNumFromKotlinCode);
          //robolectric tests
          int robolectricTestsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricTestsNumFromJavaCode"));
          System.out.print("\t" + robolectricTestsNumFromJavaCode);
          int robolectricTestsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricTestsNumFromKotlinCode"));
          System.out.print("\t" + robolectricTestsNumFromKotlinCode);
          //mockito mocks in jvm tests
          int mockitoMocksNumInJvmTestsFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "mockitoMocksNumInJvmTestsFromJavaCode"));
          System.out.print("\t" + mockitoMocksNumInJvmTestsFromJavaCode);
          int mockitoMocksNumInJvmTestsFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "mockitoMocksNumInJvmTestsFromKotlinCode"));
          System.out.print("\t" + mockitoMocksNumInJvmTestsFromKotlinCode);
          //mockito mocks in device tests
          int mockitoMocksNumInDeviceTestsFromJavaCode = Integer.parseInt(ProjectUtils
              .getStastisticByName(appElement, "mockitoMocksNumInDeviceTestsFromJavaCode"));
          System.out.print("\t" + mockitoMocksNumInDeviceTestsFromJavaCode);
          int mockitoMocksNumInDeviceTestsFromKotlinCode = Integer.parseInt(ProjectUtils
              .getStastisticByName(appElement, "mockitoMocksNumInDeviceTestsFromKotlinCode"));
          System.out.print("\t" + mockitoMocksNumInDeviceTestsFromKotlinCode);
          //mockito mocks in kotlin app
          int mockitoMocksNumInAppFilesFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "mockitoMocksNumInAppFilesFromJavaCode"));
          System.out.print("\t" + mockitoMocksNumInAppFilesFromJavaCode);
          int mockitoMocksNumInAppFilesFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "mockitoMocksNumInAppFilesFromKotlinCode"));
          System.out.print("\t" + mockitoMocksNumInAppFilesFromKotlinCode);
          //robolectric shadows
          int robolectricShadowsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricShadowsNumFromJavaCode"));
          System.out.print("\t" + robolectricShadowsNumFromJavaCode);
          int robolectricShadowsNumFromKotlinCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricShadowsNumFromKotlinCode"));
          System.out.print("\t" + robolectricShadowsNumFromKotlinCode);
          //robolectric shadows
          int robolectricShadowMethodsNumFromJavaCode = Integer.parseInt(
              ProjectUtils.getStastisticByName(appElement, "robolectricShadowMethodsNumFromJavaCode"));
          System.out.print("\t" + robolectricShadowMethodsNumFromJavaCode);
          int robolectricShadowMethodsNumFromKotlinCode = Integer.parseInt(ProjectUtils
              .getStastisticByName(appElement, "robolectricShadowMethodsNumFromKotlinCode"));
          System.out.print("\t" + robolectricShadowMethodsNumFromKotlinCode);
          //add app to new file
          processedApps.add(appNodeCopy);
          System.out.println();
        }
      }
      System.out.println("processed apps:"+processedApps.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
