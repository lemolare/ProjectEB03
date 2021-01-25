package com.eb03.dimmer;

/**
 * Classe qui assure l'encodage et le décodage des trames
 */

public class FrameProcessor {

    public StringBuilder str=new StringBuilder("");

    /**
     * décodage des trames
     * @param trameFromOscillo
     * @return
     */
    public byte[] fromFrame(byte[] trameFromOscillo) {
        int begin =0;
        int end =0;
        byte[] resultWith0x06=new byte[1024];
        byte[] resultWithout0x06=new byte[1024];
        for(int i=0;i<trameFromOscillo.length;i++){
            if(trameFromOscillo[i]==(byte)0x8F) {
                begin=i;
            }
            if(trameFromOscillo[i]==(byte)0x04) {
                end=i;
                break;
            }

        }
        int size=end-begin;
        for (int j=0;j<size-1;j++){
            resultWith0x06[j]=trameFromOscillo[begin+1];
            begin++;
        }
        resultWithout0x06=toUnechap(resultWith0x06);
        return resultWithout0x06;
    }

    /**
     * Encodage de la trame
     * @param commande
     * @return
     */
    // On reçoit en paramètres un tableau de byte contenant la commande
    public byte[] toFrame(byte[] commande) {
        // On construit la trame
        byte header = 0x05;
        byte tail = 0x04;
        // length indique la taille de la commande
        byte[] length = {0x00, (byte) commande.length};
        //byte[] payload = toEchap(commande);
        int sum =length[1] +toSumTab(commande);
        byte ctrl = (byte) unsignedToBytes((byte)Integer.parseInt(toComplement2(Integer.toHexString(sum)),16));
        byte[] preEchap = new byte[length.length+commande.length+1];
        preEchap[0]=length[0];
        preEchap[1]=length[1];
        int j=2;
        for(byte b:commande){
            preEchap[j]=b;
            j++;
        }
        preEchap[j]=ctrl;

        byte[] payload = toEchap(preEchap);
        byte[] frame = new byte[2 + payload.length];


        // On assemble tous les éléments de la frame pour la construire
        frame[0] = header;
        int i=1;
        for(byte b:payload){
            frame[i]=b;
            i++;
        }
        frame[i]=tail;

        if (str.length()>frame.length){
            str.setLength(0);
        }
        for (byte b : frame) {
            str.append(String.format("%02X ", b));
        }

        return frame;
    }

    /**
     * Méthode qui Renvoie le complement à deux
     * @param hex
     * @return
     */

    String toComplement2(String hex){
        int i = Integer.parseInt(hex, 16);
        i = i%256;
        int result=256+~i +1;

        return  Integer.toHexString(result);

    }

    /**
     *  Méthode qui Permet de sommer les octets contenu dans un tableau de byte
     * @param tab
     * @return
     */

    int toSumTab(byte[] tab){
        int total = 0 ;
        for (int val : tab ) {
            total += (int) val;
        }
        return total;
    }
    private byte[] toUnechap(byte[] b) {
        int i = 0;
        for (byte bi : b) {
            if (bi == 0x06) {
                i++;
            }
        }
        byte[] result = new byte[b.length - i];
        int j = 0;
        for (int k = 0; k < b.length; k++) {

            if (b[k] == 0x06) {

                result[j] = (byte) (b[k + 1] - 0x06);
                j++;
                k++;
            } else {
                result[j] = b[k];
                j++;
            }
            if (j == result.length) {
                break;
            }
        }
        return result;
    }

    /**
     * Mérhode qui permet d'inserer un caractère d'ecchappemet devant les bytes réservés ,
     * ici 0x04,0x05,0x06. Rajoute 0x06 au carcatère suivant et renvoie le tableau de byte échappé
     * @param b
     * @return
     */

    private byte[] toEchap(byte[] b){
        int i=0;
        for (byte bi: b) {
            if (bi == 0x06 || bi == 0x04 || bi == 0x05) {
                i++;
            }
        }
        byte[] result= new byte[b.length+i];
        i=0;
        for (byte bi:b){

            if(bi== 0x06 || bi == 0x04 || bi == 0x05){
                result[i]=0x06;
                result[i+1]=(byte) (bi +0x06);
                i+=2;
            }else{
                result[i]=bi;
                i++;
            }
        }
        return result;
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }


}
