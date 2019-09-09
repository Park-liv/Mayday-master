package application.minseong.capstone;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    //final int DIALOG_TEXT = 1;
    static final int ADD_FRIEND = 5;

    @BindView(R.id.addFriend)
    ImageView _addFriend;

    @BindView(R.id.gps_image)
    ImageView _gpsImg;

    @BindView(R.id.alarmBtn)
    ImageView _alarmBtn;

    @BindView(R.id.emergencycall)
    ImageView _emergencyImg;

    @BindView(R.id.youtube)
    ImageView _youtube;


    public static MediaPlayer mp2 = null;
    public static MediaPlayer mp3 = null;


    public boolean isPlayingSong = false;
    private BluetoothSPP bt;

//    Dialog myDialog;

    Switch aSwitch;

    //Using the Accelometer & Gyroscoper
    SensorManager mSensorManager;

    //Using the Accelometer
    SensorEventListener mAccLis;
    Sensor mAccelometerSensor = null;
    public Float y;

    public String getFriendName;
    public Integer getFriendNum;

//    YouTubePlayerView youTubeView;
//    YouTubePlayer.OnInitializedListener listener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bt = new BluetoothSPP(this);
        final Context context = this;
        //myDialog = new Dialog(this);

//        youTubeView = (YouTubePlayerView)findViewById(R.id.youtubeView);
//        listener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.loadVideo("G9YegpvgBe0");
//
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        };

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }


        _gpsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                startActivity(intent);
                finish();
            }
        });


        _alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlayingSong){
                    isPlayingSong = false;
                    mp2.stop();
                }else{
                    AlarmFun();
                }
            }
        });

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                String gas= message.substring(0,3);
                String temp= message.substring(3,5);
                String hu= message.substring(5,7);
                int gas1=Integer.parseInt(gas);
                int temp1=Integer.parseInt(temp);
                int hu1=Integer.parseInt(hu);
                String Play_sound = "NotPlay";
                String dangas;
                String dantemp;
                String danhu;
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sample);

                if(gas1>900){
                    dangas="가스:위험";
                    Play_sound = "PS" +
                            "";
                }
                else{
                    dangas="가스:정상";
                }
                if(temp1>50){
                    dantemp="온도:위험";
                    Play_sound = "PS";
                }
                else{
                    dantemp="온도:정상";
                }
                if(hu1>100000000){
                    danhu="습도:위험";
                    Play_sound = "PS";
                }
                else{
                    danhu="습도:정상";
                }////////기준치 넘는지

                if (Play_sound.equals("PS")){
                    mediaPlayer.start(); //소리
                }
                TextView GasView = (TextView)findViewById(R.id.gasValue);
                GasView.setText(gas);
                TextView TempView = (TextView)findViewById(R.id.tempValue);
                TempView.setText(temp);
                TextView HuView = (TextView)findViewById(R.id.huValue);
                HuView.setText(hu);


                Toast.makeText(MainActivity.this, dangas+"  "+dantemp+"  "+danhu, Toast.LENGTH_SHORT).show();
                //if (Integer.parseInt(message)>0){
                //   Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                //}
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        aSwitch = findViewById(R.id.sw1);
        //sound_pool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        mp3 = MediaPlayer.create(MainActivity.this,R.raw.siren);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Using the Accelometer 센서 객체 생성
        //assert mSensorManager != null;
        mAccelometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccLis = new accListener();

//        danger = sound_pool.load(this, R.raw.siren,1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"OFF",Toast.LENGTH_SHORT).show();
                    onPause();
                }
            }
        });

        _addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddFriend.class);
                startActivityForResult(intent,ADD_FRIEND);

//                AddFriend addFriend = new AddFriend();
//                getFriendName = addFriend.getFriendName();
//                getFriendNum = addFriend.getFriendNum();
//                Log.d("tag",getFriendName);
//                Log.d("tag",getFriendNum);
            }
        });


        _emergencyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFriendName == null && getFriendNum == null){
                    Toast.makeText(MainActivity.this,"Let me know your friend's info!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,AddFriend.class);
                    startActivityForResult(intent,ADD_FRIEND);
                }else{
                    String tel = "tel:0"+ getFriendNum.toString();
                    startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                }
            }
        });

        _youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final Dialog myDialog = new Dialog(MainActivity.this);
