package zowe.client.sdk.zosworkflow;

/**
 * Constants for various workflow-related info
 *
 * @author Carlo Maria Proietti
 */
public final class WorkflowConstants {

    /**
     * prefix of the URI of the workflow API
     */
    public static final String RESOURCE_PREFIX = "/zosmf/workflow/rest";

    /**
     * suffix of the URI of the workflow API
     */
    public static final String RESOURCE_SUFFIX = "/workflows";

    /**
     * File delimiter
     */
    public static final String FILE_DELIM = "/";

    /**
     * Use for an exception error message
     */
    public static final String WORKFLOW_NAME_ILLEGAL_MSG = "workflowName is either null or empty";

    /**
     * Use for an exception error message
     */
    public static final String WORKFLOW_DEFINITION_FILE_ILLEGAL_MSG = "workflowDefinitionFile is either null or empty";

    /**
     * Use for an exception error message
     */
    public static final String WORKFLOW_OWNER_ILLEGAL_MSG = "owner is either null or empty";

    /**
     * Use for an exception error message
     */
    public static final String WORKFLOW_SYSTEM_ILLEGAL_MSG = "system is either null or empty";

    /**
     * Use for an exception error message
     */
    public static final String WORKFLOW_VERSION_ILLEGAL_MSG = "version is either null or empty";
}
