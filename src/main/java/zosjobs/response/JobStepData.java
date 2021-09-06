/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.response;

/**
 * Step info on a job interface
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class JobStepData {

    /**
     * smfid
     */
    private String smfid;

    /**
     * Active
     */
    private String active;

    /**
     * Job relevant step
     */
    private Integer stepNumber;

    /**
     * Job relevant proc
     */
    private String procStepName;

    /**
     * Step for which job dd exists
     */
    private String stepName;

    /**
     * Program EXEC=
     */
    private String programName;

    // TODO

}
