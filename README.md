# zowe-client-java-sdk

Work In Progress implementation of a Zowe SDK for Java.

Functionality provided so far:  
  
    GetJobs   
    IssueCommand (mvs commands)  
    IssuesTsoCommand  
    SubmitJobs  

Seeing the following sample programs:
    
    GetJobsTest.java  
    IssueCommandTest.java  
    IssueTsoCommandTest.java  
    SubmitJobsTest.java  
  
You need to replace all instances of "XXX" accordingly.   
    
## Build
  
The following maven command at the root prompt of the project will produce ZoweJavaSDK.jar in the target directory:
  
    mvn clean package  
  
## Logger  
  
Logger packaged used for the project is log4j2.  
  
Under 'resources' directory you will find the logger configuration file log4j2.xml.  
  
Change <Root level="debug"> assignment accordingly for your needs.  

