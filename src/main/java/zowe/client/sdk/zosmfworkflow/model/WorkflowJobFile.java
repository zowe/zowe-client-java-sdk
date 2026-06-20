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

/**
 * Workflow job files information returned within a step jobInfo object.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowJobFile {

    /**
     * Data set number (key).
     */
    private final Integer id;

    /**
     * DDNAME for the data set creation.
     */
    private final String ddname;

    /**
     * Number of bytes on spool that is consumed by the spool file.
     */
    private final Integer byteCount;

    /**
     * Number of records in the spool file.
     */
    private final Integer recordCount;

    /**
     * Class that is assigned to the spool file.
     */
    private final String classs;

    /**
     * Step name for the step that created this data set.
     */
    private final String stepname;

    /**
     * Procedure name for the step that created this data set.
     */
    private final String procstep;

    /**
     * WorkflowJobFile Jackson constructor.
     *
     * @param id          data set number
     * @param ddname      DDNAME for the data set creation
     * @param byteCount   number of bytes on spool consumed by the spool file
     * @param recordCount number of records in the spool file
     * @param classs      class assigned to the spool file
     * @param stepname    step name that created this data set
     * @param procstep    procedure name that created this data set
     */
    @JsonCreator
    public WorkflowJobFile(
            @JsonProperty("id") final Integer id,
            @JsonProperty("ddname") final String ddname,
            @JsonProperty("byte-count") final Integer byteCount,
            @JsonProperty("record-count") final Integer recordCount,
            @JsonProperty("class") final String classs,
            @JsonProperty("stepname") final String stepname,
            @JsonProperty("procstep") final String procstep) {
        this.id = id;
        this.ddname = orEmpty(ddname);
        this.byteCount = byteCount;
        this.recordCount = recordCount;
        this.classs = orEmpty(classs);
        this.stepname = orEmpty(stepname);
        this.procstep = orEmpty(procstep);
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    /**
     * Retrieve id value.
     *
     * @return id value
     */
    public Integer getId() {
        return id;
    }

    /**
     * Retrieve ddname value.
     *
     * @return ddname value
     */
    public String getDdname() {
        return ddname;
    }

    /**
     * Retrieve byteCount value.
     *
     * @return byteCount value
     */
    public Integer getByteCount() {
        return byteCount;
    }

    /**
     * Retrieve recordCount value.
     *
     * @return recordCount value
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * Retrieve class value.
     *
     * @return class value
     */
    public String getClasss() {
        return classs;
    }

    /**
     * Retrieve stepname value.
     *
     * @return stepname value
     */
    public String getStepname() {
        return stepname;
    }

    /**
     * Retrieve procstep value.
     *
     * @return procstep value
     */
    public String getProcstep() {
        return procstep;
    }

    /**
     * Return string value representing WorkflowJobFile object.
     *
     * @return string representation of WorkflowJobFile
     */
    @Override
    public String toString() {
        return "WorkflowJobFile{" +
                "id=" + id +
                ", ddname='" + ddname + '\'' +
                ", byteCount=" + byteCount +
                ", recordCount=" + recordCount +
                ", classs='" + classs + '\'' +
                ", stepname='" + stepname + '\'' +
                ", procstep='" + procstep + '\'' +
                '}';
    }

}
