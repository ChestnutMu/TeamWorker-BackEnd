package com.info.xiaotingtingBackEnd.repository;

import com.info.xiaotingtingBackEnd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRep extends JpaRepository<User, String> {

    User findByAccountAndPassword(String account,String password);
    User findByAccount(String account);
}
