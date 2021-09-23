package atm.cs.analysis;

import atm.cs.analysis.data.InvocationInfo;
import atm.cs.analysis.data.MockitoHint;
import atm.cs.analysis.data.VerificationInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtractInvocationInfo {

  private static final boolean DEBUG = false;

  public void extractInvocationsInfo(String cmockitoFile, String classesFile){
    try{
      Set<String> testClasses = new HashSet<String>();
      BufferedReader classesFileInput = new BufferedReader(new FileReader(classesFile));
      String classesFileLine = "";
      while ((classesFileLine = classesFileInput.readLine()) != null) {
        testClasses.add(classesFileLine);
      }
      classesFileInput.close();
      BufferedReader input = new BufferedReader(new FileReader(cmockitoFile));
      String fileLine = "";
      boolean isCollectingInvocationInfo = false;
      //invocation info
      boolean isMock = false;
      boolean isMockStatic = false;
      boolean isSpy = false;
      boolean isStubbed = false;
      String stubbedMethod = "";
      String stubbingLocation = "";
      String invokedMethod = "";
      int returnedType = 0;
      String hash = "";
      String methodLocation = "";
      String fileLocation = "";
      boolean doNotConsider = false;
      int lineCount = 0;
      List<InvocationInfo> invocationInfos = new ArrayList<InvocationInfo>();
      List<VerificationInfo> verificationInfos = new ArrayList<VerificationInfo>();
      while ((fileLine = input.readLine()) != null) {
        lineCount++;
        String line = fileLine;
        line = line.trim();
        if(isCollectingInvocationInfo){
          //get type
          if(line.startsWith("study_info#type#")){
            String lineArray[] = line.split("#");
            if(lineArray[2].equals("mock")){
              isMock = true;
            }
            else if(lineArray[2].equals("mockStatic")){
              isMockStatic = true;
            }
            else if(lineArray[2].equals("spy")){
              isSpy = true;
            }
          }
          else if(line.startsWith("study_info#stubbed#")){
            String lineArray[] = line.split("#");
            stubbedMethod = lineArray[2];
            isStubbed = true;
          }
          else if(line.startsWith("study_info#stubbing_location#")){
            String lineArray[] = line.split("#");
            stubbingLocation = lineArray[2];
          }
          else if(line.startsWith("study_info#return_type#")){
            String lineArray[] = line.split("#");
            if(lineArray[2].equals("exception")){
              returnedType = InvocationInfo.RETURNED_EXCEPTION;
            }
            else if(lineArray[2].equals("object")){
              returnedType = InvocationInfo.RETURNED_OBJECT;
            }
          }
          else if(line.startsWith("study_info#invoked_method_signature#")){
            String lineArray[] = line.split("#");
            invokedMethod = lineArray[2];
          }
          else if(line.startsWith("study_info#hash#")){
            String lineArray[] = line.split("#");
            hash = lineArray[2];
          }
          else if(line.startsWith("study_info#location#method#")){
            String lineArray[] = line.split("#");
            String methodInfo = lineArray[3];
            methodInfo = methodInfo.replace("-> at ", "");
            for(String testClass:testClasses){
              if(methodInfo.startsWith(testClass+".")){
                doNotConsider = true;
                break;
              }
            }
            methodLocation = methodInfo;
          }
          else if(line.startsWith("study_info#location#file#")){
            String lineArray[] = line.split("#");
            String fileInfo = lineArray[3];
            fileLocation = fileInfo;
          }
          else if(line.startsWith("end_study_info############")){
            if(doNotConsider){
              isCollectingInvocationInfo = false;
              isMock = false;
              isMockStatic = false;
              isSpy = false;
              isStubbed = false;
              stubbedMethod = "";
              stubbingLocation = "";
              invokedMethod = "";
              hash = "";
              methodLocation = "";
              fileLocation = "";
              doNotConsider = false;
              continue;
            }
            //create invocation info object
            if(isStubbed && !invokedMethod.equals(stubbedMethod)){
              System.out.println("not matching:"+invokedMethod+"#"+stubbedMethod);
              System.out.println(lineCount);
              System.exit(1);
            }
            if(!isMock && !isMockStatic && !isSpy){
              System.out.println("not type");
              System.out.println(lineCount);
              System.exit(1);
            }
            if(isMock || isMockStatic){
              if(isStubbed){
                invocationInfos.add(new InvocationInfo(hash, InvocationInfo.INVOCATION_ON_STUBBED_METHOD, invokedMethod, invokedMethod+"#"+methodLocation, fileLocation, stubbedMethod, stubbedMethod+"#"+stubbingLocation, returnedType));
              }
              else{
                invocationInfos.add(new InvocationInfo(hash, InvocationInfo.INVOCATION_ON_EMPTY_METHOD, invokedMethod, invokedMethod+"#"+methodLocation, fileLocation,"", "", returnedType));
              }
            }
            else if(isSpy){
              invocationInfos.add(new InvocationInfo(hash, InvocationInfo.INVOCATION_ON_REAL_METHOD, invokedMethod, invokedMethod+"#"+methodLocation, fileLocation,"", "", returnedType));
            }
            isCollectingInvocationInfo = false;
            isMock = false;
            isMockStatic = false;
            isSpy = false;
            isStubbed = false;
            stubbedMethod = "";
            stubbingLocation = "";
            invokedMethod = "";
            hash = "";
            methodLocation = "";
            fileLocation = "";
            doNotConsider = false;
          }
        }
        else {
          if (line.startsWith("start_study_info############")) {
            isCollectingInvocationInfo = true;
          }
        }
        if(line.startsWith("study_info_verification#verified#")){
          String lineArray[] = line.split("#");
          String verificationHash = lineArray[2];
          verificationInfos.add(new VerificationInfo(verificationHash));
        }
      }
      input.close();

      int invocationsOnRealMethodCount = 0;
      int invocationsOnEmptyMethodCount = 0;
      int invocationsOnStubbedMethodCount = 0;
      int returnedExceptionCount = 0;
      int returnedObjectCount = 0;
      Set<String> testDoubles = new HashSet<String>();
      Set<String> stubbedTestDoubles = new HashSet<String>();
      Set<String> realTestDoubles = new HashSet<String>();
      Set<String> emptyTestDoubles = new HashSet<String>();
      Set<String> uniquelyStubbedMethodsConsideringLocation = new HashSet<String>();
      Set<String> uniquelyStubbedMethods = new HashSet<String>();
      Set<String> consideredStubbingLocations = new HashSet<String>();
      Set<String> verifiedInstances = new HashSet<String>();
      Map<String,Integer> emptyInvokedMethodsCount = new HashMap<String, Integer>();
      Map<String,Integer> emptyInvokedLocationsCount = new HashMap<String, Integer>();
      Map<String,Integer> realInvokedMethodsCount = new HashMap<String, Integer>();
      Map<String,Integer> realInvokedLocationsCount = new HashMap<String, Integer>();
      Map<String,Integer> stubbedInvokedMethodsCount = new HashMap<String, Integer>();
      Map<String,Integer> stubbedInvokedLocationsCount = new HashMap<String, Integer>();
      Map<String,Integer> stubbingLocationsPerMethodCount = new HashMap<String, Integer>();
      for(InvocationInfo ii:invocationInfos){
        testDoubles.add(ii.getIdentifier());
        if(ii.getInvocationType()== InvocationInfo.INVOCATION_ON_REAL_METHOD){
          realTestDoubles.add(ii.getIdentifier());
          invocationsOnRealMethodCount++;
          if(realInvokedMethodsCount.containsKey(ii.getInvokedMethod())){
            Integer count = realInvokedMethodsCount.get(ii.getInvokedMethod());
            realInvokedMethodsCount.put(ii.getInvokedMethod(), Integer.valueOf(count.intValue()+1));
          }
          else{
            realInvokedMethodsCount.put(ii.getInvokedMethod(),Integer.valueOf(1));
          }
          if(realInvokedLocationsCount.containsKey(ii.getInvocationMethodLocation())){
            Integer count = realInvokedLocationsCount.get(ii.getInvocationMethodLocation());
            realInvokedLocationsCount.put(ii.getInvocationMethodLocation(), Integer.valueOf(count.intValue()+1));
          }
          else{
            realInvokedLocationsCount.put(ii.getInvocationMethodLocation(),Integer.valueOf(1));
          }
        }
        else if(ii.getInvocationType()== InvocationInfo.INVOCATION_ON_EMPTY_METHOD){
          emptyTestDoubles.add(ii.getIdentifier());
          invocationsOnEmptyMethodCount++;
          if(emptyInvokedMethodsCount.containsKey(ii.getInvokedMethod())){
            Integer count = emptyInvokedMethodsCount.get(ii.getInvokedMethod());
            emptyInvokedMethodsCount.put(ii.getInvokedMethod(), Integer.valueOf(count.intValue()+1));
          }
          else{
            emptyInvokedMethodsCount.put(ii.getInvokedMethod(),Integer.valueOf(1));
          }
          if(emptyInvokedLocationsCount.containsKey(ii.getInvocationMethodLocation())){
            Integer count = emptyInvokedLocationsCount.get(ii.getInvocationMethodLocation());
            emptyInvokedLocationsCount.put(ii.getInvocationMethodLocation(), Integer.valueOf(count.intValue()+1));
          }
          else{
            emptyInvokedLocationsCount.put(ii.getInvocationMethodLocation(),Integer.valueOf(1));
          }
        }
        else if(ii.getInvocationType()== InvocationInfo.INVOCATION_ON_STUBBED_METHOD){
          stubbedTestDoubles.add(ii.getIdentifier());
          String stubbingLocationArray[] = ii.getStubbingLocation().split("#");
          String stubbingLocationCleaned = stubbingLocationArray[1].replace("-> at ","");
          consideredStubbingLocations.add(stubbingLocationCleaned);
          //System.out.println(stubbingLocationCleaned);
          invocationsOnStubbedMethodCount++;
          if(stubbedInvokedMethodsCount.containsKey(ii.getInvokedMethod())){
            Integer count = stubbedInvokedMethodsCount.get(ii.getInvokedMethod());
            stubbedInvokedMethodsCount.put(ii.getInvokedMethod(), Integer.valueOf(count.intValue()+1));
          }
          else{
            stubbedInvokedMethodsCount.put(ii.getInvokedMethod(),Integer.valueOf(1));
          }
          if(stubbedInvokedLocationsCount.containsKey(ii.getInvocationMethodLocation())){
            Integer count = stubbedInvokedLocationsCount.get(ii.getInvocationMethodLocation());
            stubbedInvokedLocationsCount.put(ii.getInvocationMethodLocation(), Integer.valueOf(count.intValue()+1));
          }
          else{
            stubbedInvokedLocationsCount.put(ii.getInvocationMethodLocation(),Integer.valueOf(1));
          }
          String identifier = ii.getStubbedMethod();
          String identifierConsideringLocation = ii.getStubbingLocation()+"#"+ii.getStubbedMethod();
          if(!uniquelyStubbedMethodsConsideringLocation.contains(identifierConsideringLocation)){          ////
            if(stubbingLocationsPerMethodCount.containsKey(ii.getStubbedMethod())){
              Integer count = stubbingLocationsPerMethodCount.get(ii.getStubbedMethod());
              stubbingLocationsPerMethodCount.put(ii.getStubbedMethod(), Integer.valueOf(count.intValue()+1));
            }
            else{
              stubbingLocationsPerMethodCount.put(ii.getStubbedMethod(),Integer.valueOf(1));
            }
            ////

            if(ii.getReturnedType()== InvocationInfo.RETURNED_EXCEPTION){
              returnedExceptionCount++;
            }
            else if(ii.getReturnedType()== InvocationInfo.RETURNED_OBJECT){
              returnedObjectCount++;
            }
          }
          uniquelyStubbedMethodsConsideringLocation.add(identifierConsideringLocation);
          uniquelyStubbedMethods.add(identifier);
        }
      }
      for(VerificationInfo vi:verificationInfos){
        verifiedInstances.add(vi.getIdentifier());
      }
      testDoubles.addAll(verifiedInstances);

//      System.out.println("test doubles:"+testDoubles.size());
//      //System.out.println("invocations:"+invocationInfos.size());
//      System.out.println("empty method:"+invocationsOnEmptyMethodCount);
//      System.out.println("stubbed method:"+invocationsOnStubbedMethodCount);
//      System.out.println("real method:"+invocationsOnRealMethodCount);
//      System.out.println("unique stubbed method:"+uniquelyStubbedMethods.size());
//      System.out.println("unique stubbed method considering stubbing location:"+uniquelyStubbedMethodsConsideringLocation.size());
//      System.out.println("returned exception:"+returnedExceptionCount);
//      System.out.println("returned object:"+returnedObjectCount);
//      System.out.println("verifications:"+verificationInfos.size());
//      System.out.println("verified instances:"+verifiedInstances.size());
      //System.out.println(testDoubles.size()+"\t"+invocationInfos.size()+"\t"+invokedMethodsCount.keySet().size()+"\t"+invokedLocationsCount.keySet().size()+"\t"+invocationsOnEmptyMethodCount+"\t"+invocationsOnStubbedMethodCount
      //    +"\t"+invocationsOnRealMethodCount+"\t"+uniquelyStubbedMethods.size()+"\t"+uniquelyStubbedMethodsConsideringLocation.size()+"\t"+returnedExceptionCount+"\t"+returnedObjectCount+"\t"+verifiedInstances.size()+"\t"+verificationInfos.size());
      int stubbedAndVeridiedCount = 0;
      for(String stubbedTDO:stubbedTestDoubles){
        if(verifiedInstances.contains(stubbedTDO)){
          stubbedAndVeridiedCount++;
        }
      }
      Set<String> verifiedButNotStubbed = new HashSet<String>();
      for(String verifiedInstance:verifiedInstances){
        if(!stubbedTestDoubles.contains(verifiedInstance)){
          verifiedButNotStubbed.add(verifiedInstance);
        }
      }
      int verificationsOnNotStubbedCount = 0;
      for(VerificationInfo vi:verificationInfos){
        if(verifiedButNotStubbed.contains(vi.getIdentifier())){
          verificationsOnNotStubbedCount++;
        }
      }
//      System.out.println(
//          //testDoubles.size()+"\t"+
//          stubbedTestDoubles.size()+"\t"+invocationsOnStubbedMethodCount+"\t"+stubbedInvokedLocationsCount.keySet().size()+"\t"+stubbedInvokedMethodsCount.keySet().size()+
//          //"\t"+realTestDoubles.size()+"\t"+invocationsOnRealMethodCount+"\t"+realInvokedMethodsCount.keySet().size()+"\t"+realInvokedLocationsCount.keySet().size()+
//          //"\t"+emptyTestDoubles.size()+"\t"+invocationsOnEmptyMethodCount+"\t"+emptyInvokedMethodsCount.keySet().size()+"\t"+emptyInvokedLocationsCount.keySet().size()+
//          "\t"+uniquelyStubbedMethodsConsideringLocation.size()+"\t"+returnedObjectCount+"\t"+returnedExceptionCount+"\t"+verifiedInstances.size()+"\t"+verificationInfos.size()//+
//          //"\t"+stubbedAndVeridiedCount+"\t"+verificationsOnNotStubbedCount
//          );



      /////////////////////////////////////////
//      MapUtil mu = new MapUtil();
//      stubbedInvokedMethodsCount = mu.sortByValue(stubbedInvokedMethodsCount);
//      System.out.println("#########################");
//      System.out.println(stubbedInvokedMethodsCount.keySet().size());
//      System.out.println("-------------------------");
//      List<Integer> counts = new ArrayList<Integer>();
//      List<Double> values = new ArrayList<Double>();
//      int count = 1;
//      for(String key:stubbedInvokedMethodsCount.keySet()){
//        double percentage = ((double) stubbedInvokedMethodsCount.get(key))/((double) invocationsOnStubbedMethodCount);
//        counts.add(count);
//        values.add((percentage*100));
//        count++;
//      }
//      System.out.print("[");
//      for(Integer c:counts){
//        System.out.print(","+c.intValue());
//      }
//      System.out.println("]");
//      System.out.print("[");
//      for(Double v:values){
//        System.out.print(","+v.doubleValue());
//      }
//      System.out.println("]");
      //////////////////////////////////////////
      MapUtil mu = new MapUtil();
      stubbingLocationsPerMethodCount = mu.sortByValue(stubbingLocationsPerMethodCount);
      int greaterThanOneCount = 0;
      for(String key:stubbingLocationsPerMethodCount.keySet()){
        if(stubbingLocationsPerMethodCount.get(key)>1){
          greaterThanOneCount++;
        }
        //System.out.println(stubbingLocationsPerMethodCount.get(key));
      }
      System.out.println(greaterThanOneCount);

//      MapUtil mu1 = new MapUtil();
//      invokedLocationsCount = mu1.sortByValue(invokedLocationsCount);
//      System.out.println("#########################");
//      System.out.println(invokedLocationsCount.keySet().size());
//      System.out.println("-------------------------");
////      for(String key:invokedLocationsCount.keySet()){
////        System.out.println(invokedLocationsCount.get(key));
////        System.out.println(key);
////      }
    }
    catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void extractProblemsInfo(String cmockitoFile){
    try{
      BufferedReader input = new BufferedReader(new FileReader(cmockitoFile));
      String fileLine = "";
      int lineCount = 0;
      List<MockitoHint> mockitoHints = new ArrayList<MockitoHint>();
      while ((fileLine = input.readLine()) != null) {
        lineCount++;
        String line = fileLine;
        line = line.trim();
        if(line.startsWith("[MockitoHint]") && line.contains(" Unused ->")){
          String token = " -> at ";
          String location = line.substring(line.indexOf(token)+token.length());
          mockitoHints.add(new MockitoHint(MockitoHint.UNUSED, location));
        }
        else if(line.startsWith("[MockitoHint]") && line.contains(" ...args ok? ->")){
          String token = " -> at ";
          String location = line.substring(line.indexOf(token)+token.length());
          mockitoHints.add(new MockitoHint(MockitoHint.ARGS, location));
        }
      }
      input.close();

      int mockitoHintUnusedCount = 0;
      Set<String> uniqueUnusedLocations = new HashSet<String>();
      int mockitoHintArgsCount = 0;
      Set<String> uniqueArgsLocations = new HashSet<String>();
      for(MockitoHint mh:mockitoHints){
        if(mh.getHintType()== MockitoHint.UNUSED){
          mockitoHintUnusedCount++;
          uniqueUnusedLocations.add(mh.getLocation());
        }
        else if(mh.getHintType()== MockitoHint.ARGS){
          mockitoHintArgsCount++;
          uniqueArgsLocations.add(mh.getLocation());
        }
      }

//      System.out.println("############################################");
//      System.out.println("mockito hint unused:"+mockitoHintUnusedCount);
//      System.out.println("mockito hint args:"+mockitoHintArgsCount);
//      System.out.println("mockito hint unused considering location:"+uniqueUnusedLocations.size());
//      System.out.println("mockito hint args considering location:"+uniqueArgsLocations.size());
      System.out.println(mockitoHintUnusedCount+"\t"+uniqueUnusedLocations.size()+"\t"+mockitoHintArgsCount+"\t"+uniqueArgsLocations.size());
    }
    catch(Exception e){
      e.printStackTrace();
      System.exit(1);
    }
  }

  public class MapUtil {
    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
      List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
      list.sort(Map.Entry.comparingByValue());
      Collections.reverse(list);
      Map<K, V> result = new LinkedHashMap<>();
      for (Map.Entry<K, V> entry : list) {
        result.put(entry.getKey(), entry.getValue());
      }

      return result;
    }
  }
}
