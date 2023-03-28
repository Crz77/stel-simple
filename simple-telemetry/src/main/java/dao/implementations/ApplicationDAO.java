package dao.implementations;

import dao.interfaces.IApplicationDAO;
import dao.interfaces.IApplicationInstanceDAO;
import domain.*;
import domain.enums.OperatingSystem;
import domain.enums.Platform;
import jakarta.persistence.EntityManager;

import java.util.List;


public class ApplicationDAO extends GenericDAO<Application> implements IApplicationDAO {

    public ApplicationDAO(EntityManager em) {
        super(em, new TypeToken<Application>(){});
    }

    @Override
    public void startApplicationInstance(IApplicationInstanceDAO appInstanceDao, ApplicationInstance appInstance){
        appInstanceDao.insertEntity(appInstance);
    }

    @Override
    public void terminateApplicationInstance(IApplicationInstanceDAO appInstanceDao, ApplicationInstance appInstance) {
        appInstanceDao.deleteEntity(appInstance.getId());
    }





}
