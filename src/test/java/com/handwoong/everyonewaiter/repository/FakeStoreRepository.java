package com.handwoong.everyonewaiter.repository;

import com.handwoong.everyonewaiter.domain.Store;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public class FakeStoreRepository implements StoreRepository {

    private final Map<Long, Store> stores = new HashMap<>();

    private Long sequence = 0L;

    public void clear() {
        stores.clear();
    }

    @Override
    public boolean existTelNumber(String username, String telNumber) {
        Optional<Store> findStore = stores.values()
                .stream()
                .filter(store -> store.getMember().getUsername().equals(username))
                .filter(store -> store.getTelephoneNumber().equals(telNumber))
                .findAny();
        return findStore.isPresent();
    }

    @Override
    public Optional<Store> findMemberStore(String username, Long storeId) {
        Store findStore = stores.get(storeId);

        if (findStore != null && findStore.getMember().getUsername().equals(username)) {
            return Optional.of(findStore);
        }

        return Optional.empty();
    }

    @Override
    public Store findStoreById(Long storeId) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Store> findAllByMemberId(Long memberId) {
        return stores.values().stream().toList();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAllInBatch(Iterable<Store> entities) {
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
    public Store getOne(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Store getById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Store getReferenceById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> long count(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store, R> R findBy(Example<S> example,
            Function<FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public <S extends Store> S save(S entity) {
        sequence++;
        stores.put(sequence, entity);
        return entity;
    }

    @Override
    public <S extends Store> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Optional<Store> findById(Long aLong) {
        return Optional.ofNullable(stores.get(aLong));
    }

    @Override
    public boolean existsById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Store> findAll() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Store> findAllById(Iterable<Long> longs) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public long count() {
        return stores.size();
    }

    @Override
    public void deleteById(Long aLong) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void delete(Store entity) {
        stores.remove(sequence);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAll(Iterable<? extends Store> entities) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public List<Store> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public Page<Store> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
