package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApplicationInstance {
    @Id
    @GeneratedValue
    private Long id;
    private UUID uuid;
    @OneToMany(mappedBy = "applicationInstance", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<TelemetryData> dataList;

    @org.hibernate.annotations.Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Application application;


    public ApplicationInstance(UUID uuid, Application application) {
        this.uuid = uuid;
        this.application = application;

        dataList = new ArrayList<>();
    }

    public void addTelemetryData(TelemetryData data){
        if (data == null)
            throw new IllegalArgumentException("Data is null");

        if(data.getApplicationInstance() != null)
            data.getApplicationInstance().setApplication(null);

        this.getDataList().add(data);
        data.setApplicationInstance(this);
    }
}
