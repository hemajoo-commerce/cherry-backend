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
package com.hemajoo.commerce.cherry.backend.persistence.test.document;

import com.hemajoo.commerce.cherry.backend.commons.entity.EntityIdentity;
import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.EntityComparator;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.ServiceFactoryPerson;
import com.hemajoo.commerce.cherry.backend.persistence.document.converter.DocumentConverter;
import com.hemajoo.commerce.cherry.backend.persistence.document.entity.DocumentServer;
import com.hemajoo.commerce.cherry.backend.persistence.document.randomizer.DocumentRandomizer;
import com.hemajoo.commerce.cherry.backend.persistence.test.base.AbstractPostgresUnitTest;
import com.hemajoo.commerce.cherry.backend.shared.base.entity.EntityException;
import com.hemajoo.commerce.cherry.backend.shared.document.DocumentClient;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentException;
import org.javers.core.diff.Diff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the document converter class.
 * <hr>
 * Unit tests are supposed to be executed with the Maven 'db-test' profile activated.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Testcontainers // Not to be used to keep container alive after the tests!
@DirtiesContext
@SpringBootTest
class DocumentConverterUnitTest extends AbstractPostgresUnitTest
{
    /**
     * Document converter facility service.
     */
    @Autowired
    private DocumentConverter converterDocument;

    /**
     * Person services.
     */
    @Autowired
    private ServiceFactoryPerson servicePerson;


    /**
     * List element count.
     */
    private final int LIST_COUNT = 10;

    @Test
    @DisplayName("Convert a server document to an identity")
    final void testConvertServerToIdentityDocument() throws DocumentException
    {
        DocumentServer document = DocumentRandomizer.generateServerEntity(true);
        EntityIdentity identity = converterDocument.fromServerToIdentity(document);

        assertThat(identity)
                .as("Entity identity should not be null!")
                .isNotNull();

        assertThat(identity.getId())
                .as("Identifiers are expected to be the same!")
                .isEqualTo(document.getId());

        assertThat(identity.getEntityType())
                .as("Identity is supposed to be of type: DOCUMENT")
                .isEqualTo(EntityType.DOCUMENT);
    }

    @Test
    @DisplayName("Convert an identity to a server document")
    final void testConvertIdentityToServerDocument() throws EntityException
    {
        // For an entity identity to be mapped to a server entity, the server entity must exist in the underlying database!
        DocumentServer reference = servicePerson.getDocumentService().save(DocumentRandomizer.generateServerEntity(true));

        EntityIdentity identity = new EntityIdentity(reference.getIdentity());
        DocumentServer server = converterDocument.fromIdentityToServer(identity);

        assertThat(server)
                .as("Server entity should not be null!")
                .isNotNull();

        assertThat(server.getId())
                .as("Identifiers are expected to be the same!")
                .isEqualTo(identity.getId());

        assertThat(server.getEntityType())
                .as("Identity is supposed to be of type: DOCUMENT")
                .isEqualTo(EntityType.DOCUMENT);
    }

    @Test
    @DisplayName("Convert an identity to a server document (that does not exist)")
    @SuppressWarnings("java:S5977")
    final void testConvertIdentityToServerDocumentNotExisting()
    {
        EntityIdentity identity = EntityIdentity.from(EntityType.DOCUMENT, UUID.randomUUID());

        assertThatThrownBy(() -> converterDocument.fromIdentityToServer(identity))
                .isInstanceOf(EntityException.class);
    }

    @Test
    @DisplayName("Convert a client document with a owner to a server document")
    final void testConvertClientToServerDocumentWithOwner() throws EntityException
    {
        // For an entity identity to be mapped to a server entity, the server entity must exist in the underlying database!
        DocumentServer server = servicePerson.getDocumentService().save(DocumentRandomizer.generateServerEntity(false));

        DocumentClient client = DocumentRandomizer.generateClientEntity(true);
        client.setParent(server.getIdentity());

        DocumentServer other = converterDocument.fromClientToServer(client);

        assertThat(other)
                .as("Server document should not be null!")
                .isNotNull();

        assertThat(other.getParent())
                .as("Server document owner should not be null!")
                .isNotNull();
    }

