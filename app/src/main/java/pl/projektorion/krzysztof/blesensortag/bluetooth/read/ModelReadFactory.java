package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface ModelReadFactory {

    /**
     * Factory Method pattern that creates {@link GenericGattReadModelInterface}
     * @return {@link GenericGattReadModelInterface}
     */
    GenericGattReadModelInterface createModel();
}
