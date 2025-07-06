This program and the accompanying materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at https://www.eclipse.org/legal/epl-v20.html

SPDX-License-Identifier: EPL-2.0

Copyright Contributors to the Zowe Project.

# Zowe Java Client SDK

![zowe org_video_outro_gif_accessibility](https://github.com/zowe/zowe-client-java-sdk/assets/7764341/aaa26c45-6fb3-4857-8e6b-80fc85dad4cd)
  
![](https://img.shields.io/badge/license-EPL--2.0-blue)
[![Maven Central](https://img.shields.io/maven-central/v/org.zowe.client.java.sdk/zowe-client-java-sdk.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=org.zowe.client.java.sdk&smo=true)
[![javadoc](https://javadoc.io/badge2/org.zowe.client.java.sdk/zowe-client-java-sdk/javadoc.svg)](https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk)

This project is a subproject of Zowe, focusing on modernizing mainframe experience. Zowe is a project hosted by the Open Mainframe Project, a Linux Foundation project.

The Java SDK lets you leverage the underlying z/OSMF REST APIs on a z/OS system to build client applications and scripts that interface with your z/OS instance seamlessly.

For instance, one API package provides the ability to upload and download z/OS data sets. You can leverage that package to rapidly build a client application that interacts with data sets.

The Java SDK joins an existing community of other language-specific SDKs. It provides the Java community with similar capabilities based on the NodeJS SDK. 
  
This SDK may differ from some others with the JobMonitor class adding prebuilt functionality for automation tasks.  
  
Issues worked on documenting the main feature set provided can be viewed within the following MVP issues:  
[#1](https://github.com/zowe/zowe-client-java-sdk/issues/5) [#2](https://github.com/zowe/zowe-client-java-sdk/issues/219) [#3](https://github.com/zowe/zowe-client-java-sdk/issues/281) [#4](https://github.com/zowe/zowe-client-java-sdk/issues/338)  
  
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
    
    UssChangeMode
    UssChangeOwner
    UssChangeTag
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

zowe.client.sdk.zosmfauth.methods  
  
    ZosmfLogin
    ZosmfLogout  
    ZosmfPassword  
    
zowe.client.sdk.zosmfinfo.methods  
  
    ZosmfStatus  
    ZosmfSystems   

zowe.client.sdk.zostso.method  
  
    IssueTso    

zowe.client.sdk.zosuss.method  
  
    IssueUss   
          
## TeamConfig Package  
  
The TeamConfig package provides API methods to retrieve a profile section from Zowe Global Team Configuration with keytar information to help perform connection processing without a hard coding username and password. Keytar represents credentials stored securely on your computer when performing the Zowe global initialize [command](https://docs.zowe.org/stable/user-guide/cli-using-initializing-team-configuration/) which prompts you for username and password.   
  
TeamConfig class only supports Zowe Global Team Configuration provided by Zowe V2.  
  
If you have Zowe CLI on your system with Global Team Configuration initialized, you can use TeamConfig API methods to retrieve a profile type which will include the secure username and password information stored in our OS credential store manager.   
  
You can use this information to create a dynamic ZosConnection object to perform zosmf authentication for all the other packages. This avoids the need to hard code values.    
  
See the following package/class:  
  
zowe.client.sdk.teamconfig  
    
    TeamConfig
    
NOTE:  
Whenever you encounter a JSON parse error for reading the Zowe Team Configuration file, make sure to include double quotes around keys and its values.  
  
## Http Rest Processing
  
SDK release version 2 uses Unirest 3.x Http functionality.  
  
SDK release version 3 and above uses Unirest 4.x which removes the dependency on Apache Commons and provides token processing for Web TOKEN authentication.   
   
Unirest's library provides the ability to retrieve IBM z/OSMF JSON error document.  
  
For example, the following http GET request will result in an HTTP 500 error:  
  
    https://xxxxxxx.xxxxx.net:xxxx/zosmf/restfiles/ds?
  
and the JSON error report document body response is:  
  
    {"rc":4,"reason":13,"category":1,"message":"query parm dslevel= or volser= must be specified"} 

## Authenticating to z/OSMF
  
Since the release of the SDK, the authentication of each REST API call is done with BASIC authentication.
  
With SDK release version 3, Web TOKEN authentication was added.   
  
The current version 4, SSL authentication from a certificate file was added.  
  
With three types of authentication available, the AuthType enum class was introduced to represent each type.    
  
This enum is used to send it to the ZosConnection constructor denoting the type of authentication to perform.  

For BASIC example, the following ZosConnection object is specified to perform BASIC authentication:  
  
    ZosConnection connection = new ZosConnection(AuthType.BASIC).host("xxxx").password("xxxx").user("xxxx").zosmfPort("xxxx").build();
  
Basic authentication means that the http request contains a BASIC header representing the username and password encrypted.   
  
For web TOKEN example, the following ZosConnection object is specified:  
  
    ZosConnection connection = new ZosConnection(AuthType.TOKEN).host("xxxx").zosmfPort("xxxx").token(new Cookie("xxxx=xxxx")).build();
  
With the zosmfauth package, ZosmfAuth provides an API (zosmfLogin) to retrieve authentication tokens (a JSON Web and an LTPA TOKEN) on a BASIC authentication request. This package contains an API that can also be used to delete the current store of JSON Web and LPTA tokens.  
  
Web TOKEN support must be enabled on your z/OSMF system. For more information, see Enabling JSON Web TOKEN support in the IBM z/OS Management Facility Configuration Guide.  
  
For SSL example, the following ZosConnection object is specified:  

    ZosConnection connection = new ZosConnection(AuthType.SSL).host("xxxx").zosmfPort("xxxx").certPassword("xxxx").certFilePath("xxxx")..build();
  
The certificate file path value is normally retrieved from the teamconfig configuration file defined as a property.  
  
See [README.md](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosmfauth/README.md) in zosmfauth package for further details.    
  
## Requirements  
    
    Maven  
    Compatible with all Java versions 11 and above.
    z/OSMF installed on your backend z/OS instance.  
  
## Code Samples  

[Samples](https://github.com/frankgiordano/zowe-client-java-sdk-examples)    
   
## Demo App  

[ZosShell](https://github.com/frankgiordano/ZosShell)

## Examples  

  See the following GITHUB [Zowe-Java-SDK](https://github.com/Zowe-Java-SDK) location for code examples and applications.  

In the project, you will find code examples located in each package's README.MD file. See:  

  [teamconfig](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/teamconfig/README.md)  
  [zosconsole](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosconsole/README.md)  
  [zosfiles-dsn](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosfiles/dsn/README.md)  
  [zosfiles-uss](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosfiles/uss/README.md)  
  [zosjobs](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosjobs/README.md)  
  [zoslogs](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zoslogs/README.md)  
  [zosmfauth](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosmfauth/README.md)  
  [zosmfinfo](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosmfinfo/README.md)  
  [zostso](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zostso/README.md)    
  [zosuss](https://github.com/zowe/zowe-client-java-sdk/blob/main/src/main/java/zowe/client/sdk/zosuss/README.md)  
      
## Build  

Java 11 and above is required to compile JAR file. Maven is required to build JAR file.     
  
The following maven command at the root prompt of the project will produce zowe-client-java-sdk.jar in the target directory:
  
    mvn clean package  
  
## Logger 
    
For logging, you will need to implement SLF4J binding and logging framework dependencies. 
    
The SDK does not provide SLF4J or any logging dependency included in its FATJAR.   
  
## Documentation  

https://javadoc.io/doc/org.zowe.client.java.sdk/zowe-client-java-sdk/latest/index.html  
  
## Maven Central Publication  

https://mvnrepository.com/artifact/org.zowe.client.java.sdk/zowe-client-java-sdk  

## Install Java SDK from an online registry

To install this library in your project, use a build tool such as Maven, Gradle or Ant. Use the following link to get the necessary artifact:

https://mvnrepository.com/artifact/org.zowe.client.java.sdk/zowe-client-java-sdk

For a Maven project add the SDK as a dependency by updating your `pom.xml` as follows:

    <dependency>
        <groupId>org.zowe.client.java.sdk</groupId>
        <artifactId>zowe-client-java-sdk</artifactId>
        <version>4.0.0-RC1</version>
    </dependency>

For a Gradle project add the SDK as a dependency by updating your `build.gradle` as follows:

    implementation group: 'org.zowe.client.java.sdk', name: 'zowe-client-java-sdk', version: '4.0.0-RC1'  
