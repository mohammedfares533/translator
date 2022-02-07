package com.cerebra.translator.repository;

import com.cerebra.translator.model.SystemUser;
import com.cerebra.translator.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SystemUser,Long> {

    SystemUser findSystemUserByUserName(String userName);

    SystemUser findSystemUserByUserNameAndRoleIs(String username, Roles roles);
}
