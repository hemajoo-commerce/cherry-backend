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
package com.hemajoo.commerce.cherry.backend.persistence.person.validation.constraint;

import com.hemajoo.commerce.cherry.backend.persistence.person.validation.validator.EmailAddressIdValidator;
import org.springframework.http.HttpStatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validation constraint used to validate an email address identifier.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Documented
@Constraint(validatedBy = EmailAddressIdValidator.class)
@Target( { ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailAddressId
{
    HttpStatus status() default HttpStatus.NOT_FOUND;
    String message() default "Email address id: '${validatedValue}' does not exist!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
