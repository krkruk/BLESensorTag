package pl.projektorion.krzysztof.blesensortag.bluetooth.reading;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface ProfileReadFactory {

    /**
     * Factory Method Pattern that creates {@link GenericGattReadProfileInterface}
     * @return {@link GenericGattReadModelInterface}
     */
    GenericGattReadProfileInterface createProfile();
}
