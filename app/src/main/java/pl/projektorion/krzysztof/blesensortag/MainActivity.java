package pl.projektorion.krzysztof.blesensortag;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.adapters.MainMenuAdapter;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.database.path.DBPathExternal;
import pl.projektorion.krzysztof.blesensortag.database.path.DBPathInterface;

public class MainActivity extends Activity
    implements AdapterView.OnItemClickListener {

    private ListView mainMenu;
    private List<MainMenuAdapter.MainMenuEntity> menuEntities;
    private MainMenuAdapter menuAdapter;
    private AlertDialog dbDeleteDialog;

    private static final int MENU_CONNECT_TO_DEVICE = 0;
    private static final int MENU_SHOW_ALL_BLE = 1;
    private static final int MENU_SHOW_STETHOSCOPE = 2;
    private static final int MENU_CLEAR_DATABASE = 3;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2345;

    private DialogInterface.OnClickListener onDeleteDatabasePositive = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DBPathInterface dbPath = new DBPathExternal(Constant.DB_NAME, Constant.DB_APP_DIR);
            deleteDatabase(Constant.DB_NAME);
            int msg = deleteDatabase(dbPath.getDbName())
                    ? R.string.toast_erase_db_successful
                    : R.string.toast_erase_db_failed;
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            dbDeleteDialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener onDeleteDatabaseAbort = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, R.string.toast_erase_db_abort,
                    Toast.LENGTH_LONG).show();
            dbDeleteDialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stop_pending_services();
        init_main_menu();
        apply_main_menu();
        check_external_storage_permission();
    }

    @Override
    protected void onStop() {
        if( dbDeleteDialog != null ) dbDeleteDialog.dismiss();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case MENU_CONNECT_TO_DEVICE:
                startActivity(new Intent(this, BLeDeviceScanActivity.class));
                break;
            case MENU_SHOW_ALL_BLE:
                startActivity(new Intent(this, DBSelectRootActivity.class));
                break;
            case MENU_SHOW_STETHOSCOPE:
                Log.i("MENU", "Showing stethoscope data not implemented yet");
                break;
            case MENU_CLEAR_DATABASE:
                dbDeleteDialog.show();
                break;
            default:
                break;
        }
    }

    private void stop_pending_services()
    {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(new Intent(DBServiceBLe.ACTION_STOP_SERVICE));
    }

    private void init_main_menu()
    {
        mainMenu = (ListView) findViewById(R.id.main_menu);
        menuEntities = new ArrayList<>();

        menuEntities.add(new MainMenuAdapter.MainMenuEntity(            //MENU_CONNECT_TO_DEVICE
                getString(R.string.label_connect_to_device)));
        menuEntities.add(new MainMenuAdapter.MainMenuEntity(            //MENU_SHOW_ALL_BLE
                getString(R.string.label_show_all_ble_sensors_data)));
        menuEntities.add(new MainMenuAdapter.MainMenuEntity(            //MENU_SHOW_STETHOSCOPE
                getString(R.string.label_show_stethoscope)));
        menuEntities.add(new MainMenuAdapter.MainMenuEntity(            //MENU_CLEAR_DATABASE
                getString(  //MENU_CLEAR_DATABASE
                R.string.label_delete_database)));

        dbDeleteDialog = create_dialog();
        menuAdapter = new MainMenuAdapter(this, menuEntities);
    }

    private void apply_main_menu()
    {
        mainMenu.setAdapter(menuAdapter);
        mainMenu.setOnItemClickListener(this);
    }

    private AlertDialog create_dialog()
    {
        return new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_erase_title)
                .setMessage(R.string.dialog_erase_db_msg)
                .setPositiveButton(R.string.dialog_erase_positive_button, onDeleteDatabasePositive)
                .setNegativeButton(R.string.dialog_erase_negative_button, onDeleteDatabaseAbort)
                .create();
    }

    private void check_external_storage_permission()
    {
        final int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if( permission != PackageManager.PERMISSION_GRANTED )
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if( grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_external_storage_not_obtained,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
