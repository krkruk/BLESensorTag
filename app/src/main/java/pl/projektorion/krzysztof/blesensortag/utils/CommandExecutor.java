package pl.projektorion.krzysztof.blesensortag.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by krzysztof on 01.11.16.
 */

public class CommandExecutor
    implements  CommandIterator {
    private Queue<CommandAbstract> commands;
    private boolean isExecutingRunning = false;

    public CommandExecutor() {
        commands = new LinkedList<>();
    }

    @Override
    public void add(CommandAbstract cmd)
    {
        commands.add(cmd);
        if( !isExecutingRunning ) {
            isExecutingRunning = true;
            nextExecute();
        }
    }

    @Override
    public void continueIteration() {
        if( hasNext() )
            nextExecute();
    }

    @Override
    public void nextExecute() {
        if( !hasNext() ) {
            isExecutingRunning = false;
            return;
        }
        final CommandAbstract cmd = commands.poll();
        cmd.execute();

        if( cmd.autoContinue() )
            nextExecute();
    }

    @Override
    public boolean hasNext() {
        return !commands.isEmpty();
    }

    @Override
    public Object next() {
        return commands.poll();
    }
}
