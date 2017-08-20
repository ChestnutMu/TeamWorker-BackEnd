package com.info.controller;

import com.info.model.Test;
import com.info.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by king on 2017/8/20.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    TestRepository testRepository;

    @RequestMapping("/")
    public String helloworld() {
        Test test=new Test();
        test.setName("chao");
        test.setUrl("tt");
        test.setId(1L);
        testRepository.save(test);
        return "hello world!!!";
    }
}
