package atm.cs.analysis.data;

public class MockitoHint {
  public static final int UNUSED = 1;
  public static final int ARGS = 2;

  private int hintType = 0;
  private String location = "";

  public MockitoHint(int hintType, String location) {
    this.hintType = hintType;
    this.location = location;
  }

  public int getHintType() {
    return hintType;
  }

  public String getLocation() {
    return location;
  }
}
