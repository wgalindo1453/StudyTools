package atm.cs.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ClassConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.options.Options;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.util.Chain;

public class ExtractMockedTypes {

  private static final boolean DEBUG = false;

  private boolean isRelevantAnnotationForMocking(SootField sootField){
    boolean result = false;
    for(Tag tag: sootField.getTags()) {
      if (tag instanceof VisibilityAnnotationTag) {
        VisibilityAnnotationTag vaTag = (VisibilityAnnotationTag) tag;
        for (AnnotationTag aTag : vaTag.getAnnotations()) {
          if (aTag.getType().equals("Lorg/mockito/Mock;")) {
            result = true;
            return result;
          } else if (aTag.getType().equals("Lorg/mockito/Spy;")) {
            result = true;
            return result;
          }
        }
      }
    }
    return result;
  }

  private boolean isRelevantMethonForMocking(InvokeExpr invokeExpr){
    boolean result = false;
    String methodName = invokeExpr.getMethodRef().getName();
    String declaringClassName = invokeExpr.getMethodRef().getDeclaringClass().getName();
    if(methodName.equals("mock") && declaringClassName.equals("org.mockito.Mockito")){
      result = true;
      return result;
    }
    else if(methodName.equals("spy") && declaringClassName.equals("org.mockito.Mockito")){
      result = true;
      return result;
    }
    else if(methodName.equals("mockStatic") && declaringClassName.equals("org.mockito.Mockito")){
      result = true;
      return result;
    }
    else
    if(methodName.equals("mock") && (declaringClassName.equals("com.nhaarman.mockito_kotlin.MockitoKt") || declaringClassName.startsWith("com.nhaarman.mockitokotlin2.MockitoKt"))){
      result = true;
      return result;
    }
    else if(methodName.equals("spy") && (declaringClassName.equals("com.nhaarman.mockito_kotlin.SpyingKt") || declaringClassName.startsWith("com.nhaarman.mockitokotlin2.SpyingKt"))){
      result = true;
      return result;
    }
    else if(methodName.equals("mockStatic") && (declaringClassName.equals("com.nhaarman.mockito_kotlin.MockitoKt") || declaringClassName.startsWith("com.nhaarman.mockitokotlin2.MockitoKt"))){
      result = true;
      return result;
    }
    if(methodName.equals("mock") || methodName.equals("spy") || methodName.equals("spy")){
      System.out.println("SKIPPED_METHOD:"+methodName);
      System.out.println("SKIPPED_CLASS:"+declaringClassName);
    }
    return result;
  }

  private boolean skipBasedOnClassName(String className){
    boolean result = false;
    if(className.equals("org.smartregister.chw.util.ChildUtilsTest")){
      result = true;
      return result;
    }
    return result;
  }

