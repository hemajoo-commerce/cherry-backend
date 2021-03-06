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
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.EmailAddressServer;
import com.hemajoo.commerce.cherry.backend.shared.person.address.AddressType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

/**
 * JPA repository for the email address entity.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public interface EmailAddressRepository extends JpaRepository<EmailAddressServer, UUID>, JpaSpecificationExecutor<EmailAddressServer>
{
    /**
     * Returns the list of email addresses matching the given address type.
     * @param addressType Address type.
     * @return List of email addresses.
     */
    List<EmailAddressServer> findByAddressType(AddressType addressType);

    /**
     * Returns the list of email addresses matching the given status type.
     * @param statusType Status type.
     * @return List of email addresses.
     */
    List<EmailAddressServer> findByStatusType(StatusType statusType);

    /**
     * Returns the list of email addresses matching the given value.
     * @param isDefaultEmail True to get default email addresses, false otherwise.
     * @return List of email addresses.
     */
    List<EmailAddressServer> findByIsDefaultEmail(Boolean isDefaultEmail);

    /**
     * Returns the list of email addresses belonging to the given parent identifier.
     * @param parentId Parent identifier.
     * @return List of email addresses.
     */
    List<EmailAddressServer> findByParentId(UUID parentId);

    /**
     * Returns the list of email addresses matching the given specification.
     * @param specification Email address specification.
     * @return List of email addresses.
     */
    @NotNull
    List<EmailAddressServer> findAll(final Specification<EmailAddressServer> specification);
}
