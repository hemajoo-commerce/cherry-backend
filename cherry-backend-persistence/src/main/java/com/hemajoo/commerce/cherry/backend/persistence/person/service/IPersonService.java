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
package com.hemajoo.commerce.cherry.backend.persistence.person.service;

import com.hemajoo.commerce.cherry.backend.persistence.base.entity.ServerEntity;
import com.hemajoo.commerce.cherry.backend.persistence.document.entity.DocumentServer;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.EmailAddressServer;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.PersonServer;
import com.hemajoo.commerce.cherry.backend.persistence.person.repository.PersonRepository;
import com.hemajoo.commerce.cherry.backend.shared.base.query.condition.QueryConditionException;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentException;
import com.hemajoo.commerce.cherry.backend.shared.person.PersonException;
import com.hemajoo.commerce.cherry.backend.shared.person.PersonQuery;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Person persistence service behavior.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public interface IPersonService
{
    /**
     * Returns the person repository.
     * @return Person repository.
     */
    PersonRepository getPersonRepository();

    /**
     * Returns the total number of persons.
     * @return Number of persons.
     */
    Long count();

    /**
     * Returns if the given person identifier exist?
     * @param id Person identifier.
     * @return True if the person exist, false otherwise.
     */
    boolean existId(final @NonNull UUID id);

    /**
     * Returns the person matching the given identifier.
     * @param id Person identifier.
     * @return Person.
     */
    PersonServer findById(UUID id);

    /**
     * Saves the person.
     * @param person Person.
     * @return Saved person.
     * @throws PersonException Thrown in case an error occurred when trying to save a person.
     */
    PersonServer save(PersonServer person) throws PersonException;

    /**
     * Saves the person and flush.
     * @param person Person to save.
     * @return Updated person.
     */
    PersonServer saveAndFlush(final @NonNull PersonServer person);

    /**
     * Deletes the person matching the given identifier.
     * @param id Person identifier.
     */
    void deleteById(UUID id);

    /**
     * Returns all the persons.
     * @return List of persons.
     */
    List<PersonServer> findAll();

    /**
     * Returns the list of persons matching the given specification.
     * @param person Person specification.
     * @return List of persons.
     */
    List<PersonServer> search(final @NonNull PersonQuery person) throws QueryConditionException;

    /**
     * Returns the list of email addresses owned by the given person.
     * @param person Person.
     * @return List of email addresses.
     */
    List<EmailAddressServer> getEmailAddresses(final @NonNull PersonServer person);

//    ServerPersonEntity loadEmailAddresses(final @NonNull ServerPersonEntity person);

    /**
     * Returns the list of documents owned by the given base entity.
     * @param entity Base entity.
     * @return List of documents.
     */
    List<DocumentServer> getDocuments(final @NonNull ServerEntity entity);
}
