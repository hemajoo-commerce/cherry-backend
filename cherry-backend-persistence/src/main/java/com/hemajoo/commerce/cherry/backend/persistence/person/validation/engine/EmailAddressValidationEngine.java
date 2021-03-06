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
package com.hemajoo.commerce.cherry.backend.persistence.person.validation.engine;

import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.persistence.base.entity.EntityComparator;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.EmailAddressServer;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.PersonServer;
import com.hemajoo.commerce.cherry.backend.persistence.person.service.IEmailAddressService;
import com.hemajoo.commerce.cherry.backend.persistence.person.service.IPersonService;
import com.hemajoo.commerce.cherry.backend.shared.base.query.condition.QueryConditionException;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentException;
import com.hemajoo.commerce.cherry.backend.shared.person.address.email.EmailAddressClient;
import com.hemajoo.commerce.cherry.backend.shared.person.address.email.EmailAddressException;
import com.hemajoo.commerce.cherry.backend.shared.person.address.email.EmailAddressQuery;
import lombok.NonNull;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * Email address validation engine.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Component
public final class EmailAddressValidationEngine
{
    @Autowired
    private IPersonService servicePerson;

    @Autowired
    private IEmailAddressService serviceEmailAddress;

    /**
     * Checks if the given search object is valid or not?
     * @param search Search email address object.
     * @throws EmailAddressException Thrown to indicate an error occurred while submitting a search email address object.
     */
    public static void isSearchValid(final @NonNull EmailAddressQuery search) throws EmailAddressException, QueryConditionException
    {
        EmailAddressQuery reference = new EmailAddressQuery();

        if (EntityComparator.getJavers().compare(reference, search).getChangesByType(ValueChange.class).isEmpty())
        {
            throw new EmailAddressException("Search object must contain at least one search value!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Checks the person identifier is valid.
     * <br>
     * The person identifier owning the email address must exist.
     * @param personId Person identifier.
     * @throws EmailAddressException Thrown in case the validation failed!
     */
    public void validatePersonId(final @NonNull UUID personId) throws EmailAddressException
    {
        if (servicePerson.findById(personId) == null)
        {
            throw new EmailAddressException(String.format("Person with id: %s does not exist!", personId));
        }
    }

    /**
     * Checks the email address identifier is valid.
     * <br>
     * The email address identifier must exist.
     * @param emailAddress Email address to check.
     * @throws EmailAddressException Thrown in case the validation failed!
     */
    public void validateEmailAddressId(final @NonNull EmailAddressClient emailAddress) throws EmailAddressException, DocumentException
    {
        validateEmailAddressId(emailAddress.getId());
    }

    /**
     * Checks the email address identifier is valid.
     * <br>
     * The email address identifier must exist.
     * @param id Email address identifier.
     * @throws EmailAddressException Thrown in case the validation failed!
     */
    public void validateEmailAddressId(final @NonNull UUID id) throws EmailAddressException, DocumentException
    {
        if (serviceEmailAddress.findById(id) == null)
        {
            throw new EmailAddressException(String.format("Email address with id: %s does not exist!", id), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Checks if the email address is active and a default one.
     * <br>
     * An active default email address must be unique for the person owning the email address.
     * @param emailAddress Email address to check.
     * @throws EmailAddressException Thrown in case the validation failed!
     */
    public void validateDefaultEmail(final @NonNull EmailAddressClient emailAddress) throws EmailAddressException
    {
        if (Boolean.TRUE.equals(emailAddress.getIsDefaultEmail()) && emailAddress.isActive())
        {
            PersonServer person = servicePerson.findById(emailAddress.getParent().getId());
            EmailAddressServer defaultEmailAddress = person.getDefaultEmailAddress();
            if (!Objects.equals(defaultEmailAddress.getIdentity(), emailAddress.getIdentity()) || emailAddress.getId() == null)
            {
                throw new EmailAddressException(
                        String.format(
                                "Person with id: '%s' already has an active default email address!",
                                emailAddress.getParent().getId()),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Checks the person being the owner of the email address does not already hold such email address.
     * @param emailAddress Email address persistent entity.
     * @throws EmailAddressException Thrown in case the validation failed!
     */
    public void validateNameUniqueness(final @NonNull EmailAddressClient emailAddress) throws EmailAddressException
    {
        PersonServer person = servicePerson.findById(emailAddress.getParent().getId());

        if (person == null)
        {
            throw new EmailAddressException(
                    String.format(
                            "Person id: '%s' cannot be found!",
                            emailAddress.getParent().getId()),
                    HttpStatus.BAD_REQUEST);
        }

        if (person.existEmail(emailAddress.getEmail()))
        {
            if (emailAddress.getId() != null) // New email address entity
            {
                EmailAddressServer email = person.getEmailById(emailAddress.getId());
                if (email != null && !email.getId().equals(emailAddress.getId()))
                {
                    throw new EmailAddressException(
                            String.format(
                                    "Email address: '%s' already belongs to another entity: '%s'!",
                                    emailAddress.getParent(),
                                    emailAddress.getEmail()),
                            HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                throw new EmailAddressException(String.format("Email address: '%s' already exist!", emailAddress.getEmail()), HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Validates an email address for an update.
     * @param emailAddress Email address to update.
     * @throws EmailAddressException Thrown to indicate an error occurred when trying to validate an email address.
     */
    public void validateEmailForUpdate(final @NonNull EmailAddressClient emailAddress) throws EmailAddressException, DocumentException
    {
        validateEmailAddressId(emailAddress.getId());
        validateEmailEntityType(emailAddress.getEntityType());
        //validatePersonId(emailAddress.getOwner().getId());
        validateDefaultEmail(emailAddress);
        validateNameUniqueness(emailAddress);
    }

    /**
     * Validates an entity type as being an <b>email address</b>.
     * @param entityType Entity type.
     * @throws EmailAddressException Thrown to indicate an error occurred when trying to validate an entity type.
     */
    private void validateEmailEntityType(final EntityType entityType) throws EmailAddressException
    {
        if (entityType != EntityType.EMAIL_ADDRESS)
        {
            throw new EmailAddressException(String.format("Entity type: %s expected!", EntityType.EMAIL_ADDRESS));
        }
    }
}
