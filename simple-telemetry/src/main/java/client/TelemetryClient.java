package client;

import dao.implementations.*;
import dao.interfaces.*;
import domain.*;
import domain.enums.LogLevel;
import domain.enums.OperatingSystem;
import domain.enums.Platform;
import domain.helpers.MetricNode;
import lombok.extern.java.Log;
import services.CPUService;


import javax.xml.datatype.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static util.JpaUtil.*;

public class TelemetryClient {


    public static void main(String[] args) {
        List<OperatingSystem> supportedOS = new ArrayList<>();
        supportedOS.add(OperatingSystem.Windows);
        supportedOS.add(OperatingSystem.IOs);

        Application app = new Application(Platform.DOT_NET, supportedOS);

        System.out.println("======================================================");
        System.out.println("                 Create an Application                ");
        System.out.println("======================================================");

        executeInTransaction(em -> {
            IApplicationDAO appDao = new ApplicationDAO(em);

            System.out.println("--------------- insert Application ------------------");
            appDao.insertEntity(app);
            System.out.println();

            System.out.println("--------------- insert successful ------------------");
            System.out.println(appDao.getAllEntities());
            System.out.println();
        });


        executeInTransaction(em -> {
            IApplicationDAO appDao = new ApplicationDAO(em);
            var app2 = em.find(Application.class, app.getId());

            System.out.println("--------------- update Application ------------------");
            app2.setPlatform(Platform.JAVA);
            app2 = appDao.updateEntity(app2);
            System.out.println();

            System.out.println("--------------- update successful ------------------");
            System.out.println(appDao.getAllEntities());
            System.out.println();

        });


        System.out.println("======================================================");
        System.out.println("           Start an Application Instance             ");
        System.out.println("======================================================");
        System.out.println();

        ApplicationInstance appInstance1 = new ApplicationInstance(UUID.randomUUID(), app);
        ApplicationInstance appInstance2 = new ApplicationInstance(UUID.randomUUID(), app);

        executeInTransaction(em -> {
            IApplicationDAO appDao = new ApplicationDAO(em);
            IApplicationInstanceDAO appInstanceDao = new ApplicationInstanceDAO(em);

            System.out.println("--------------- starting new application instance 1 ------------------");
            appDao.startApplicationInstance(appInstanceDao, appInstance1);
            app.addApplicationInstance(appInstance1);
            appDao.updateEntity(app);
            System.out.println();

            System.out.println("--------------- starting successful ------------------");
            for(var i : app.getInstances()){
                System.out.println(i);
            }
            System.out.println();
        });

        executeInTransaction(em -> {
            IApplicationDAO appDao = new ApplicationDAO(em);
            IApplicationInstanceDAO appInstanceDao = new ApplicationInstanceDAO(em);

            System.out.println("--------------- starting new application instance 2 ------------------");
            appDao.startApplicationInstance(appInstanceDao, appInstance2);
            app.addApplicationInstance(appInstance2);
            appDao.updateEntity(app);
            System.out.println();

            System.out.println("--------------- starting successful ------------------");
            for(var i : app.getInstances()){
                System.out.println(i);
            }
            System.out.println();

        });


        System.out.println("======================================================");
        System.out.println("               Create some Log Messages               ");
        System.out.println("======================================================");
        System.out.println();

        LogMessage lm1 = new LogMessage(appInstance1.getId(), "logData1", LocalDateTime.now(), "commonName1", true, LogLevel.TRACE, "data is trace");
        LogMessage lm2 = new LogMessage(appInstance1.getId(), "logData2", LocalDateTime.now(), "commonName1", true, LogLevel.WARNING, "data is warning");

        executeInTransaction(em -> {
            IApplicationDAO appDao = new ApplicationDAO(em);
            IApplicationInstanceDAO appInstanceDao = new ApplicationInstanceDAO(em);
            ILogMessageDAO lmDao = new LogMessageDAO(em);

            System.out.println("--------------- insert LogMessage ------------------");
            lmDao.insertEntity(lm1);
            lmDao.insertEntity(lm2);
            appInstance1.addTelemetryData(lm1);
            appInstance1.addTelemetryData(lm2);

            appInstanceDao.updateEntity(appInstance1);
            System.out.println();
            System.out.println();

            System.out.println("--------------- insert successful ------------------");
            for (var i : appInstance1.getDataList()){
                System.out.println(i);
            }
            System.out.println();
            System.out.println();



        });

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                  Create some Metrics                 ");
        System.out.println("======================================================");
        System.out.println();

        CPUService cpuService = new CPUService();

        Metric met1 = new Metric(appInstance1.getId(), "metric1", LocalDateTime.now(), "commonName1", false, "CPUUsage");
        Metric met2 = new Metric(appInstance1.getId(), "metric2", LocalDateTime.now(), "commonName1", false, "FreeMemorySpace");

        executeInTransaction(em -> {
            IApplicationDAO appDAO = new ApplicationDAO(em);
            IApplicationInstanceDAO appInstanceDao = new ApplicationInstanceDAO(em);
            IMetricDAO metricDAO = new MetricDAO(em);


            System.out.println("--------------- insert Metrics ------------------");
            metricDAO.insertEntity(met1);
            metricDAO.insertEntity(met2);
            System.out.println();

            appInstance1.addTelemetryData(met1);
            appInstance1.addTelemetryData(met2);

            appInstanceDao.updateEntity(appInstance1);

            System.out.println("--------------- insert successful ------------------");
            for (var i : appInstance1.getDataList()){
                System.out.println(i);
            }


            System.out.println("--------------- insert Metric Data ------------------");

            for(int i = 0; i<5; ++i){
                MetricNode node1 = new MetricNode(cpuService.getCpuUsage(), LocalDateTime.now());
                met1.addMetricNode(node1);

                MetricNode node2 = new MetricNode(cpuService.getFreeMemorySpace(), LocalDateTime.now());
                met2.addMetricNode(node2);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            metricDAO.updateEntity(met1);
            metricDAO.updateEntity(met2);

            appInstanceDao.updateEntity(appInstance1);

            System.out.println("--------------- insert successful ------------------");
            System.out.println(met1.getMetricName());
            for (var v : met1.getValues()){
                System.out.println(v.parseTimeStampToString() + ": " + v.getValue() + " %");
            }
            System.out.println();

            System.out.println(met2.getMetricName());
            for (var v : met2.getValues()){
                System.out.println(v.parseTimeStampToString() + ": " + v.getValue() + " GB");
            }

        });


        System.out.println();
        System.out.println("======================================================");
        System.out.println("                  Create some Detectors                 ");
        System.out.println("======================================================");
        System.out.println();

        Detector cpuUsageDetector = new Detector("CpuUsageDetector", 2D, 5D, 6000D);
        Detector memUsageDetector = new Detector("MemUsageDetector", 2D, 5D, 6000D);

        executeInTransaction(em -> {
            IDetectorDAO detectorDAO = new DetectorDAO(em);

            System.out.println("--------------- insert Detectors ------------------");
            detectorDAO.insertEntity(cpuUsageDetector);
            detectorDAO.insertEntity(memUsageDetector);
            System.out.println();

            System.out.println("--------------- insert successful ------------------");
            for (var i : detectorDAO.getAllEntities()){
                System.out.println(i);
            }
            System.out.println();

            System.out.println("--------------- add Metrics to Detectors ------------------");
            Metric m1 = em.find(Metric.class, 3);
            Metric m2 = em.find(Metric.class, 4);

            cpuUsageDetector.addMetric(m1);
            memUsageDetector.addMetric(m2);

            detectorDAO.updateEntity(cpuUsageDetector);
            detectorDAO.updateEntity(memUsageDetector);

            System.out.println("--------------- update successful ------------------");
            System.out.println(cpuUsageDetector.getName() + " includes " + cpuUsageDetector.getMetric().getName());
            System.out.println(memUsageDetector.getName() + " includes " + memUsageDetector.getMetric().getName());

        });

        System.out.println();
        System.out.println("======================================================");
        System.out.println("                Create some Incidents                 ");
        System.out.println("======================================================");
        System.out.println();

        Incident incident1 = new Incident(cpuUsageDetector,  met1.getValues().get(3));
        Incident incident2 = new Incident(memUsageDetector,  met2.getValues().get(4));

        executeInTransaction(em -> {
            IIncidentDAO incidentDAO = new IncidentDAO(em);

            System.out.println("--------------- insert Incidents ------------------");
            incidentDAO.insertEntity(incident1);
            incidentDAO.insertEntity(incident2);
            System.out.println();

            System.out.println("--------------- insert successful ------------------");
            for (var i : incidentDAO.getAllEntities()){
                System.out.println(i + ":");
                System.out.println("Detector: " + i.getDetector().getName());
                System.out.println("Timestamp: " + i.getNode().parseTimeStampToString());
                System.out.println("Value: " + i.getNode().getValue());
                System.out.println();
            }
            System.out.println();

        });
        }
}
