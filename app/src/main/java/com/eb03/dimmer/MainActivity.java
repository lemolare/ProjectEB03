package com.eb03.dimmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Activité principale du projet
 * permet de faire varier le rapport cyclique depuis le slider
 * permet de vérifier les droits
 * permet de gérer les autorisations
 */

public class MainActivity extends AppCompatActivity {

    private final static int BT_CONNECT_CODE = 1;
    private final static int PERMISSIONS_REQUEST_CODE = 0;
    private final static String[] BT_DANGEROUS_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private TextView mStatus;
    private SeekBar mSlider;
    private OscilloManager mOscilloManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOscilloManager= OscilloManager.getInstance();
        mSlider=findViewById(R.id.slider);
        mStatus = findViewById(R.id.status);
        mSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mOscilloManager.setCallibrationDutyCycle((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        verifyBtRights();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItem = item.getItemId();
        switch(menuItem){
            case R.id.connect:
                Intent BTConnect;
                BTConnect = new Intent(this,BTConnectActivity.class);
                startActivityForResult(BTConnect,BT_CONNECT_CODE);
        }
        return true;
    }

    /**
     * Méthode qui permet de la vérifications des droits bluetooth
     */


    private void verifyBtRights(){
        if(BluetoothAdapter.getDefaultAdapter() == null){
            Toast.makeText(this,"Cette application nécessite un adaptateur BT",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
                requestPermissions(BT_DANGEROUS_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }

    }

    /**
     * Méthode qui permet la gestion des autorisation Bluetooth
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Les autorisations BT sont requises pour utiliser l'application",Toast.LENGTH_LONG).show();
                finish();
                return;
            }

        }
    }

    /**
     * Méthode qui permet de récupérer les données isssues de BTConnectActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BT_CONNECT_CODE:
                if (resultCode == RESULT_OK) {
                    String address = data.getStringExtra("device");
                    mStatus.setText(address);
                }
                break;
            default:
        }

    }
}



