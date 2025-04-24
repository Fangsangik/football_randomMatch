package com.side.football_project.global.util;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.global.security.auth.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsUtil {
   public static User getUser(UserDetails userDetails) {
       return ((CustomUserDetails) userDetails).getUser();
   }
}
