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

### Identifying an Untrusted z/OSMF Server Certificate

When executing SDK operations against an untrusted or self-signed z/OSMF server, the SDK will throw a
`ZosmfRequestException` with one of the following underlying SSL/TLS error messages:

**1. Untrusted Certificate / PKIX Path Building Failure:**

```
zowe.client.sdk.rest.exception.ZosmfRequestException: java.io.IOException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

*Cause*: The z/OSMF server's certificate or issuing CA is self-signed or not present in Java's trusted `cacerts` store.

**2. Hostname Mismatch Failure:**

```
zowe.client.sdk.rest.exception.ZosmfRequestException: java.io.IOException: No name matching your-zosmf-host.com found
```

*Cause*: The hostname used in your connection configuration does not match the `Common Name` (CN) or
`Subject Alternative Name` (SAN) inside the server's SSL certificate.

When navigating to the same z/OSMF URL in your web browser (for example, `https://your-zosmf-host.com:10443/zosmf/`):

- The browser displays a warning page such as **"Your connection is not private"** or **
  `NET::ERR_CERT_AUTHORITY_INVALID`**.
- The address bar displays a **"Not Secure"** status or warning icon, indicating that the server's certificate is
  untrusted in your environment.

To connect to this server securely via the Java SDK without disabling TLS verification, you can export/download the
certificate from your browser and configure Java or the SDK to trust it using Method 1 or Method 2 below.

### How to Download the Certificate from Your Browser

1. Navigate to your z/OSMF URL in Google Chrome or Microsoft Edge (e.g. `https://your-zosmf-host.com:10443/zosmf/`).
2. Click the **"Not Secure"** warning icon in the address bar next to the URL.
3. Click **"Connection is not secure"** -> **"Certificate is invalid"** (or **"View Certificate"**).
4. Go to the **Details** tab and click **"Export..."** (or **"Save to File"**).
5. Save the certificate to disk (e.g., `C:\Users\youruser\Downloads\zosmf-server.crt`).

---

### Method 1: Import Certificate into Java's `cacerts` Store (Global JDK Fix)

Import the downloaded certificate into your Java runtime's global `cacerts` truststore file using `keytool`:

**On Windows (PowerShell):**

```powershell
keytool -importcert -alias zosmf-server -file "C:\Users\youruser\Downloads\zosmf-server.crt" -keystore "C:\path\to\jdk\lib\security\cacerts" -storepass changeit -noprompt
```

*(Note: Use `$env:JAVA_HOME\lib\security\cacerts` or the explicit path to your JDK's `cacerts` file).*

**On Linux / macOS:**

```bash
sudo keytool -importcert -alias zosmf-server -file /path/to/zosmf-server.crt -keystore "$JAVA_HOME/lib/security/cacerts" -storepass changeit -noprompt
```

*Result*: All Java applications running on that JDK will trust the z/OSMF server out-of-the-box without needing any SDK
system properties or code changes.

---

### Method 2: Use a Custom TrustStore File (Application-Level Fix)

Instead of modifying Java's global `cacerts`, create a dedicated `.p12` or `.jks` truststore file containing the
downloaded certificate:

1. **Create the TrustStore file using `keytool`:**

```powershell
keytool -importcert -alias zosmf-server -file "C:\Users\youruser\Downloads\zosmf-server.crt" -keystore "C:\certs\zosmf-truststore.p12" -storetype PKCS12 -storepass mypassword -noprompt
```

2. **Configure the SDK to use the TrustStore:**

```java
System.setProperty("zowe.sdk.truststore.path","C:/certs/zosmf-truststore.p12");
System.

setProperty("zowe.sdk.truststore.password","mypassword"); // Optional
```

Or via JVM launch argument: `-Dzowe.sdk.truststore.path=C:/certs/zosmf-truststore.p12`

*Result*: The SDK validates the z/OSMF server against your custom truststore file without altering Java's global
`cacerts`.

---

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

---

### Insecure Mode (`zowe.sdk.allow.insecure.connection=true`)

To bypass server TLS certificate verification and hostname checks for isolated test or local sandbox environments:

```java
System.setProperty("zowe.sdk.allow.insecure.connection","true");
```

Or via JVM launch argument: `-Dzowe.sdk.allow.insecure.connection=true`

*Result*: Disables server certificate and hostname verification for all REST requests (acts like `curl -k`). A warning
is logged on connection setup.is logged on connection setup.