package com.michaelvol.bankingapp.holder.controller;


import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.dto.CreateHolderResponseDto;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.service.HolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Holder API", description = "Methods for user management")
@RequestMapping(AppConstants.API_BASE_URL + "/holders/")
public class HolderController {

    private final HolderService holderService;

    private final MessageSource messageSource;

    /**
     * Creates a holder and persists it given a {@link CreateHolderRequestDto}
     * @param requestDto the request dto
     * @return a {@link ResponseEntity} containing {@link CreateHolderResponseDto}
     */
    @PostMapping
    @Operation(summary = "Creates an app user/holder")
    public ResponseEntity<CreateHolderResponseDto> createHolder(@RequestBody CreateHolderRequestDto requestDto) {
        Holder holder = holderService.createHolder(requestDto);
        String successMessage = messageSource.getMessage("holder.create.success",
                                                         null,
                                                         LocaleContextHolder.getLocale());
        return new ResponseEntity<>(CreateHolderResponseDto.builder()
                                                           .holder(holder)
                                                           .message(successMessage)
                                                           .build(),
                                    HttpStatus.CREATED);
    }

    /**
     * Fetches a holder by its id
     * @return a {@link ResponseEntity} containing the Holder
     */
    @GetMapping("/{id}")
    @Operation(summary = "Gets a holder by its ID")
    public ResponseEntity<Holder> getHolderById(@PathVariable Long id) {
        Holder holder = holderService.getHolderById(id);
        return new ResponseEntity<>(holder, HttpStatus.OK);
    }
}
