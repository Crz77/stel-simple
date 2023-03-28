package dao.interfaces;

import domain.Application;
import domain.ApplicationInstance;
import domain.Incident;
import domain.LogMessage;

import java.time.LocalDateTime;
import java.util.Collection;

public interface IIncidentDAO extends IGenericDAOClass<Incident>{
    public Collection<Incident> getByApp(Application app);
    public Collection<Incident> getByAppInstance(ApplicationInstance appInstance);
    public Collection<Incident> getByTimeInterval(LocalDateTime from, LocalDateTime to);
    public void deleteByAppAndTimestamp(Application app, LocalDateTime timestamp);
    public void deleteByAppInstanceAndTimestamp(ApplicationInstance appInstance, LocalDateTime timestamp);
}
