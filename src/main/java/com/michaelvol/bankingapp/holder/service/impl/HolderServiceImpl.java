package com.michaelvol.bankingapp.holder.service.impl;

import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.dto.HolderMapper;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.repository.HolderRepository;
import com.michaelvol.bankingapp.holder.service.HolderService;

import java.util.NoSuchElementException;

public class HolderServiceImpl implements HolderService {

    HolderRepository holderRepository;

    HolderMapper mapper;

    @Override
    public Holder createHolder(CreateHolderRequestDto dto) {
        Holder holder = mapper.createHolderRequestDtoToHolder(dto);
        return holderRepository.save(holder);
    }

    @Override
    public Holder getHolderById(Long holderId) {
        return holderRepository.findById(holderId)
                               .orElseThrow(() -> new NoSuchElementException("Holder with id " + holderId + " not found"));

    }
}
