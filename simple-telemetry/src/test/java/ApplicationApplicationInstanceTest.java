import dao.implementations.ApplicationDAO;
import dao.implementations.ApplicationInstanceDAO;
import domain.Application;
import domain.ApplicationInstance;
import domain.Metric;
import domain.enums.OperatingSystem;
import domain.enums.Platform;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationApplicationInstanceTest {
    private EntityManager em = Persistence.createEntityManagerFactory("TelemetryPU").createEntityManager();
    private static Application application;
    private static ApplicationInstance applicationInstance;

    private ApplicationDAO appDao = new ApplicationDAO(em);
    private ApplicationInstanceDAO applicationInstanceDAO = new ApplicationInstanceDAO(em);

    @BeforeEach
    void setUP(){
        List<OperatingSystem> supportedOS = new ArrayList<>();
        supportedOS.add(OperatingSystem.Windows);
        supportedOS.add(OperatingSystem.IOs);
        application = new Application(Platform.JAVA, supportedOS);
        applicationInstance = new ApplicationInstance(UUID.randomUUID(), application);
    }


    @Test
    public void insertApplicationEntitySuccess(){
        //arrange
        appDao.insertEntity(application);

        //act
        Application foundApp = appDao.getEntityByID(application.getId());

        //assert
        assertEquals(application, foundApp);

        //clean up
        appDao.deleteEntity(application.getId());
    }

    @Test
    public void insertApplicationEntityFailure(){
        // arrange
        Application application = new Application();
        application.setId(1L);
        application.setPlatform(Platform.F_SHARP);

        // act
        try {
            appDao.insertEntity(application);
            fail("Expected DataAccessException was not thrown");
        } catch (Exception ex) {
            // assert
            assertNotNull(ex.getMessage());
        }

        //clean up
        appDao.deleteEntity(application.getId());
    }

    @Test
    public void insertApplicationAndCreateInstanceSuccess(){
        //arrange
        appDao.insertEntity(application);
        applicationInstanceDAO.insertEntity(applicationInstance);


        //act
        ApplicationInstance foundApp = applicationInstanceDAO.getEntityByID(applicationInstance.getId());

        //assert
        assertEquals(applicationInstance, foundApp);

        //clean up
        applicationInstanceDAO.deleteEntity(applicationInstance.getId());
        appDao.deleteEntity(application.getId());
    }

    @Test
    public void startApplicationInstanceSuccess(){
        //arrange
        appDao.insertEntity(application);
        appDao.startApplicationInstance(applicationInstanceDAO, applicationInstance);

        //act
        ApplicationInstance appIns1 = applicationInstanceDAO.getEntityByID(applicationInstance.getId());

        //assert
        assertNotNull(appIns1);
    }

    @Test
    public void terminateApplicationInstanceSuccess(){
        //arrange
        appDao.insertEntity(application);
        appDao.startApplicationInstance(applicationInstanceDAO, applicationInstance);

        //act
        appDao.terminateApplicationInstance(applicationInstanceDAO, applicationInstance);
        ApplicationInstance appIns1 = applicationInstanceDAO.getEntityByID(applicationInstance.getId());

        //assert
        assertNull(appIns1);
    }

    @Test
    public void updateAppAndAllInstancesSuccess(){
        //arrange
        ApplicationInstance applicationInstance2 = new ApplicationInstance(UUID.randomUUID(), application);

        appDao.insertEntity(application);
        appDao.startApplicationInstance(applicationInstanceDAO, applicationInstance);
        appDao.startApplicationInstance(applicationInstanceDAO, applicationInstance2);

        //act
        application.setPlatform(Platform.F_SHARP);
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        applicationInstance.setUuid(uuid1);
        applicationInstance2.setUuid(uuid2);

        //assert
        assertEquals(application.getPlatform(), Platform.F_SHARP);
        assertEquals(applicationInstance.getUuid(), uuid1);
        assertEquals(applicationInstance2.getUuid(), uuid2);
    }

}
