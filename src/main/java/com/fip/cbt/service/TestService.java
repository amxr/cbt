package com.fip.cbt.service;

import com.fip.cbt.controller.request.TestRequest;
import com.fip.cbt.controller.request.UpdateTestRequest;
import com.fip.cbt.model.Test;

public interface TestService {
    Test add(TestRequest testRequest);

    Test getOne(String testNumber);

    void delete(String testNumber);

    void update(UpdateTestRequest updateTestRequest);
}
