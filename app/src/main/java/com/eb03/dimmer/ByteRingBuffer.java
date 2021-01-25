package com.eb03.dimmer;
import java.io.*;
import java.lang.*;
import java.util.Arrays;

/**
 * Buffer circulaire qui permet la sauvegarde et l'envoie des données
 */

public class ByteRingBuffer {

        private int capacity = 0;
        private int size = 0;
        private int head = 0;
        private int tail = -1; // pointer pour parcourir le buffer
        private byte[] array;

        ByteRingBuffer(int capacity)
        {
            this.capacity = capacity;
            array = new byte[capacity];
        }

    /**
     * Ecriture d'un élément
     */

        public void put(byte element) throws Exception
        {
            int index = (tail + 1) % capacity;
            size++;
            if (size == capacity) {
                throw new Exception("Buffer Overflow");
            }
            array[index] = element;
            tail++;
        }

    /**
     *  Lecture d'un élément
     * @return
     * @throws Exception
     */

        public byte get() throws Exception
        {
            if (isEmpty()) {
                throw new Exception("Empty Buffer");

            }
            int index = head % capacity;
            byte element = array[index];
            head++;
            size--;
            return element;
        }

    /**
     * nombre d'octects à lire
     * @return
     */
        public int byteToRead() {
            return size;
        }

    /**
     * Lecture de tous les éléments
     */

        public byte[] getAll() throws Exception {
            byte[] data = new byte[size];
            if (size == 0) {
                throw new Exception("Empty Buffer");

            }else {
                for(int i=0; i<size-1; i++) {
                    data [i]= get();
                }
            }
            return data;
        }

    /**
     * Ecriture d'un tableau d'éléments
     * @param data
     * @throws Exception
     */

        public void putByte(byte[] data) throws Exception {

            int mSize = data.length;
            if (mSize > capacity) {
                throw new Exception(" size of data pass capacity ");
            }else {

                for(int i=0; i<mSize-1; i++) {
                    put(data[i]);
                }
            }
        }

    /**
     * verification de la présence des données
     * @return
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * construction d'un object de type buffer circulaire
      * @return
     */

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("nombre d'octects ");
        sb.append(byteToRead()); 
        return sb.toString();
    }
}


