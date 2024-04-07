package com.github.realzimbguy.tigerbeetle.repo;

import com.github.realzimbguy.tigerbeetle.repo.entity.AccountFlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountFlagsRepo extends JpaRepository<AccountFlagEntity, Integer> {
    public AccountFlagEntity findByName(String name);
}
