# Core Package

Contains connection management and authentication classes used to establish connections to z/OSMF and z/OS SSH services.

## Overview

The `core` package provides connection structures (`ZosConnection` and `SshConnection`), connection factory methods
(`ZosConnectionFactory`), and authentication types (`AuthType`).

- **`ZosConnection`**: Holds connection parameters (host, port, authentication credentials, base path) for REST API
  calls to z/OSMF.
- **`ZosConnectionFactory`**: Factory class providing static helper methods to create `ZosConnection` instances for
  `BASIC`, `TOKEN`, or `SSL` authentication.
- **`AuthType`**: Enum representing the client authentication method (`BASIC`, `TOKEN`, or `SSL`).
- **`SshConnection`**: Holds connection parameters (host, port, user, password) for SSH operations (used by the `zosuss`
  package).

---

## Authenticating to z/OSMF

All REST API requests to z/OSMF are executed over an **HTTPS** encrypted transport channel (TLS). The SDK supports three
authentication types (`AuthType`) to prove client identity over that encrypted channel:

1. **BASIC (`AuthType.BASIC`)**: Authenticates client identity using a username and password in the HTTP
   `Authorization: Basic` header.
2. **TOKEN (`AuthType.TOKEN`)**: Authenticates client identity using a cookie/token value (e.g., JWT or LTPA token)
   retrieved via `zosmfLogin`.
3. **SSL (`AuthType.SSL`)**: Authenticates client identity via **Mutual TLS (mTLS)** using a PKCS12 (`.p12`) key store
   file containing a client certificate and private key. No username or password is required.

### Code Examples

**Basic Authentication**

```java
ZosConnection connection = ZosConnectionFactory.createBasicConnection("host", 10443, "user", "password");
```

**Token Authentication**

```java
ZosConnection connection = ZosConnectionFactory.createTokenConnection("host", 10443, new Cookie("xxx", "xxx"));
```

**SSL / Client Certificate Authentication (mTLS)**

```java
ZosConnection connection = ZosConnectionFactory.createSslConnection("host", 10443, "c:/file.p12", "certpassword");
```

---

## Server Certificate Validation & Trust Options

During the HTTPS TLS handshake, Java validates the z/OSMF **server's SSL certificate**. Server certificate validation
applies across all authentication types (`BASIC`, `TOKEN`, and `SSL`).

If your z/OSMF server uses a self-signed certificate or an internal Enterprise CA, you can configure server trust using
one of the following methods:

### Method 1: Import Certificate into Java's `cacerts` Store (Global JDK Fix)

Import the server's public certificate into your Java runtime's global `cacerts` file using `keytool`:

```bash
keytool -importcert -alias zosmf-server -file zosmf-server.crt -keystore "$JAVA_HOME/lib/security/cacerts" -storepass changeit -noprompt
```

*Result*: All Java applications running on that JDK will trust the z/OSMF server out-of-the-box without needing any SDK
system properties or code changes.

### Method 2: Use a Custom TrustStore File (Application-Level Fix)

Create a `.p12` or `.jks` truststore file and specify its path using system properties:

```java
System.setProperty("zowe.sdk.truststore.path","/path/to/zosmf-truststore.p12");
System.

setProperty("zowe.sdk.truststore.password","mypassword"); // Optional
```

Or via JVM launch argument: `-Dzowe.sdk.truststore.path=/path/to/zosmf-truststore.p12`

*Result*: The SDK validates the z/OSMF server against your custom truststore file without altering Java's global
`cacerts`.

### Method 3: Use Windows OS Certificate Store (Windows Enterprise CAs)

If your z/OSMF server's certificate is issued by a corporate CA that is already installed in your Windows Certificate
Store (`certmgr.msc`), instruct Java to read the OS truststore:

```bash
-Djavax.net.ssl.trustStoreType=WINDOWS-ROOT
```

Or in Java code:

```java
System.setProperty("javax.net.ssl.trustStoreType","WINDOWS-ROOT");
```

*Result*: Java inherits trusted certificates directly from the Windows Certificate Store.

### Insecure Mode (`zowe.sdk.allow.insecure.connection=true`)

To bypass server TLS certificate verification and hostname checks for isolated test or local sandbox environments:

```java
System.setProperty("zowe.sdk.allow.insecure.connection","true");
```

Or via JVM launch argument: `-Dzowe.sdk.allow.insecure.connection=true`

*Result*: Disables server certificate and hostname verification for all REST requests (acts like `curl -k`). A warning
is logged on connection setup.