package com.side.football_project.global.util;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.security.auth.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsUtil {
   public static User getUser(UserDetails userDetails) {
       if (userDetails == null) {
           throw new IllegalArgumentException("UserDetails가 null입니다. 인증이 필요한 요청입니다.");
       }
       if (!(userDetails instanceof CustomUserDetails)) {
           throw new IllegalArgumentException("UserDetails가 CustomUserDetails 타입이 아닙니다.");
       }
       return ((CustomUserDetails) userDetails).getUser();
   }
}
