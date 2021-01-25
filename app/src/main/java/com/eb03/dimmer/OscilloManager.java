package com.eb03.dimmer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * pilote de gestion de l'oscilloscope
 */

public class OscilloManager {

    private  Transceiver mTransceiver = null;
    private  static OscilloManager m0scilloManager = null;


    private OscilloManager(){ }

    public static OscilloManager getInstance(){
        if(m0scilloManager==null){
            m0scilloManager = new OscilloManager();
        }

        return m0scilloManager;
    }

   /* public byte[] setCalibrationDutyCycle(float alpha){
        byte[] input = {0x0A,0};
        input[1]=(byte)(alpha);
        return input;
    }*/
    public byte[] setChannel(int numCan, int state){
        byte[] input = {0x0B,(byte)numCan,(byte)state};
        return input;
    }

    public byte[] setVerticalOffset(int channel,int value){
        byte[] input = {0x03,0,0};
        if (channel==0){
            input[1]=0;
        }
        else if(channel==1){
            input[1]=0x01;
        }
        return input;
    }

    /*public void attachTransceiver(String sb){
        BTManager btManager= new BTManager();
        btManager.connect(sb);
    }*/

    /**
     * Méthode peremmettant d'attacher un transceiver
     * @param transceiver
     */
    public void attachTransceiver(Transceiver transceiver) {
        mTransceiver=transceiver;
    }

    /**
     * Méthode permettant d'assurer la connexion entre le tranceiver et le device
     * @param id
     * identifiant du device
     */
    public void connectTrasceiver(String id){
        mTransceiver.connect(id);
    }

    /**
     * Méthode permettant de régler le rapport cyclique
     * @param b
     * b valeur du rapport cyclique à envoyer
     */
    public void setCallibrationDutyCycle(byte b){
        byte[] msg={0x0A, b};
        mTransceiver.send(msg);
    }

}
