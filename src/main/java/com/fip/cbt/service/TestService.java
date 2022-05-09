package com.fip.cbt.service;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.controller.request.UpdateTestRequest;
import com.fip.cbt.model.Test;

import java.util.List;

public interface TestService {
    Test add(TestRequest testRequest);

    Test getOne(String testNumber);

    void delete(String testNumber);

    Test update(UpdateTestRequest updateTestRequest);

    List<Test> getAll();
}
