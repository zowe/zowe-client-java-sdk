This program and the accompanying materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at https://www.eclipse.org/legal/epl-v20.html

SPDX-License-Identifier: EPL-2.0

Copyright Contributors to the Zowe Project.

# Zowe Java Client SDK

[![Maven Central](https://img.shields.io/maven-central/v/org.zowe.client.java.sdk/zowe-client-java-sdk.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.zowe.client.java.sdk%22%20AND%20a:%22zowe-client-java-sdk%22)
[![javadoc](https://javadoc.io/badge2/org.zowe.client.java.sdk/zowe-client-java-sdk/javadoc.svg)](https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk)

This Java SDK is a sub-project of Zowe, focusing on modernizing mainframe experience. Zowe is a project hosted by the Open Mainframe Project, a Linux Foundation project.  

Version 1.0 provides functionality documented in [MVP issue](https://github.com/zowe/zowe-client-java-sdk/issues/5). Project requires Java 11 and above. 

This SDK lets you leverage the underlying z/OSMF REST APIs on a z/OS system to build applications that interface with your z/OS instance.

The goal is to help Java developers leverage the Java Zowe Client Software Development Kit (SDK) to build client applications and scripts that interface with the mainframe easily. 

For example, one API package provides the ability to upload and download z/OS data sets. You can leverage that package to rapidly build a client application that interacts with data sets.
  
The SDK is built to perform the tedious leg work to interface with z/OSMF and provide services that access the mainframe in ways where you can build automation, testing, and DevOps applications without the need to build those directly on the mainframe itself, and as such, opens these applications to modern tools.  
  
Class names providing services:

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
    CheckStatus (zosmf info)
    ListDefinedSystems (zosmf info)
    Shell (uss commands)
      
## Requirements  
    
    Maven  
    Java 11  
    z/OSMF installed on your backend z/OS instance.  
  
## Code Samples  

[Samples](https://github.com/frankgiordano/zowe-client-java-sdk-examples)    
   
## Demo App  

[ZosShell](https://github.com/frankgiordano/ZosShell)
    
## Build
  
The following maven command at the root prompt of the project will produce zowe-client-java-sdk.jar in the target directory:
  
    mvn clean package  
  
## Logger 

In your project consuming this SDK, you will need to implement a logging framework. 

SLF4J is in place for logging APIs within this SDK.   
  
SLF4J stands for Simple Logging Facade for Java. It provides a simple abstraction of all the logging frameworks.   
  
As such, as needed in your parent project implement a logging framework which wraps around SLF4j, for instance logging frameworks such as Log4j2, Logback, JUL (java. util. logging), etc.

## Documentation  

https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk/latest/index.html  
  
## Maven Central Publication  

https://mvnrepository.com/artifact/org.zowe.client.java.sdk/zowe-client-java-sdk  
  

