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
package com.hemajoo.commerce.cherry.backend.persistence.document.entity;

import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.ServerEntity;
import com.hemajoo.commerce.cherry.backend.shared.base.entity.EntityException;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentContentException;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentException;
import com.hemajoo.commerce.cherry.backend.shared.document.type.DocumentType;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.ressec.avocado.core.helper.FileHelper;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Represents a server document entity.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "DOCUMENT")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DocumentServer extends ServerEntity implements IDocumentServer
{
    /**
     * Document type.
     */
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "DOCUMENT_TYPE", length = 50)
    private DocumentType documentType;

    /**
     * Document file extension.
     */
    @Getter
    @Setter
    private String extension;

    /**
     * Document file name.
     */
    @Getter
    @Setter
    @Column(name = "FILENAME")
    private String filename;

    /**
     * Multipart file.
     */
    @DiffIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Transient
    private transient MultipartFile multiPartFile; // Only stored until the content of the file is loaded into the content store.

    /**
     * Base file name.
     */
    @DiffIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Getter
    @Transient
    private transient String baseFilename; // Only stored until the content of the file is loaded into the content store.

    /**
     * File content identifier.
     * <br>
     * UUID of the file in the content store.
     */
    @Getter
    @Setter
    @ContentId
    private String contentId;

    /**
     * File content length.
     */
    @Getter
    @Setter
    @ContentLength
    private long contentLength;

    /**
     * File MIME type.
     */
    @Getter
    @Setter
    @MimeType
    private String mimeType = "text/plain";

    /**
     * File path (in the content store).
     */
    @Getter
    @Setter
    private String contentPath;

    /**
     * Document content.
     */
    @DiffIgnore
    @Getter
    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private transient InputStream content;

    /**
     * Creates a new document.
     */
    public DocumentServer()
    {
        super(EntityType.DOCUMENT);
    }

    /**
     * Creates a new document.
     * @param owner Document owner.
     * @param documentType Document type.
     * @throws DocumentException Thrown to indicate an error occurred when trying to add a document to another document.
     */
    public DocumentServer(final @NonNull ServerEntity owner, final @NonNull DocumentType documentType) throws DocumentException
    {
        super(EntityType.DOCUMENT);

        setActive();
        this.documentType = documentType;

        try
        {
            setParent(owner);
        }
        catch (EntityException e)
        {
            throw new DocumentException(e);
        }

        owner.addDocument(this);
    }

    /**
     * Creates a new document given its path name.
     * @param owner Document owner.
     * @param documentType Document type.
     * @param filename File name.
     * @throws DocumentException Thrown in case an error occurred while initializing the document.
     */
    public DocumentServer(final @NonNull ServerEntity owner, final @NonNull DocumentType documentType, final @NonNull String filename) throws DocumentException
    {
        this(owner, documentType);

        this.filename = filename;
        setName(FilenameUtils.getName(FilenameUtils.removeExtension(filename)));
        setExtension(FilenameUtils.getExtension(this.filename));

        detectMimeType(filename);
    }

    /**
     * Creates a new document given an associated media file.
     * @param owner Document owner.
     * @param documentType Document type.
     * @param file File.
     * @throws DocumentException Thrown in case an error occurred while initializing the document.
     */
    public DocumentServer(final @NonNull ServerEntity owner, final @NonNull DocumentType documentType, final @NonNull File file) throws DocumentException
    {
        this(owner, documentType);

        this.filename = file.getName();
        setName(FilenameUtils.getName(FilenameUtils.removeExtension(filename)));
        setExtension(FilenameUtils.getExtension(this.filename));

        detectMimeType(file);
    }

    /**
     * Creates a new document given its path name.
     * @param owner Document owner.
     * @param documentType Document type.
     * @param multiPartFile Multipart file.
     * @throws DocumentException Thrown in case an error occurred while initializing the document.
     */
    public DocumentServer(final @NonNull ServerEntity owner, final @NonNull DocumentType documentType, final @NonNull MultipartFile multiPartFile) throws DocumentException
    {
        this(owner, documentType);

        this.filename = multiPartFile.getOriginalFilename();
        this.multiPartFile = multiPartFile;
        setName(FilenameUtils.getName(FilenameUtils.removeExtension(filename)));
        setExtension(FilenameUtils.getExtension(filename));
        detectMimeType(multiPartFile);
    }

    /**
     * Sets the document content.
     * @param filename File name of the media file to store as the document content.
     * @throws DocumentContentException Raised when an error occurred while trying to set the document content.
     */
    public final void setContent(final @NonNull String filename) throws DocumentContentException
    {
        try
        {
            detectMimeType(filename);
            this.baseFilename = filename;
            this.filename = FilenameUtils.getName(filename);
            this.extension = FilenameUtils.getExtension(filename);
            content = new FileInputStream(FileHelper.getFile(filename));
            this.contentLength = content.available();
        }
        catch (Exception e)
        {
            throw new DocumentContentException(e);
        }
    }

    /**
     * Sets the document content.
     * @param file File representing the media file to store as the document content.
     * @throws DocumentContentException Raised when an error occurred while trying to set the document content.
     */
    public final void setContent(final @NonNull File file) throws DocumentContentException
    {
        try
        {
            detectMimeType(file.getName());
            this.filename = FilenameUtils.getName(file.getName());
            this.extension = FilenameUtils.getExtension(file.getName());
            content = new FileInputStream(FileHelper.getFile(file.getName()));
        }
        catch (Exception e)
        {
            throw new DocumentContentException(e);
        }
    }

    /**
     * Sets the document content.
     * @param inputStream Input stream of the file representing the media file to store as the document content.
     */
    public final void setContent(final InputStream inputStream)
    {
        this.content = inputStream;
    }

    /**
     * Detects the media file {@code Mime} type.
     * @param filename File name.
     * @throws DocumentContentException Thrown in case an error occurred while processing the media file.
     */
    private void detectMimeType(final @NonNull String filename) throws DocumentContentException
    {
        try
        {
            mimeType = new Tika().detect(FileHelper.getFile(filename));
        }
        catch (Exception e)
        {
            throw new DocumentContentException(e.getMessage());
        }
    }

    /**
     * Detects the media file {@code Mime} type.
     * @param file File.
     * @throws DocumentContentException Thrown in case an error occurred while processing the media file.
     */
    private void detectMimeType(final @NonNull File file) throws DocumentContentException
    {
        try
        {
            mimeType = new Tika().detect(file);
        }
        catch (Exception e)
        {
            throw new DocumentContentException(e.getMessage());
        }
    }

    /**
     * Detects the media file {@code Mime} type.
     * @param inputStream Input Stream.
     * @throws DocumentContentException Thrown in case an error occurred while processing the media file.
     */
    private void detectMimeType(final @NonNull InputStream inputStream) throws DocumentContentException
    {
        try
        {
            mimeType = new Tika().detect(inputStream);
        }
        catch (Exception e)
        {
            throw new DocumentContentException(e.getMessage());
        }
    }

    /**
     * Detects the media file {@code Mime} type.
     * @param multiPartFile Multipart file.
     * @throws DocumentContentException Thrown in case an error occurred while processing the media file.
     */
    private void detectMimeType(final @NonNull MultipartFile multiPartFile) throws DocumentContentException
    {
        try
        {
            mimeType = new Tika().detect(multiPartFile.getInputStream());
        }
        catch (Exception e)
        {
            throw new DocumentContentException(e.getMessage());
        }
    }

    /**
     * Returns the document output full path name (path + name).
     * @param outputPath Output path and file name.
     * @return Document full path and file name.
     */
    @DiffIgnore
    public final String getOutputFilename(final @NonNull String outputPath)
    {
        String end = getName() + "." + extension;

        return outputPath.endsWith(File.separator)
                ? (outputPath + end)
                : (outputPath + File.separator + end);
    }
//
//    @Override
//    public void setOwner(ServerEntity owner)
//    {
//        this.owner = owner;
//    }
}
