package com.example.pillsweight;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class FileSaver {

    public void saveFile(boolean isWritable, File file){
        if (isWritable) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write("Ass \n bass".getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile(boolean isWritable, File file, double[] doubleArray){
        if (isWritable) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                for(double i : doubleArray){
                 fos.write(String.valueOf(i + "\n").getBytes());
                }
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile(boolean isWritable, File file, double[] doubleArray, String[] stringArray){
        if (isWritable) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                String[]composite = new String[stringArray.length];
                for(int i=0; i<doubleArray.length; i++){
                    composite[i] = stringArray[i] + String.valueOf(doubleArray[i]) + "\n";
                    fos.write(composite[i].getBytes());
                }
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFile(boolean isReadable, File file, Context context) {

        if(isReadable) {
            StringBuilder sb = new StringBuilder();
            try {
                FileInputStream fis = new FileInputStream(file);

                if (fis != null) {
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        sb.append(line + "\n");

                    }
                    fis.close();
                }
                Toast.makeText(context, sb, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList loadFile(boolean isReadable, File file, Context context, ArrayList<String> arrayList) {

        if(isReadable) {
            try {
                FileInputStream fis = new FileInputStream(file);

                if (fis != null) {
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        arrayList.add(line);
                    }
                    fis.close();
                }
                if(!arrayList.isEmpty()){
                    Collections.reverse(arrayList);

                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        if (file.exists()){
            file.delete();
        }
        return arrayList;
    }
}
