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
package com.hemajoo.commerce.cherry.backend.persistence.document.converter;

import com.hemajoo.commerce.cherry.backend.commons.entity.EntityIdentity;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.AbstractEntityMapper;
import com.hemajoo.commerce.cherry.backend.persistence.base.mapper.CycleAvoidingMappingContext;
import com.hemajoo.commerce.cherry.backend.persistence.document.entity.DocumentServer;
import com.hemajoo.commerce.cherry.backend.persistence.document.mapper.AbstractDocumentMapper;
import com.hemajoo.commerce.cherry.backend.shared.document.ClientDocument;
import com.hemajoo.commerce.cherry.backend.shared.document.DocumentException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Component to convert between instances of client and server documents.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Component
public class DocumentConverter
{
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Converts from a server document entity to an entity identity.
     * @param server Server document entity.
     * @return Entity identity.
     */
    public EntityIdentity fromServerToIdentity(DocumentServer server)
    {
        return AbstractDocumentMapper.INSTANCE.fromServerToIdentity(server, new CycleAvoidingMappingContext());
    }

    /**
     * Converts from an entity identity to a server document entity.
     * @param identity Entity identity.
     * @return Server document entity.
     * @throws DocumentException Thrown to indicate an error occurred when trying to convert a document.
     */
    public DocumentServer fromIdentityToServer(EntityIdentity identity) throws DocumentException
    {
        try
        {
            return AbstractEntityMapper.INSTANCE.map(identity,entityManager);
        }
        catch (Exception e)
        {
            throw new DocumentException(e);
        }
    }

    /**
     * Converts from a client document entity to a server document entity.
     * @param client Client document entity.
     * @return Server document entity.
     * @throws DocumentException Thrown to indicate an error occurred when trying to convert a document.
     */
    public DocumentServer fromClientToServer(ClientDocument client) throws DocumentException
    {
        try
        {
            return AbstractDocumentMapper.INSTANCE.fromClientToServer(client, new CycleAvoidingMappingContext(), entityManager);
        }
        catch (Exception e)
        {
            throw new DocumentException(e);
        }
    }

    /**
     * Converts from a server document entity to a client document entity.
     * @param server Server document entity.
     * @return Client document entity.
     */
    public ClientDocument fromServerToClient(DocumentServer server)
    {
        return AbstractDocumentMapper.INSTANCE.fromServerToClient(server, new CycleAvoidingMappingContext());
    }

    /**
     * Copy a server document entity.
     * @param server Server document entity.
     * @return Copied server document entity.
     * @throws DocumentException Thrown to indicate an error occurred when trying to copy a document.
     */
    public static DocumentServer copy(DocumentServer server) throws DocumentException
    {
        try
        {
            return AbstractDocumentMapper.INSTANCE.copy(server, new CycleAvoidingMappingContext());
        }
        catch (Exception e)
        {
            throw new DocumentException(e);
        }
    }

    /**
     * Copy a client document entity.
     * @param client Client document entity.
     * @return Copied client document entity.
     * @throws DocumentException Thrown to indicate an error occurred when trying to copy a document.
     */
    public static ClientDocument copy(ClientDocument client) throws DocumentException
    {
        try
        {
            return AbstractDocumentMapper.INSTANCE.copy(client, new CycleAvoidingMappingContext());
        }
        catch (Exception e)
        {
            throw new DocumentException(e);
        }
    }
}
