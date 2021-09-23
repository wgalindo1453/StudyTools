package atm.cs.analysis;

import java.net.URI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProjectUtils {

  public static String getRepoName(Element appElement){
    NodeList sourceList = appElement.getElementsByTagName("source");
    //get repo name
    String result = "";
    for(int i=0; i<sourceList.getLength(); ++i) {
      Node sourceNode = sourceList.item(i);
      if (sourceNode.getNodeType() == Node.ELEMENT_NODE) {
        Element sourceElement = (Element) sourceNode;
        try {
          URI sourceURI = URI.create(sourceElement.getTextContent());
          if (sourceURI.getHost().equals("github.com")) {
            String repoName = sourceURI.getPath().substring(1);
            if (repoName.endsWith("/")) {
              repoName = repoName.substring(0, repoName.length() - 1);
            }
            if (repoName.contains("/tree/")) {
              repoName = repoName.substring(0, repoName.indexOf("/tree/"));
            }
            result = "https://github.com/" + repoName + ".git";
            break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public static String getRepoAuthorAndName(Element appElement){
    NodeList sourceList = appElement.getElementsByTagName("source");
    //get repo name
    String result = "";
    for(int i=0; i<sourceList.getLength(); ++i) {
      Node sourceNode = sourceList.item(i);
      if (sourceNode.getNodeType() == Node.ELEMENT_NODE) {
        Element sourceElement = (Element) sourceNode;
        try {
          URI sourceURI = URI.create(sourceElement.getTextContent());
          if (sourceURI.getHost().equals("github.com")) {
            String repoName = sourceURI.getPath().substring(1);
            if (repoName.endsWith("/")) {
              repoName = repoName.substring(0, repoName.length() - 1);
            }
            if (repoName.contains("/tree/")) {
              repoName = repoName.substring(0, repoName.indexOf("/tree/"));
            }
            result = repoName;
            break;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
    }
    return result;
  }

  public static String getStastisticByName(Element appElement, String statisticName) {
    String result = "";
    NodeList repoNameList = appElement.getElementsByTagName(statisticName);
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement.getTextContent();
        break;
      }
    }
    return result;
  }

  public static int getJavaLoc(Element appElement) {
    int result = 0;
    NodeList repoNameList = appElement.getElementsByTagName("javaLoc");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = Integer.parseInt(repoNameElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static Element getJavaLocElement(Element appElement) {
    Element result = null;
    NodeList repoNameList = appElement.getElementsByTagName("javaLoc");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement;
        break;
      }
    }
    return result;
  }


  public static int getKotlinLoc(Element appElement) {
    int result = 0;
    NodeList repoNameList = appElement.getElementsByTagName("kotlinLoc");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = Integer.parseInt(repoNameElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static Element getKotlinLocElement(Element appElement) {
    Element result = null;
    NodeList repoNameList = appElement.getElementsByTagName("kotlinLoc");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement;
        break;
      }
    }
    return result;
  }

  public static int getXMLLoc(Element appElement) {
    int result = 0;
    NodeList repoNameList = appElement.getElementsByTagName("xmlLoc");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = Integer.parseInt(repoNameElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static Element getXMLLocElement(Element appElement) {
    Element result = null;
    NodeList repoNameList = appElement.getElementsByTagName("xmlLoc");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement;
        break;
      }
    }
    return result;
  }

  public static String getLocalRepoName(Element appElement) {
    String result = "";
    NodeList repoNameList = appElement.getElementsByTagName("repoName");
    for (int j = 0; j < repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement.getTextContent();
        break;
      }
    }
    return result;
  }

  public static int getStars(Element appElement){
    int result = 0;
    NodeList starsList = appElement.getElementsByTagName("stars");
    for(int j=0; j<starsList.getLength(); ++j) {
      Node starsNode = starsList.item(j);
      if (starsNode.getNodeType() == Node.ELEMENT_NODE) {
        Element starsElement = (Element) starsNode;
        result = Integer.parseInt(starsElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static int getGooglePlay(Element appElement){
    int result = 0;
    NodeList googlePlayList = appElement.getElementsByTagName("googlePlay");
    for(int j=0; j<googlePlayList.getLength(); ++j) {
      Node googlePlayNode = googlePlayList.item(j);
      if (googlePlayNode.getNodeType() == Node.ELEMENT_NODE) {
        Element googlePlayElement = (Element) googlePlayNode;
        result = Integer.parseInt(googlePlayElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static int getDayOfLastUpdate(Element appElement){
    int result = 0;
    NodeList dayOfLastUpdateList = appElement.getElementsByTagName("dayOfLastUpdate");
    for(int j=0; j<dayOfLastUpdateList.getLength(); ++j) {
      Node dayOfLastUpdateNode = dayOfLastUpdateList.item(j);
      if (dayOfLastUpdateNode.getNodeType() == Node.ELEMENT_NODE) {
        Element dayOfLastUpdateElement = (Element) dayOfLastUpdateNode;
        result = Integer.parseInt(dayOfLastUpdateElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static int getMonthOfLastUpdate(Element appElement){
    int result = 0;
    NodeList monthOfLastUpdateList = appElement.getElementsByTagName("monthOfLastUpdate");
    for(int j=0; j<monthOfLastUpdateList.getLength(); ++j) {
      Node monthOfLastUpdateNode = monthOfLastUpdateList.item(j);
      if (monthOfLastUpdateNode.getNodeType() == Node.ELEMENT_NODE) {
        Element monthOfLastUpdateElement = (Element) monthOfLastUpdateNode;
        result = Integer.parseInt(monthOfLastUpdateElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static int getYearOfLastUpdate(Element appElement){
    int result = 0;
    NodeList yearOfLastUpdateList = appElement.getElementsByTagName("yearOfLastUpdate");
    for(int j=0; j<yearOfLastUpdateList.getLength(); ++j) {
      Node yearOfLastUpdateNode = yearOfLastUpdateList.item(j);
      if (yearOfLastUpdateNode.getNodeType() == Node.ELEMENT_NODE) {
        Element yearOfLastUpdateElement = (Element) yearOfLastUpdateNode;
        result = Integer.parseInt(yearOfLastUpdateElement.getTextContent());
        break;
      }
    }
    return result;
  }

  public static int getRepoIsGradleStyle(Element appElement){
    int result = 0;
    NodeList projectStyleList = appElement.getElementsByTagName("repoIsGradleType");
    for(int j=0; j<projectStyleList.getLength(); ++j) {
      Node projectStyleNode = projectStyleList.item(j);
      if (projectStyleNode.getNodeType() == Node.ELEMENT_NODE) {
        Element projectStyleElement = (Element) projectStyleNode;
        if(projectStyleElement.getTextContent().equals("true")){
          result = 1;
        }
        break;
      }
    }
    return result;
  }

  public static String getAppSourceFolder(Element appElement){
    String result = "";
    NodeList appSourceFolderList = appElement.getElementsByTagName("appSourceFolder");
    for(int j=0; j<appSourceFolderList.getLength(); ++j) {
      Node appSourceFolderNode = appSourceFolderList.item(j);
      if (appSourceFolderNode.getNodeType() == Node.ELEMENT_NODE) {
        Element appSourceFolderElement = (Element) appSourceFolderNode;
        result = appSourceFolderElement.getTextContent();
        break;
      }
    }
    return result;
  }

  public static String getAppGradleBuildName(Element appElement){
    String result = "";
    NodeList repoNameList = appElement.getElementsByTagName("appBuildGradle");
    for(int j=0; j<repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement.getTextContent();
        break;
      }
    }
    return result;
  }

  public static void addNewNode(Document newDoc, Node appNodeCopy, String tag, String value){
    Element newElement = newDoc.createElement(tag);
    newElement.setTextContent(value);
    appNodeCopy.appendChild(newElement);
  }



  public static String getGradlewFileName(Element appElement){
    String result = "";
    NodeList repoNameList = appElement.getElementsByTagName("gradlewFileName");
    for(int j=0; j<repoNameList.getLength(); ++j) {
      Node repoNameNode = repoNameList.item(j);
      if (repoNameNode.getNodeType() == Node.ELEMENT_NODE) {
        Element repoNameElement = (Element) repoNameNode;
        result = repoNameElement.getTextContent();
        break;
      }
    }
    return result;
  }
}
