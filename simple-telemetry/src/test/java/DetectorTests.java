import dao.implementations.ApplicationDAO;
import dao.implementations.ApplicationInstanceDAO;
import dao.implementations.DetectorDAO;
import dao.implementations.MetricDAO;
import domain.Application;
import domain.ApplicationInstance;
import domain.Metric;
import domain.enums.OperatingSystem;
import domain.enums.Platform;
import domain.helpers.MetricNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DetectorTests {
    private EntityManager em = Persistence.createEntityManagerFactory("TelemetryPU").createEntityManager();
    private static Application application;
    private static ApplicationInstance applicationInstance;

    private MetricDAO metricDAO;
    private DetectorDAO detectorDAO;

    private ApplicationDAO appDao = new ApplicationDAO(em);
    private ApplicationInstanceDAO applicationInstanceDAO = new ApplicationInstanceDAO(em);

    @BeforeEach
    void setUP(){
        List<OperatingSystem> supportedOS = new ArrayList<>();
        supportedOS.add(OperatingSystem.Windows);
        supportedOS.add(OperatingSystem.IOs);
        application = new Application(Platform.JAVA, supportedOS);
        applicationInstance = new ApplicationInstance(UUID.randomUUID(), application);
        applicationInstanceDAO.insertEntity(applicationInstance);
        metricDAO = new MetricDAO(em);
        detectorDAO = new DetectorDAO(em);
    }

    @Test
    public void getAllNodesOlderThanSuccess(){
        //arrange
        Metric metric = new Metric(applicationInstance.getId(), "Metric1", LocalDateTime.now(), "commonname1", false, "metricName1");
        metricDAO.insertEntity(metric);

        MetricNode node1 = new MetricNode(22.2, LocalDateTime.now());
        MetricNode node2 = new MetricNode(24.7, LocalDateTime.now().plusDays(7));
        MetricNode node3 = new MetricNode(27.2, LocalDateTime.now().plusDays(7));

        metric.addMetricNode(node1);
        metric.addMetricNode(node2);
        metric.addMetricNode(node3);
        metricDAO.updateEntity(metric);

        List<MetricNode> nodes = detectorDAO.getAllNodesOlderThan(LocalDateTime.now());

        Assertions.assertEquals(2, nodes.size());
    }

    @Test
    public void getAllNodesYoungerThanSuccess(){
        //arrange
        Metric metric = new Metric(applicationInstance.getId(), "Metric1", LocalDateTime.now(), "commonname1", false, "metricName1");
        metricDAO.insertEntity(metric);

        MetricNode node1 = new MetricNode(22.2, LocalDateTime.now());
        MetricNode node2 = new MetricNode(24.7, LocalDateTime.now().minusDays(7));
        MetricNode node3 = new MetricNode(27.2, LocalDateTime.now().minusDays(7));

        metric.addMetricNode(node1);
        metric.addMetricNode(node2);
        metric.addMetricNode(node3);
        metricDAO.updateEntity(metric);

        List<MetricNode> nodes = detectorDAO.getAllNodesYoungerThan(LocalDateTime.now());

        Assertions.assertEquals(2, nodes.size());
    }

    @Test
    public void getAllNodesForMetricSuccess(){
        //arrange
        Metric metric = new Metric(applicationInstance.getId(), "Metric1", LocalDateTime.now(), "commonname1", false, "metricName1");
        metricDAO.insertEntity(metric);

        MetricNode node1 = new MetricNode(22.2, LocalDateTime.now());
        MetricNode node2 = new MetricNode(24.7, LocalDateTime.now().minusDays(7));
        MetricNode node3 = new MetricNode(27.2, LocalDateTime.now().minusDays(7));

        metric.addMetricNode(node1);
        metric.addMetricNode(node2);
        metric.addMetricNode(node3);
        metricDAO.updateEntity(metric);

        List<MetricNode> nodes = detectorDAO.getAllNodesForMetric(metric);

        Assertions.assertEquals(3, nodes.size());
    }

}
