package com.fip.cbt.service;

import com.fip.cbt.controller.request.TestTakenRequest;
import com.fip.cbt.controller.request.UpdateTestTakenRequest;
import com.fip.cbt.model.TestTaken;

import java.util.List;

public interface TestTakenService {
    TestTaken add(TestTakenRequest testTakenRequest);
    
    TestTaken getOne(String id);
    
    void delete(String id);
    
    TestTaken update(UpdateTestTakenRequest updateTestTakenRequest);
    
    List<TestTaken> getAll();
}