  public void extractMockedTypes(String classFolders) {
    Set<String> mockedTypes = new HashSet<String>();
    int mocksCount = 0;
    String classFoldersArray[]=classFolders.split(",");
    List<String> classFoldersList = new ArrayList<String>();
    for(String classFolder:classFoldersArray){
      classFoldersList.add(classFolder);
    }
    //initialize soot
    G.reset();
    Options.v().set_allow_phantom_refs(true);
    Options.v().set_output_format(Options.output_format_jimple);
    Options.v().set_process_dir(classFoldersList);
    Options.v().set_whole_program(true);
    Scene.v().loadNecessaryClasses();
    Chain<SootClass> testClasses = Scene.v().getApplicationClasses();
    //retrive method bodies
    List<SootClass> classesList = new ArrayList<SootClass>();
    for(SootClass tc:testClasses){
      classesList.add(tc);
    }

    boolean changed = true;
    while(changed){
      changed = false;
      int initialClassSize = testClasses.size();
      for(SootClass c: classesList){
        for(SootMethod sm:c.getMethods()){
          if(sm.isConcrete()) {
            sm.retrieveActiveBody();
          }
        }
      }
      if(initialClassSize!=Scene.v().getApplicationClasses().size()){
        classesList.clear();
        for(SootClass sc:Scene.v().getApplicationClasses()){
          classesList.add(sc);
        }
        changed=true;
      }
    }
    for(SootClass tc : testClasses){
      boolean shouldSkip = skipBasedOnClassName(tc.getName());
      if(shouldSkip){
        continue;
      }
      if(DEBUG){
        System.out.println("CLASS:"+tc.getName());
      }
      int classMocksCount = 0;
      //process all fields in class
      Chain<SootField> classFields = tc.getFields();
      for(SootField cf:classFields){
        boolean isRelevantAnnotation = isRelevantAnnotationForMocking(cf);
        if(isRelevantAnnotation){
          if(DEBUG) {
            System.out.println("FIELD:" + cf);
            System.out.println("TYPE:" + cf.getType().toString());
          }
          String mockedTypeName = cf.getType().toString();
          mockedTypeName = mockedTypeName.replaceAll("\\$", ".");
          if (mockedTypeName.startsWith("kotlin.jvm.functions.Function")) {
            System.out.println("======================================");
            System.out.println("CLASS:" + tc.getName());
            System.out.println("FIELD:" + cf);
            System.out.println("======================================");
            mocksCount++;
            //mockedTypes.add("LAMBDA");
            //classMocksCount++;
          }
          else {
            mocksCount++;
            mockedTypes.add(mockedTypeName);
            classMocksCount++;
          }
        }
      }
      List<SootMethod> sootMethods = tc.getMethods();
      for(SootMethod cm:sootMethods){
        if(DEBUG) {
          System.out.println("METHOD:" + cm);
        }
        if(cm.hasActiveBody()){
          Body body = cm.getActiveBody();
          for (Unit unit : body.getUnits()) {
            for (ValueBox vb : unit.getUseAndDefBoxes()) {
              if (vb.getValue() instanceof InvokeExpr) {
                InvokeExpr invokeExpr = (InvokeExpr) vb.getValue();
                boolean isRelevantCall = isRelevantMethonForMocking(invokeExpr);
                if(isRelevantCall) {
                  if(DEBUG) {
                    System.out.println("INVOCATION:" + vb.getValue());
                  }
                  //case of using mock(Class.food)
                  if(invokeExpr.getArgs().size()>0) {
                    Value classArgument = invokeExpr.getArg(0);
                    if(classArgument instanceof ClassConstant){
                      ClassConstant classConstant = (ClassConstant) classArgument;
                      String mockedTypeName = classConstant.getValue();
                      mockedTypeName = mockedTypeName.substring(1,mockedTypeName.length()-1);
                      mockedTypeName = mockedTypeName.replaceAll("\\$", ".");
                      mockedTypeName = mockedTypeName.replaceAll("/", ".");
                      if(DEBUG) {
                        System.out.println("TYPE:" + mockedTypeName);
                      }
                      if (mockedTypeName.startsWith("kotlin.jvm.functions.Function")) {
                        System.out.println("======================================");
                        System.out.println("CLASS:" + tc.getName());
                        System.out.println("METHOD:" + cm.getName());
                        System.out.println("UNIT:" + unit);
                        System.out.println("======================================");
                        mocksCount++;
                        //mockedTypes.add("LAMBDA");
                        //classMocksCount++;
                      }
                      else {
                        mockedTypes.add(mockedTypeName);
                        mocksCount++;
                        classMocksCount++;
                      }
                    }
                    else if (classArgument instanceof JimpleLocal) {
                      JimpleLocal jimpleLocal = (JimpleLocal) classArgument;
                      String mockedTypeName = jimpleLocal.getType().toString();
                      mockedTypeName = mockedTypeName.replaceAll("\\$", ".");
                      mockedTypeName = mockedTypeName.replaceAll("/", ".");

                      if (DEBUG) {
                        System.out.println("TYPE:" + mockedTypeName);
                      }
                      if (mockedTypeName.startsWith("kotlin.jvm.functions.Function")) {
                        System.out.println("======================================");
                        System.out.println("CLASS:" + tc.getName());
                        System.out.println("METHOD:" + cm.getName());
                        System.out.println("UNIT:" + unit);
                        System.out.println("======================================");
                        mocksCount++;
                        //mockedTypes.add("LAMBDA");
                        //classMocksCount++;
                      }
                      else {
                        mockedTypes.add(mockedTypeName);
                        mocksCount++;
                        classMocksCount++;
                      }
                    }
                    else{
                      System.out.println("HANDLE CASE"+classArgument.getClass());
                      System.exit(1);
                    }
                  }
                  else{
                    System.out.println("HANDLE NO ARGUMENT CASE");
                    System.exit(1);
                  }
                }
              }
            }
          }
        }
      }
      if(DEBUG) {
        if (classMocksCount > 0) {
          System.out.println(tc.getName() + "#" + classMocksCount);
        }
      }
    }
    System.out.println("////////////////////RESULTS////////////////////////");
    for(String mockedType:mockedTypes){
      System.out.println(mockedType);
    }
    System.out.println("types count:"+mockedTypes.size());
    System.out.println("test doubles count:"+mocksCount);
  }

  public void extractTestClasses(String classFolders) {
    String classFoldersArray[]=classFolders.split(",");
    List<String> classFoldersList = new ArrayList<String>();
    for(String classFolder:classFoldersArray){
      classFoldersList.add(classFolder);
    }
    //initialize soot
    G.reset();
    Options.v().set_allow_phantom_refs(true);
    Options.v().set_output_format(Options.output_format_jimple);
    Options.v().set_process_dir(classFoldersList);
    Options.v().set_whole_program(true);
    Scene.v().loadNecessaryClasses();
    Chain<SootClass> testClasses = Scene.v().getApplicationClasses();
    //retrive method bodies
    List<SootClass> classesList = new ArrayList<SootClass>();
    for(SootClass tc:testClasses){
      classesList.add(tc);
    }

    boolean changed = true;
    while(changed){
      changed = false;
      int initialClassSize = testClasses.size();
      for(SootClass c: classesList){
        for(SootMethod sm:c.getMethods()){
          if(sm.isConcrete()) {
            sm.retrieveActiveBody();
          }
        }
      }
      if(initialClassSize!=Scene.v().getApplicationClasses().size()){
        classesList.clear();
        for(SootClass sc:Scene.v().getApplicationClasses()){
          classesList.add(sc);
        }
        changed=true;
      }
    }
    for(SootClass tc : testClasses){
      boolean shouldSkip = skipBasedOnClassName(tc.getName());
      if(shouldSkip){
        continue;
      }
      System.out.println(tc.getName());
    }
  }
}
