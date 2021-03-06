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
package com.hemajoo.commerce.cherry.backend.rest.controller.person;

import com.hemajoo.commerce.cherry.backend.persistence.base.entity.ServiceFactoryPerson;
import com.hemajoo.commerce.cherry.backend.persistence.person.converter.PersonConverter;
import com.hemajoo.commerce.cherry.backend.persistence.person.entity.PersonServer;
import com.hemajoo.commerce.cherry.backend.persistence.person.randomizer.PersonRandomizer;
import com.hemajoo.commerce.cherry.backend.persistence.person.validation.constraint.ValidPersonId;
import com.hemajoo.commerce.cherry.backend.persistence.person.validation.engine.EmailAddressValidationEngine;
import com.hemajoo.commerce.cherry.backend.shared.document.exception.DocumentException;
import com.hemajoo.commerce.cherry.backend.shared.person.PersonClient;
import com.hemajoo.commerce.cherry.backend.shared.person.PersonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Controller providing service endpoints to manage the persons.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
@Tag(name = "Person REST controller", description = "Set of REST-API endpoints to manage the person entities.")
@Validated
@RestController
@RequestMapping("/api/v1/person")
public class PersonController
{
    /**
     * Server entity factory.
     */
    @Autowired
    private ServiceFactoryPerson servicePerson;

    /**
     * Person converter.
     */
    @Autowired
    private PersonConverter converterPerson;

    /**
     * Email address validation engine.
     */
    @Autowired
    private EmailAddressValidationEngine validationEmailAddress;

    /**
     * Service to count the number of persons.
     * @return Number of persons.
     */
    @Operation(summary = "Count the number of persons")
    @GetMapping("/count")
    public long count()
    {
        return servicePerson.getPersonService().count();
    }

    /**
     * Service to retrieve a person.
     * @param id Email address identifier.
     * @return Email address matching the given identifier.
     */
    @Operation(summary = "Retrieve an email address")
    @GetMapping("/get/{id}")
    public ResponseEntity<PersonClient> get(
            @Parameter(description = "Person identifier", required = true)
            @Valid @ValidPersonId // Handles person id validation automatically, need both annotations!
            @NotNull
            @PathVariable String id)
    {
        PersonServer person = servicePerson.getPersonService().findById(UUID.fromString(id));
        return ResponseEntity.ok(converterPerson.fromServerToClient(person));
    }

//    /**
//     * Service to add a new person.
//     * @param person Email address.
//     * @return Newly created email address.
//     * @throws EmailAddressException Thrown to indicate an error occurred while trying to create the email address.
//     */
//    @ApiOperation(value = "Create a new email address")
//    @PostMapping("/create")
//    public ResponseEntity<ClientPersonEntity> create(
//            @ApiParam(value = "Person", required = true)
//            @Valid @ValidPersonForCreation @RequestBody ClientPersonEntity person) throws PersonException
//    {
//        ServerPersonEntity serverPerson = personConverter.fromClientToServer(person);
//        serverPerson = entityFactory.getServices().getPersonService().save(serverPerson);
//
//        return ResponseEntity.ok(personConverter.fromServerToClient(serverPerson));
//    }

    /**
     * Service to create a random person.
     * @return Randomly generated person.
     * @throws PersonException Thrown to indicate an error occurred with a person.
     */
    @Operation(summary = "Create a new random person")
    @PostMapping("/random")
    public ResponseEntity<PersonClient> random() throws PersonException
    {
        PersonServer person = PersonRandomizer.generateServerEntity(false);
        person = servicePerson.getPersonService().save(person);

        return ResponseEntity.ok(converterPerson.fromServerToClient(person));
    }

//    /**
//     * Service to update a person.
//     * @param person Person to update.
//     * @return Updated person.
//     */
//    @ApiOperation(value = "Update a person")
//    @PutMapping("/update")
//    public ResponseEntity<ClientPersonEntity> update(
//            @NotNull @Valid @ValidPersonForUpdate @RequestBody ClientPersonEntity person) throws PersonException
//    {
////        personRuleEngine.validateForUpdate(person);
//
//        ServerPersonEntity updated = entityFactory.getServices().getPersonService().update(personConverter.fromClientToServer(person));
//        ClientPersonEntity client = personConverter.fromServerToClient(updated);
//
//        return ResponseEntity.ok(client);
//    }

    /**
     * Service to delete a person given its identifier.
     * @param id Person identifier.
     * @return Confirmation message.
     */
    @Operation(summary = "Delete a person")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Person identifier (UUID)", required = true)
            @NotNull @Valid @ValidPersonId @PathVariable String id)
    {
        servicePerson.getPersonService().deleteById(UUID.fromString(id));

        return ResponseEntity.ok(String.format("Person id: '%s' has been deleted successfully!", id));
    }

//    /**
//     * Service to search for email addresses given some criteria.
//     * @param search Email address specification object.
//     * @return List of matching email addresses.
//     * @throws EmailAddressException Thrown to indicate an error occurred while trying to search for email addresses.
//     */
//    @ApiOperation(value = "Search for email addresses", notes = "Search for email addresses matching the given predicates. Fill only the fields to be taken into account.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successful operation"),
//            @ApiResponse(code = 404, message = "No email address found matching the given criteria"),
//            @ApiResponse(code = 400, message = "Missing or invalid request"),
//            @ApiResponse(code = 500, message = "Internal server error")})
//    @PatchMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<ClientEmailAddressEntity>> search(final @RequestBody @NotNull SearchEmailAddress search) throws EmailAddressException
//    {
//        EmailAddressValidationEngine.isSearchValid(search);
//
//        List<ClientEmailAddressEntity> clients = entityFactory.getServices().getEmailAddressService().search(search)
//                .stream()
//                .map(element -> converter.fromServerToClient(element))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(clients);
//    }
//
//    /**
//     * Service to query for email addresses identifiers matching some criteria.
//     * @param search Email address specification criteria.
//     * @return List of matching email address identifiers.
//     * @throws EmailAddressException Thrown to indicate an error occurred while trying to query for email addresses.
//     */
//    @ApiOperation(value = "Query email addresses", notes = "Returns a list of email addresses matching the given criteria.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successful query"),
//            @ApiResponse(code = 404, message = "No email address found matching the given criteria"),
//            @ApiResponse(code = 400, message = "Missing or invalid request"),
//            @ApiResponse(code = 500, message = "Internal server error")})
//    @GetMapping("/query")
//    public ResponseEntity<List<String>> query(final @NotNull SearchEmailAddress search) throws EmailAddressException
//    {
//        EmailAddressValidationEngine.isSearchValid(search);
//
//        List<ClientEmailAddressEntity> clients = entityFactory.getServices().getEmailAddressService().search(search)
//                .stream()
//                .map(element -> converter.fromServerToClient(element))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(GenericEntityConverter.toIdList(clients));
//    }
}
