package com.ASAF.service;

import com.ASAF.entity.RoleEntity;
import com.ASAF.entity.UserEntity;
import com.ASAF.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (RoleEntity role : userEntity.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                grantedAuthorities);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id: " + id));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (RoleEntity role : userEntity.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                grantedAuthorities);
    }

    public static class UserPrincipal extends User {
        private final Long id;

        public UserPrincipal(Long id, String username, String password, Set<GrantedAuthority> grantedAuthorities) {
            super(username, password, grantedAuthorities);
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
