/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Template step-info type returned for a workflow properties request. A template step runs a program,
 * such as a batch job, REXX exec, or UNIX shell script.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowTemplateStepInfo extends WorkflowStepInfo {

    /**
     * Template that is used to run a program or batch job (inline or external file).
     */
    private final String template;

    /**
     * Indicates whether the template contains variable substitution.
     */
    private final Boolean templateSub;

    /**
     * Type of executable program, such as JCL, TSO-REXX, or TSO-UNIX-shell.
     */
    private final String submitAs;

    /**
     * Maximum record length, in bytes, for the input data for the job.
     */
    private final Integer maxLrecl;

    /**
     * For a step that submits a job, the jobInfo object that contains details about the job.
     */
    private final WorkflowJobInfo jobInfo;

    /**
     * Name of the output file that is produced by the step.
     */
    private final String output;

    /**
     * Indicates whether the output file name contains variable substitution.
     */
    private final Boolean outputSub;

    /**
     * For a step that creates a variable, the prefix that identifies a string as a variable.
     */
    private final String outputVariablesPrefix;

    /**
     * Name of the logon procedure that is used to log into the TSO/E address space.
     */
    private final String procName;

    /**
     * Region size for the TSO/E address space.
     */
    private final String regionSize;

    /**
     * Return code that was returned when the job was submitted.
     */
    private final String returnCode;

    /**
     * Data set name (fully qualified, no quotation marks) that contains the saved JCL.
     */
    private final String saveAsDataset;

    /**
     * Indicates whether the data set name contains variable substitution.
     */
    private final Boolean saveAsDatasetSub;

    /**
     * UNIX file name (absolute name) that contains the saved JCL.
     */
    private final String saveAsUnixFile;

    /**
     * Indicates whether the UNIX file name contains variable substitution.
     */
    private final Boolean saveAsUnixFileSub;

    /**
     * Input parameters that can be set by the step owner.
     */
    private final String scriptParameters;

    /**
     * Regular expression that is returned for a successful program execution.
     */
    private final String successPattern;

    /**
     * Optional regular expression that can be returned for program execution failures.
     */
    private final List<String> failedPattern;

    /**
     * Maximum amount of time that the program can run before it is ended by a timeout condition.
     */
    private final String timeout;

    /**
     * Detailed instructions on what the user must do to perform the step.
     */
    private final String instructions;

    /**
     * Indicates whether the step instructions contain variables.
     */
    private final Boolean instructionsSub;

    /**
     * An array of variable-reference objects.
     */
    private final List<WorkflowVariableReference> variableReferences;

    /**
     * WorkflowTemplateStepInfo Jackson constructor.
     *
     * @param name                  step name
     * @param title                 step title
     * @param description           step description
     * @param state                 state of the step
     * @param stepNumber            step number
     * @param optional              indicates whether the step is optional
     * @param autoEnable            indicates whether the step can be performed automatically
     * @param prereqStep            names of prerequisite steps
     * @param userDefined           indicates whether the step was added manually
     * @param runAsUser             user ID under which the step is performed
     * @param runAsUserDynamic      indicates whether the runAsUser ID can change
     * @param isRestStep            indicates whether this step is a REST API step
     * @param owner                 user ID of the step owner
     * @param assignees             step assignees
     * @param skills                skills required to perform the step
     * @param weight                relative difficulty of the step
     * @param hasCalledWorkflow     indicates whether this step calls another workflow
     * @param isConditionStep       indicates whether this step is a conditional step
     * @param steps                 nested step-info objects
     * @param template              template used to run a program or batch job
     * @param templateSub           indicates whether the template contains variable substitution
     * @param submitAs              type of executable program
     * @param maxLrecl              maximum record length for the job input data
     * @param jobInfo               details about the submitted job
     * @param output                name of the output file produced by the step
     * @param outputSub             indicates whether the output file name contains variable substitution
     * @param outputVariablesPrefix prefix that identifies a string as a variable
     * @param procName              logon procedure name for the TSO/E address space
     * @param regionSize            region size for the TSO/E address space
     * @param returnCode            return code returned when the job was submitted
     * @param saveAsDataset         data set name that contains the saved JCL
     * @param saveAsDatasetSub      indicates whether the data set name contains variable substitution
     * @param saveAsUnixFile        UNIX file name that contains the saved JCL
     * @param saveAsUnixFileSub     indicates whether the UNIX file name contains variable substitution
     * @param scriptParameters      input parameters that can be set by the step owner
     * @param successPattern        regular expression returned for a successful program execution
     * @param failedPattern         regular expression returned for program execution failures
     * @param timeout               maximum amount of time the program can run before timeout
     * @param instructions          detailed instructions to perform the step
     * @param instructionsSub       indicates whether the step instructions contain variables
     * @param variableReferences    array of variable-reference objects
     */
    @JsonCreator
    public WorkflowTemplateStepInfo(
            @JsonProperty("name") final String name,
            @JsonProperty("title") final String title,
            @JsonProperty("description") final String description,
            @JsonProperty("state") final String state,
            @JsonProperty("stepNumber") final String stepNumber,
            @JsonProperty("optional") final Boolean optional,
            @JsonProperty("autoEnable") final Boolean autoEnable,
            @JsonProperty("prereqStep") final List<String> prereqStep,
            @JsonProperty("userDefined") final Boolean userDefined,
            @JsonProperty("runAsUser") final String runAsUser,
            @JsonProperty("runAsUserDynamic") final Boolean runAsUserDynamic,
            @JsonProperty("isRestStep") final Boolean isRestStep,
            @JsonProperty("owner") final String owner,
            @JsonProperty("assignees") final String assignees,
            @JsonProperty("skills") final String skills,
            @JsonProperty("weight") final String weight,
            @JsonProperty("hasCalledWorkflow") final Boolean hasCalledWorkflow,
            @JsonProperty("isConditionStep") final Boolean isConditionStep,
            @JsonProperty("steps") final List<WorkflowStepInfo> steps,
            @JsonProperty("template") final String template,
            @JsonProperty("templateSub") final Boolean templateSub,
            @JsonProperty("submitAs") final String submitAs,
            @JsonProperty("maxLrecl") final Integer maxLrecl,
            @JsonProperty("jobInfo") final WorkflowJobInfo jobInfo,
            @JsonProperty("output") final String output,
            @JsonProperty("outputSub") final Boolean outputSub,
            @JsonProperty("outputVariablesPrefix") final String outputVariablesPrefix,
            @JsonProperty("procName") final String procName,
            @JsonProperty("regionSize") final String regionSize,
            @JsonProperty("returnCode") final String returnCode,
            @JsonProperty("saveAsDataset") final String saveAsDataset,
            @JsonProperty("saveAsDatasetSub") final Boolean saveAsDatasetSub,
            @JsonProperty("saveAsUnixFile") final String saveAsUnixFile,
            @JsonProperty("saveAsUnixFileSub") final Boolean saveAsUnixFileSub,
            @JsonProperty("scriptParameters") final String scriptParameters,
            @JsonProperty("successPattern") final String successPattern,
            @JsonProperty("failedPattern") final List<String> failedPattern,
            @JsonProperty("timeout") final String timeout,
            @JsonProperty("instructions") final String instructions,
            @JsonProperty("instructionsSub") final Boolean instructionsSub,
            @JsonProperty("variable-references") final List<WorkflowVariableReference> variableReferences) {
        super(name, title, description, state, stepNumber, optional, autoEnable, prereqStep, userDefined,
                runAsUser, runAsUserDynamic, isRestStep, owner, assignees, skills, weight, hasCalledWorkflow,
                isConditionStep, steps);
        this.template = orEmpty(template);
        this.templateSub = templateSub;
        this.submitAs = orEmpty(submitAs);
        this.maxLrecl = maxLrecl;
        this.jobInfo = jobInfo;
        this.output = orEmpty(output);
        this.outputSub = outputSub;
        this.outputVariablesPrefix = orEmpty(outputVariablesPrefix);
        this.procName = orEmpty(procName);
        this.regionSize = orEmpty(regionSize);
        this.returnCode = orEmpty(returnCode);
        this.saveAsDataset = orEmpty(saveAsDataset);
        this.saveAsDatasetSub = saveAsDatasetSub;
        this.saveAsUnixFile = orEmpty(saveAsUnixFile);
        this.saveAsUnixFileSub = saveAsUnixFileSub;
        this.scriptParameters = orEmpty(scriptParameters);
        this.successPattern = orEmpty(successPattern);
        this.failedPattern = orEmpty(failedPattern);
        this.timeout = orEmpty(timeout);
        this.instructions = orEmpty(instructions);
        this.instructionsSub = instructionsSub;
        this.variableReferences = orEmpty(variableReferences);
    }

    /**
     * Retrieve template value.
     *
     * @return template value
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Retrieve templateSub value.
     *
     * @return templateSub value
     */
    public Boolean getTemplateSub() {
        return templateSub;
    }

    /**
     * Retrieve submitAs value.
     *
     * @return submitAs value
     */
    public String getSubmitAs() {
        return submitAs;
    }

    /**
     * Retrieve maxLrecl value.
     *
     * @return maxLrecl value
     */
    public Integer getMaxLrecl() {
        return maxLrecl;
    }

    /**
     * Retrieve jobInfo value.
     *
     * @return jobInfo value
     */
    public WorkflowJobInfo getJobInfo() {
        return jobInfo;
    }

    /**
     * Retrieve output value.
     *
     * @return output value
     */
    public String getOutput() {
        return output;
    }

    /**
     * Retrieve outputSub value.
     *
     * @return outputSub value
     */
    public Boolean getOutputSub() {
        return outputSub;
    }

    /**
     * Retrieve outputVariablesPrefix value.
     *
     * @return outputVariablesPrefix value
     */
    public String getOutputVariablesPrefix() {
        return outputVariablesPrefix;
    }

    /**
     * Retrieve procName value.
     *
     * @return procName value
     */
    public String getProcName() {
        return procName;
    }

    /**
     * Retrieve regionSize value.
     *
     * @return regionSize value
     */
    public String getRegionSize() {
        return regionSize;
    }

    /**
     * Retrieve returnCode value.
     *
     * @return returnCode value
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * Retrieve saveAsDataset value.
     *
     * @return saveAsDataset value
     */
    public String getSaveAsDataset() {
        return saveAsDataset;
    }

    /**
     * Retrieve saveAsDatasetSub value.
     *
     * @return saveAsDatasetSub value
     */
    public Boolean getSaveAsDatasetSub() {
        return saveAsDatasetSub;
    }

    /**
     * Retrieve saveAsUnixFile value.
     *
     * @return saveAsUnixFile value
     */
    public String getSaveAsUnixFile() {
        return saveAsUnixFile;
    }

    /**
     * Retrieve saveAsUnixFileSub value.
     *
     * @return saveAsUnixFileSub value
     */
    public Boolean getSaveAsUnixFileSub() {
        return saveAsUnixFileSub;
    }

    /**
     * Retrieve scriptParameters value.
     *
     * @return scriptParameters value
     */
    public String getScriptParameters() {
        return scriptParameters;
    }

    /**
     * Retrieve successPattern value.
     *
     * @return successPattern value
     */
    public String getSuccessPattern() {
        return successPattern;
    }

    /**
     * Retrieve failedPattern value.
     *
     * @return failedPattern value
     */
    public List<String> getFailedPattern() {
        return failedPattern;
    }

    /**
     * Retrieve timeout value.
     *
     * @return timeout value
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Retrieve instructions value.
     *
     * @return instructions value
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Retrieve instructionsSub value.
     *
     * @return instructionsSub value
     */
    public Boolean getInstructionsSub() {
        return instructionsSub;
    }

    /**
     * Retrieve variableReferences value.
     *
     * @return variableReferences value
     */
    public List<WorkflowVariableReference> getVariableReferences() {
        return variableReferences;
    }

    /**
     * Return string value representing WorkflowTemplateStepInfo object.
     *
     * @return string representation of WorkflowTemplateStepInfo
     */
    @Override
    public String toString() {
        return "WorkflowTemplateStepInfo{" +
                "name='" + getName() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", stepNumber='" + getStepNumber() + '\'' +
                ", state='" + getState() + '\'' +
                ", submitAs='" + submitAs + '\'' +
                ", template='" + template + '\'' +
                ", procName='" + procName + '\'' +
                ", jobInfo=" + jobInfo +
                ", variableReferences=" + variableReferences +
                '}';
    }

}
