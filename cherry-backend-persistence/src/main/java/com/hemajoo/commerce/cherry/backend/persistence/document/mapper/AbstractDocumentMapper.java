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
package com.hemajoo.commerce.cherry.backend.persistence.document.mapper;

import com.hemajoo.commerce.cherry.backend.commons.entity.EntityIdentity;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.AbstractEntityMapper;
import com.hemajoo.commerce.cherry.backend.persistence.base.mapper.CycleAvoidingMappingContext;
import com.hemajoo.commerce.cherry.backend.persistence.document.entity.DocumentServer;
import com.hemajoo.commerce.cherry.backend.shared.base.entity.EntityException;
import com.hemajoo.commerce.cherry.backend.shared.document.DocumentClient;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.persistence.EntityManager;

/**
 * Abstract document <b>mapper</b> (used by a document <b>converter</b>) to convert between client and server document instances.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Mapper(uses = { AbstractEntityMapper.class })
public abstract class AbstractDocumentMapper
{
    /**
     * Document mapper instance.
     */
    public static final AbstractDocumentMapper INSTANCE = Mappers.getMapper(AbstractDocumentMapper.class);

    /**
     * Maps from a server document entity to an entity identity.
     * @param entity Server document entity.
     * @param context Context object.
     * @return Entity identity.
     */
    public abstract EntityIdentity fromServerToIdentity(DocumentServer entity, @Context CycleAvoidingMappingContext context);

    /**
     * Maps from a server document entity to a client document entity.
     * @param document Server document entity.
     * @param context Context object.
     * @param entityManager Entity manager.
     * @return Client document entity.
     * @throws EntityException Thrown to indicate an error occurred while trying to convert a document entity.
     */
    public abstract DocumentServer fromClientToServer(DocumentClient document, @Context CycleAvoidingMappingContext context, @Context EntityManager entityManager) throws EntityException;

    /**
     * Maps from a server document entity to a client document entity.
     * @param document Server document entity.
     * @param context Context object.
     * @return Client document entity.
     */
    public abstract DocumentClient fromServerToClient(DocumentServer document, @Context CycleAvoidingMappingContext context);

    /**
     * Copy a server document entity.
     * @param entity Server document entity.
     * @param context Context object.
     * @return Copy of the server document entity.
     * @throws EntityException Thrown to indicate an error occurred while trying to copy a document entity.
     */
    public abstract DocumentServer copy(DocumentServer entity, @Context CycleAvoidingMappingContext context) throws EntityException;

    /**
     * Copy a client document entity.
     * @param entity Client document entity.
     * @param context Context object.
     * @return Copy of the client document entity.
     * @throws EntityException Thrown to indicate an error occurred while trying to copy a document entity.
     */
    public abstract DocumentClient copy(DocumentClient entity, @Context CycleAvoidingMappingContext context) throws EntityException;
}
