package com.autocall.autoanswercalls;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.net.ParseException;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telecom.CallAudioState;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String[] PERMISSIONS = {
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.CALL_PHONE,
//            Manifest.permission.DISABLE_KEYGUARD,
//            Manifest.permission.RECEIVE_BOOT_COMPLETED,
//            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    final int PERMISSIONS_REQUEST_CODE = 1;
    final int OVERLAY_PERMISSION_REQUEST_CODE = 2;
    private BroadcastReceiver receiver;
    TextView timestarttextview1,timeendtextview1,audiofiletextview1, startdatetextview1, enddatetextview1;
    EditText editdiscriptions1;
//    TextView timestarttextview,timeendtextview,audiofiletextview;
//    EditText editdiscriptions;
//    SharedPreferences prefs;
//    SharedPreferences.Editor editor;
    String timestart="",timeend="",audio="",discription="",startdate ="",enddate="";
    Calendar mcurrentTime;
    DatabaseHelper myDb;
//    Button btnadd;
    List <Model> audiolist;
    private RecyclerView mRecyclerView;
    private DataAdapter1 dataAdapter1;
    FloatingActionButton floatingadd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        timestarttextview=findViewById(R.id.timestart);
//        timeendtextview=findViewById(R.id.timeend);
//        audiofiletextview=findViewById(R.id.audiofile);
//        editdiscriptions=findViewById(R.id.editdiscriptions);
//        btnadd=findViewById(R.id.btnadd);
        floatingadd=findViewById(R.id.floatingadd);
        mcurrentTime = Calendar.getInstance();
        myDb = new DatabaseHelper(this);
        audiolist=new ArrayList<>();

        /*AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(false); // CODE FOR PLAYING ON SPEAKERS*/


        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setBluetoothScoOn(true); // CODE FOR EARPIECE DOESNT ANSWER AUTOMATICALLY


        mRecyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(manager);

        requestPermissions();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
//        filter.addAction("SOME_OTHER_ACTION");

//       receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //do something based on the intent's action
//            }
//        };
        receiver =new PhoneStateReceiver();
        registerReceiver(receiver, filter);

//         prefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
//         editor=prefs.edit();
//         timestart = prefs.getString("timestart", "");
//        timeend = prefs.getString("timeend", "");
//        audio = prefs.getString("audio", "");
//        discription = prefs.getString("discription", "");

//        if(timestart.isEmpty()){
//            timestarttextview.setText("00:00");
//        }else {
//            timestarttextview.setText(timestart);
//        }
//
//        if(timeend.isEmpty()){
//            timeendtextview.setText("00:00");
//        }else {
//            timeendtextview.setText(timeend);
//        }
//
//        if(audio.isEmpty()){
//            audiofiletextview.setText("Select Audio");
//        }else {
//            audiofiletextview.setText(audio);
//        }
//
//        if(!discription.isEmpty()){editdiscriptions.setText(discription);}


//        timestarttextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String AM_PM = " am";
//                        String mm_precede = "";
//                        if (selectedHour >= 12) {
//                            AM_PM = " pm";
//                            if (selectedHour >=13 && selectedHour < 24) {
//                                selectedHour -= 12;
//                            }
//                            else {
//                                selectedHour = 12;
//                            }
//                        } else if (selectedHour == 0) {
//                            selectedHour = 12;
//                        }
//                        if (selectedMinute < 10) {
//                            mm_precede = "0";
//                        }
//                        timestarttextview.setText("" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
//                        timestart="" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM;
////                        editor.putString("timestart", "" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
////                        editor.apply();
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });

//        timeendtextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO Auto-generated method stub
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String AM_PM = " am";
//                        String mm_precede = "";
//                        if (selectedHour >= 12) {
//                            AM_PM = " pm";
//                            if (selectedHour >=13 && selectedHour < 24) {
//                                selectedHour -= 12;
//                            }
//                            else {
//                                selectedHour = 12;
//                            }
//                        } else if (selectedHour == 0) {
//                            selectedHour = 12;
//                        }
//                        if (selectedMinute < 10) {
//                            mm_precede = "0";
//                        }
//
//                        timeendtextview.setText("" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
//                        timeend="" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM;
////                        editor.putString("timeend", "" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
////                        editor.apply();
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });


