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
package com.hemajoo.commerce.cherry.backend.persistence.person.repository;

import com.hemajoo.commerce.cherry.backend.commons.type.StatusType;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.PersonServer;
import com.hemajoo.commerce.cherry.backend.shared.person.GenderType;
import com.hemajoo.commerce.cherry.backend.shared.person.PersonType;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for the person entity.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public interface PersonRepository extends JpaRepository<PersonServer, UUID>, JpaSpecificationExecutor<PersonServer>
{
    /**
     * Returns a person matching the given identifier.
     * @param id Person identifier.
     * @return Person.
     */
    @NotNull
    @EntityGraph(attributePaths = "documents")
    Optional<PersonServer> findById(final @NonNull UUID id);

    /**
     * Returns a list of persons matching the given status.
     * @param statusType Status type.
     * @return List of persons.
     * @see StatusType
     */
    List<PersonServer> findByStatusType(StatusType statusType);

    /**
     * Returns the list of persons matching the given person type.
     * @param personType Person type.
     * @return List of persons.
     * @see PersonType
     */
    List<PersonServer> findByPersonType(PersonType personType);

    /**
     * Returns the list of persons matching the given gender type.
     * @param gender Gender type.
     * @return List of persons.
     * @see GenderType
     */
    List<PersonServer> findByGenderType(GenderType gender);

    /**
     * Returns the list of persons matching the given last name.
     * @param lastName Last name (strict).
     * @return List of persons.
     */
    List<PersonServer> findByLastName(String lastName);

    /**
     * Returns the list of persons matching the given first name.
     * @param firstName First name (strict).
     * @return List of persons.
     */
    List<PersonServer> findByFirstName(String firstName);

    /**
     * Returns the list of persons matching the given specification.
     * @param specification Person specification.
     * @return List of persons.
     */
    @NotNull
    List<PersonServer> findAll(final Specification<PersonServer> specification);
}
