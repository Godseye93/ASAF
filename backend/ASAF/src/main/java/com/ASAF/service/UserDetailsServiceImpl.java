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
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + memberEmail));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (RoleEntity role : userEntity.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getMemberEmail(),
                userEntity.getMemberPassword(),
                grantedAuthorities);
    }

    public UserDetails loadUserById(int id) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id: " + id));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (RoleEntity role : userEntity.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getMemberEmail(),
                userEntity.getMemberPassword(),
                grantedAuthorities);
    }

    public static class UserPrincipal extends User {
        private final int id;

        public UserPrincipal(int id, String username, String password, Set<GrantedAuthority> grantedAuthorities) {
            super(username, password, grantedAuthorities);
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