//        audiofiletextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent_upload = new Intent();
//                intent_upload.setType("audio/*");
//                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent_upload,1);
//            }
//        });

//        btnadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(timestarttextview.getText().toString().contains("00:00")
//                        || timeendtextview.getText().toString().contains("00:00")
//                        || audiofiletextview.getText().toString().contains("Select Audio")
//                        || editdiscriptions.getText().toString().isEmpty()){
//                    Toast.makeText(MainActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
//                }else {
//                    boolean isInserted = myDb.insertData(timestarttextview.getText().toString(),
//                            timeendtextview.getText().toString(), audiofiletextview.getText().toString(),
//                            editdiscriptions.getText().toString());
//                    if (isInserted == true) {
//                        timestarttextview.setText("00:00");
//                        timeendtextview.setText("00:00");
//                        audiofiletextview.setText("Select Audio");
//                        editdiscriptions.setText("");
//                        viewAllDB();
//                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
//                    }else {
//                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });


        floatingadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(MainActivity.this);
            }
        });

        viewAllDB();

    }


    @Override
    protected void onStop() {
        super.onStop();

//        editor.putString("discription", editdiscriptions.getText().toString());
//        editor.apply();
    }

        @Override
    protected void onStart() {
        super.onStart();

////        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
////        int minute = mcurrentTime.get(Calendar.MINUTE);
////        String timm=hour + ":" + minute;
//            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//            String timm = dateFormat.format(new Date()).toString();
//
//            Cursor res = myDb.getAllData();
//            if(res.getCount() == 0) {
//                return;
//            }
//
//            while (res.moveToNext()) {
//                Model model=new Model(res.getString(0),res.getString(1)
//                        ,res.getString(2),res.getString(3),res.getString(4));
//                if(checktimings(model.getSTARTTIME(),timm) && checktimings(timm,model.getENDTIME())){
//                    Log.e("abcdtimm  in",model.getSTARTTIME()+"  =  "+timm+"  =  "+model.getENDTIME());
//                    break;
//                }else {
//                    Toast.makeText(this, "out time", Toast.LENGTH_SHORT).show();
//                    Log.e("abcdtimm  out",model.getSTARTTIME()+"  =  "+timm+"  =  "+model.getENDTIME());
//                }
//            }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        for (String permission : PERMISSIONS) {
//            if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
//                continue;
//            }

            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                return;
            }
        }

//        if (!Settings.canDrawOverlays(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
            case OVERLAY_PERMISSION_REQUEST_CODE:
                requestPermissions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                String path=getAudioPath(uri);
                audiofiletextview1.setText(path+"");
