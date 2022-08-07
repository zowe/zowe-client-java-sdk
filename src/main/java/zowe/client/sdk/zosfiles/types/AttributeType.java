/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.types;

/**
 * Attribute enum for querying a dataset and how its returned data will be retrieved with what properties.
 * <p>
 * BASE return all properties of a dataset and its values.
 * VOL return volume and dataset name properties and its values only.
 *
 * @author Frank Giordano
 */
public enum AttributeType {
    BASE, VOL
}
