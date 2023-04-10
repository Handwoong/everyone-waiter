package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
