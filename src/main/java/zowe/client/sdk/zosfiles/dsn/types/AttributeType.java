/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.types;

/**
 * Attribute enum for querying a dataset(s) and member(s) and how its returned data will be retrieved with what properties.
 * <p>
 * Member request that only member names be returned. If you omit this header, it is set to "member".
 * BASE returns all properties of a dataset and its values.
 * VOL returns volume and dataset name properties and its values only.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public enum AttributeType {

    /**
     * A request that only member names be returned.
     * If you omit this header, it is set to "member".
     */
    MEMBER,
    /**
     * Setting the X-IBM-Attribute to base returns all the basic attributes for the data sets or members being queried.
     * These attributes are commonly found in the ISPF List Data set panel.
     * The base key is mutually exclusive with member.
     */
    BASE,
    /**
     * Return volume and dataset name properties and its values only.
     */
    VOL

}
