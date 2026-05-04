package zowe.client.sdk.utility;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Utility class, contains validator methods used in workflow package.
 *
 * @author Carlo Maria Proietti
 */
public class WorkflowUtils {
    private static final Set<String> VALID_PROPERTY_NAMES = new HashSet<>(Arrays.asList(
            "workflowDefinitionFileSystem",
            "variableInputFile",
            "variables",
            "resolveGlobalConflictByUsing",
            "workflowArchiveSAFID",
            "comments",
            "assignToOwner",
            "accessType",
            "accountInfo",
            "jobStatement",
            "deleteCompletedJobs",
            "jobsOutputDirectory",
            "autoDeleteOnCompletion",
            "targetSystemuid",
            "targetSystempwd"
    ));

    /**
     * Validates if the provided key matches any of the z/OSMF workflow request field names.
     *
     * @param key The string to validate
     * @throws IllegalArgumentException if the key is null, blank, or not a valid field name.
     */
    public static void validateOptionalPropertyKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Property key cannot be null or blank.");
        }

        if (!VALID_PROPERTY_NAMES.contains(key)) {
            throw new IllegalArgumentException(
                    String.format("Invalid workflow property key: '%s'. Please refer to the z/OSMF documentation for valid field names.", key)
            );
        }
    }
}
