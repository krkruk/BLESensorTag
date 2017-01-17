package pl.projektorion.krzysztof.blesensortag.math.interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by krzysztof on 17.01.17.
 */

public interface MDerivativeMask {
    /*
    Retrieved from https://youtu.be/SYVrLncRYlU?t=22m38s
     */
    List<Double> BACKWARD_DIFFERENCE = new ArrayList<>(Arrays.asList(-1.0, 1.0));
    List<Double> CENTRAL_DIFFERENCE = new ArrayList<>(Arrays.asList(-1.0, 0.0, 1.0));
    List<Double> FORWARD_DIFFERENCE = new ArrayList<>(Arrays.asList(1.0, -1.0));
}
