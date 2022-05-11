package com.fip.cbt.repository;

import com.fip.cbt.model.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@RunWith(SpringRunner.class)
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void createAndDeleteRole(){
        //given
        Role admin = new Role().setName("Admin");
        Role user = new Role().setName("user");
        roleRepository.saveAll(List.of(admin, user));

        //when
        List<Role> roles = roleRepository.findAll();

        //then
        assertThat(roles.size()).isEqualTo(2);

        //when
        roleRepository.deleteAll();
        List<Role> roles1 = roleRepository.findAll();

        //then
        assertThat(roles1.size()).isEqualTo(0);
    }
}
