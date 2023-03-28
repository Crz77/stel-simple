package dao.implementations;

import dao.interfaces.IIncidentDAO;
import domain.Application;
import domain.ApplicationInstance;
import domain.Incident;
import domain.LogMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class IncidentDAO extends GenericDAO<Incident> implements IIncidentDAO {
    public IncidentDAO(EntityManager em) {
        super(em, new TypeToken<Incident>(){});
    }

    @Override
    public Collection<Incident> getByApp(Application app) {
        return em.createQuery("select i from Incident i where i.metric.applicationInstance.application.id = :appid",Incident.class)
                .setParameter("appid", app.getId())
                .getResultList();
    }

    @Override
    public Collection<Incident> getByAppInstance(ApplicationInstance appInstance) {
        return em.createQuery("select i from Incident i where i.metric.applicationInstance.id = :appinstanceid", Incident.class)
                .setParameter("appinstanceid", appInstance.getId())
                .getResultList();
    }


    //Criteria API Query
    @Override
    public Collection<Incident> getByTimeInterval(LocalDateTime from, LocalDateTime to) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Incident> query = cb.createQuery(Incident.class);
        Root<Incident> root = query.from(Incident.class);
        query.select(root).where(cb.between(root.get("node").get("timeStamp"), from, to));
        return em.createQuery(query).getResultList();
    }

    @Override
    public void deleteByAppAndTimestamp(Application app, LocalDateTime timestamp) {
        List<Incident> iList = em.createQuery("select i from Incident i where i.metric.applicationInstance.application.id = :appid and i.node.timeStamp < :timestamp", Incident.class)
                .setParameter("appid", app.getId())
                .setParameter("timestamp", timestamp)
                .getResultList();

        for(var i : iList){
            em.remove(i);
        }
    }

    @Override
    public void deleteByAppInstanceAndTimestamp(ApplicationInstance appInstance, LocalDateTime timestamp) {
        List<Incident> iList = em.createQuery("select i from Incident i where i.metric.applicationInstance.id = :appid and i.node.timeStamp < :timestamp", Incident.class)
                .setParameter("appid", appInstance.getId())
                .setParameter("timestamp", timestamp)
                .getResultList();

        for(var i : iList){
            em.remove(i);
        }
    }
}
