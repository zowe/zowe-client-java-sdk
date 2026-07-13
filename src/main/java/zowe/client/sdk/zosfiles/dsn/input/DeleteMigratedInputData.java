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
package zowe.client.sdk.zosfiles.dsn.input;

import java.util.Optional;

/**
 * Parameter container class for the delete migrated dataset function
 *
 * @version 6.0
 */
public class DeleteMigratedInputData {

    /**
     * If true, the function waits for completion of the request.
     * If false (default) the request is queued.
     */
    private final Boolean wait;

    /**
     * If true, the function uses the PURGE=YES on ARCHDEL request.
     * If false (default) the function uses the PURGE=NO on ARCHDEL request.
     */
    private final Boolean purge;

    /**
     * DeleteMigratedInputData constructor
     *
     * @param builder DeleteMigratedInputData.Builder object
     */
    private DeleteMigratedInputData(final Builder builder) {
        this.wait = builder.wait;
        this.purge = builder.purge;
    }

    /**
     * Retrieve wait value
     *
     * @return wait value
     */
    public Optional<Boolean> getWait() {
        return Optional.ofNullable(wait);
    }

    /**
     * Retrieve purge value
     *
     * @return purge value
     */
    public Optional<Boolean> getPurge() {
        return Optional.ofNullable(purge);
    }

    @Override
    public String toString() {
        return "DeleteMigratedInputData{" +
                "wait=" + wait +
                ", purge=" + purge +
                '}';
    }

    /**
     * Builder class for DeleteMigratedInputData
     */
    public static class Builder {

        private Boolean wait;
        private Boolean purge;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set wait value
         *
         * @param wait wait value
         * @return Builder this object
         */
        public Builder wait(final Boolean wait) {
            this.wait = wait;
            return this;
        }

        /**
         * Set purge value
         *
         * @param purge purge value
         * @return Builder this object
         */
        public Builder purge(final Boolean purge) {
            this.purge = purge;
            return this;
        }

        /**
         * Return DeleteMigratedInputData object based on Builder this object
         *
         * @return DeleteMigratedInputData this object
         */
        public DeleteMigratedInputData build() {
            return new DeleteMigratedInputData(this);
        }
    }
}
