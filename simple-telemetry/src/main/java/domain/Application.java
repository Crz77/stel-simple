package domain;

import domain.enums.OperatingSystem;
import domain.enums.Platform;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Application {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Enumerated(EnumType.STRING)
    private List<OperatingSystem> supportedOS;
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ApplicationInstance> instances;

    public Application(Platform platform, List<OperatingSystem> supportedOS){
        this.platform = platform;
        this.supportedOS = supportedOS;

        this.instances = new ArrayList<>();
    }

    public void addApplicationInstance(ApplicationInstance instance){
        if (instance == null)
            throw new IllegalArgumentException("Instance is null");

        if(instance.getApplication() != null)
            instance.getApplication().getInstances().remove(instance);

        this.getInstances().add(instance);
        instance.setApplication(this);
    }
}
