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
package com.hemajoo.commerce.cherry.backend.commons.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Represents an <b>entity identity</b> containing minimal information of the identity of an entity.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@ToString
@NoArgsConstructor
public class EntityIdentity implements Identity
{
    /**
     * Entity type.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)

    @Schema(hidden = true)
    @Getter
    @Setter
    private EntityType entityType;

    /**
     * Entity unique identifier.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    @Getter
    @Setter
    private UUID id;

    /**
     * Creates a new entity identity.
     * @param identity Identity.
     */
    public EntityIdentity(final Identity identity)
    {
        if (identity != null)
        {
            this.id = identity.getId();
            this.entityType = identity.getEntityType();
        }
    }

    /**
     * Creates a new entity identity.
     * @param entityType Entity type.
     * @param id Entity identifier.
     */
    public EntityIdentity(final EntityType entityType, final UUID id)
    {
        this.entityType = entityType;
        this.id = id;
    }

    public static EntityIdentity from(final EntityType type, final UUID id)
    {
        return new EntityIdentity(type, id);
    }
}
