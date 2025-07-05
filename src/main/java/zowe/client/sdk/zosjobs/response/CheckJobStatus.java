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
package zowe.client.sdk.zosjobs.response;

/**
 * Class used internally to help determine current job status
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class CheckJobStatus {

    /**
     * Has the desired job status been seen, true or false?
     */
    private final boolean statusFound;

    /**
     * The given Job for checking its status
     */
    private final Job job;

    /**
     * CheckJobStatus constructor
     *
     * @param statusFound holds if job status was found or not
     * @param job         job used for status checking
     * @author Frank Giordano
     */
    public CheckJobStatus(final boolean statusFound, final Job job) {
        this.statusFound = statusFound;
        this.job = job;
    }

    /**
     * Retrieve job specified
     *
     * @return job value
     */
    public Job getJob() {
        return job;
    }

    /**
     * Retrieve is statusFound specified
     *
     * @return boolean true or false
     */
    public boolean isStatusFound() {
        return statusFound;
    }

    /**
     * Return string value representing CheckJobStatus object
     *
     * @return string representation of CheckJobStatus
     */
    @Override
    public String toString() {
        return "CheckJobStatus{" +
                "statusFound=" + statusFound +
                ", job=" + job +
                '}';
    }

}
