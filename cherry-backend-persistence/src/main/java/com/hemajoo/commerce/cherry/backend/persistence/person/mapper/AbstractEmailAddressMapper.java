/*
 * (C) Copyright Resse Christophe 2021 - All Rights Reserved
 * -----------------------------------------------------------------------------------------------
 * All information contained herein is, and remains the property of
 * Resse Christophe. and its suppliers, if any. The intellectual and technical
 * concepts contained herein are proprietary to Resse C. and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents
 * in process, and are protected by trade secret or copyright law.
 *
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from
 * Resse Christophe (christophe.resse@gmail.com).
 * -----------------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.cherry.backend.persistence.person.mapper;

import com.hemajoo.commerce.cherry.backend.commons.entity.EntityIdentity;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.AbstractEntityMapper;
import com.hemajoo.commerce.cherry.backend.persistence.base.mapper.CycleAvoidingMappingContext;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.EmailAddressServer;
import com.hemajoo.commerce.cherry.backend.shared.base.entity.EntityException;
import com.hemajoo.commerce.cherry.backend.shared.person.address.email.EmailAddressClient;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.persistence.EntityManager;

/**
 * Mapper interface to convert between instances of client and server email addresses.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Mapper(uses = { AbstractEntityMapper.class })
public abstract class AbstractEmailAddressMapper
{
    /**
     * Instance to this bean mapper.
     */
    public static final AbstractEmailAddressMapper INSTANCE = Mappers.getMapper(AbstractEmailAddressMapper.class);

    /**
     * Maps from a server email address entity to an entity identity.
     * @param entity Server email address entity.
     * @param context Context object.
     * @return Entity identity.
     */
    public abstract EntityIdentity fromServerToIdentity(EmailAddressServer entity, @Context CycleAvoidingMappingContext context);

    /**
     * Maps from a server email address entity to a client email address entity.
     * @param entity Server email address entity.
     * @param context Context object.
     * @param entityManager Entity manager.
     * @return Client email address entity.
     * @throws EntityException Thrown to indicate an error occurred while trying to convert an email address entity.
     */
    public abstract EmailAddressServer fromClientToServer(EmailAddressClient entity, @Context CycleAvoidingMappingContext context, @Context EntityManager entityManager) throws EntityException;

    /**
     * Maps from a server email address entity to a client email address entity.
     * @param entity Server email address entity.
     * @param context Context object.
     * @return Client email address entity.
     */
    public abstract EmailAddressClient fromServerToClient(EmailAddressServer entity, @Context CycleAvoidingMappingContext context);

    /**
     * Copy a server email address entity.
     * @param entity Server email address entity.
     * @param context Context object.
     * @return Copy of the server email address entity.
     * @throws EntityException Thrown to indicate an error occurred while trying to copy an email address entity.
     */
    public abstract EmailAddressServer copy(EmailAddressServer entity, @Context CycleAvoidingMappingContext context) throws EntityException;

    /**
     * Copy a client email address entity.
     * @param entity Client email address entity.
     * @param context Context object.
     * @return Copy of the client email address entity.
     * @throws EntityException Thrown to indicate an error occurred while trying to copy an email address entity.
     */
    public abstract EmailAddressClient copy(EmailAddressClient entity, @Context CycleAvoidingMappingContext context) throws EntityException;
}
