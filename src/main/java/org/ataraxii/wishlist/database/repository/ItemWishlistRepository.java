package org.ataraxii.wishlist.database.repository;

import org.ataraxii.wishlist.database.entity.ItemWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemWishlistRepository extends JpaRepository<ItemWishlist, Long> {
}
