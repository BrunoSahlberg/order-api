package br.market.flow.order.infrastructure.persistence;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {

    @Override
    @EntityGraph(attributePaths = "items")
    @NonNull
    Optional<OrderEntity> findById(String id);
}
