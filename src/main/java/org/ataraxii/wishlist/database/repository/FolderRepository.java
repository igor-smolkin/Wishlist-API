package org.ataraxii.wishlist.database.repository;

import org.ataraxii.wishlist.database.entity.Folder;
import org.ataraxii.wishlist.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
    Optional<Folder> findByIdAndUser(UUID id, User user);
    Optional<Folder> findByName(String name);
    Optional<Folder> findByNameAndUser(String name, User user);
    List<Folder> findByUser(User user);
}
