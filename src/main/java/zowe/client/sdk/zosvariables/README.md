# z/OS Variables Package

Contains APIs to interact with z/OS system variables and z/OSMF system symbols (using z/OSMF variable REST endpoints).

APIs are located in the methods package.

## API Examples

**Retrieve system variables and system symbols**

````java
package zowe.client.sdk.examples.zosvariables;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.input.factory.VariableGetInputData;
import zowe.client.sdk.zosvariables.input.factory.VariableGetInputFactory;
import zowe.client.sdk.zosvariables.methods.VariableGet;
import zowe.client.sdk.zosvariables.response.VariableGetResponse;

import java.util.List;

/**
 * Class example to showcase VariableGet class functionality.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableGetExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * VariableGet functionality.
     *
     * @param args for main not used
     * @author Ashish Kumar Dash
     */
    public static void main(String[] args) {
        String sysplexName = "PLEX1";
        String systemName = "SYS1";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        VariableGetExp.getLocalVariables(connection);
        VariableGetExp.getVariables(connection, sysplexName, systemName);
        VariableGetExp.getVariablesByNames(connection, sysplexName, systemName);
        VariableGetExp.getSymbols(connection, sysplexName, systemName);
    }

    /**
     * Retrieve z/OS system variables from the local system.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Ashish Kumar Dash
     */
    public static void getLocalVariables(ZosConnection connection) {
        VariableGetResponse response;
        try {
            VariableGet variableGet = new VariableGet(connection);
            VariableGetInputData getInputData = VariableGetInputFactory.createZosVariableLocal();
            response = variableGet.get(getInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        response.getSystemVariableList().forEach(item ->
                System.out.println(item.getName() + " = " + item.getValue()));
    }

    /**
     * Retrieve z/OS system variables from the given sysplex and system.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @author Ashish Kumar Dash
     */
    public static void getVariables(ZosConnection connection, String sysplexName, String systemName) {
        VariableGetResponse response;
        try {
            VariableGet variableGet = new VariableGet(connection);
            VariableGetInputData getInputData = VariableGetInputFactory.createZosVariable(sysplexName, systemName);
            response = variableGet.get(getInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        response.getSystemVariableList().forEach(item ->
                System.out.println(item.getName() + " = " + item.getValue()));
    }

    /**
     * Retrieve specific z/OS system variables by name from the given sysplex and system.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @author Ashish Kumar Dash
     */
    public static void getVariablesByNames(ZosConnection connection, String sysplexName, String systemName) {
        VariableGetResponse response;
        try {
            VariableGet variableGet = new VariableGet(connection);
            VariableGetInputData getInputData =
                    VariableGetInputFactory.createZosVariable(sysplexName, systemName, List.of("var1", "var2"));
            response = variableGet.get(getInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        response.getSystemVariableList().forEach(item ->
                System.out.println(item.getName() + " = " + item.getValue()));
    }

    /**
     * Retrieve z/OSMF system symbols from the given sysplex and system.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex (local sysplex only)
     * @param systemName  name of the target z/OS system
     * @author Ashish Kumar Dash
     */
    public static void getSymbols(ZosConnection connection, String sysplexName, String systemName) {
        VariableGetResponse response;
        try {
            VariableGet variableGet = new VariableGet(connection);
            VariableGetInputData getInputData = VariableGetInputFactory.createZosmfSymbol(sysplexName, systemName);
            response = variableGet.get(getInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        response.getSystemSymbolList().forEach(item ->
                System.out.println(item.getName() + " = " + item.getValue()));
    }

}
````

**Create or update system variables**

````java
package zowe.client.sdk.examples.zosvariables;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.methods.VariableCreate;
import zowe.client.sdk.zosvariables.model.SystemVariable;

import java.util.List;

/**
 * Class example to showcase VariableCreate class functionality.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableCreateExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * VariableCreate functionality.
     *
     * @param args for main not used
     * @author Ashish Kumar Dash
     */
    public static void main(String[] args) {
        String sysplexName = "PLEX1";
        String systemName = "SYS1";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        VariableCreateExp.createVariables(connection, sysplexName, systemName);
    }

    /**
     * Create or update z/OS system variables in the system variable pool.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @author Ashish Kumar Dash
     */
    public static void createVariables(ZosConnection connection, String sysplexName, String systemName) {
        Response response;
        try {
            VariableCreate variableCreate = new VariableCreate(connection);
            List<SystemVariable> variables = List.of(
                    new SystemVariable("var1", "value1", "first variable description"),
                    new SystemVariable("var2", "value2"));
            response = variableCreate.create(sysplexName, systemName, variables);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
````

**Delete system variables**

````java
package zowe.client.sdk.examples.zosvariables;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.methods.VariableDelete;

import java.util.List;

/**
 * Class example to showcase VariableDelete class functionality.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableDeleteExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * VariableDelete functionality.
     *
     * @param args for main not used
     * @author Ashish Kumar Dash
     */
    public static void main(String[] args) {
        String sysplexName = "PLEX1";
        String systemName = "SYS1";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        VariableDeleteExp.deleteVariables(connection, sysplexName, systemName);
        VariableDeleteExp.deleteAllVariables(connection, sysplexName, systemName);
    }

    /**
     * Delete the specified z/OS system variables from the target system's variable pool.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @author Ashish Kumar Dash
     */
    public static void deleteVariables(ZosConnection connection, String sysplexName, String systemName) {
        Response response;
        try {
            VariableDelete variableDelete = new VariableDelete(connection);
            response = variableDelete.delete(sysplexName, systemName, List.of("var1", "var2"));
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

    /**
     * Delete the entire system variable pool for the target system.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @author Ashish Kumar Dash
     */
    public static void deleteAllVariables(ZosConnection connection, String sysplexName, String systemName) {
        Response response;
        try {
            VariableDelete variableDelete = new VariableDelete(connection);
            response = variableDelete.deleteAll(sysplexName, systemName);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
````

**Export system variables to a CSV file on USS**

````java
package zowe.client.sdk.examples.zosvariables;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.methods.VariableExport;

/**
 * Class example to showcase VariableExport class functionality.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableExportExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * VariableExport functionality.
     *
     * @param args for main not used
     * @author Ashish Kumar Dash
     */
    public static void main(String[] args) {
        String sysplexName = "PLEX1";
        String systemName = "SYS1";
        String targetFile = "/u/user1/vars.csv";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        VariableExportExp.exportVariables(connection, sysplexName, systemName, targetFile);
    }

    /**
     * Export z/OS system variables to a CSV data file on USS.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @param targetFile  UNIX path to variables export file on USS
     * @author Ashish Kumar Dash
     */
    public static void exportVariables(ZosConnection connection, String sysplexName,
                                       String systemName, String targetFile) {
        Response response;
        try {
            VariableExport variableExport = new VariableExport(connection);
            response = variableExport.export(sysplexName, systemName, targetFile, true);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
````

**Import system variables from a CSV file on USS**

````java
package zowe.client.sdk.examples.zosvariables;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.methods.VariableImport;

/**
 * Class example to showcase VariableImport class functionality.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableImportExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * VariableImport functionality.
     *
     * @param args for main not used
     * @author Ashish Kumar Dash
     */
    public static void main(String[] args) {
        String sysplexName = "PLEX1";
        String systemName = "SYS1";
        String targetFile = "/u/user1/vars.csv";
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        VariableImportExp.importVariables(connection, sysplexName, systemName, targetFile);
    }

    /**
     * Import z/OS system variables from a CSV data file on USS.
     *
     * @param connection  for connection information, see ZosConnection object
     * @param sysplexName name of the target sysplex
     * @param systemName  name of the target z/OS system
     * @param targetFile  UNIX path to variables import file on USS
     * @author Ashish Kumar Dash
     */
    public static void importVariables(ZosConnection connection, String sysplexName,
                                       String systemName, String targetFile) {
        Response response;
        try {
            VariableImport variableImport = new VariableImport(connection);
            response = variableImport.load(sysplexName, systemName, targetFile);
        } catch (ZosmfRequestException e) {
            String errMsg = e.getMessage();
            if (e.getResponse() != null && e.getResponse().hasTextResponsePhrase()) {
                errMsg = e.getResponse().getResponsePhraseAsString().orElse(errMsg);
            }
            throw new RuntimeException(errMsg, e);
        }

        System.out.println(response.toString());
    }

}
````

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.model.ProfileDao;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final int zosmfPort = 0;
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZosConnection getSecureZosConnection() throws TeamConfigException {
        TeamConfig teamConfig = new TeamConfig();
        ProfileDao profile = teamConfig.getDefaultProfile("zosmf");
        return (ZosConnectionFactory.createBasicConnection(
                profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
````
