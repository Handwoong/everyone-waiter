package com.handwoong.everyonewaiter.config.security;

import com.handwoong.everyonewaiter.domain.Member;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class MemberContext extends User {

    public MemberContext(Member member,
            Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
    }
}
