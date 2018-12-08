package pl.beny.nsai.service;

import org.springframework.transaction.annotation.Transactional;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.repository.BaseRepository;
import pl.beny.nsai.util.GamesException;

import java.util.List;

public abstract class BaseService<T, U extends BaseRepository<T>> {

    private final U repository;

    public BaseService(U repository) {
        this.repository = repository;
    }

    @Transactional
    protected void save(T data) {
        repository.save(data);
    }

    @Transactional
    protected void saveAdmin(UserContext ctx, T data) throws GamesException {
        checkAdmin(ctx);
        save(data);
    }

    @Transactional
    protected T saveAndFlushAdmin(UserContext ctx, T data) throws GamesException {
        checkAdmin(ctx);
        return saveAndFlush(data);
    }

    @Transactional
    protected T saveAndFlush(T data) {
        return repository.saveAndFlush(data);
    }

    public List<T> findAllAdmin(UserContext ctx) throws GamesException {
        checkAdmin(ctx);
        return findAll();
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findOne(Long id) throws GamesException {
        return repository.findById(id).orElseThrow(() -> new GamesException(GamesException.GamesErrors.ITEM_NOT_EXISTS));
    }

    public T findOneAdmin(UserContext ctx, Long id) throws GamesException {
        checkAdmin(ctx);
        return findOne(id);
    }

    protected void checkAdmin(UserContext ctx) throws GamesException {
        if (!ctx.isAdmin()) throw new GamesException(GamesException.GamesErrors.FORBIDDEN);
    }

    protected U getRepository() {
        return repository;
    }

}