package com.example.anithavikkiraman.sms_manager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etMessage;
    EditText etTelNr;
    int MY_PERMISSIONS_REQUEST_SEND_SMS=1;

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage=(EditText) findViewById(R.id.etMessage);
        etTelNr=(EditText) findViewById(R.id.etTelNr);

        sentPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS sent!",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this, "Generic failure!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this, "No Service!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity.this, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity.this, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this, "SMS Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this, "SMS not Delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //registerReceiver{smsSentReceiver,new IntentFilter{SENT}};
        registerReceiver(smsSentReceiver,new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver,new IntentFilter(DELIVERED));

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsSentReceiver);

    }

    public  void btn_SendSMS_OnClick(View v)
    {

        String msg=etMessage.getText().toString();
        String phno=etTelNr.getText().toString();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            SmsManager sms =SmsManager.getDefault();
            sms.sendTextMessage(phno,null,msg,sentPI,deliveredPI);
        }


    }
}
