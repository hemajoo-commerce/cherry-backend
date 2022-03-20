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
package com.hemajoo.commerce.cherry.backend.shared.person.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hemajoo.commerce.cherry.backend.commons.type.EntityType;
import com.hemajoo.commerce.cherry.backend.shared.base.search.BaseSearch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Represents a search object for the <b>email address</b> entities.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
//@ApiModel(value = "EmailAddressSearch", description = "Specification object used to search for email addresses.")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class SearchEmailAddress extends BaseSearch
{
    /**
     * Email address.
     */
    @JsonProperty("email")
    @Schema(description = "Email address" /*, allowEmptyValue = true*/)
    private String email;

    /**
     * Is it the default email address?
     */
    @JsonProperty("isDefault")
    private Boolean isDefaultEmail;

    /**
     * Email address type.
     */
    @JsonProperty("addressType")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Email address type"/*, allowEmptyValue = true*/)
    private AddressType addressType;

    /**
     * The person identifier this email address belongs to.
     */
    @JsonProperty("personId")
    @Schema(description = "Person identifier (UUID) owning the email address(es)", hidden = false)
    private String personId;

    /**
     * Creates a new email address search entity.
     */
    public SearchEmailAddress()
    {
        super(EntityType.EMAIL_ADDRESS);
    }
}
