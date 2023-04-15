package services;

import dao.DaoImpl;
import entities.SIBParameterEntity;
import org.hibernate.SessionFactory;

import java.util.List;

public class SIBParameterRepository extends DaoImpl<SIBParameterEntity> {
    SessionFactory factory;

    public SIBParameterRepository(SessionFactory factory) {
        super(factory);
        this.factory = factory;
    }

    @Override
    public void save(SIBParameterEntity SIBParameterEntity) {
            super.save(SIBParameterEntity);
    }

    @Override
    public SIBParameterEntity get(Long id) {
        return super.get(id);
    }

    @Override
    public List<SIBParameterEntity> getAll() {
        return super.getAll();
    }

    @Override
    public Class<SIBParameterEntity> getType() {
        return SIBParameterEntity.class;
    }
}
