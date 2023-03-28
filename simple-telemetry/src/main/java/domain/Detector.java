package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Detector {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    @Column(name = "minValue")
    private Double min;
    @Column(name = "maxValue")
    private Double max;
    private Double interval;
    @OneToOne
    @ToString.Exclude
    private Metric metric;
    @OneToMany(mappedBy = "detector", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Incident> incidents = new ArrayList<>();
    private LocalDateTime lastNode = LocalDateTime.now();


    public Detector(String name, Double min, Double max, Double interval) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.interval = interval;
    }

    public void addIncident(Incident incident){
        if(incident == null) throw new IllegalArgumentException("Incident is null");

        incidents.add(incident);
    }

    public void addMetric(Metric metric) {
        if(metric == null)
            throw new IllegalArgumentException("Metric is null");

        if(metric.getDetector() != null)
            metric.getDetector().setMetric(null);

        this.metric = metric;
        metric.setDetector(this);
    }
}
