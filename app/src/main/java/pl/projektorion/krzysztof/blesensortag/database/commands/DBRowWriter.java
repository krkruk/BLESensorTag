package pl.projektorion.krzysztof.blesensortag.database.commands;

import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBRowWriter {
    private ConcurrentLinkedQueue<DBRowWriteInterface> writeCmds;
    private SQLiteDatabase db;

    public DBRowWriter(SQLiteDatabase db) {
        this.db = db;
        writeCmds = new ConcurrentLinkedQueue<>();
    }

    public synchronized void add(DBRowWriteInterface writeCmd)
    {
        writeCmds.add(writeCmd);
    }

    public synchronized void write()
    {
        while(!writeCmds.isEmpty()) {
            DBRowWriteInterface dbWrite = writeCmds.poll();
            dbWrite.execute();
        }
    }

    public SQLiteDatabase getWritableDatabase()
    {
        return db;
    }
}
