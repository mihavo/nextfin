package com.nextfin.holder.controller;


import com.nextfin.AppConstants;
import com.nextfin.holder.dto.CreateCustomerDto;
import com.nextfin.holder.dto.CreateCustomerResponseDto;
import com.nextfin.holder.dto.CreateHolderDto;
import com.nextfin.holder.dto.CreateHolderResponseDto;
import com.nextfin.holder.entity.Holder;
import com.nextfin.holder.service.HolderService;
import com.nextfin.users.entity.User;
import com.nextfin.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Holder API", description = "Methods for user management")
@RequestMapping(AppConstants.API_BASE_URL + "/holders/")
public class HolderController {

    private final HolderService holderService;

    private final UserService userService;

    private final MessageSource messageSource;

    /**
     * Creates a customer (holder + user) and persists it given a {@link CreateCustomerDto}. To be used only by privileged users.
     * For main user creation, use the /auth/register endpoint.
     * @param dto the request dto
     * @return a {@link ResponseEntity} containing {@link CreateCustomerResponseDto}
     */
    @PostMapping("/register-customer")
    @Operation(summary = "Creates an app user & holder")
    @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<CreateCustomerResponseDto> createCustomer(@Valid @RequestBody CreateCustomerDto dto) {
        User user = userService.createUser(dto.getUser());
        Holder holder = holderService.createHolder(dto.getHolder(), user);
        String successMessage = messageSource.getMessage("customer.create.success",
                                                         new String[]{holder.getId().toString(), user.getId().toString()},
                                                         LocaleContextHolder.getLocale());
        return new ResponseEntity<>(CreateCustomerResponseDto
                                            .builder()
                                            .user(user)
                                            .holder(holder)
                                            .message(successMessage)
                                            .build(),
                                    HttpStatus.CREATED);
    }

    @PostMapping("register-holder")
    @Operation(summary = "Creates a holder only")
    public ResponseEntity<CreateHolderResponseDto> createHolder(@RequestParam String username, @Valid @RequestBody CreateHolderDto dto) {
        User user = userService.findUserByUsername(username);
        Holder holder = holderService.createHolder(dto, user);
        String message = messageSource.getMessage("holder.create.success",
                                                  new UUID[]{holder.getId()},
                                                  LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new CreateHolderResponseDto(holder, message), HttpStatus.CREATED);
    }

    /**
     * Fetches a holder by its ID. Requires the user role to be MANAGER, EMPLOYEE or ADMIN.
     * @return a {@link ResponseEntity} containing the Holder
     */
    @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE', 'ADMIN')")
    @Operation(summary = "Gets a holder by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Holder> getHolderById(@PathVariable UUID id) {
        Holder holder = holderService.getHolderById(id);
        return new ResponseEntity<>(holder, HttpStatus.OK);
    }

    @Operation(summary = "Gets the current user's folder")
    @GetMapping("/me")
    public ResponseEntity<Holder> getMyHolder() {
        Holder holder = holderService.getHolderByCurrentUser();
        return new ResponseEntity<>(holder, HttpStatus.OK);
    }
}
