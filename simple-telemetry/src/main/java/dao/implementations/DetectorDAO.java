package dao.implementations;

import dao.interfaces.IDetectorDAO;
import domain.Detector;
import domain.Metric;
import domain.helpers.MetricNode;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DetectorDAO extends GenericDAO<Detector> implements IDetectorDAO {

    public DetectorDAO(EntityManager em) {
        super(em, new TypeToken<Detector>(){});
    }

    @Override
    public List<MetricNode> getAllNodesOlderThan(LocalDateTime timestamp) {
        List<MetricNode> nodes = em.createQuery("select m.values from Metric m", MetricNode.class).getResultList();

        List<MetricNode> resNodes = new ArrayList<>();

        for(var n : nodes){
            if(n.getTimeStamp().isAfter(timestamp) )
                resNodes.add(n);
        }
        return resNodes;
    }

    @Override
    public List<MetricNode> getAllNodesYoungerThan(LocalDateTime timestamp) {
        List<MetricNode> nodes = em.createQuery("select m.values from Metric m", MetricNode.class).getResultList();

        List<MetricNode> resNodes = new ArrayList<>();

        for(var n : nodes){
            if(n.getTimeStamp().isBefore(timestamp) )
                resNodes.add(n);
        }
        return resNodes;
    }

    @Override
    public List<MetricNode> getAllNodesForMetric(Metric metric) {
        return em.createQuery("select m.values from Metric m where m.id = :mid", MetricNode.class)
                .setParameter("mid", metric.getId())
                .getResultList();
    }
}
