package com.hansol.board.mock;

import com.hansol.board.attachment.domain.Attachment;
import com.hansol.board.attachment.repository.AttachmentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class EmptyAttachmentRepository implements AttachmentRepository {
    @Override
    public void flush() {

    }

    @Override
    public <S extends Attachment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Attachment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Attachment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Attachment getOne(Long aLong) {
        return null;
    }

    @Override
    public Attachment getById(Long aLong) {
        return null;
    }

    @Override
    public Attachment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Attachment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Attachment> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Attachment> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Attachment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Attachment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Attachment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Attachment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Attachment> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Attachment> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Attachment> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Attachment> findAll() {
        return null;
    }

    @Override
    public List<Attachment> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Attachment entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Attachment> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Attachment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Attachment> findAll(Pageable pageable) {
        return null;
    }
}
