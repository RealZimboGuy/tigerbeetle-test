package com.github.realzimbguy.tigerbeetle.repo;

import com.github.realzimbguy.tigerbeetle.repo.entity.AccountCodeEntity;
import com.github.realzimbguy.tigerbeetle.repo.entity.AccountFlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCodesRepo extends JpaRepository<AccountCodeEntity, Integer> {
    public AccountCodeEntity findByName(String name);
}
