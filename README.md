# zowe-client-java-sdk

Work In Progress implementation of a Zowe SDK for Java.

The SDK lets you leverage the underlying z/OSMF APIs on a z/OS system to build applications that interface with the mainframe.

The goal is to provide Java developers an SDK to easily interface with a backend mainframe from your Windows, Linux, or Mac instance through Rest APIs. Let the SDK do the leg work while you focus on coding your client side applications. An SDK will help developers to produce testing, building, and automation type applications without the need to build those directly on the mainframe itself, and as such, opens these applications to modern tools like CI/CD pipelines etc.  

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

