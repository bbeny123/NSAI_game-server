package pl.beny.nsai.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.repository.BaseRepository;
import pl.beny.nsai.util.RentalException;

import java.util.List;

@Service
public abstract class BaseService<T> {

    private final BaseRepository<T> repository;

    public BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    @Transactional
    void saveAdmin(UserContext ctx, T data) throws RentalException {
        checkAdmin(ctx);
        save(data);
    }

    @Transactional
    void save(T data) {
        repository.save(data);
    }

    @Transactional
    public T saveAndFlushAdmin(UserContext ctx, T data) throws RentalException {
        checkAdmin(ctx);
        return saveAndFlush(data);
    }

    @Transactional
    T saveAndFlush(T data) {
        return repository.saveAndFlush(data);
    }

    public List<T> findAllAdmin(UserContext ctx) throws RentalException {
        checkAdmin(ctx);
        return findAll();
    }

    private List<T> findAll() {
        return repository.findAll();
    }

    T findOneAdmin(UserContext ctx, Long id) throws RentalException {
        checkAdmin(ctx);
        return findOne(id);
    }


    T findOne(Long id) throws RentalException {
        return repository.findById(id).orElseThrow(() -> new RentalException(RentalException.RentalErrors.ITEM_NOT_EXISTS));
    }

    void checkAdmin(UserContext ctx) throws RentalException {
        if (!ctx.isAdmin()) throw new RentalException(RentalException.RentalErrors.NOT_AUTHORIZED);
    }

}
