package com.michaelvol.bankingapp.holder.controller;


import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.dto.CreateHolderResponseDto;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
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
@RequestMapping(AppConstants.API_BASE_URL + "/holders")
@PropertySource("messages/messages.properties")
public class HolderController {

    private final HolderService holderService;

    private MessageSource messageSource;

    /**
     * Creates a holder and persists it given a {@link CreateHolderRequestDto}
     * @param requestDto the request dto
     * @return a {@link ResponseEntity} containing {@link CreateHolderResponseDto}
     */
    @PostMapping
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
    public ResponseEntity<Holder> getHolderById(@PathVariable Long id) {
        Holder holder = holderService.getHolderById(id);
        return new ResponseEntity<>(holder, HttpStatus.OK);
    }
}
