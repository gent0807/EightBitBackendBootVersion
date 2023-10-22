package com.eightbit.biz.user.inter;

import com.eightbit.biz.user.vo.*;

import java.io.IOException;

public interface UserService {
        public String getUserProfileImagePath(String nickname);
        public String getUserRole(String nickname);
        public String getPassword(String nickname);
        public String getAccessToken(String writer);
        public String getRefreshToken(String writer);
        public String alreadyEmailRegisterCheck(String email);
        public String alreadyNickRegisterCheck(String nickname);
        public String insertUser(UserVO userVO) throws IOException;
        public TokenInfo login(UserVO userVO);
        public String alreadyPasswordUsingCheck(UserVO userVO);
        public String updateUserPw(UserVO userVO);
        public void updateToken(TokenInfo tokenInfo);
        public PointVO updatePoint(PointVO pointVO);
        public void deleteUser(String param);

        public String deletePhoneNum(String phoneNum);
        public String checkRightAuthNum(TempVO tempVO);
        public String checkRightPhoneAuthNum(PhoneVO phoneVO);


}
