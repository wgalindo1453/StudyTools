package atm.cs.analysis.data;

import java.time.LocalDateTime;

public class Commit {

  private String commitID;
  private LocalDateTime commitDate;

  public Commit(String commitID, LocalDateTime commitDate){
    this.commitID=commitID;
    this.commitDate=commitDate;
  }

  public String getCommitID() {
    return commitID;
  }

  public LocalDateTime getCommitDate() {
    return commitDate;
  }
}
