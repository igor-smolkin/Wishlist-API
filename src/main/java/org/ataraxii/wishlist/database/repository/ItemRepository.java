package org.ataraxii.wishlist.database.repository;

import org.ataraxii.wishlist.database.entity.Item;
import org.ataraxii.wishlist.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    Optional<Item> findByName(String name);
    List<Item> findAllByUser(User user);
    Optional<Item> findByIdAndUser(UUID id, User user);
    Optional<Item> findByNameAndUser(String name, User user);
}
