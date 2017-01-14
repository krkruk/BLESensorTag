package pl.projektorion.krzysztof.blesensortag.utils.path;

/**
 * Created by krzysztof on 14.01.17.
 */

public interface PathInterface {

    /**
     * Get file name
     * @return {@link String} File name
     */
    String getName();

    /**
     * Get file path. No separator at the end
     * @return {@link String} File path
     */
    String getPath();

    /**
     * Get full file directory. Comprises a path and a name
     * with a separator
     * @return {@link String} Full directory
     */
    String getFull();

}