//                editor.putString("audio", path);
//                editor.apply();


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private boolean checktimings(String time, String endtime) {

//        Log.e("abcd match ",time+" = "+endtime);

        String pattern = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if(date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private String getAudioPath(Uri uri) {

        /*String[] data = {MediaStore.Audio.Media.DATA};

        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);*/

        String pathFileString = uri.toString(); //converts to string, probably wrong cause its reused, used multiple times
        Log.e("LOG_TAG", pathFileString);


        return pathFileString;
    }

    public void viewAllDB() {
        audiolist.clear();
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            // show message
//            showMessage("Error","Nothing found");
            return;
        }

        while (res.moveToNext()) {
            audiolist.add(new Model(res.getString(0),res.getString(1)
            ,res.getString(2),res.getString(3),res.getString(4),res.getString(5), res.getString(6)));
//            buffer.append("Id :"+ res.getString(0)+"\n");
//            buffer.append("Name :"+ res.getString(1)+"\n");
//            buffer.append("Surname :"+ res.getString(2)+"\n");
//            buffer.append("Marks :"+ res.getString(3)+"\n\n");
        }

        // Show all data
        dataAdapter1 = new DataAdapter1(MainActivity.this,audiolist);
        mRecyclerView.setAdapter(dataAdapter1);

    }


        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);

             timestarttextview1 = (TextView) dialog.findViewById(R.id.timestart);
             timeendtextview1 = (TextView) dialog.findViewById(R.id.timeend);
             audiofiletextview1 = (TextView) dialog.findViewById(R.id.audiofile);
             editdiscriptions1 = (EditText) dialog.findViewById(R.id.editdiscriptions);
             startdatetextview1 = (TextView) dialog.findViewById(R.id.Date1);
             enddatetextview1 = (TextView) dialog.findViewById(R.id.Date2);

             startdatetextview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    final Calendar Cal = Calendar.getInstance();
                    int mDate = Cal.get(Calendar.DATE);
                    int mMonth = Cal.get(Calendar.MONTH);
                    int mYear = Cal.get(Calendar.YEAR);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int Month, int Date){
                            startdatetextview1.setText( Date +"/" + Month + "/" + year);
                            enddate = "" + Date +  "/ " + (Month) + "/ " + year;


                        }
                    },  mYear, (mMonth+1), mDate);
                    datePickerDialog.show();
                }
            });

            enddatetextview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    final Calendar Cal = Calendar.getInstance();
                    int mDate = Cal.get(Calendar.DATE);
                    int mMonth = Cal.get(Calendar.MONTH);
                    int mYear = Cal.get(Calendar.YEAR);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener(){

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int Month, int Date){
                            enddatetextview1.setText( Date +"/" + Month + "/" + year);
                            enddate = "" + Date +  "/ " + Month + "/ " + year;

                        }
                    },  mYear, (mMonth+1), mDate);
                    datePickerDialog.show();
                }
            });


            timestarttextview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String AM_PM = " am";
                            String mm_precede = "";
                            if (selectedHour >= 12) {
                                AM_PM = " pm";
                                if (selectedHour >=13 && selectedHour < 24) {
                                    selectedHour -= 12;
                                }
                                else {
                                    selectedHour = 12;
                                }
                            } else if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            if (selectedMinute < 10) {
                                mm_precede = "0";
                            }
                            timestarttextview1.setText("" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
                            timestart="" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM;
//                        editor.putString("timestart", "" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
//                        editor.apply();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });

            timeendtextview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String AM_PM = " am";
                            String mm_precede = "";
                            if (selectedHour >= 12) {
                                AM_PM = " pm";
                                if (selectedHour >=13 && selectedHour < 24) {
                                    selectedHour -= 12;
                                }
                                else {
                                    selectedHour = 12;
                                }
                            } else if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                            if (selectedMinute < 10) {
                                mm_precede = "0";
                            }

                            timeendtextview1.setText("" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
                            timeend="" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM;
//                        editor.putString("timeend", "" + selectedHour + ":" + mm_precede + selectedMinute + AM_PM);
//                        editor.apply();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });


            audiofiletextview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_upload = new Intent();
                    intent_upload.setType("audio/*");
                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_upload,1);
                }
            });

            Button btnadd = (Button) dialog.findViewById(R.id.btnadd);
            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(timestarttextview1.getText().toString().contains("00:00")
                            || timeendtextview1.getText().toString().contains("00:00")
                            || audiofiletextview1.getText().toString().contains("Select Audio")
                            || editdiscriptions1.getText().toString().isEmpty()
                            || startdatetextview1.getText().toString().contains("MM/DD/YY")
                            || audiofiletextview1.getText().toString().contains("Select Audio")){
                        Toast.makeText(MainActivity.this, "Enter Data", Toast.LENGTH_SHORT).show();
                    }else {
                        boolean isInserted = myDb.insertData(timestarttextview1.getText().toString(),
                                timeendtextview1.getText().toString(), audiofiletextview1.getText().toString(),
                                editdiscriptions1.getText().toString(),startdatetextview1.getText().toString(),enddatetextview1.getText().toString());
                        if (isInserted == true) {
                            timestarttextview1.setText("00:00");
                            timeendtextview1.setText("00:00");
                            audiofiletextview1.setText("Select Audio");
                            editdiscriptions1.setText("");
                            startdatetextview1.setText("DD/MM/YY");
                            enddatetextview1.setText("DD/MM/YY");
                            viewAllDB();
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            dialog.show();

        }

}