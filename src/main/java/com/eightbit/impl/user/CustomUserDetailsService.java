package com.eightbit.impl.user;

import com.eightbit.persistence.user.UserRepository;
import com.eightbit.entity.user.Member;
import com.eightbit.entity.user.User;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository myBatisDAO;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, NullPointerException {
        List<String> roles=new ArrayList<>();
        Optional<Member> memberObject;
        User user=myBatisDAO.findByNickname(username);

        roles.add(user.getRole());

        Member member=new Member();

        member.setMemberId(user.getNickname());
        member.setPassword(user.getPassword());
        member.setRoles(roles);
        memberObject=Optional.of(member);

        return memberObject.map(this::createUserDetails).orElseThrow(()->new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

        private UserDetails createUserDetails(Member member) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(member.getUsername())
                    .password(passwordEncoder.encode(member.getPassword()))
                    .roles(member.getRoles().toArray(new String[0]))
                    .build();
        }
}
