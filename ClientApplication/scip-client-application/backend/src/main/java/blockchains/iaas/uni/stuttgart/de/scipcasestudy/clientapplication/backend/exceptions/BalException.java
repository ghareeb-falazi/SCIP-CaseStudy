/*******************************************************************************
 * Copyright (c) 2019 Institute for the Architecture of Application System - University of Stuttgart
 * Author: Ghareeb Falazi
 *
 * This program and the accompanying materials are made available under the
 * terms the Apache Software License 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/
package blockchains.iaas.uni.stuttgart.de.scipcasestudy.clientapplication.backend.exceptions;

public class BalException extends RuntimeException {
    private int code;

    public BalException() {
    }

    public BalException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BalException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BalException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return this.code;
    }
}
