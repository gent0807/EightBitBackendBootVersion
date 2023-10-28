package com.eightbit.inter.user;

import com.eightbit.entity.user.TokenInfo;
import com.eightbit.entity.user.User;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {

        public TokenInfo login(User user);
        public String insertUser(User user) throws IOException;

}
