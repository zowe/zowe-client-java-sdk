# z/OSMF System Variables

Contains APIs to retrieve z/OS system variables and z/OS system symbols through the z/OSMF REST API.

IBM REST API reference:

https://www.ibm.com/docs/en/zos/2.5.0?topic=services-get-system-variables

## Retrieve local system variables

```java
ZosConnection connection = ZosConnectionFactory.createBasicConnection(
        "host",
        443,
        "user",
        "password"
);

ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection);

SystemVariablesResponse response = systemVariables.getLocal();

for (SystemVariable variable : response.getSystemVariableList()) {
    System.out.println(variable.getName() + "=" + variable.getValue());
}
```

## Retrieve local system symbols

```java
ZosConnection connection = ZosConnectionFactory.createBasicConnection(
        "host",
        443,
        "user",
        "password"
);

ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection);

SystemVariablesInputData inputData = new SystemVariablesInputData()
        .withSource(SystemVariableSource.SYMBOL);

SystemVariablesResponse response = systemVariables.get(inputData);

for (SystemVariable symbol : response.getSystemSymbolList()) {
    System.out.println(symbol.getName() + "=" + symbol.getValue());
}
```

## Retrieve variables from a named sysplex and system

```java
ZosConnection connection = ZosConnectionFactory.createBasicConnection(
        "host",
        443,
        "user",
        "password"
);

ZosmfSystemVariables systemVariables = new ZosmfSystemVariables(connection);

SystemVariablesInputData inputData = new SystemVariablesInputData("PLEX1", "SYS1")
        .withSource(SystemVariableSource.VARIABLE)
        .withVariableNames("sample1", "sample2");

SystemVariablesResponse response = systemVariables.get(inputData);

for (SystemVariable variable : response.getSystemVariableList()) {
    System.out.println(variable.getName() + "=" + variable.getValue());
}
```

## API classes

```text
zowe.client.sdk.zosvariables.methods.ZosmfSystemVariables
zowe.client.sdk.zosvariables.input.SystemVariablesInputData
zowe.client.sdk.zosvariables.input.SystemVariableSource
zowe.client.sdk.zosvariables.response.SystemVariablesResponse
zowe.client.sdk.zosvariables.model.SystemVariable
```