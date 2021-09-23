package atm.cs.analysis.data;

public class MockInfo {

  private String fileName;
  private int line;
  private String type;
  private String framework;

  public MockInfo(String fileName, int line, String type, String framework){
    this.fileName=fileName;
    this.line=line;
    this.type=type;
    this.framework=framework;
  }

  public String getFileName() {
    return fileName;
  }

  public int getLine() {
    return line;
  }

  public String getType() {
    return type;
  }

  public String getFramework() {
    return framework;
  }
}
