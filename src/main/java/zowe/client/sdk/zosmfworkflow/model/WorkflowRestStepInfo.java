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
 * REST step-info type returned for a workflow properties request. A REST step issues a REST API
 * request, such as a GET or PUT request.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowRestStepInfo extends WorkflowStepInfo {

    /**
     * The actual HTTP status code that is received from the REST API request.
     */
    private final String actualStatusCode;

    /**
     * The expected HTTP status code from the REST API request.
     */
    private final String expectedStatusCode;

    /**
     * Hostname or IP address of the site to which the REST request is directed.
     */
    private final String hostname;

    /**
     * Indicates whether the hostname contains variable substitution.
     */
    private final Boolean hostnameSub;

    /**
     * HTTP method that is used for issuing the REST API request.
     */
    private final String httpMethod;

    /**
     * Port number that is associated with the REST request.
     */
    private final String port;

    /**
     * Indicates whether the port number contains variable substitution.
     */
    private final Boolean portSub;

    /**
     * Query parameters for the REST request.
     */
    private final String queryParameters;

    /**
     * Indicates whether the query parameters contain variable substitution.
     */
    private final Boolean queryParametersSub;

    /**
     * Request body for the REST request.
     */
    private final String requestBody;

    /**
     * Indicates whether the request body contains variable substitution.
     */
    private final Boolean requestBodySub;

    /**
     * Scheme name that is used for the REST request.
     */
    private final String schemeName;

    /**
     * Indicates whether the scheme name contains variable substitution.
     */
    private final Boolean schemeNameSub;

    /**
     * URI path to use for the REST request.
     */
    private final String uriPath;

    /**
     * Indicates whether the URI path contains variable substitution.
     */
    private final Boolean uriPathSub;

    /**
     * WorkflowRestStepInfo Jackson constructor.
     *
     * @param name               step name
     * @param title              step title
     * @param description        step description
     * @param state              state of the step
     * @param stepNumber         step number
     * @param optional           indicates whether the step is optional
     * @param autoEnable         indicates whether the step can be performed automatically
     * @param prereqStep         names of prerequisite steps
     * @param userDefined        indicates whether the step was added manually
     * @param runAsUser          user ID under which the step is performed
     * @param runAsUserDynamic   indicates whether the runAsUser ID can change
     * @param isRestStep         indicates whether this step is a REST API step
     * @param owner              user ID of the step owner
     * @param assignees          step assignees
     * @param skills             skills required to perform the step
     * @param weight             relative difficulty of the step
     * @param hasCalledWorkflow  indicates whether this step calls another workflow
     * @param isConditionStep    indicates whether this step is a conditional step
     * @param steps              nested step-info objects
     * @param actualStatusCode   actual HTTP status code received from the REST API request
     * @param expectedStatusCode expected HTTP status code from the REST API request
     * @param hostname           hostname or IP address of the REST request site
     * @param hostnameSub        indicates whether the hostname contains variable substitution
     * @param httpMethod         HTTP method used for the REST API request
     * @param port               port number associated with the REST request
     * @param portSub            indicates whether the port number contains variable substitution
     * @param queryParameters    query parameters for the REST request
     * @param queryParametersSub indicates whether the query parameters contain variable substitution
     * @param requestBody        request body for the REST request
     * @param requestBodySub     indicates whether the request body contains variable substitution
     * @param schemeName         scheme name used for the REST request
     * @param schemeNameSub      indicates whether the scheme name contains variable substitution
     * @param uriPath            URI path to use for the REST request
     * @param uriPathSub         indicates whether the URI path contains variable substitution
     */
    @JsonCreator
    public WorkflowRestStepInfo(
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
            @JsonProperty("actualStatusCode") final String actualStatusCode,
            @JsonProperty("expectedStatusCode") final String expectedStatusCode,
            @JsonProperty("hostname") final String hostname,
            @JsonProperty("hostnameSub") final Boolean hostnameSub,
            @JsonProperty("httpMethod") final String httpMethod,
            @JsonProperty("port") final String port,
            @JsonProperty("portSub") final Boolean portSub,
            @JsonProperty("queryParameters") final String queryParameters,
            @JsonProperty("queryParametersSub") final Boolean queryParametersSub,
            @JsonProperty("requestBody") final String requestBody,
            @JsonProperty("requestBodySub") final Boolean requestBodySub,
            @JsonProperty("schemeName") final String schemeName,
            @JsonProperty("schemeNameSub") final Boolean schemeNameSub,
            @JsonProperty("uriPath") final String uriPath,
            @JsonProperty("uriPathSub") final Boolean uriPathSub) {
        super(name, title, description, state, stepNumber, optional, autoEnable, prereqStep, userDefined,
                runAsUser, runAsUserDynamic, isRestStep, owner, assignees, skills, weight, hasCalledWorkflow,
                isConditionStep, steps);
        this.actualStatusCode = orEmpty(actualStatusCode);
        this.expectedStatusCode = orEmpty(expectedStatusCode);
        this.hostname = orEmpty(hostname);
        this.hostnameSub = hostnameSub;
        this.httpMethod = orEmpty(httpMethod);
        this.port = orEmpty(port);
        this.portSub = portSub;
        this.queryParameters = orEmpty(queryParameters);
        this.queryParametersSub = queryParametersSub;
        this.requestBody = orEmpty(requestBody);
        this.requestBodySub = requestBodySub;
        this.schemeName = orEmpty(schemeName);
        this.schemeNameSub = schemeNameSub;
        this.uriPath = orEmpty(uriPath);
        this.uriPathSub = uriPathSub;
    }

    /**
     * Retrieve actualStatusCode value.
     *
     * @return actualStatusCode value
     */
    public String getActualStatusCode() {
        return actualStatusCode;
    }

    /**
     * Retrieve the expectedStatusCode value.
     *
     * @return expectedStatusCode value
     */
    public String getExpectedStatusCode() {
        return expectedStatusCode;
    }

    /**
     * Retrieve hostname value.
     *
     * @return hostname value
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Retrieve hostnameSub value.
     *
     * @return hostnameSub value
     */
    public Boolean getHostnameSub() {
        return hostnameSub;
    }

    /**
     * Retrieve httpMethod value.
     *
     * @return httpMethod value
     */
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * Retrieve port value.
     *
     * @return port value
     */
    public String getPort() {
        return port;
    }

    /**
     * Retrieve portSub value.
     *
     * @return portSub value
     */
    public Boolean getPortSub() {
        return portSub;
    }

    /**
     * Retrieve queryParameters value.
     *
     * @return queryParameters value
     */
    public String getQueryParameters() {
        return queryParameters;
    }

    /**
     * Retrieve queryParametersSub value.
     *
     * @return queryParametersSub value
     */
    public Boolean getQueryParametersSub() {
        return queryParametersSub;
    }

    /**
     * Retrieve requestBody value.
     *
     * @return requestBody value
     */
    public String getRequestBody() {
        return requestBody;
    }

    /**
     * Retrieve requestBodySub value.
     *
     * @return requestBodySub value
     */
    public Boolean getRequestBodySub() {
        return requestBodySub;
    }

    /**
     * Retrieve schemeName value.
     *
     * @return schemeName value
     */
    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Retrieve schemeNameSub value.
     *
     * @return schemeNameSub value
     */
    public Boolean getSchemeNameSub() {
        return schemeNameSub;
    }

    /**
     * Retrieve uriPath value.
     *
     * @return uriPath value
     */
    public String getUriPath() {
        return uriPath;
    }

    /**
     * Retrieve uriPathSub value.
     *
     * @return uriPathSub value
     */
    public Boolean getUriPathSub() {
        return uriPathSub;
    }

    /**
     * Return a string value representing a WorkflowRestStepInfo object.
     *
     * @return string representation of WorkflowRestStepInfo
     */
    @Override
    public String toString() {
        return "WorkflowRestStepInfo{" +
                "name='" + getName() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", stepNumber='" + getStepNumber() + '\'' +
                ", state='" + getState() + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", hostname='" + hostname + '\'' +
                ", uriPath='" + uriPath + '\'' +
                ", actualStatusCode='" + actualStatusCode + '\'' +
                ", expectedStatusCode='" + expectedStatusCode + '\'' +
                '}';
    }

}
