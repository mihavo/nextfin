package com.michaelvol.bankingapp.holder.service.impl;

import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.dto.HolderMapper;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.repository.HolderRepository;
import com.michaelvol.bankingapp.holder.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {

    private final HolderRepository holderRepository;

    private final HolderMapper mapper;

    @Override
    public Holder createHolder(CreateHolderRequestDto dto) {
        Holder holder = mapper.createHolderRequestDtoToHolder(dto);
        return holderRepository.save(holder);
    }

    @Override
    public Holder getHolderById(Long holderId) throws NoSuchElementException {
        return holderRepository.findById(holderId)
                               .orElseThrow(() -> new NotFoundException("Holder with id " + holderId + " not found"));
    }
}
