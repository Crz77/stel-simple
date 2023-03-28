package dao.implementations;

import dao.interfaces.IMetricDAO;
import domain.*;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class MetricDAO extends GenericDAO<Metric> implements IMetricDAO {
    public MetricDAO(EntityManager em) {
        super(em, new TypeToken<Metric>(){});
    }


    @Override
    public Collection<Metric> getByApp(Application app) {
        return em.createQuery("select td from TelemetryData td join ApplicationInstance ai " +
                        "                                                         on ai.id = td.senderID" +
                        "                                                         where td.isLogMessage = false" +
                        "                                                 and ai.application.id = :appid", Metric.class)
                .setParameter("appid", app.getId())
                .getResultList();    }

    @Override
    public Collection<Metric> getByAppInstance(ApplicationInstance appInstance) {
        return em.createQuery("select td from TelemetryData td where applicationInstance.id = :appinstanceid " +
                                "and isLogMessage=false"
                        , Metric.class)
                .setParameter("appinstanceid", appInstance.getId()).getResultList();
    }

    @Override
    public Collection<Metric> getByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select td from TelemetryData td where timestamp between :from and :to " +
                        "                                                 and isLogMessage=false ", Metric.class)
                .setParameter("from", from)
                .setParameter("to",to)
                .getResultList();
    }

    @Override
    public void deleteByAppAndTimestamp(Application app, LocalDateTime timestamp) {
        List<TelemetryData> td = em.createQuery("select td from TelemetryData td where td.applicationInstance.application.id = :appid " +
                                                   "and td.timestamp < :timestamp " +
                                                   "and isLogMessage=false ",TelemetryData.class)
                                                   .setParameter("appid",app.getId())
                                                   .setParameter("timestamp", timestamp)
                                                   .getResultList();
        for(var data : td){
            em.remove(data);
        }
    }

    @Override
    public void deleteByAppInstanceAndTimestamp(ApplicationInstance appInstance, LocalDateTime timestamp) {
        List<TelemetryData> td = em.createQuery("select td from TelemetryData td where td.applicationInstance.id = :appid " +
                                                   "and td.timestamp < :timestamp " +
                                                   "and isLogMessage=false ",TelemetryData.class)
                                                   .setParameter("appid",appInstance.getId())
                                                   .setParameter("timestamp", timestamp)
                                                   .getResultList();
        for(var data : td){
            em.remove(data);
        }
    }
}
