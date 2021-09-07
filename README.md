This program and the accompanying materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at https://www.eclipse.org/legal/epl-v20.html

SPDX-License-Identifier: EPL-2.0

Copyright Contributors to the Zowe Project.  

# Zowe SDK for Java

This Java SDK is a sub-project of Zowe, focusing on modernizing mainframe experience. Zowe is a project hosted by the Open Mainframe Project, a Linux Foundation project.  

Version 1.0 provides functionality documented in MVP issue #5. Project requires Java 11 and above. 

The SDK lets you leverage the underlying z/OSMF REST APIs on a z/OS system to build applications that interface with the mainframe.

The goal is to provide Java developers an SDK to easily interface with a backend mainframe from any computer that has a JVM. The SDK will do the leg work to allow you to interface with z/OS services. These services provides access to the mainframe in ways where you can build automation, testing, and devOps applications without the need to build those directly on the mainframe itself, and as such, opens these applications to modern tools.  

Functionality provided:

    GetJobs   
    IssueCommand (mvs commands)  
    IssuesTsoCommand  
    SubmitJobs  
    DownloadDataset  
    CreateDataset  
    DeleteDataset  
    WriteDataset  
    ListDataset  
    MonitorJobs  
    CopyDataset
    CancelJobs
    DeleteJobs

See the following example programs within each examples' package:

    src/main/java/zosconsole/examples   
        IssueCommand.java  
  
    src/main/java/zosfiles/examples    
        CopyDataset.java
        CreateDataset.java  
        DeleteDataset.java  
        DownloadDataset.java
        ListDatasets.java
        WriteDataset.java  
  
    src/main/java/zosjobs/examples    
        CancelJobs.java
        DeleteJobs.java
        GetJobs.java
        MonitorJobs.java
        SubmitJobs

    src/main/java/zostos/examples  
        IssueTsoCommand.java
  
You need to replace all instances of "XXX" accordingly to meet your target's credentials and host information.   
    
## Build
  
The following maven command at the root prompt of the project will produce zowe-client-java-sdk.jar in the target directory:
  
    mvn clean package  
  
## Logger  
  
Logger packaged used for the project is log4j2.  
  
Under 'resources' directory you will find the logger configuration file log4j2.xml.  
  
Change <Root level="debug"> assignment accordingly for your needs.  
  
## Documentation  

https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk/latest/index.html  
  
## Maven Central Publication  

https://mvnrepository.com/artifact/org.zowe.client.java.sdk/zowe-client-java-sdk  
  

