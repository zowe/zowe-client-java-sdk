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

import java.util.Collections;
import java.util.List;

/**
 * Workflow variable-definition information returned for a workflow definition.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-retrieve-workflow-definition">z/OSMF REST API</a>
 *
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowVariableDefinition {

    /**
     * Name of the variable.
     */
    private final String name;

    /**
     * Variable scope, which is either instance or global.
     */
    private final String scope;

    /**
     * A brief description of the variable.
     */
    private final String abstractInfo;

    /**
     * Name of the logical grouping to which the variable belongs.
     */
    private final String category;

    /**
     * The choice value for the variable.
     */
    private final List<String> choice;

    /**
     * Maximum number of decimal places that can be specified.
     */
    private final Integer decimalPlaces;

    /**
     * Default value of the variable.
     */
    private final String defaultValue;

    /**
     * Description of the variable.
     */
    private final String description;

    /**
     * Indicates whether the variable is displayed to the user in the Workflows task.
     */
    private final Boolean exposeToUser;

    /**
     * Maximum length of the variable value.
     */
    private final Integer maxLength;

    /**
     * Maximum value of the variable.
     */
    private final String maxValue;

    /**
     * Minimum length of the variable value.
     */
    private final Integer minLength;

    /**
     * Minimum value of the variable.
     */
    private final String minValue;

    /**
     * Indicates whether the user is prompted to specify a value for the variable during the create workflow process.
     */
    private final Boolean promptAtCreate;

    /**
     * Standard regular expression that constrains the variable value.
     */
    private final String regularExpression;

    /**
     * Indicates whether a value must be specified for the variable during the create workflow process.
     */
    private final Boolean requiredAtCreate;

    /**
     * Type of variable.
     */
    private final String type;

    /**
     * Specifies the validation type for the variable.
     */
    private final String validationType;

    /**
     * Indicates whether the variable value must come from the provided choices.
     */
    private final Boolean valueMustBeChoice;

    /**
     * Indicates whether the variable is displayed to the Workflows task user (either public or private).
     */
    private final String visibility;

    /**
     * WorkflowVariableDefinition Jackson constructor.
     *
     * @param name              variable name
     * @param scope             variable scope
     * @param abstractInfo      brief description of the variable
     * @param category          logical grouping of the variable
     * @param choice            choice values for the variable
     * @param decimalPlaces     maximum number of decimal places
     * @param defaultValue      default value of the variable
     * @param description       description of the variable
     * @param exposeToUser      indicates whether the variable is displayed to the user
     * @param maxLength         maximum length of the variable value
     * @param maxValue          maximum value of the variable
     * @param minLength         minimum length of the variable value
     * @param minValue          minimum value of the variable
     * @param promptAtCreate    indicates whether the user is prompted during create
     * @param regularExpression regular expression that constrains the variable value
     * @param requiredAtCreate  indicates whether a value is required during create
     * @param type              type of variable
     * @param validationType    validation type for the variable
     * @param valueMustBeChoice indicates whether the value must come from the provided choices
     * @param visibility        indicates whether the variable is public or private
     */
    @JsonCreator
    public WorkflowVariableDefinition(
            @JsonProperty("name") final String name,
            @JsonProperty("scope") final String scope,
            @JsonProperty("abstract") final String abstractInfo,
            @JsonProperty("category") final String category,
            @JsonProperty("choice") final List<String> choice,
            @JsonProperty("decimalPlaces") final Integer decimalPlaces,
            @JsonProperty("default") final String defaultValue,
            @JsonProperty("description") final String description,
            @JsonProperty("exposeToUser") final Boolean exposeToUser,
            @JsonProperty("maxLength") final Integer maxLength,
            @JsonProperty("maxValue") final String maxValue,
            @JsonProperty("minLength") final Integer minLength,
            @JsonProperty("minValue") final String minValue,
            @JsonProperty("promptAtCreate") final Boolean promptAtCreate,
            @JsonProperty("regularExpression") final String regularExpression,
            @JsonProperty("requiredAtCreate") final Boolean requiredAtCreate,
            @JsonProperty("type") final String type,
            @JsonProperty("validationType") final String validationType,
            @JsonProperty("valueMustBeChoice") final Boolean valueMustBeChoice,
            @JsonProperty("visibility") final String visibility) {
        this.name = orEmpty(name);
        this.scope = orEmpty(scope);
        this.abstractInfo = orEmpty(abstractInfo);
        this.category = orEmpty(category);
        this.choice = orEmpty(choice);
        this.decimalPlaces = decimalPlaces;
        this.defaultValue = orEmpty(defaultValue);
        this.description = orEmpty(description);
        this.exposeToUser = exposeToUser;
        this.maxLength = maxLength;
        this.maxValue = orEmpty(maxValue);
        this.minLength = minLength;
        this.minValue = orEmpty(minValue);
        this.promptAtCreate = promptAtCreate;
        this.regularExpression = orEmpty(regularExpression);
        this.requiredAtCreate = requiredAtCreate;
        this.type = orEmpty(type);
        this.validationType = orEmpty(validationType);
        this.valueMustBeChoice = valueMustBeChoice;
        this.visibility = orEmpty(visibility);
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    private static <T> List<T> orEmpty(final List<T> value) {
        return value == null ? Collections.emptyList() : value;
    }

    /**
     * Retrieve name value.
     *
     * @return name value
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve scope value.
     *
     * @return scope value
     */
    public String getScope() {
        return scope;
    }

    /**
     * Retrieve abstract value.
     *
     * @return abstract value
     */
    public String getAbstractInfo() {
        return abstractInfo;
    }

    /**
     * Retrieve category value.
     *
     * @return category value
     */
    public String getCategory() {
        return category;
    }

    /**
     * Retrieve choice value.
     *
     * @return choice value
     */
    public List<String> getChoice() {
        return choice;
    }

    /**
     * Retrieve decimalPlaces value.
     *
     * @return decimalPlaces value
     */
    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    /**
     * Retrieve default value.
     *
     * @return default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Retrieve description value.
     *
     * @return description value
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieve exposeToUser value.
     *
     * @return exposeToUser value
     */
    public Boolean getExposeToUser() {
        return exposeToUser;
    }

    /**
     * Retrieve maxLength value.
     *
     * @return maxLength value
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Retrieve maxValue value.
     *
     * @return maxValue value
     */
    public String getMaxValue() {
        return maxValue;
    }

    /**
     * Retrieve minLength value.
     *
     * @return minLength value
     */
    public Integer getMinLength() {
        return minLength;
    }

    /**
     * Retrieve minValue value.
     *
     * @return minValue value
     */
    public String getMinValue() {
        return minValue;
    }

    /**
     * Retrieve promptAtCreate value.
     *
     * @return promptAtCreate value
     */
    public Boolean getPromptAtCreate() {
        return promptAtCreate;
    }

    /**
     * Retrieve regularExpression value.
     *
     * @return regularExpression value
     */
    public String getRegularExpression() {
        return regularExpression;
    }

    /**
     * Retrieve requiredAtCreate value.
     *
     * @return requiredAtCreate value
     */
    public Boolean getRequiredAtCreate() {
        return requiredAtCreate;
    }

    /**
     * Retrieve type value.
     *
     * @return type value
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve validationType value.
     *
     * @return validationType value
     */
    public String getValidationType() {
        return validationType;
    }

    /**
     * Retrieve valueMustBeChoice value.
     *
     * @return valueMustBeChoice value
     */
    public Boolean getValueMustBeChoice() {
        return valueMustBeChoice;
    }

    /**
     * Retrieve visibility value.
     *
     * @return visibility value
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Return a string value representing a WorkflowVariableDefinition object.
     *
     * @return string representation of WorkflowVariableDefinition
     */
    @Override
    public String toString() {
        return "WorkflowVariableDefinition{" +
                "name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                ", abstractInfo='" + abstractInfo + '\'' +
                ", category='" + category + '\'' +
                ", choice=" + choice +
                ", decimalPlaces=" + decimalPlaces +
                ", defaultValue='" + defaultValue + '\'' +
                ", description='" + description + '\'' +
                ", exposeToUser=" + exposeToUser +
                ", maxLength=" + maxLength +
                ", maxValue='" + maxValue + '\'' +
                ", minLength=" + minLength +
                ", minValue='" + minValue + '\'' +
                ", promptAtCreate=" + promptAtCreate +
                ", regularExpression='" + regularExpression + '\'' +
                ", requiredAtCreate=" + requiredAtCreate +
                ", type='" + type + '\'' +
                ", validationType='" + validationType + '\'' +
                ", valueMustBeChoice=" + valueMustBeChoice +
                ", visibility='" + visibility + '\'' +
                '}';
    }

}
