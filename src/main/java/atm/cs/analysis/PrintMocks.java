package atm.cs.analysis;

import atm.cs.analysis.data.MockInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class PrintMocks {

  public static Map<String, String> kotlinFileKtLintResultMap = new HashMap<String, String>();
  public static String[] MOCKITO_HEADERS = {"ID", "GitHub Link", "Source Type", "Test Type", "Local File", "Line", "API", "Framework", "Mocked Class Name", "Mocked Class Functionality", "Test Double Type"};
  public static String[] MOCK_CLASS_HEADERS = {"ID","GitHub Link", "Source Type", "Test Type", "Local File", "Class Name", "Class Functionality", "Test Double Type"};
  public static String[] SHADOWS_HEADERS = {"ID","GitHub Link", "Source Type", "Test Type", "Local File", "Class Name", "Class Functionality", "Test Double Type"};

  private String createGitHubLink(String githubRepoName, String repoName, String commitID, String fileName){
    String result = "";
    String githubRepoNamePart = githubRepoName.replace(".git", "");
    String fileNamePart = fileName.substring(fileName.lastIndexOf(repoName)+repoName.length()+1);
    result = githubRepoNamePart+"/blob/"+commitID+"/"+fileNamePart;
    return result;
  }

  public void printMocks(String configFile, String githubRepoName, String localRepoName, String appAnalysisFolderName, String commitID, String resultFolderName) {
    try {
      //load properties
      Properties prop = new Properties();
      prop.load(new FileInputStream(configFile));
      //java files
      Set<String> javaFiles = AnalysisUtils.getRelevantFiles(appAnalysisFolderName, AnalysisUtils.JAVA_EXTENSION);
      Set<String> javaJvmTestFiles = AnalysisUtils
          .getJvmTestFiles(appAnalysisFolderName, AnalysisUtils.JAVA_EXTENSION);
      Set<String> javaDeviceTestFiles = AnalysisUtils
          .getDeviceTestFiles(appAnalysisFolderName, AnalysisUtils.JAVA_EXTENSION);
      //kotlin files
      Set<String> kotlinFiles = AnalysisUtils.getRelevantFiles(appAnalysisFolderName, AnalysisUtils.KOTLIN_EXTENSION);
      Set<String> kotlinJvmTestFiles = AnalysisUtils
          .getJvmTestFiles(appAnalysisFolderName, AnalysisUtils.KOTLIN_EXTENSION);
      Set<String> kotlinDeviceTestFiles = AnalysisUtils
          .getDeviceTestFiles(appAnalysisFolderName, AnalysisUtils.KOTLIN_EXTENSION);

      //get info from ktlint on relevant files
      AnalysisUtils
          .runKtLintOnRelevantKtFiles(prop, appAnalysisFolderName, kotlinFileKtLintResultMap);

      //mockito mock info
      //get mockito mock info
      List<MockInfo> javaJvmMockitoMockInfo = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaJvmTestFiles);
      List<MockInfo> javaDeviceMockitoMockInfo = AnalysisUtils.getMockitoMockInfoFromJavaCode(javaDeviceTestFiles);
      List<MockInfo> kotlinJvmMockitoMockInfo = AnalysisUtils
          .getMockitoMockInfoFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap);
      List<MockInfo> kotlinDeviceMockitoMockInfo = AnalysisUtils
          .getMockitoMockInfoFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
      File resultFolder = new File(resultFolderName);
      if(!resultFolder.exists()){
        Files.createDirectory(Paths.get(resultFolderName));
      }
      //print mockito mock info
      FileWriter outMockitoMock = new FileWriter(resultFolder+File.separator+localRepoName+"_mockito_mocks.csv");
      CSVPrinter printerMockitoMock = new CSVPrinter(outMockitoMock, CSVFormat.DEFAULT.withHeader(MOCKITO_HEADERS));
      int mockitoMockID = 1;
      for(MockInfo mockInfo:javaJvmMockitoMockInfo) {
        //{"GitHub Link", "Source Type", "Workstation File", "Line", "API", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockitoMock.printRecord(mockitoMockID, gitHubLink, "Java", "jvm", mockInfo.getFileName(), mockInfo.getLine(), mockInfo.getType(), mockInfo.getFramework(), "", "", "");
        mockitoMockID++;
      }
      for(MockInfo mockInfo:javaDeviceMockitoMockInfo) {
        //{"GitHub Link", "Source Type", "Workstation File", "Line", "API", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockitoMock.printRecord(mockitoMockID, gitHubLink, "Java", "device", mockInfo.getFileName(), mockInfo.getLine(), mockInfo.getType(), mockInfo.getFramework(), "", "", "");
        mockitoMockID++;
      }
      for(MockInfo mockInfo:kotlinJvmMockitoMockInfo) {
        //{"GitHub Link", "Source Type", "Workstation File", "Line", "API", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockitoMock.printRecord(mockitoMockID, gitHubLink, "Kotlin", "jvm", mockInfo.getFileName(), mockInfo.getLine(), mockInfo.getType(), mockInfo.getFramework(), "", "", "");
        mockitoMockID++;
      }
      for(MockInfo mockInfo:kotlinDeviceMockitoMockInfo) {
        //{"GitHub Link", "Source Type", "Workstation File", "Line", "API", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockitoMock.printRecord(mockitoMockID, gitHubLink, "Kotlin", "device", mockInfo.getFileName(), mockInfo.getLine(), mockInfo.getType(), mockInfo.getFramework(), "", "", "");
        mockitoMockID++;
      }
      outMockitoMock.close();

      //mock classes info
      List<MockInfo> javaJvmMockClassesInfo = AnalysisUtils.getMockClassesFromJavaCode(javaJvmTestFiles);
      List<MockInfo> javaDeviceMockClassesInfo = AnalysisUtils.getMockClassesFromJavaCode(javaDeviceTestFiles);
      List<MockInfo> kotlinJvmMockClassesInfo = AnalysisUtils
          .getMockClassesFromKotlinCode(kotlinJvmTestFiles,kotlinFileKtLintResultMap);
      List<MockInfo> kotlinDeviceMockClassesInfo = AnalysisUtils
          .getMockClassesFromKotlinCode(kotlinDeviceTestFiles,kotlinFileKtLintResultMap);
      //print mock classes info
      FileWriter outMockClasses = new FileWriter(resultFolder+File.separator+localRepoName+"_mock_classes.csv");
      CSVPrinter printerMockClasses = new CSVPrinter(outMockClasses, CSVFormat.DEFAULT.withHeader(MOCK_CLASS_HEADERS));
      int mockClassesID = 1;
      for(MockInfo mockInfo:javaJvmMockClassesInfo) {
        //{"ID","GitHub Link", "Source Type", "Workstation File", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockClasses.printRecord(mockClassesID, gitHubLink, "Java", "jvm", mockInfo.getFileName(), "", "", "");
        mockClassesID++;
      }
      for(MockInfo mockInfo:javaDeviceMockClassesInfo) {
        //{"ID","GitHub Link", "Source Type", "Workstation File", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockClasses.printRecord(mockClassesID, gitHubLink, "Java", "device", mockInfo.getFileName(), "", "", "");
        mockClassesID++;
      }
      for(MockInfo mockInfo:kotlinJvmMockClassesInfo) {
        //{"ID","GitHub Link", "Source Type", "Workstation File", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockClasses.printRecord(mockClassesID, gitHubLink, "Kotlin", "jvm", mockInfo.getFileName(), "", "", "");
        mockClassesID++;
      }
      for(MockInfo mockInfo:kotlinDeviceMockClassesInfo) {
        //{"ID","GitHub Link", "Source Type", "Workstation File", "Test Double Type", "Label"};
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerMockClasses.printRecord(mockClassesID, gitHubLink, "Kotlin", "device", mockInfo.getFileName(), "", "", "");
        mockClassesID++;
      }
      outMockClasses.close();

      //robolectic shadows
      List<MockInfo> robolectricShadowsFromJavaJvmCode = AnalysisUtils.getRobolectricShadowsFromJavaCode(javaJvmTestFiles);
      List<MockInfo> robolectricShadowsFromJavaDeviceCode = AnalysisUtils.getRobolectricShadowsFromJavaCode(javaDeviceTestFiles);
      List<MockInfo>  robolectricShadowsFromKotlinJvmCode = AnalysisUtils
          .getRobolectricShadowsFromKotlinCode(kotlinJvmTestFiles, kotlinFileKtLintResultMap);
      List<MockInfo>  robolectricShadowsFromKotlinDeviceCode = AnalysisUtils
          .getRobolectricShadowsFromKotlinCode(kotlinDeviceTestFiles, kotlinFileKtLintResultMap);
      //print mock classes info
      FileWriter outShadows = new FileWriter(resultFolder+File.separator+localRepoName+"_shadows.csv");
      CSVPrinter printerShadows = new CSVPrinter(outShadows, CSVFormat.DEFAULT.withHeader(SHADOWS_HEADERS));
      int shadowsID = 1;
      for(MockInfo mockInfo:robolectricShadowsFromJavaJvmCode) {
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerShadows.printRecord(shadowsID, gitHubLink, "Java", "jvm", mockInfo.getFileName(), "", "", "");
        shadowsID++;
      }
      for(MockInfo mockInfo:robolectricShadowsFromJavaDeviceCode) {
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerShadows.printRecord(shadowsID, gitHubLink, "Java", "device", mockInfo.getFileName(), "", "", "");
        shadowsID++;
      }
      for(MockInfo mockInfo:robolectricShadowsFromKotlinJvmCode) {
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerShadows.printRecord(shadowsID, gitHubLink, "Kotlin", "jvm", mockInfo.getFileName(), "", "", "");
        shadowsID++;
      }
      for(MockInfo mockInfo:robolectricShadowsFromKotlinDeviceCode) {
        String gitHubLink = createGitHubLink(githubRepoName, localRepoName, commitID, mockInfo.getFileName());
        printerShadows.printRecord(shadowsID, gitHubLink, "Kotlin", "device", mockInfo.getFileName(), "", "", "");
        shadowsID++;
      }
      outShadows.close();

      System.out.println(localRepoName+"\t"+(javaJvmMockitoMockInfo.size()+kotlinJvmMockitoMockInfo.size())+"\t"+(javaDeviceMockitoMockInfo.size()+kotlinDeviceMockitoMockInfo.size())+"\t"+(javaJvmMockClassesInfo.size()+kotlinJvmMockClassesInfo.size())+"\t"+(javaDeviceMockClassesInfo.size()+kotlinDeviceMockClassesInfo.size())+"\t"+(robolectricShadowsFromJavaJvmCode.size()+robolectricShadowsFromKotlinJvmCode.size())+"\t"+(robolectricShadowsFromJavaDeviceCode.size()+robolectricShadowsFromKotlinDeviceCode.size()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
