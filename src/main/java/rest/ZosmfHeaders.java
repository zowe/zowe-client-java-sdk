/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package rest;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * z/OSMF Headers info
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosmfHeaders {

    /**
     * lrecl header
     */
    public final static String X_IBM_INTRDR_LRECL = "X_IBM_INTRDR_LRECL";
    /**
     * recfm header
     */
    public final static String X_IBM_INTRDR_RECFM = "X_IBM_INTRDR_RECFM";
    /**
     * Jcl symbol header to be completed by API
     */
    public final static String X_IBM_JCL_SYMBOL_PARTIAL = "X_IBM_JCL_SYMBOL_PARTIAL";
    /**
     * Job class header
     */
    public final static String X_IBM_INTRDR_CLASS_A = "X_IBM_INTRDR_CLASS_A";
    /**
     * Fixed recfm header
     */
    public final static String X_IBM_INTRDR_RECFM_F = "X_IBM_INTRDR_RECFM_F";
    /**
     * 80 lrecl header
     */
    public final static String X_IBM_INTRDR_LRECL_80 = "X_IBM_INTRDR_LRECL_80";
    /**
     * 256 lrecl header
     */
    public final static String X_IBM_INTRDR_LRECL_256 = "X_IBM_INTRDR_LRECL_256";
    /**
     * Text type header
     */
    public final static String X_IBM_INTRDR_MODE_TEXT = "X_IBM_INTRDR_MODE_TEXT";
    /**
     * Base header
     */
    public final static String X_IBM_NOTIFICATION_URL = "X_IBM_NOTIFICATION_URL";
    /**
     * Base header
     */
    public final static String X_IBM_ATTRIBUTES_BASE = "X_IBM_ATTRIBUTES_BASE";
    /**
     * If you use this header, delete job API will be asynchronous.
     * this is the default setting, so using this header is not really necessary unless you want to be explicit.
     */
    public final static String X_IBM_JOB_MODIFY_VERSION_1 = "X_IBM_JOB_MODIFY_VERSION_1";
    /**
     * If you use this header, delete job API will be synchronous.
     * but using it may cause problems for some users depending on their maintenance level and configuration.
     */
    public final static String X_IBM_JOB_MODIFY_VERSION_2 = "X_IBM_JOB_MODIFY_VERSION_2";
    /**
     * Security header
     */
    public final static String X_CSRF_ZOSMF_HEADER = "X_CSRF_ZOSMF_HEADER";
    /**
     * Binary transfer header
     */
    public final static String X_IBM_BINARY = "X_IBM_BINARY";
    /**
     * Binary by record header
     */
    public final static String X_IBM_BINARY_BY_RECORD = "X_IBM_BINARY_BY_RECORD";
    /**
     * Text transfer header
     */
    public final static String X_IBM_TEXT = "X_IBM_TEXT";
    /**
     * Encoding value for text headers
     */
    public final static String X_IBM_TEXT_ENCODING = "X_IBM_TEXT_ENCODING";
    /**
     * Octet stream header
     */
    public final static String OCTET_STREAM = "OCTET_STREAM";
    /**
     * Plain text header
     */
    public final static String TEXT_PLAIN = "TEXT_PLAIN";
    /**
     * This header value specifies the maximum number of items to return.
     * To request that all items be returned, set this header to 0. If you omit this header, or specify an incorrect
     * value, up to 1000 items are returned by default.
     */
    public final static String X_IBM_MAX_ITEMS = "X_IBM_MAX_ITEMS";
    // Next few are data set migrated recall headers
    /**
     * Data set migrated recall wait
     */
    public final static String X_IBM_MIGRATED_RECALL_WAIT = "X_IBM_MIGRATED_RECALL_WAIT";
    /**
     * Data set migrated recall no wait
     */
    public final static String X_IBM_MIGRATED_RECALL_NO_WAIT = "X_IBM_MIGRATED_RECALL_NO_WAIT";
    /**
     * Data set migrated recall error
     */
    public final static String X_IBM_MIGRATED_RECALL_ERROR = "X_IBM_MIGRATED_RECALL_ERROR";
    /**
     * Header to check ETag on read
     * Request returns HTTP 304 if not modified
     */
    public final static String IF_NONE_MATCH = "IF_NONE_MATCH";
    /**
     * Header to check ETag on write
     * Request returns HTTP 412 if not matched
     */
    public final static String IF_MATCH = "IF_MATCH";
    /**
     * Header to set response timeout defaults to 30 seconds if not modified
     */
    public final static String X_IBM_RESPONSE_TIMEOUT = "X_IBM_RESPONSE_TIMEOUT";
    /**
     * Header to force return of ETag in response regardless of file size
     * By default Etag is returned only for files smaller than a system determined value (which is at least 8mb)
     */
    public final static String X_IBM_RETURN_ETAG = "X_IBM_RETURN_ETAG";
    /**
     * Header that specifies GZIP compression is supported
     * Recent versions of z/OSMF issue a warning if this header is omitted
     */
    public final static String ACCEPT_ENCODING = "ACCEPT_ENCODING";

    /**
     * Map of headers for quick access
     */
    public static final ListMultimap<String, String> HEADERS =
            new ImmutableListMultimap.Builder<String, String>()
                    .put("X_IBM_INTRDR_LRECL", "X-IBM-Intrdr-Lrecl")
                    .put("X_IBM_INTRDR_RECFM", "X-IBM-Intrdr-Recfm")
                    .put("X_IBM_JCL_SYMBOL_PARTIAL", "X-IBM-JCL-Symbol-")
                    .putAll("X_IBM_INTRDR_CLASS_A", "X-IBM-Intrdr-Class", "A")
                    .putAll("X_IBM_INTRDR_RECFM_F", "X-IBM-Intrdr-Recfm", "F")
                    .putAll("X_IBM_INTRDR_LRECL_80", "X-IBM-Intrdr-Lrecl", "80")
                    .putAll("X_IBM_INTRDR_LRECL_256", "X-IBM-Intrdr-Lrecl", "256")
                    .putAll("X_IBM_INTRDR_MODE_TEXT", "X-IBM-Intrdr-Mode", "TEXT")
                    .putAll("X_IBM_NOTIFICATION_URL", "X-IBM-Notification-URL", "")
                    .putAll("X_IBM_ATTRIBUTES_BASE", "X-IBM-Attributes", "base")
                    .putAll("X_IBM_ATTRIBUTES_VOL", "X-IBM-Attributes", "vol")
                    .putAll("X_IBM_JOB_MODIFY_VERSION_1", "X-IBM-Job-Modify-Version", "1.0")
                    .putAll("X_IBM_JOB_MODIFY_VERSION_2", "X-IBM-Job-Modify-Version", "2.0")
                    // the value does not matter
                    .putAll("X_CSRF_ZOSMF_HEADER", "X-CSRF-ZOSMF-HEADER", "true")
                    .putAll("X_IBM_BINARY", "X-IBM-Data-Type", "binary")
                    .putAll("X_IBM_BINARY_BY_RECORD", "X-IBM-Data-Type", "record")
                    .putAll("X_IBM_TEXT", "X-IBM-Data-Type", "text")
                    .put("X_IBM_TEXT_ENCODING", ";fileEncoding=")
                    .putAll("OCTET_STREAM", "Content-Type", "application/octet-stream")
                    .putAll("X_IBM_TEXT", "X-IBM-Data-Type", "text")
                    .putAll("TEXT_PLAIN", "Content-Type", "text/plain")
                    .putAll("X_IBM_MAX_ITEMS", "X-IBM-Max-Items", "0")
                    .putAll("X_IBM_MIGRATED_RECALL_WAIT", "X-IBM-Migrated-Recall", "wait")
                    .putAll("X_IBM_MIGRATED_RECALL_NO_WAIT", "X-IBM-Migrated-Recall", "nowait")
                    .putAll("X_IBM_MIGRATED_RECALL_ERROR", "X-IBM-Migrated-Recall", "error")
                    .put("IF_NONE_MATCH", "If-None-Match")
                    .put("IF_MATCH", "If-Match")
                    .put("X_IBM_RESPONSE_TIMEOUT", "X-IBM-Response-Timeout")
                    .putAll("X_IBM_RETURN_ETAG", "X-IBM-Return-Etag", "true")
                    .putAll("ACCEPT_ENCODING", "Accept-Encoding", "gzip")
                    .build();

}
