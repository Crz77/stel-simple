package domain;

import domain.helpers.MetricNode;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.FetchMode;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Incident {
    @Id
    @GeneratedValue
    private Long id;
    @org.hibernate.annotations.Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Detector detector;
//    @OneToOne
//    @ToString.Exclude
//    private Metric metric;
    @ToString.Exclude
    private MetricNode node;

//    public Incident(Detector detector, Metric metric, MetricNode node){
//        this.detector = detector;
//        this.metric = metric;
//        this.node = node;
//
//        this.detector.getIncidents().add(this);
//    }

    public Incident(Detector detector, MetricNode node){
        this.detector = detector;
        this.node = node;

        this.detector.getIncidents().add(this);
    }

}
