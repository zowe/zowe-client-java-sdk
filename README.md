This program and the accompanying materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at https://www.eclipse.org/legal/epl-v20.html

SPDX-License-Identifier: EPL-2.0

Copyright Contributors to the Zowe Project.

# Zowe Java Client SDK

![](https://img.shields.io/badge/license-EPL--2.0-blue)
[![Maven Central](https://img.shields.io/maven-central/v/org.zowe.client.java.sdk/zowe-client-java-sdk.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=org.zowe.client.java.sdk&smo=true)
[![javadoc](https://javadoc.io/badge2/org.zowe.client.java.sdk/zowe-client-java-sdk/javadoc.svg)](https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk)

This project is a subproject of Zowe, focusing on modernizing mainframe experience. Zowe is a project hosted by the Open Mainframe Project, a Linux Foundation project.

The Java SDK lets you leverage the underlying z/OSMF REST APIs on a z/OS system to build client applications and scripts that interface with your z/OS instance seamlessly.

For instance, one API package provides the ability to upload and download z/OS data sets. You can leverage that package to rapidly build a client application that interacts with data sets.

The Java SDK joins an existing community of other language-specific SDKs. It provides the Java community similar capabilities based on the NodeJS SDK. 
  
This SDK may differ from some others with the JobMonitor class adding prebuilt functionality for automation tasks.  
  
Issues worked on documenting main feature set provided can be viewed within the following MVP issues:  
[#1](https://github.com/zowe/zowe-client-java-sdk/issues/5) [#2](https://github.com/zowe/zowe-client-java-sdk/issues/219) [#3](https://github.com/zowe/zowe-client-java-sdk/issues/281)
  
Prebuilt API services located in the following packages/classes:  

zowe.client.sdk.zosconsole.method  
  
    IssueConsole
  
zowe.client.sdk.zosfiles.dsn.methods  
  
    DsnCopy
    DsnCreate
    DsnDelete
    DsnGet
    DsnList
    DsnRename
    DsnWrite
  
zowe.client.sdk.zosfiles.uss.methods  
    
    UssChgMode
    UssChgOwner
    UssChTag
    UssCopy
    UssCreate
    UssDelete
    UssExtAttrs
    UssGet
    UssGetAcl
    UssList
    UssMount
    UssSetAcl
    UssWrite

zowe.client.sdk.zosjobs.methods
  
    JobCancel
    JobDelete
    JobGet
    JobMonitor
    JobSubmit

zowe.client.sdk.zoslogs.method  
  
    ZosLog  

zowe.client.sdk.zosmfinfo.methods  
  
    ZosmfStatus
    ZosmfSystems 

zowe.client.sdk.zostso.method  
  
    IssueTso    

zowe.client.sdk.zosuss.method  
  
    IssueUss   
          
## TeamConfig Package  
  
The TeamConfig package provides API methods to retrieve a profile section from Zowe Global Team Configuration with keytar information to help perform connection processing without hard coding username and password. Keytar represents credentials stored securely on your computer when performing the Zowe global initialize [command](https://docs.zowe.org/stable/user-guide/cli-using-initializing-team-configuration/) which prompts you for username and password.   
  
TeamConfig class only supports Zowe Global Team Configuration provided by Zowe V2.  
  
If you have Zowe CLI on your system with Global Team Configuration initialized, you can use TeamConfig API methods to retrieve a profile type which will include the secure username and password information stored in our OS credential store manager.   
  
You can use this information to create a dynamic ZosConnection object to perform zosmf authentication for the all the other packages. This avoids the need to hard code values.    
  
See following package/class:  
  
zowe.client.sdk.teamconfig  
    
    TeamConfig
  
## Http Rest Processing
   
SDK release version 2 and above uses Unirest for Java for Http functionality.  
   
Unirest's library provides the ability to retrieve IBM z/OSMF JSON error document.  
  
For example, the following http GET request will result in an HTTP 500 error:  
  
    https://xxxxxxx.xxxxx.net:xxxx/zosmf/restfiles/ds?
  
and the JSON error report document body response is:  
  
    {"rc":4,"reason":13,"category":1,"message":"query parm dslevel= or volser= must be specified"} 
  
## Requirements  
    
    Maven  
    Compatible with all Java versions 11 and above.
    z/OSMF installed on your backend z/OS instance.  
  
## Code Samples  

[Samples](https://github.com/frankgiordano/zowe-client-java-sdk-examples)    
   
## Demo App  

[ZosShell](https://github.com/frankgiordano/ZosShell)
    
## Build
  
The following maven command at the root prompt of the project will produce zowe-client-java-sdk.jar in the target directory:
  
    mvn clean package  
  
## Logger 
  
SLF4J is in place for logging APIs within this SDK.   
  
SLF4J stands for Simple Logging Facade for Java. It provides a simple abstraction of all the logging frameworks.   
  
To enable SDK logging within your project, implement a logging framework which wraps around SLF4j, for instance logging frameworks such as Log4j2, Logback, JUL (java. util. logging), etc.

## Documentation  

https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk/latest/index.html  
  
## Maven Central Publication  

https://mvnrepository.com/artifact/org.zowe.client.java.sdk/zowe-client-java-sdk  
  
