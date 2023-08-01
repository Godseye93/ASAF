package com.ASAF.controller;

import com.ASAF.config.JwtTokenProvider;
import com.ASAF.dto.MemberDTO;
import com.ASAF.entity.MemberEntity;
import com.ASAF.entity.RoleEntity;
import com.ASAF.repository.MemberRepository;
import com.ASAF.repository.RoleRepository;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService,
                          MemberRepository memberRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody MemberDTO memberDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        memberDTO.getMemberEmail(),
                        memberDTO.getMemberPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        MemberEntity memberEntity = byMemberEmail.get();
        String jwtToken = jwtTokenProvider.generateToken(authentication);
        MemberDTO responseMemberDTO = MemberDTO.toMemberDTO(memberEntity);

        // Create response object
        HashMap<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("memberDTO", responseMemberDTO);

        return ResponseEntity.ok().header("Authorization", "Bearer " + jwtToken).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        if (memberRepository.findByMemberEmail(memberDTO.getMemberEmail()).isPresent()) {
            return new ResponseEntity<>("중복된 이메일이 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        // Create UserEntity
//        MemberEntity newUser = new MemberEntity();
//        newUser.setMemberEmail(memberDTO.getMemberEmail());
//        newUser.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
//        newUser.setElectronic_student_id(memberDTO.getElectronic_student_id());
//        newUser.setMember_info(memberDTO.getMember_info());

        MemberEntity newUser = MemberEntity.toMemberEntity(memberDTO);
        newUser.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));


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

        memberRepository.save(newUser);
        return ResponseEntity.ok().body("회원가입 성공!");
    }
}
