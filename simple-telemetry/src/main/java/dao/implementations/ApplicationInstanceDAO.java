package dao.implementations;

import dao.interfaces.IApplicationInstanceDAO;
import domain.*;
import jakarta.persistence.EntityManager;

public class ApplicationInstanceDAO extends GenericDAO<ApplicationInstance> implements IApplicationInstanceDAO {
    public ApplicationInstanceDAO(EntityManager em) {
        super(em, new TypeToken<ApplicationInstance>(){});
    }
}
