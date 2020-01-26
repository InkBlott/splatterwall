package com.example.pillsweight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    TextView textViewDate;
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    LinearLayout linLay;
    LinearLayout.LayoutParams layParam;
    Button deleteBtn;
    Button addBtn;
    Button average;
    EditText editTextEnterWght;
    FileSaver saver; //delete mark
    File weightTrackerSaveFile;
    ScrollView scrollView;
    ArrayList<String> fileSaverArrayListDoubles;
    ArrayList<String> dateArrayList;
    double[] doubleArray;
    int viewId = 0;
    double all = 0.0;
    double med;
    final String FILENAME = "weightTrackerSave.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weightTrackerSaveFile = new File (getExternalFilesDir(null), FILENAME);
        scrollView = (ScrollView) findViewById((R.id.sView));
        linLay = (LinearLayout) findViewById(R.id.linLay);
        editTextEnterWght = (EditText) findViewById(R.id.editTextEnterWght);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        saver = new FileSaver();
        fileSaverArrayListDoubles = new ArrayList();
        dateArrayList = new ArrayList();
        addBtn = (Button) findViewById(R.id.buttonAddWeight);
        average = (Button) findViewById(R.id.buttonAverage);
        average.setOnClickListener(averager);
        addBtn.setOnClickListener(adder);
        deleteBtn.setOnClickListener(deleter);
        layParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layParam.setMargins(0, 10, 0, 0);
    }


    @Override
    public void onPause() {
        super.onPause();
        saver.saveFile(isExternalStorageWritable(), weightTrackerSaveFile, getDoubles(), getStrings()); //saving file with text view parameters of weight and date.
        linLay.removeAllViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(weightTrackerSaveFile.exists()){
            ArrayList<String> fullSaveList= saver.loadFile(isExternalStorageReadable(), weightTrackerSaveFile, this, fileSaverArrayListDoubles);
            doubleArray = new double[fullSaveList.size()];
            for (int i = 0; i<fullSaveList.size(); i++){
                doubleArray[i] = Double.parseDouble(fullSaveList.get(i).substring(10));
                dateArrayList.add(i, fullSaveList.get(i).substring(0,10));
                linLay.addView(addNewTextView(doubleArray[i], dateArrayList.get(i)), 0);

            }
            fullSaveList.clear();
        }


    }

    private DefineTextView addNewTextView(double weight) {
        String date = df.format(cal.getTime());
        DefineTextView view = new DefineTextView(this, layParam, weight, date);
        view.setId(viewId);
        viewId++;
        return view;
    }

    private DefineTextView addNewTextView(double weight, String date) {
        DefineTextView view = new DefineTextView(this, layParam, weight, date);
        view.setId(viewId);
        viewId++;
        return view;
    }

    View.OnClickListener deleter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (viewId > 0) {
                linLay.removeViewAt(0);
                viewId--;
            }
        }
    };

    View.OnClickListener averager = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (linLay.getChildCount() >= 7) {
                get7dayAvg();
                Intent intent = new Intent (MainActivity.this, AveragePop.class);
                intent.putExtra("com.example.pillsweight.AVERAGE", Double.toString(roundTo2Decimals(med)));
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Not enough data", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    };

    public void clearLinLay(View v){
        if(viewId > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_AlertTheme));
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("Delete all entries?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    linLay.removeAllViews();
                    viewId = 0;
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }


    View.OnClickListener adder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (editTextEnterWght.getText().toString().trim().length() > 0) {
                double d1 = Double.parseDouble(editTextEnterWght.getText().toString());
                linLay.addView(addNewTextView(d1), 0);
            }
        }
    };

    private double get7dayAvg() {
        for (int i = 0; i < 7; i++) {
            DefineTextView z = (DefineTextView) linLay.getChildAt(i);
            all = all + z.getDl();
        }
        med = all / 7;
        all = 0.0;
        return med;
    }

    ;

    private double roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }

    private boolean isExternalStorageWritable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    private boolean isExternalStorageReadable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            return true;
        }else {
            return false;
        }
    }

    private double[] getDoubles() {
        double[] doubles = new double[linLay.getChildCount()];
        for(int i=0; i <= linLay.getChildCount(); i++){
            if(linLay.getChildAt(i)!= null) {
                DefineTextView defTV = (DefineTextView) linLay.getChildAt(i);
                double d = defTV.getDl();
                doubles[i] = d;
            }
        }
        return doubles;
    }

    private String[] getStrings() {
        String[] strings = new String[linLay.getChildCount()];
        for(int i=0; i <= linLay.getChildCount(); i++){
            if(linLay.getChildAt(i)!= null) {
                DefineTextView defTV = (DefineTextView) linLay.getChildAt(i);
                String d = defTV.getDate();
                strings[i] = d;
            }
        }
        return strings;
    }


    private boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
