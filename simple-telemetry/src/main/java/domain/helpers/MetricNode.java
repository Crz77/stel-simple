package domain.helpers;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class MetricNode {
    private Double value;
    private LocalDateTime timeStamp;

    public MetricNode(Double value, LocalDateTime timeStamp){
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public String parseTimeStampToString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        return timeStamp.format(formatter);
    }

}
