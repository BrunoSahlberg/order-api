package br.market.flow.order.infrastructure.persistence;

import br.market.flow.order.domain.Order;
import br.market.flow.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper = new OrderPersistenceMapper();

    public OrderRepositoryAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = jpaRepository.findById(order.getId())
                .map(existing -> mapper.updateEntity(existing, order))
                .orElseGet(() -> mapper.toEntity(order));
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Order> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}
