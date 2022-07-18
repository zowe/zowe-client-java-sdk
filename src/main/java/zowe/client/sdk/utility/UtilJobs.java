/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import zowe.client.sdk.zosjobs.input.ModifyJobParams;

/**
 * Utility Class for GetJobs related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class UtilJobs {

    /**
     * Check the validity of a ModifyJobParams object
     *
     * @param params ModifyJobParams object
     * @author Frank Giordano
     */
    public static void checkModifyJobParameters(ModifyJobParams params) {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkIllegalParameter(params.getJobId().isEmpty(), "job id not specified");
        Util.checkIllegalParameter(params.getJobId().get().isEmpty(), "job id not specified");
        Util.checkIllegalParameter(params.getJobName().isEmpty(), "job name not specified");
        Util.checkIllegalParameter(params.getJobName().get().isEmpty(), "job name not specified");
    }

    /**
     * Formulate an exception to throw base on http error code
     *
     * @param params    ModifyJobParams object
     * @param exception incoming exception to inspect and use
     * @throws Exception error base on http error code
     * @author Frank Giordano
     */
    public static void throwHttpException(ModifyJobParams params, Exception exception) throws Exception {
        UtilJobs.checkModifyJobParameters(params);
        String errorMsg = exception.getMessage();
        if (errorMsg.contains("400")) {
            throw new Exception(errorMsg + " JobId " + params.getJobId().orElse("n/a") + " may not exist.");
        }
        throw new Exception(errorMsg);
    }

}
