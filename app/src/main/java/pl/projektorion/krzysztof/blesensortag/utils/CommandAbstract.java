package pl.projektorion.krzysztof.blesensortag.utils;


/**
 * Created by krzysztof on 01.11.16.
 */

public abstract class CommandAbstract implements Command {

    /**
     * Determine whether or not iteration should continue
     * automatically without waiting for any callback.
     * @return True if the next command should be processed in series.
     * Otherwise false;
     */
    public boolean autoContinue(){
        return false;
    }
}
