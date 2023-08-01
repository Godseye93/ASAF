package com.ASAF.controller;

import com.ASAF.config.JwtTokenProvider;
import com.ASAF.dto.LoginForm;
import com.ASAF.dto.SignUpForm;
import com.ASAF.entity.RoleEntity;
import com.ASAF.entity.UserEntity;
import com.ASAF.repository.RoleRepository;
import com.ASAF.repository.UserRepository;
import com.ASAF.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService,
                          UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getMemberEmail(),
                        loginForm.getMemberPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok().header("Authorization", "Bearer " + jwtToken).body(jwtToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpForm signUpForm) {
        if (userRepository.findByMemberEmail(signUpForm.getMemberEmail()).isPresent()) {
            return new ResponseEntity<>("Fail -> Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Create UserEntity
        UserEntity newUser = new UserEntity();
        newUser.setMemberEmail(signUpForm.getMemberEmail());
        newUser.setMemberPassword(passwordEncoder.encode(signUpForm.getMemberPassword()));
        newUser.setElectronic_student_id(signUpForm.getElectronic_student_id());
        newUser.setMember_info(signUpForm.getMember_info());


        Set<RoleEntity> roles = new HashSet<>();
        // 각 사용자에게 적절한 역할을 할당해야 합니다.
        // 예를 들어, 사용자가 관리자인 경우 관리자 역할을 할당해야 합니다.
        RoleEntity userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new RoleEntity();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        roles.add(userRole);
        newUser.setRoles(roles);

        userRepository.save(newUser);
        return ResponseEntity.ok().body("User registered successfully!");
    }
}
