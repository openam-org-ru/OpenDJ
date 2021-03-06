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
 * Copyright 2008 Sun Microsystems, Inc.
 * Portions Copyright 2014-2016 ForgeRock AS.
 */
package org.forgerock.opendj.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.forgerock.i18n.LocalizableMessage;
import org.forgerock.opendj.config.client.ClientConstraintHandler;
import org.forgerock.opendj.config.client.ManagedObject;
import org.forgerock.opendj.config.client.ManagementContext;
import org.forgerock.opendj.config.conditions.Condition;
import org.forgerock.opendj.config.server.ConfigException;
import org.forgerock.opendj.config.server.ServerConstraintHandler;
import org.forgerock.opendj.config.server.ServerManagedObject;
import org.forgerock.opendj.ldap.LdapException;

/**
 * A generic constraint which comprises of an underlying condition and a
 * description. The condition must evaluate to <code>true</code> in order for a
 * new managed object to be created or modified.
 */
public class GenericConstraint extends Constraint {

    /** The client-side constraint handler. */
    private final class ClientHandler extends ClientConstraintHandler {

        /** Private constructor. */
        private ClientHandler() {
            // No implementation required.
        }

        @Override
        public boolean isAddAcceptable(ManagementContext context, ManagedObject<?> managedObject,
            Collection<LocalizableMessage> unacceptableReasons) throws LdapException {
            if (!condition.evaluate(context, managedObject)) {
                unacceptableReasons.add(getSynopsis());
                return false;
            } else {
                return true;
            }
        }

        @Override
        public boolean isModifyAcceptable(ManagementContext context, ManagedObject<?> managedObject,
            Collection<LocalizableMessage> unacceptableReasons) throws LdapException {
            if (!condition.evaluate(context, managedObject)) {
                unacceptableReasons.add(getSynopsis());
                return false;
            } else {
                return true;
            }
        }
    }

    /** The server-side constraint handler. */
    private final class ServerHandler extends ServerConstraintHandler {

        /** Private constructor. */
        private ServerHandler() {
            // No implementation required.
        }

        @Override
        public boolean isUsable(ServerManagedObject<?> managedObject,
            Collection<LocalizableMessage> unacceptableReasons) throws ConfigException {
            if (!condition.evaluate(managedObject)) {
                unacceptableReasons.add(getSynopsis());
                return false;
            } else {
                return true;
            }
        }
    }

    /** The client-side constraint handler. */
    private final ClientConstraintHandler clientHandler = new ClientHandler();

    /** The condition associated with this constraint. */
    private final Condition condition;

    /** The managed object definition associated with this constraint. */
    private final AbstractManagedObjectDefinition<?, ?> definition;

    /** The constraint ID. */
    private final int id;

    /** The server-side constraint handler. */
    private final ServerConstraintHandler serverHandler = new ServerHandler();

    /**
     * Creates a new generic constraint.
     *
     * @param definition
     *            The managed object definition associated with this constraint.
     * @param id
     *            The constraint ID.
     * @param condition
     *            The condition associated with this constraint.
     */
    public GenericConstraint(AbstractManagedObjectDefinition<?, ?> definition, int id, Condition condition) {
        this.definition = definition;
        this.id = id;
        this.condition = condition;
    }

    @Override
    public Collection<ClientConstraintHandler> getClientConstraintHandlers() {
        return Collections.singleton(clientHandler);
    }

    @Override
    public Collection<ServerConstraintHandler> getServerConstraintHandlers() {
        return Collections.singleton(serverHandler);
    }

    /**
     * Gets the synopsis of this constraint in the default locale.
     *
     * @return Returns the synopsis of this constraint in the default locale.
     */
    public final LocalizableMessage getSynopsis() {
        return getSynopsis(Locale.getDefault());
    }

    /**
     * Gets the synopsis of this constraint in the specified locale.
     *
     * @param locale
     *            The locale.
     * @return Returns the synopsis of this constraint in the specified locale.
     */
    public final LocalizableMessage getSynopsis(Locale locale) {
        ManagedObjectDefinitionI18NResource resource = ManagedObjectDefinitionI18NResource.getInstance();
        String property = "constraint." + id + ".synopsis";
        return resource.getMessage(definition, property, locale);
    }

    @Override
    protected void initialize() throws Exception {
        condition.initialize(definition);
    }

}
