package com.services.ServiceProvider.service.impl;

import com.services.ServiceProvider.entity.Role;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.service.RoleService;
import com.services.ServiceProvider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserService userService;

    @Override
    public Set<String> getUserRoleNames(String username) {
        User user = userService.getUserByUserName(username);
        return user.getRoles()
                .stream()
                .map(Role:: getRoleName)
                .collect(Collectors.toSet());
    }
}