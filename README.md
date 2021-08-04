# zowe-client-java-sdk

Work In Progress implementation of a Zowe SDK for Java.

The SDK lets you leverage the underlying z/OSMF APIs on a z/OS system to build applications that interface with the mainframe.

The goal is to provide Java developers an SDK to easily interface with a backend mainframe from any computer that has a JVM. The SDK will do the leg work to allow you to interface with z/OS services. These services provides access to the mainframe in ways where you can build automation, testing, and devOps applications without the need to build those directly on the mainframe itself, and as such, opens these applications to modern tools.  

Functionality provided so far:

    GetJobs   
    IssueCommand (mvs commands)  
    IssuesTsoCommand  
    SubmitJobs  

See the following sample programs:

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

