package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	public User findByUsername(String username);
	public boolean existsByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsernameOptional(@Param("username") String username);
	
}
