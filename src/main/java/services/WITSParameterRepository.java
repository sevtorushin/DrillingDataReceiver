package services;

import dao.DaoImpl;
import entities.WITSParameterEntity;
import org.hibernate.SessionFactory;

import java.util.List;

public class WITSParameterRepository extends DaoImpl<WITSParameterEntity> {
    SessionFactory factory;

    public WITSParameterRepository(SessionFactory factory) {
        super(factory);
        this.factory = factory;
    }

    @Override
    public void save(WITSParameterEntity WITSParameterEntity) {
            super.save(WITSParameterEntity);
    }

    @Override
    public WITSParameterEntity get(Long id) {
        return super.get(id);
    }

    @Override
    public List<WITSParameterEntity> getAll() {
        return super.getAll();
    }

    @Override
    public Class<WITSParameterEntity> getType() {
        return WITSParameterEntity.class;
    }
}
