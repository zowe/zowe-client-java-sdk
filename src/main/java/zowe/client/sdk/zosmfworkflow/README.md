# Workflow Package

Contains API to interact with z/OSMF Workflows (using z/OSMF logs REST endpoints).

APIs located in the method package.

## API Examples

**The following example shows the entire API set for Workflow processing**

````java
package zowe.client.sdk.examples.workflow;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zoslogs.input.ZosLogInputData;
import zowe.client.sdk.zoslogs.method.ZosLog;
import zowe.client.sdk.zoslogs.response.ZosLogResponse;
import zowe.client.sdk.zoslogs.types.DirectionType;
import zowe.client.sdk.zoslogs.types.HardCopyType;

/**
 * Class example to showcase Workflow API functionalities.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class WorkflowExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * z/OSMF Workflow functionalities.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        ZosConnection connection = ZosConnectionFactory
                .createBasicConnection(hostName, zosmfPort, userName, password);

        WorkflowList workflowList = new WorkflowList(connection);
        WorkflowGet workflowGet = new WorkflowGet(connection);
        WorkflowArchive workflowArchive = new WorkflowArchive(connection);
        WorkflowDelete workflowDelete = new WorkflowDelete(connection);
        WorkflowStart workflowStart = new WorkflowStart(connection);
        WorkflowCancel workflowCancel = new WorkflowCancel(connection);

        String workflowName = "x";
        String owner = "x";
        String system = "x";
        String target = "x";

        // End-to-end workflow management: creation, start, cancel, archiving, retrieval, and deletion.

        System.out.println("workflow.getDefinition: " + workflowGet.getDefinition(target));
        System.out.println();

        System.out.println("workflow.getDefinition by system: " + workflowGet.getDefinition(target, system));
        System.out.println();

        WorkflowCreate workflowCreate = new WorkflowCreate(connection);
        WorkflowCreateInputData inputData = WorkflowCreateInputData.builder()
                .workflowDefinitionFile(target)
                .system(system)
                .owner(owner)
                .workflowName(workflowName).build();

        WorkflowCreateResponse workflowCreateResponse = workflowCreate.create(inputData);
        String workflowKey = workflowCreateResponse.getWorkflowKey();

        System.out.println("workflowCreateResponse: " + workflowCreateResponse);
        System.out.println();
        System.out.println("workflow.getProperties: " + workflowGet.getProperties(workflowKey));
        System.out.println();

        System.out.println("workflow.getWorkflows: ");
        workflowList.getWorkflows().stream()
                .filter(i -> i.getWorkflowName().contains(workflowName))
                .forEach(i -> System.out.println(i.getWorkflowKey() + " " + i.getWorkflowName()));
        System.out.println();

        System.out.println("Start Workflow with Key: " + workflowKey);
        System.out.println(workflowStart.start(workflowKey));
        System.out.println();

        Thread.sleep(20000);

        System.out.println("Cancel Workflow with Key: " + workflowKey);
        System.out.println(workflowCancel.cancel(workflowKey));
        System.out.println();

        System.out.println("archive workflowKey: " + workflowKey);
        System.out.println(workflowArchive.archive(workflowKey));
        System.out.println();

        System.out.println("workflow.getArchived: ");
        workflowList.getArchived().stream()
                .filter(i -> i.getName().contains(workflowName))
                .forEach(i -> System.out.println(i.getName() + " " + i.getKey()));
        System.out.println();

        System.out.println("workflow.getArchivedByView: ");
        workflowList.getArchivedByView(ViewType.USER).stream()
                .filter(i -> i.getName().contains(workflowName))
                .forEach(i -> System.out.println(i.getName() + " " + i.getKey()));
        System.out.println();

        System.out.println("workflow.getArchivedByOrderBy: ");
        workflowList.getArchivedByOrderBy(OrderByType.DESC).stream()
                .filter(i -> i.getName().contains(workflowName))
                .forEach(i -> System.out.println(i.getName() + " " + i.getKey()));
        System.out.println();

        System.out.println("deleting archive for workflowKey: " + workflowKey);
        workflowDelete.deleteArchived(workflowKey);
        System.out.println();

        System.out.println("workflowList.getArchived: ");
        workflowList.getArchived().stream()
                .filter(i -> i.getName().contains(workflowName))
                .forEach(i -> System.out.println(i.getName() + " " + i.getKey()));
        System.out.println();

        // End-to-end workflow management: creation, retrieval, and deletion.

        workflowCreateResponse = workflowCreate.create(inputData);
        workflowKey = workflowCreateResponse.getWorkflowKey();

        System.out.println("workflowCreateResponse: " + workflowCreateResponse);
        System.out.println();
        System.out.println("workflow.getProperties: " + workflowGet.getProperties(workflowKey));
        System.out.println();

        System.out.println("workflow.getWorkflows: ");
        workflowList.getWorkflows().stream()
                .filter(i -> i.getWorkflowName().contains(workflowName))
                .forEach(i -> System.out.println(i.getWorkflowKey() + " " + i.getWorkflowName()));
        System.out.println();

        System.out.println("workflow.getWorkflowsByOwner: ");
        workflowList.getWorkflowsByOwner(owner).stream()
                .filter(i -> i.getWorkflowName().contains(workflowName))
                .forEach(i -> System.out.println(i.getWorkflowKey() + " " + i.getWorkflowName()));
        System.out.println();

        System.out.println("workflow.getWorkflowsByName: ");
        workflowList.getWorkflowsByName(workflowName).stream()
                .filter(i -> i.getWorkflowName().contains(workflowName))
                .forEach(i -> System.out.println(i.getWorkflowKey() + " " + i.getWorkflowName()));
        System.out.println();

        System.out.println("workflow.getWorkflowsBySystem: ");
        workflowList.getWorkflowsBySystem(system).stream()
                .filter(i -> i.getWorkflowName().contains(workflowName))
                .forEach(i -> System.out.println(i.getWorkflowKey() + " " + i.getWorkflowName()));
        System.out.println();

        System.out.println("deleting for workflowKey: " + workflowKey);
        workflowDelete.delete(workflowKey);
        System.out.println();

        System.out.println("workflow.getWorkflows: ");
        workflowList.getWorkflows().stream()
                .filter(i -> i.getWorkflowName().contains(workflowName))
                .forEach(i -> System.out.println(i.getWorkflowKey() + " " + i.getWorkflowName()));
    }

}
`````

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
`````
