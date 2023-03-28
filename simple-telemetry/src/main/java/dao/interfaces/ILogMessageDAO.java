package dao.interfaces;

import domain.Application;
import domain.ApplicationInstance;
import domain.LogMessage;
import domain.enums.LogLevel;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ILogMessageDAO extends IGenericDAOClass<LogMessage> {
    public Collection<LogMessage> getByApp(Application app);
    public Collection<LogMessage> getByAppInstance(ApplicationInstance appInstance);
    public Collection<LogMessage> getByTimeInterval(LocalDateTime from, LocalDateTime to);
    public void deleteByAppAndTimestamp(Application app, LocalDateTime timestamp);
    public void deleteByAppInstanceAndTimestamp(ApplicationInstance appInstance, LocalDateTime timestamp);

}
