package com.autocall.autoanswercalls;

import static android.content.Context.MODE_PRIVATE;
import static android.media.AudioManager.AUDIOFOCUS_GAIN;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.telecom.CallAudioState;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PhoneStateReceiver extends BroadcastReceiver {
    MediaPlayer mp = null;
//    String audio;
    DatabaseHelper myDb;
//    SharedPreferences prefs;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {


//        prefs = context.getSharedPreferences("MY_PREFS", MODE_PRIVATE);
//        timestart = prefs.getString("timestart", "");
//        timeend = prefs.getString("timeend", "");
//        audio = prefs.getString("audio", "");

        myDb = new DatabaseHelper(context);


            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            DateFormat dateformat1 = new SimpleDateFormat("dd/MM/yyyy");
            String timm = dateFormat.format(new Date()).toString();
            String datee = dateformat1.format(new Date()).toString();
             Log.e("LOG_TAG", timm);
             Log.e("LOG_TAG", datee);



        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            Model model = new Model(res.getString(0), res.getString(1)
                    , res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
            Log.e("LOG_TAG", datee);
            Log.e("ENDDATE", model.getENDDATE());
            Log.e("STARTDATE", model.getSTARTDATE());



            if(checkdate(model.getSTARTDATE(), datee) && checkdate(datee, model.getENDDATE())) {
                Log.e("LOG_TAG", "PROBLEM IS NOT HERE");


                if (checktimings(model.getSTARTTIME(), timm) && checktimings(timm, model.getENDTIME())) {

                    if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
//            RingingWindow ringingWindow = RingingWindow.getInstance();

                        try {
                            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
//                    processRinging(context, intent);
//                        Toast.makeText(context, "Ringing ", Toast.LENGTH_SHORT).show();
                                TelecomManager tm = (TelecomManager) context
                                        .getSystemService(Context.TELECOM_SERVICE);
                                if (tm == null) {
                                    // whether you want to handle this is up to you really
                                    throw new NullPointerException("tm == null");
                                }
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }

                                tm.acceptRingingCall();
                                //                       mp = MediaPlayer.create(context, R.raw.djj);//Onreceive gives you context
                                //                       mp.start();// and this to play it


                                mp = new MediaPlayer();
                                mp.setAudioAttributes(
                                        new AudioAttributes.Builder()
                                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                                .build()
                                );

                                try {   //convert string to uri, use appcontext

                                    Uri uri;
                                    uri = Uri.parse(model.getAUDIO());
                                    Log.e("LOG_TAG", uri.toString());
                                    Log.e("LOG_TAG", "CALL INSTANCE SUCCESSFULLY MADE DEBUG VOICE");

                                    mp.setVolume(1f, 1f);
                                    mp.setAudioStreamType(AUDIOFOCUS_GAIN);
                                    mp.setDataSource(context, uri);
                                    mp.prepare();
                                    mp.start();


                                } catch (IOException e) {
                                    Log.e("LOG_TAG", "prepare() failed");
                                }

                            }
                            if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
//                    ringingWindow.hide();
                            }
                            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
//                    ringingWindow.hide();
                            }
                        } catch (Exception e) {
//                ringingWindow.hide();
                            e.printStackTrace();
                        }
                    }

                    break;
                }
            }

        }




    }

    private boolean checkdate(String setdate, String currentdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = sdf.parse(setdate);
            Date date2 = sdf.parse(currentdate);

            if(date1.equals(date2)||(date1.before(date2))){
                return true;
            } else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



    private boolean checktimings(String time, String endtime) {

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


}