//                myDialog.setContentView(R.layout.activity_popup);
//                TextView txtclose;
//                ImageView _burn,_cpr,_ext;
//
//                _burn = (ImageView)myDialog.findViewById(R.id.burn);
//                _cpr = (ImageView)myDialog.findViewById(R.id.cpr);
//                _ext = (ImageView)myDialog.findViewById(R.id.ext);
//
//
//                txtclose = (TextView)myDialog.findViewById(R.id.txtclose);
//
//                myDialog.show();
//
//                txtclose.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        myDialog.dismiss();
//                    }
//                });
//
//                _burn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse( "http://youtu.be/" + "rcW7sGDUaJM" ));
//
//                        startActivity( intent );
//                    }
//                });
//                _cpr.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse( "http://youtu.be/" + "Zbp74ri21YE" ));
//
//                        startActivity( intent );
//                    }
//                });
//                _ext.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse( "http://youtu.be/" + "G9YegpvgBe0" ));
//
//                        startActivity( intent );
//                    }
//                });

                //DialogYoutube();

            DialogPopup();
            }
        });

    }

    public void DialogPopup() {
        final Dialog myDialog = new Dialog(MainActivity.this);
        myDialog.setContentView(R.layout.activity_popup);
        TextView txtclose;
        ImageView _burn, _cpr, _ext;
        _burn = (ImageView) myDialog.findViewById(R.id.burn);
        _cpr = (ImageView) myDialog.findViewById(R.id.cpr);
        _ext = (ImageView) myDialog.findViewById(R.id.ext);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        myDialog.show();

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        _burn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://youtu.be/" + "rcW7sGDUaJM"));
                startActivity(intent);
            }
        });
        _cpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://youtu.be/" + "Zbp74ri21YE"));

                startActivity(intent);
            }
        });
        _ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://youtu.be/" + "G9YegpvgBe0"));

                startActivity(intent);
            }
        });
    }

    public void AlarmFun(){
        mp2 = MediaPlayer.create(MainActivity.this,R.raw.sample);
        mp2.start();
        isPlayingSong = true;
    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                //setup();
            }
        }
    }

//    public void setup() {
//        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                bt.send("Text", true);
//            }
//        });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                //setup();
            } else {
                Toast.makeText(MainActivity.this
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        if(requestCode == ADD_FRIEND){
            if(resultCode == RESULT_OK){
                getFriendName = data.getStringExtra("friend1");
                getFriendNum = data.getIntExtra("friend2",0);
                if(getFriendName != null){
                    Log.d("tag1",getFriendName);
                }
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mAccLis, mAccelometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccLis); // unregister orientation listener
    }

    private class accListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {
            y=event.values[1];
            if(y<=-15){
                DialogSimple();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }


//    protected Dialog onCreateDialog(int id){
//        Log.d("test","onCreateDialog");
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_HOLO_DARK);
//        builder.setTitle("Warning")
//                .setMessage("Are you in danger?")
//                .setPositiveButton("Yes",null)
//                .setNegativeButton("No",null);
//        return builder.create();
//    }
    private void DialogSimple() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("Are you in danger?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        AlarmFun();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("WARNING?");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.logo_transparent1);
        alert.show();
    }

    private void DialogYoutube(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("What do you want to watch?").setCancelable(
                false).setPositiveButton("Extinguisher",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse( "http://youtu.be/" + "rcW7sGDUaJM" ));

                        startActivity( intent );

                    }
                }).setNegativeButton("CPR",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse( "http://youtu.be/" + "Zbp74ri21YE" ));

                        startActivity( intent );
                    }
                }).setNeutralButton("Burn",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse( "http://youtu.be/" + "G9YegpvgBe0" ));

                        startActivity( intent );

                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("YOUTUBE");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.logo_transparent1);
        alert.show();
    }

//    public void ShowPopUp(View view){
//        TextView txtclose;
//        ImageView _burn,_cpr,_ext;
//
//        _burn = (ImageView)myDialog.findViewById(R.id.burn);
//        _cpr = (ImageView)myDialog.findViewById(R.id.cpr);
//        _ext = (ImageView)myDialog.findViewById(R.id.ext);
//
//        myDialog.setContentView(R.layout.activity_popup);
//        txtclose = (TextView)myDialog.findViewById(R.id.txtclose);
//
//        txtclose.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//
//        _burn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse( "http://youtu.be/" + "rcW7sGDUaJM" ));
//
//                startActivity( intent );
//            }
//        });
//        _cpr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse( "http://youtu.be/" + "Zbp74ri21YE" ));
//
//                startActivity( intent );
//            }
//        });
//        _ext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse( "http://youtu.be/" + "G9YegpvgBe0" ));
//
//                startActivity( intent );
//            }
//        });
//
//        myDialog.show();
//    }
}

