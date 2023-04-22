package com.handwoong.everyonewaiter.config.security;

import com.handwoong.everyonewaiter.domain.Member;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    public CustomUserDetails(Member member,
            Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
    }
}
