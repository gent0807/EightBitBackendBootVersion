package com.eightbit.biz.user.impl;

import com.eightbit.biz.user.persistence.UserMyBatisDAO;
import com.eightbit.biz.user.vo.MemberVO;
import com.eightbit.biz.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMyBatisDAO myBatisDAO;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, NullPointerException {
        List<String> roles=new ArrayList<>();
        Optional<MemberVO> memberObject;
        UserVO user=myBatisDAO.findByNickname(username);

        roles.add(user.getRole());

        MemberVO member=new MemberVO();

        member.setMemberId(user.getNickname());
        member.setPassword(user.getPassword());
        member.setRoles(roles);
        memberObject=Optional.of(member);

        return memberObject.map(this::createUserDetails).orElseThrow(()->new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

        private UserDetails createUserDetails(MemberVO member) {
            return User.builder()
                    .username(member.getUsername())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .roles(member.getRoles().toArray(new String[0]))
                    .build();
        }
}
