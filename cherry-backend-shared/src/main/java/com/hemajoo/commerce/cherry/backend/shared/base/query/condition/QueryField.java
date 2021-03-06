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
package com.hemajoo.commerce.cherry.backend.shared.base.query.condition;

import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.shared.base.query.DataType;
import lombok.*;

/**
 * Represents a query <b>field</b>.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@NoArgsConstructor
public class QueryField
{
    /**
     * Field name.
     */
    @Getter
    @Setter
    private String fieldName;

    /**
     * Field data type.
     */
    @Getter
    @Setter
    private DataType fieldType;

    /**
     * Field class type.
     */
    @Getter
    @Setter
    private Class<?> fieldClassType;

    /**
     * Entity type the field references.
     */
    @Getter
    @Setter
    private EntityType entityType;

    @Builder(setterPrefix = "with")
    public QueryField(final @NonNull String fieldName, final @NonNull DataType fieldType, final Class<?> classType, final EntityType entityType)
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldClassType = classType;
        this.entityType = entityType;
    }
}
