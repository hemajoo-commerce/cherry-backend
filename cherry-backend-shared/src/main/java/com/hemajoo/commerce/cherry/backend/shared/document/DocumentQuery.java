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
package com.hemajoo.commerce.cherry.backend.shared.document;

import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.shared.base.query.BaseEntityQuery;
import com.hemajoo.commerce.cherry.backend.shared.base.query.DataType;
import com.hemajoo.commerce.cherry.backend.shared.base.query.condition.QueryField;

/**
 * Represents a <b>query</b> object for issuing queries on documents.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class DocumentQuery extends BaseEntityQuery
{
    public static final String DOCUMENT_EXTENSION = "extension";
    public static final String DOCUMENT_FILENAME = "filename";
    public static final String DOCUMENT_CONTENT_PATH = "contentPath";
    public static final String DOCUMENT_CONTENT_LENGTH = "contentLength";
    public static final String DOCUMENT_TYPE = "documentType";
    public static final String DOCUMENT_MIMETYPE = "mimeType";
    public static final String DOCUMENT_TAGS = "tags";
    public static final String DOCUMENT_OWNER = "owner";

    /**
     * Creates a new <b>query</b> instance for the documents.
     */
    public DocumentQuery()
    {
        super(EntityType.DOCUMENT);

        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_EXTENSION)
                .withFieldType(DataType.STRING)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_FILENAME)
                .withFieldType(DataType.STRING)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_CONTENT_PATH)
                .withFieldType(DataType.STRING)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_TYPE)
                .withFieldType(DataType.ENUM)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_CONTENT_LENGTH)
                .withFieldType(DataType.LONG)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_MIMETYPE)
                .withFieldType(DataType.STRING)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_TAGS)
                .withFieldType(DataType.STRING)
                .build());
        fields.add(QueryField.builder()
                .withFieldName(DOCUMENT_OWNER)
                .withFieldType(DataType.UUID)
                .build());
    }
}
