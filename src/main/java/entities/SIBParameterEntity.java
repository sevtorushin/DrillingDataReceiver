package entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import utils.DateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SIBData", schema = "sib")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SIBParameterEntity extends ParameterEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Convert(converter = DateTimeConverter.class)
    @Column(name = "local_date_time", nullable = false)
    private LocalDateTime localDateTime;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "value", nullable = false)
    private double value;

    @Basic
    @Column(name = "quality", nullable = false)
    private int quality;

    public SIBParameterEntity(String name, double value, int quality) {
        this.name = name;
        this.value = value;
        this.quality = quality;
    }
}

