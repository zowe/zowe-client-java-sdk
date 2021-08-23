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
package zosjobs.response;

/**
 * Class used internally to help determine current job status
 *
 * @author Frank Giordano
 * @verion 1.0.0
 */
public class CheckJobStatus {

    private boolean statusFound;
    private Job job;

    public CheckJobStatus(boolean statusFound, Job job) {
        this.statusFound = statusFound;
        this.job = job;
    }

    public boolean isStatusFound() {
        return statusFound;
    }

    public Job getJob() {
        return job;
    }

}
