package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Member> findByUsername(String username);
}
