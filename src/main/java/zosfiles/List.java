/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles;

import java.util.*;

import core.ZOSConnection;
import utility.Util;
import zosfiles.response.Dataset;

import java.util.ArrayList;

public class List {

    public static  void listDsn(ZOSConnection connection, String namePattern, Boolean listAttributes, String response ) {
        listAttributes = listAttributes == null ? false : listAttributes;
        Util.checkConnection(connection);

        java.util.List<Dataset> datasets = new ArrayList<>();

    }
}
