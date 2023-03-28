package dao.interfaces;

import domain.Application;
import domain.ApplicationInstance;
import domain.Metric;

import java.time.LocalDateTime;
import java.util.Collection;

public interface IMetricDAO extends IGenericDAOClass<Metric>{
    public Collection<Metric> getByApp(Application app);
    public Collection<Metric> getByAppInstance(ApplicationInstance appInstance);
    public Collection<Metric> getByTimeInterval(LocalDateTime from, LocalDateTime to);
    public void deleteByAppAndTimestamp(Application app, LocalDateTime timestamp);
    public void deleteByAppInstanceAndTimestamp(ApplicationInstance appInstance, LocalDateTime timestamp);
}
