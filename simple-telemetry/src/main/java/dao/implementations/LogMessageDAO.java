package dao.implementations;

import dao.interfaces.ILogMessageDAO;
import domain.Application;
import domain.ApplicationInstance;
import domain.LogMessage;
import domain.TelemetryData;
import domain.enums.LogLevel;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogMessageDAO extends GenericDAO<LogMessage> implements ILogMessageDAO {
    public LogMessageDAO(EntityManager em) {
        super(em, new TypeToken<LogMessage>(){});
    }


    @Override
    public Collection<LogMessage> getByApp(Application app) {
        return em.createQuery("select td from TelemetryData td join ApplicationInstance ai " +
        "                                                         on ai.id = td.senderID" +
        "                                                         where td.isLogMessage = true" +
                "                                                 and ai.application.id = :appid", LogMessage.class)
                              .setParameter("appid", app.getId())
                              .getResultList();
    }

    @Override
    public Collection<LogMessage> getByAppInstance(ApplicationInstance appInstance) {
        return em.createQuery("select td from TelemetryData td where applicationInstance.id = :appinstanceid " +
                                                                 "and isLogMessage=true"
                                                                , LogMessage.class)
                              .setParameter("appinstanceid", appInstance.getId()).getResultList();
    }

    @Override
    public Collection<LogMessage> getByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select td from TelemetryData td where timestamp between :from and :to " +
                "                                                 and isLogMessage=true", LogMessage.class)
                .setParameter("from", from)
                .setParameter("to",to)
                .getResultList();
    }

    @Override
    public void deleteByAppAndTimestamp(Application app, LocalDateTime timestamp) {
        List<TelemetryData> td = em.createQuery("select td from TelemetryData td where td.applicationInstance.application.id = :appid " +
                                                    "and td.timestamp < :timestamp " +
                        "                            and isLogMessage=true",TelemetryData.class)
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
                        "                            and isLogMessage=true",TelemetryData.class)
                                                    .setParameter("appid",appInstance.getId())
                                                    .setParameter("timestamp", timestamp)
                                                    .getResultList();
        for(var data : td){
            em.remove(data);
        }
    }
}
