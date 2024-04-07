package com.github.realzimbguy.tigerbeetle.repo;

import com.github.realzimbguy.tigerbeetle.repo.entity.AccountCodeEntity;
import com.github.realzimbguy.tigerbeetle.repo.entity.TransferCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferCodesRepo extends JpaRepository<TransferCodeEntity, Integer> {
    public TransferCodeEntity findByName(String name);
}
