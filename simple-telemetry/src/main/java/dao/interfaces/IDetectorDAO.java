package dao.interfaces;

import domain.Detector;
import domain.Metric;
import domain.helpers.MetricNode;

import java.time.LocalDateTime;
import java.util.List;

public interface IDetectorDAO extends IGenericDAOClass<Detector>{
    List<MetricNode> getAllNodesOlderThan(LocalDateTime timestamp);
    List<MetricNode> getAllNodesYoungerThan(LocalDateTime timestamp);
    List<MetricNode> getAllNodesForMetric(Metric metric);

}
