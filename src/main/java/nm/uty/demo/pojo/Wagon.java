package nm.uty.demo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Wagon implements Serializable {
    private Integer wNumber;
    private Integer netto;
    private Integer tara;
}
