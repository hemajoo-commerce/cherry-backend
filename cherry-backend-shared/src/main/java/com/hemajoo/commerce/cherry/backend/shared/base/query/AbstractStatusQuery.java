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
package com.hemajoo.commerce.cherry.backend.shared.base.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.commons.type.StatusType;
import com.hemajoo.commerce.cherry.backend.shared.base.query.condition.QueryField;
import lombok.Data;
import lombok.NonNull;

/**
 * Represents an abstract <b>query</b> object for the <b>status</b> part of entities.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Data
public abstract class AbstractStatusQuery extends AbstractAuditQuery
{
    /**
     * Field: <b>statusType</b> of an entity.
     */
    @JsonIgnore
    public static final String BASE_STATUS_TYPE = "statusType";

    /**
     * Field: <b>since</b> (inactive date) of an entity.
     */
    @JsonIgnore
    public static final String BASE_SINCE = "since";

    /**
     * Creates a new abstract status search.
     * @param entityType Entity type.
     */
    public AbstractStatusQuery(final @NonNull EntityType entityType)
    {
        super(entityType);

        fields.add(QueryField.builder()
                .withFieldName(BASE_STATUS_TYPE)
                .withFieldType(DataType.ENUM)
                .withClassType(StatusType.class)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(BASE_SINCE)
                .withFieldType(DataType.DATE)
                .build());
    }

    /**
     * Creates a new abstract status search.
     */
    public AbstractStatusQuery()
    {
        fields.add(QueryField.builder()
                .withFieldName(BASE_STATUS_TYPE)
                .withFieldType(DataType.ENUM)
                .withClassType(StatusType.class)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(BASE_SINCE)
                .withFieldType(DataType.DATE)
                .build());
    }
}
