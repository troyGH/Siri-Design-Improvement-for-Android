package com.continuesvoicerecognition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class VoiceCustomization extends Activity {
    private SpeechRecognizerManager mSpeechManager;
    private Button cancel, next, exit;
    private TextView lgtext, smtext,steps_text;
    private int steps=0;
    private String result="";
    private String command="";
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_customization);

        Bundle b = getIntent().getExtras();
        command = b.getString("id");

        cancel = (Button) findViewById(R.id.cancel_button);
        next = (Button) findViewById(R.id.next_button);
        exit = (Button) findViewById(R.id.exit_button);
        lgtext = (TextView) findViewById(R.id.lg_textview);
        smtext = (TextView) findViewById(R.id.small_textview);
        steps_text = (TextView) findViewById(R.id.steps_textView);
        bar = (ProgressBar) findViewById(R.id.progressBar);

        cancel.setText("Cancel");
        next.setText("Set Up");
        exit.setEnabled(false);
        exit.setText("");
        lgtext.setText("Set Up \"" + command + "\"");
        smtext.setText("This helps Siri recognize your voice when you say \"" + command + "\"");
        steps_text.setText("");
        bar.setVisibility(View.INVISIBLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                steps++;
                next.setEnabled(false);
                next.setText("");
                exit.setEnabled(true);
                exit.setText("Set Up \"" + command + "\" Later");
                lgtext.setText("Say \"" + command + "\" into the phone");
                smtext.setText("");
                steps_text.setText(steps + " of 5");
                bar.setVisibility(View.VISIBLE);
                exit.setVisibility(View.VISIBLE);
                listen();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        exit.setVisibility(View.INVISIBLE);

        ActionBar bar = getActionBar();
        bar.hide();

    }
    public void step2(){
        steps++;
        next.setEnabled(false);
        next.setText("");
        exit.setEnabled(true);
        exit.setText("Set Up \"" + command + "\" Later");
        lgtext.setText("Say \"" + command + "\" again");
        smtext.setText("");
        steps_text.setText(steps + " of 5");
        listen();
    }

    public void step3(){
        steps++;
        next.setEnabled(false);
        next.setText("");
        exit.setEnabled(true);
        exit.setText("Set Up \"" + command + "\" Later");
        lgtext.setText("Say \"" + command + "\" one more time");
        smtext.setText("");
        steps_text.setText(steps + " of 5");
        listen();
    }
    public void step4(){
        steps++;
        next.setEnabled(false);
        next.setText("");
        exit.setEnabled(true);
        exit.setText("Set Up \"" + command + "\" Later");
        lgtext.setText("Say \"Text Mom hello " + command + "\"");
        smtext.setText("");
        steps_text.setText(steps + " of 5");
        listen();
    }
    public void step5(){
        steps++;
        next.setEnabled(false);
        next.setText("");
        exit.setEnabled(true);
        exit.setText("Set Up \"" + command + "\" Later");
        lgtext.setText("Say \"Set alarm for 7PM " + command + "\"");
        smtext.setText("");
        steps_text.setText(steps + " of 5");
        listen();
    }

    public void listen(){
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {
                if(results!=null && results.size()>0){
                    if(results.get(0).contains(command)){

                        mSpeechManager.destroy();
                        mSpeechManager = null;

                        if(steps == 1)
                            step2();
                        else if(steps == 2)
                            step3();
                        else if(steps==3)
                            step4();
                        else if(steps==4)
                            step5();
                        else if(steps>=5)
                            finishCustomization();
                    }
                }
            }
        });
    }
    public void finishCustomization(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", command);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu., menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
