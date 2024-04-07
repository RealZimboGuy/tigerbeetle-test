package com.github.realzimbguy.tigerbeetle.repo;

import com.github.realzimbguy.tigerbeetle.repo.entity.LedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepo extends JpaRepository<LedgerEntity, Integer> {
    public LedgerEntity findByName(String name);
}
