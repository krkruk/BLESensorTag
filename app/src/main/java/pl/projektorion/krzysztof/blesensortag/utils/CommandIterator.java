package pl.projektorion.krzysztof.blesensortag.utils;

import java.util.Iterator;

/**
 * Created by krzysztof on 01.11.16.
 */

public interface CommandIterator extends Iterator {

    /**
     * Add a command to iterate automatically
     * @param cmd {@link CommandAbstract} A command executor method
     *                                   and a flag method
     */
    void add(CommandAbstract cmd);


    /**
     * Force continue iteration. May have no effect if
     * a stack is empty.
     */
    void continueIteration();


    /**
     * Poll and execute a next command.
     */
    void nextExecute();
}
