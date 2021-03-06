/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2009-2010 Sun Microsystems, Inc.
 * Portions copyright 2012 ForgeRock AS.
 */

package org.forgerock.opendj.ldap.requests;

import java.util.List;

import org.forgerock.opendj.ldap.DecodeException;
import org.forgerock.opendj.ldap.DecodeOptions;
import org.forgerock.opendj.ldap.controls.Control;
import org.forgerock.opendj.ldap.controls.ControlDecoder;

/**
 * The Abandon operation allows a client to request that the server abandon an
 * uncompleted operation.
 * <p>
 * Abandon, Bind, Unbind, and StartTLS operations cannot be abandoned.
 */
public interface AbandonRequest extends Request {

    @Override
    AbandonRequest addControl(Control control);

    @Override
    <C extends Control> C getControl(ControlDecoder<C> decoder, DecodeOptions options)
            throws DecodeException;

    @Override
    List<Control> getControls();

    /**
     * Returns the request ID of the request to be abandoned.
     *
     * @return The request ID of the request to be abandoned.
     */
    int getRequestID();

    /**
     * Sets the request ID of the request to be abandoned.
     *
     * @param id
     *            The request ID of the request to be abandoned.
     * @return This abandon request.
     * @throws UnsupportedOperationException
     *             If this abandon request does not permit the request ID to be
     *             set.
     */
    AbandonRequest setRequestID(int id);

}
