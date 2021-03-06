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
package com.hemajoo.commerce.cherry.backend.persistence.document.randomizer;

import com.hemajoo.commerce.cherry.backend.persistence.base.randomizer.AbstractEntityRandomizer;
import com.hemajoo.commerce.cherry.backend.persistence.document.entity.DocumentServer;
import com.hemajoo.commerce.cherry.backend.shared.document.DocumentClient;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentContentException;
import com.hemajoo.commerce.cherry.backend.shared.document.type.DocumentType;
import lombok.experimental.UtilityClass;
import org.ressec.avocado.core.random.EnumRandomGenerator;

import java.util.UUID;

/**
 * Randomizer used to randomly generate <b>documents</b>.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@UtilityClass
public final class DocumentRandomizer extends AbstractEntityRandomizer
{
    /**
     * Document type enumeration generator.
     */
    private static final EnumRandomGenerator DOCUMENT_TYPE_GENERATOR = new EnumRandomGenerator(DocumentType.class).exclude(DocumentType.UNKNOWN);

    /**
     * Test media type enumeration generator.
     */
    private static final EnumRandomGenerator TEST_MEDIA_TYPE_GENERATOR = new EnumRandomGenerator(TestMediaType.class);

    /**
     * Generates a new random persistent document.
     * @param withRandomId Do we need to generate a random identifier? False by default.
     * @return Random document.
     * @throws DocumentContentException Raised in case an error occurred while trying to set the document content (media file)!
     */
    public static DocumentServer generateServerEntity(final boolean withRandomId) throws DocumentContentException
    {
        var entity = new DocumentServer();
        AbstractEntityRandomizer.populateBaseFields(entity);

        if (withRandomId)
        {
            entity.setId(UUID.randomUUID());
        }

        entity.setName(FAKER.name().title());
        entity.setContent(((TestMediaType) TEST_MEDIA_TYPE_GENERATOR.gen()).getPath());
        entity.setTags(FAKER.elderScrolls().creature());
        entity.setDocumentType((DocumentType) DOCUMENT_TYPE_GENERATOR.gen());

        return entity;
    }

    /**
     * Generates a new random client document.
     * @param withRandomId Do we need to generate a random identifier? False by default.
     * @return Random document.
     * @throws DocumentContentException Raised in case an error occurred while trying to set the document content (media file)!
     */
    public static DocumentClient generateClientEntity(final boolean withRandomId) throws DocumentContentException
    {
        var entity = new DocumentClient();
        AbstractEntityRandomizer.populateBaseFields(entity);

        if (withRandomId)
        {
            entity.setId(UUID.randomUUID());
        }

        entity.setName(FAKER.name().title());
        entity.setContent(((TestMediaType) TEST_MEDIA_TYPE_GENERATOR.gen()).getPath());
        entity.setTags(FAKER.elderScrolls().creature());
        entity.setDocumentType((DocumentType) DOCUMENT_TYPE_GENERATOR.gen());

        return entity;
    }
}