    @Test
    @DisplayName("Convert a client document with a non existing owner to a server document. Should raise an exception!")
    @SuppressWarnings("java:S5977")
    final void testConvertClientToServerDocumentWithNonExistingOwner() throws DocumentException
    {
        DocumentClient client = DocumentRandomizer.generateClientEntity(true);
        client.setParent(new EntityIdentity(EntityType.PERSON,UUID.randomUUID()));

        // If the owner of the client document to convert does not exist, ensure an exception is raised!
        assertThatThrownBy(() -> converterDocument.fromClientToServer(client))
                .isInstanceOf(EntityException.class);
    }

    @Test
    @DisplayName("Convert a list of server documents to a list of client documents")
    final void testConvertServerToClientDocumentList() throws DocumentException
    {
        List<DocumentServer> documents = new ArrayList<>();
        for (int i = 0; i < LIST_COUNT; i++)
        {
            documents.add(DocumentRandomizer.generateServerEntity(true));
        }

        List<DocumentClient> clients = documents.stream()
                .map(document -> converterDocument.fromServerToClient(document)).toList();

        assertThat(clients.size())
                .as("Document server and client list should have the same size!")
                .isEqualTo(documents.size());

        clients.forEach(client -> assertThat(client)
                .as("Client document should not be null!")
                .isNotNull());
    }

    @Test
    @DisplayName("Convert a list of client documents to a list of server documents")
    final void testConvertClientToServerDocumentList() throws EntityException
    {
        List<DocumentClient> clients = new ArrayList<>();
        for (int i = 0; i < LIST_COUNT; i++)
        {
            clients.add(DocumentRandomizer.generateClientEntity(true));
        }

        List<DocumentServer> servers = new ArrayList<>();
        DocumentServer serverDocumentEntity;
        for (DocumentClient client : clients)
        {
            serverDocumentEntity = converterDocument.fromClientToServer(client);
            servers.add(serverDocumentEntity);
        }

        assertThat(servers.size())
                .as("Both lists should have the same size!")
                .isEqualTo(clients.size());

        servers.forEach(document -> assertThat(document)
                .as("Server document should not be null!")
                .isNotNull());
    }

    @Test
    @DisplayName("Convert a list of server documents to a list of entity identities")
    final void testConvertServerDocumentToIdentityList() throws DocumentException
    {
        List<DocumentServer> documents = new ArrayList<>();
        for (int i = 0; i < LIST_COUNT; i++)
        {
            documents.add(DocumentRandomizer.generateServerEntity(true));
        }

        List<EntityIdentity> identities = documents.stream()
                .map(document -> converterDocument.fromServerToIdentity(document)).toList();

        assertThat(identities.size())
                .as("Both lists should have the same size!")
                .isEqualTo(documents.size());

        identities.forEach(identity -> assertThat(identity)
                .as("Identity should not be null!")
                .isNotNull());
    }

    @Test
    @DisplayName("Copy a server document")
    final void testCopyServerDocument() throws EntityException
    {
        DocumentServer document = DocumentRandomizer.generateServerEntity(true);
        DocumentServer copy = DocumentConverter.copy(document);

        assertThat(document)
                .as("Both server documents should be equal!")
                .isEqualTo(copy);

        Diff diff = EntityComparator.getJavers().compare(document, copy);
        assertThat(diff.getChanges().size())
                .as("Both server documents should be equal!")
                .isZero();
    }

    @Test
    @DisplayName("Copy a server document")
    final void testCopyClientDocument() throws EntityException
    {
        DocumentClient document = DocumentRandomizer.generateClientEntity(true);
        DocumentClient copy = DocumentConverter.copy(document);

        assertThat(document)
                .as("Both client documents should be equal!")
                .isEqualTo(copy);

        Diff diff = EntityComparator.getJavers().compare(document, copy);
        assertThat(diff.getChanges().size())
                .as("Both client documents should be equal!")
                .isZero();
    }
}
