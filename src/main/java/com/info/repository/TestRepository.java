package com.info.repository;

import com.info.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by king on 2017/8/20.
 */
@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
}
