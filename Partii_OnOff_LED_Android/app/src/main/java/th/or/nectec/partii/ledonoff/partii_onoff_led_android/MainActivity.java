package th.or.nectec.partii.ledonoff.partii_onoff_led_android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;


import th.or.nectec.partii.embedded.android.EmbeddedUtils.ModelUtil;
import th.or.nectec.partii.embedded.android.RecognitionListener;
import th.or.nectec.partii.embedded.android.SpeechRecognizer;


public class MainActivity extends AppCompatActivity implements RecognitionListener, ModelUtil.OnReceiveStatusListener  {
    int record_state = 0; // 0 non record, 1 record

    ImageView imgview;
    TextView textview;
    private SpeechRecognizer recognizer;
    private boolean isSetupRecognizer = false;
    private ModelUtil mUtil = new ModelUtil();

    String apikey = "2go-KgTo97gTYw";

    private Microgear microgear = new Microgear(this);
    private String appid = ""; //APP_ID
    private String key = ""; //KEY
    private String secret = ""; //SECRET
    private String thing = "partiiled";
    private String alias = "partiiandroid";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            System.out.println(string);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MicrogearCallBack callback = new MicrogearCallBack();
        microgear.connect(appid, key, secret,alias);
        microgear.setCallback(callback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgview = (ImageView) findViewById(R.id.imageView);
        textview = (TextView) findViewById(R.id.textview);

        if (mUtil.isPermissionGranted(getApplicationContext())) {
            if (mUtil.isSyncDir(getExternalFilesDir("")) && !isSetupRecognizer) {
                setUpRecognizer();
            }
        } else {
            mUtil.requestPermission(getApplicationContext());
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUtil.isSyncDir(getExternalFilesDir("")) && !isSetupRecognizer) {
                    setUpRecognizer();
                    Snackbar.make(view, "ลองใหม่อีกครั้ง ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    if (record_state == 0) {
                        record_state = 1;
                        if (isSetupRecognizer) {
                            Log.d("Recognizer", "start recognizer");

                            recognizer.startListening();
                            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), android.R.drawable.presence_audio_busy));
                        }
                    } else if (record_state == 1) {
                        record_state = 0;
                        if (isSetupRecognizer) {
                            Log.d("Recognizer", "stop recognizer");
                            recognizer.stop();
                            fab.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), android.R.drawable.presence_audio_online));
                        }
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("Download", "Start download");
            mUtil.startDownload(this, MainActivity.this, getExternalFilesDir(""), apikey);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpRecognizer() {
        Log.d("Recognizer", "Setting recognizer");
        recognizer = mUtil.getRecognizer(this);
        if (recognizer.getDecoder() == null) {
            finish();
        }
        recognizer.addListener(this);
        isSetupRecognizer = true;
    }

    @Override
    public void onReceiveDownloadComplete() {
        recognizer.stop();
        recognizer.shutdown();
        setUpRecognizer();
    }

    @Override
    public void onReceiveDownloadFailed() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onProgress(int i) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(String s) {

    }

    @Override
    public void onResult(String s) {
        if (s != null) {
            if (!s.equals(SpeechRecognizer.NO_HYP) &&
                    !s.equals(SpeechRecognizer.REQUEST_NEXT)) {
                System.out.println("onResult:" + s);
                sendCMD(s);
            }
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }

    public void sendCMD(String s) {
        if (s.trim().equals("เปิดไฟสีแดง") == true) {
            microgear.chat(thing, "REDLED_ON");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.on);
        } else if (s.trim().equals("เปิดไฟสีส้ม") == true) {
            microgear.chat(thing, "ORANGELED_ON");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.on);
        }else if (s.trim().equals("เปิดไฟสีน้ำเงิน") == true) {
            microgear.chat(thing, "BULELED_ON");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.on);
        }else if (s.trim().equals("ปิดไฟสีแดง") == true) {
            microgear.chat(thing, "REDLED_OFF");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.off);
        }else if (s.trim().equals("ปิดไฟสีส้ม") == true) {
            microgear.chat(thing, "ORANGELED_OFF");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.off);
        }else if (s.trim().equals("ปิดไฟสีน้ำเงิน") == true) {
            microgear.chat(thing, "BULELED_OFF");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.off);
        }else if (s.trim().equals("ปิดไฟทุกดวง") == true) {
            microgear.chat(thing, "ALLLED_OFF");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.off);
        }else if (s.trim().equals("เปิดไฟทุกดวง") == true) {
            microgear.chat(thing, "ALLLED_ON");
            textview.setText(s.trim());
            imgview.setImageResource(R.drawable.on);
        }
    }

    class MicrogearCallBack implements MicrogearEventListener {
        @Override
        public void onConnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Now I'm connected with netpie");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Connected", "Now I'm connected with netpie");
        }

        @Override
        public void onMessage(String topic, String message) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", topic + " : " + message);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("Message", topic + " : " + message);
        }

        @Override
        public void onPresent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "New friend Connect :" + token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("present", "New friend Connect :" + token);
        }

        @Override
        public void onAbsent(String token) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Friend lost :" + token);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("absent", "Friend lost :" + token);
        }

        @Override
        public void onDisconnect() {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Disconnected");
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("disconnect", "Disconnected");
        }

        @Override
        public void onError(String error) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : " + error);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("exception", "Exception : " + error);
        }

        @Override
        public void onInfo(String info) {
            Message msg = handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("myKey", "Exception : " + info);
            msg.setData(bundle);
            handler.sendMessage(msg);
            Log.i("info", "Info : " + info);
        }
    }
}
