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
package com.hemajoo.commerce.cherry.backend.shared.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.commons.type.StatusType;
import com.hemajoo.commerce.cherry.backend.shared.base.entity.EntityClient;
import com.hemajoo.commerce.cherry.backend.shared.person.address.AddressType;
import com.hemajoo.commerce.cherry.backend.shared.person.address.email.EmailAddressClient;
import com.hemajoo.commerce.cherry.backend.shared.person.address.postal.PostalAddressClient;
import com.hemajoo.commerce.cherry.backend.shared.person.phone.PhoneNumberClient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Represents a <b>client person entity</b>.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class PersonClient extends EntityClient implements IPersonClient
{
    /**
     * Minimal birthdate.
     */
    public static final LocalDate MIN_BIRTHDATE = LocalDate.of(1500, Month.JANUARY, 1);

    /**
     * Person last name.
     */
    @Schema(name = "lastName", description = "Person last name", example = "Eastwood", required = true)
    private String lastName;

    /**
     * Person first name.
     */
    @Schema(name = "firstName", description = "Person first name", example = "Clint", required = true)
    private String firstName;

    /**
     * Person birthdate.
     */
    @Schema(name = "birthDate", description = "Person birth date (YYYY-MM-DD)", example = "1930-05-31", required = true)
    private Date birthDate;

    /**
     * Person gender type.
     */
    @Schema(name = "genderType", description = "Person gender", example = "MALE", required = true)
    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    /**
     * Person type.
     */
    @Schema(name = "personType", description = "Person type", example = "PHYSICAL", required = true)
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    /**
     * Set of postal addresses associated to the person.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<PostalAddressClient> postalAddresses = new ArrayList<>();

    /**
     * Set of phone numbers associated to the person.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<PhoneNumberClient> phoneNumbers = new ArrayList<>();

    /**
     * Set of email addresses associated to the person.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<EmailAddressClient> emailAddresses = new ArrayList<>();

    /**
     * Creates a new person.
     */
    public PersonClient()
    {
        super(EntityType.PERSON);
    }

    /**
     * Sets the person last name.
     * @param lastName Last name.
     */
    public void setLastName(final @NonNull String lastName)
    {
        this.lastName = lastName;
        setName(this.lastName + ", " + this.firstName);
    }

    /**
     * Sets the person first name.
     * @param firstName First name.
     */
    public void setFirstName(final @NonNull String firstName)
    {
        this.firstName = firstName;
        setName(this.lastName + ", " + this.firstName);
    }

    /**
     * Returns the default email address.
     * @return Default email address if one, null otherwise.
     */
    @JsonIgnore
    public final EmailAddressClient getDefaultEmailAddress()
    {
        Optional<EmailAddressClient> optional = emailAddresses.stream()
                .filter(EmailAddressClient::getIsDefaultEmail).findFirst();

        return optional.orElse(null);
    }

    public final boolean hasDefaultEmailAddress()
    {
        return emailAddresses.stream().anyMatch(EmailAddressClient::getIsDefaultEmail);
    }

    /**
     * Returns the default postal address.
     * @return Default postal address if one, null otherwise.
     */
    @JsonIgnore
    public final PostalAddressClient getDefaultPostalAddress()
    {
        Optional<PostalAddressClient> optional =  postalAddresses.stream()
                .filter(PostalAddressClient::getIsDefault).findFirst();

        return optional.orElse(null);
    }

    /**
     * Checks if the given email already exist?
     * @param email Email to check.
     * @return {@code True} if it already exist, {@code false} otherwise.
     */
    public final boolean existEmail(final @NonNull String email)
    {
        return emailAddresses.stream()
                .anyMatch(emailAddress -> emailAddress.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Retrieves email addresses matching the given {@link AddressType}.
     * @param type Address type.
     * @return List of email addresses.
     */
    public final List<EmailAddressClient> findEmailAddressByType(final AddressType type)
    {
        return emailAddresses.stream()
                .filter(emailAddress -> emailAddress.getAddressType() == type)
                .toList();
    }

    /**
     * Retrieves email addresses matching the given {@link StatusType}.
     * @param status Status type.
     * @return List of email addresses.
     */
    public final List<EmailAddressClient> findEmailAddressByStatus(final StatusType status)
    {
        return emailAddresses.stream()
                .filter(emailAddress -> emailAddress.getStatusType() == status)
                .toList();
    }

    /**
     * Retrieves postal addresses matching the given {@link AddressType}.
     * @param type Address type.
     * @return List of postal addresses.
     */
    public final List<PostalAddressClient> findPostalAddressByType(final AddressType type)
    {
        return postalAddresses.stream()
                .filter(postalAddress -> postalAddress.getAddressType() == type)
                .toList();
    }

    /**
     * Retrieves postal addresses matching the given {@link StatusType}.
     * @param status Status type.
     * @return List of postal addresses.
     */
    public final List<PostalAddressClient> findPostalAddressByStatus(final StatusType status)
    {
        return postalAddresses.stream()
                .filter(postalAddress -> postalAddress.getStatusType() == status)
                .toList();
    }
}
