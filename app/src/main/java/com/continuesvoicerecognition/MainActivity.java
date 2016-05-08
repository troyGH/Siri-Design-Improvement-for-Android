package com.continuesvoicerecognition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;


/*
        Copyright (c) <2015> <Gal Rom>

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:



        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.



        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

/*


/**
 * Class modified by Group Siri to fit the needs of making the "end" and "nevermind" command
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private SpeechRecognizerManager mSpeechManager;
    private TextView returnedText;
    private TextToSpeech tts;
    private ImageButton settings;
    private ToggleButton microphone;
    private ProgressBar listeningBar;
    private String text, textback;
    private static final String KEYPHRASE = "hey Siri";
    public static String END = "finish";
    public static String NEVERMIND = "nevermind";
    private static boolean VOICE_TRIGGER = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
        findViews();
        setClickListeners();

        if (Advanced.HEYSIRI == 1) {
            VOICE_TRIGGER = true;
            SetSpeechListener();
        }
        returnedText.setText("What can I help you with?");
        tts.speak("What can I help you with?", TextToSpeech.QUEUE_FLUSH, null);
        text = "";
        textback = "";
    }


    private void findViews()
    {
        returnedText = (TextView) findViewById(R.id.text);
        listeningBar = (ProgressBar) findViewById(R.id.progressBar1);
        microphone = (ToggleButton) findViewById(R.id.toggleButton1);
        settings = (ImageButton) findViewById(R.id.Settings);
        ActionBar bar = getActionBar();
        bar.hide();
    }


    private void setClickListeners()
    {
        microphone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    listeningBar.setVisibility(View.VISIBLE);
                    if(mSpeechManager==null)
                    {
                        SetSpeechListener();
                    }
                    else if(!mSpeechManager.ismIsListening())
                    {
                        mSpeechManager.destroy();
                        SetSpeechListener();
                    }
                    returnedText.setText(getString(R.string.you_may_speak));
                } else {
                    listeningBar.setVisibility(View.INVISIBLE);
                    if(mSpeechManager!=null) {
                        mSpeechManager.destroy();
                        mSpeechManager = null;
                    }
                    if (Advanced.HEYSIRI == 1) {
                        VOICE_TRIGGER = true;
                        SetSpeechListener();
                    }
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_menu_intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(main_menu_intent);
                finish();
            }
        });
        listeningBar.setVisibility(View.INVISIBLE);
    }


    public void execute () {
        if (text.equals("") || text.equals(" ")) {
            return;
        }

        if (text.contains("call")) {
            text = text.replaceAll("call", "");
            returnedText.setText("calling " + text);
            tts.speak("calling" + text, TextToSpeech.QUEUE_FLUSH, null);
            text = "";
        } else if (text.contains("text")) {
            text = text.replaceAll("text", "");
            returnedText.setText("texting" + text + "..." + " text sent!");
            tts.speak("texting " + text + "..." + " text sent!", TextToSpeech.QUEUE_FLUSH, null);
            text = "";
        } else if (text.contains("search")) {
            text = text.replaceAll("search", "");
            returnedText.setText("searching " + text);
            tts.speak("searching" + text, TextToSpeech.QUEUE_FLUSH, null);
            text = "";
        } else if (text.contains("hey") || text.contains("hello")) {
            returnedText.setText("Hello!");
            tts.speak("Hello!", TextToSpeech.QUEUE_FLUSH, null);
            text = "";
        } else {
            returnedText.setText("I'm sorry! I did not understand what you said");
            tts.speak("I'm sorry! I did not understand what you said", TextToSpeech.QUEUE_FLUSH, null);
            text = "";
        }
    }

    private void SetSpeechListener()
    {
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {

                if(results!=null && results.size()>0)
                {
                    if (VOICE_TRIGGER) {
                        if (results.get(0).equals(KEYPHRASE)) {
                            returnedText.setText(results.get(0));
                            mSpeechManager.destroy();
                            mSpeechManager = null;
                            VOICE_TRIGGER = false;
                            microphone.setChecked(true);
                            return;
                        }
                        else {
                            return;
                        }
                    }
                    if (text.equals("")) {
                        text = text + results.get(0);
                    }
                    else {
                        text = text + " " + results.get(0);
                    }
                    if((Advanced.END == 1 && text.endsWith(END)) || Advanced.END == 0)
                    {
                        if ((Advanced.END == 1 && text.endsWith(END))) {
                            text = text.substring(0, text.length() - END.length());
                        }
                        returnedText.setText(text);
                        mSpeechManager.destroy();
                        mSpeechManager = null;
                        microphone.setChecked(false);
                        execute();
                    }
                    else if (Advanced.CANCEL == 1 && text.endsWith(NEVERMIND)) {
                        text = text.substring(0, text.length() - NEVERMIND.length());
                        text = "Ok. I made sure to not process what you just said.";
                        tts.speak("Ok. I made sure to not process what you just said.", TextToSpeech.QUEUE_FLUSH, null);
                        returnedText.setText(text);
                        text = "";
                        microphone.setChecked(false);
                        return;
                    }
                    else {
                        returnedText.setText(text);
                    }
                }
                else {
                    returnedText.setText("Nothing!");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if(mSpeechManager!=null) {
            mSpeechManager.destroy();
            mSpeechManager=null;
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {

    }
}
