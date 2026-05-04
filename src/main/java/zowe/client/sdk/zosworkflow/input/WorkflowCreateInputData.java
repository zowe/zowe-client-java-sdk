package zowe.client.sdk.zosworkflow.input;

import java.util.Map;
import java.util.Optional;

/**
 * Parameters for create (workflowCreate) operation
 *
 * @author Carlo Maria Proietti
 */

public class WorkflowCreateInputData {

    private String workflowName;
    private String workflowDefinitionFile;
    private String system;
    private String owner;
    private String version;
    private Map<String, Object> optionalProperties;

    // Private constructor so it can only be instantiated via the Builder
    private WorkflowCreateInputData(Builder builder) {
        this.workflowName = builder.workflowName;
        this.workflowDefinitionFile = builder.workflowDefinitionFile;
        this.system = builder.system;
        this.owner = builder.owner;
        this.version = builder.version;
        this.optionalProperties = builder.optionalProperties;
    }

    private Optional<String> wrap(String value) {
        return (value == null || value.isBlank()) ? Optional.empty() : Optional.of(value);
    }

    // Custom Getters
    public Optional<String> getWorkflowName() {
        return wrap(workflowName);
    }

    public Optional<String> getWorkflowDefinitionFile() {
        return wrap(workflowDefinitionFile);
    }

    public Optional<String> getSystem() {
        return wrap(system);
    }

    public Optional<String> getOwner() {
        return wrap(owner);
    }

    public Optional<String> getVersion() {
        return wrap(version);
    }

    public Map<String, Object> getOptionalProperties() {
        return optionalProperties;
    }

    /**
     * Static method to start the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder Class
     */
    public static class Builder {
        private String workflowName;
        private String workflowDefinitionFile;
        private String system;
        private String owner;
        private String version;
        private Map<String, Object> optionalProperties;

        public Builder workflowName(String workflowName) {
            this.workflowName = workflowName;
            return this;
        }

        public Builder workflowDefinitionFile(String workflowDefinitionFile) {
            this.workflowDefinitionFile = workflowDefinitionFile;
            return this;
        }

        public Builder system(String system) {
            this.system = system;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder optionalProperties(Map<String, Object> optionalProperties) {
            this.optionalProperties = optionalProperties;
            return this;
        }

        public WorkflowCreateInputData build() {
            return new WorkflowCreateInputData(this);
        }
    }
}
