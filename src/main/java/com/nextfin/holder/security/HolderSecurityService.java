package com.nextfin.holder.security;

import com.nextfin.holder.entity.Holder;
import com.nextfin.holder.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolderSecurityService {
    private final HolderService holderService;

    public boolean isHolderOwner(UUID holderId) {
        Holder holderByCurrentUser = holderService.getHolderByCurrentUser();
        return holderByCurrentUser.getId().equals(holderId);
    }
}
