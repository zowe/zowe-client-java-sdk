/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zosjobs.types;

/**
 * The possible job status strings (as specified by the z/OSMF documentation). Used in the Jobs APIs for monitoring
 * job status, etc.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class JobStatus {

    /**
     * Job statuses
     */
    public enum Type {
        INPUT, ACTIVE, OUTPUT
    }

    /**
     * The "order" indicates the logical order of job progression within the system. Used to determine if the job will
     * NEVER enter the status that is requested on the API (e.g. if the status is OUTPUT, the job will never be ACTIVE)
     */
    public static final String[] Order = {"INPUT", "ACTIVE", "OUTPUT"};

}
