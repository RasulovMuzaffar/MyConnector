package nm.uty.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Train {
    private String trainIndex;
    private List<Wagon> wagons;
}
