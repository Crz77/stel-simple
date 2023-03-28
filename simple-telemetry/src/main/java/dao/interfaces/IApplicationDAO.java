package dao.interfaces;

import domain.*;
import domain.enums.OperatingSystem;
import domain.enums.Platform;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface IApplicationDAO extends IGenericDAOClass<Application> {
    public void startApplicationInstance(IApplicationInstanceDAO appInstanceDao,
                                         ApplicationInstance appInstance);

    public void terminateApplicationInstance(IApplicationInstanceDAO appInstanceDao,
                                             ApplicationInstance appInstance);



}
