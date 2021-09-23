package atm.cs.analysis.data;

public class InvocationInfo {
  public static final int INVOCATION_ON_REAL_METHOD = 1;
  public static final int INVOCATION_ON_EMPTY_METHOD = 2;
  public static final int INVOCATION_ON_STUBBED_METHOD = 3;
  public static final int RETURNED_EXCEPTION = 4;
  public static final int RETURNED_OBJECT = 5;

  private String identifier = "";
  private int invocationType = 0;
  private String invokedMethod = "";
  private String invocationMethodLocation = "";
  private String invocationFileLocation = "";
  private String stubbedMethod = "";
  private String stubbingLocation = "";
  private int returnedType = 0;

  public InvocationInfo(String identifier, int invocationType, String invokedMethod, String invocationMethodLocation, String invocationFileLocation, String stubbedMethod, String stubbingLocation, int returnedType) {
    this.identifier=identifier;
    this.invocationType=invocationType;
    this.invokedMethod=invokedMethod;
    this.invocationMethodLocation=invocationMethodLocation;
    this.invocationFileLocation=invocationFileLocation;
    this.stubbedMethod=stubbedMethod;
    this.stubbingLocation=stubbingLocation;
    this.returnedType=returnedType;
  }

  public String getIdentifier() {
    return identifier;
  }

  public int getInvocationType() {
    return invocationType;
  }

  public String getInvokedMethod() {
    return invokedMethod;
  }

  public String getInvocationMethodLocation() {
    return invocationMethodLocation;
  }

  public String getInvocationFileLocation() {
    return invocationFileLocation;
  }

  public String getStubbedMethod() {
    return stubbedMethod;
  }

  public String getStubbingLocation() {
    return stubbingLocation;
  }

  public int getReturnedType() {
    return returnedType;
  }
}
