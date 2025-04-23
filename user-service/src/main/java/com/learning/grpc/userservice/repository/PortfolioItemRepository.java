package com.learning.grpc.userservice.repository;

import com.learning.common.Ticker;
import com.learning.grpc.userservice.entity.PortfolioItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioItemRepository extends CrudRepository<PortfolioItem, Integer> {
    List<PortfolioItem> findAllByUserId(int userId);

    Optional<PortfolioItem> findByUserIdAndTicker(int userId, Ticker ticker);
}
