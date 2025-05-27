package org.ataraxii.wishlist.database.repository;

import org.ataraxii.wishlist.database.entity.ItemFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFolderRepository extends JpaRepository<ItemFolder, Long> {
}
