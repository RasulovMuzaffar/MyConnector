package nm.uty.demo.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Wagon implements Serializable {
    private Integer no;
    private Integer wNumber;
    private Integer netto;
    private Integer tara;
    private Integer destinationStation;
    private Integer cargoCode;
}
