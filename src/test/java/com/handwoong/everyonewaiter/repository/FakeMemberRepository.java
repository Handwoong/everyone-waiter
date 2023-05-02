package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public class FakeMemberRepository implements MemberRepository {

    private final Map<Long, Member> members = new HashMap<>();

    private Long sequence = 0L;

    public void clear() {
        members.clear();
    }

    @Override
    public boolean existsByUsernameOrPhoneNumber(String username, String phoneNumber) {
        Optional<Member> findMember = members.values()
                .stream()
                .filter(member ->
                        member.getUsername().equals(username) ||
                                member.getPhoneNumber().equals(phoneNumber))
                .findAny();

        return findMember.isPresent();
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return members.values()
                .stream()
                .filter(member -> member.getUsername().equals(username))
                .findAny();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAllInBatch(Iterable<Member> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Member getOne(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Member getById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Member getReferenceById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> long count(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member, R> R findBy(Example<S> example,
            Function<FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Member> S save(S entity) {
        sequence++;
        members.put(sequence, entity);
        return entity;
    }

    @Override
    public <S extends Member> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Member> findAll() {
        return members.values()
                .stream()
                .toList();
    }

    @Override
    public List<Member> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public long count() {
        return members.size();
    }

    @Override
    public void deleteById(Long memberId) {
        members.remove(memberId);
    }

    @Override
    public void deleteByUsername(String username) {
        for (Entry<Long, Member> member : members.entrySet()) {
            if (member.getValue().getUsername().equals(username)) {
                members.remove(member.getKey());
                break;
            }
        }
    }

    @Override
    public void delete(Member entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAll(Iterable<? extends Member> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Member> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
