package com.continuesvoicerecognition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
 * Class modified by Group Siri to fit the needs of making the "end", "nevermind", and "hey Siri" commands
 */
public class MainActivity extends Activity implements View.OnClickListener{

    private SpeechRecognizerManager mSpeechManager;
    private TextView returnedText;
    private TextView spokenText;
    private TextToSpeech tts;
    private ImageButton settings;
    private Button send, cancel;
    private ToggleButton microphone;
    private ProgressBar listeningBar;
    private String text;
    private Sound beep;
    private static final String KEYPHRASE = "hey Siri";
    private static boolean VOICE_TRIGGER = false;
    public static boolean TEXT_CONFIRMATION_TRIGGER = false;

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
        beep = new Sound (this);
        findViews();
        setClickListeners();
        if (Advanced.HEYSIRI) {
            VOICE_TRIGGER = true;
            SetSpeechListener();
        }
        returnedText.setText("What can I help you with?");
        spokenText.setText("");
        tts.speak("What can I help you with?", TextToSpeech.QUEUE_FLUSH, null);
        text = "";
    }


    private void findViews()
    {
        returnedText = (TextView) findViewById(R.id.text);
        spokenText = (TextView) findViewById(R.id.userText);
        listeningBar = (ProgressBar) findViewById(R.id.progressBar1);
        microphone = (ToggleButton) findViewById(R.id.toggleButton1);
        settings = (ImageButton) findViewById(R.id.Settings);
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);
        send.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        ActionBar bar = getActionBar();
        bar.hide();
    }


    private void setClickListeners()
    {

            microphone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(PermissionHandler.checkPermission(MainActivity.this,PermissionHandler.RECORD_AUDIO)) {
                        if (isChecked) {
                            if (VOICE_TRIGGER && Advanced.HEYSIRI) {
                                VOICE_TRIGGER = false;
                            }
                            listeningBar.setVisibility(View.VISIBLE);
                            if (mSpeechManager == null) {
                                SetSpeechListener();
                            } else if (!mSpeechManager.ismIsListening()) {
                                mSpeechManager.destroy();
                                SetSpeechListener();
                            }
                        } else {
                            listeningBar.setVisibility(View.INVISIBLE);
                            if (mSpeechManager != null) {
                                mSpeechManager.destroy();
                                mSpeechManager = null;
                            }
                            if (Advanced.HEYSIRI) {
                                VOICE_TRIGGER = true;
                                SetSpeechListener();
                            }
                        }
                    } else {
                        PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,MainActivity.this);
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

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    microphone.setChecked(false);
                    TEXT_CONFIRMATION_TRIGGER = false;
                    textconfirmation(true);
                }
            });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphone.setChecked(false);
                TEXT_CONFIRMATION_TRIGGER = false;
                textconfirmation(false);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case PermissionHandler.RECORD_AUDIO:
                if(grantResults.length>0) {
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                        microphone.setChecked(true);
                    }
                }
                break;

        }
    }

    public void execute () {
        if (text.equals("") || text.equals(" ")) {
            return;
        }

        spokenText.setText("\"" + text + "\"");
        if (text.contains("call")) {
            text = text.replaceAll("call", "");
            returnedText.setText("calling " + text);
            beep.initiateCall(tts, "calling" + text);
            text = "";
        } else if (text.contains("text")) {
            text = text.replaceAll("text", "");
             if (!Advanced.HEYSIRI) {
                returnedText.setText("Ready to send it?");
                beep.initiateCall(tts, "Ready to send it?");
                 TEXT_CONFIRMATION_TRIGGER = true;
                 send.setVisibility(View.VISIBLE);
                 cancel.setVisibility(View.VISIBLE);
                 microphone.setChecked(true);
            }
            else {
                 returnedText.setText("texting" + text + "..." + " text sent!");
                 beep.initiateCall(tts, "texting " + text + "..." + " text sent!");
                 text = "";
             }
        } else if (text.contains("search")) {
            text = text.replaceAll("search", "");
            returnedText.setText("searching " + text);
            beep.initiateCall(tts, "searching" + text);
            text = "";
        } else if (text.contains("hey") || text.contains("hello")) {
            returnedText.setText("Hello! I hope you are having a great day!");
            beep.initiateCall(tts, "Hello! I hope you are having a great day!");
            text = "";
        } else if (text.startsWith("find the nearest")) {
            text = text.replace("find the nearest", "");
            returnedText.setText("found " + text + " near you!");
            beep.initiateCall(tts, "found " + text + " near you!");
            text = "";
        } else if (text.startsWith("find")) {
            text = text.replace("find", "");
            returnedText.setText("found " + text + ".");
            beep.initiateCall(tts, "found " + text + ".");
            text = "";
        } else {
            returnedText.setText("I'm sorry! I did not understand what you said");
            beep.initiateCall(tts, "I'm sorry! I did not understand what you said");
            text = "";
        }

    }

    private void textconfirmation(boolean response) {
        send.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        if (response) {
            returnedText.setText("texting" + text + "..." + " text sent!");
            beep.initiateCall(tts, "texting " + text + "..." + " text sent!");
            text = "";
        }
        else {
            returnedText.setText("Not sending the text.");
            beep.initiateCall(tts, "Not sending the text");
            text = "";
        }
        TEXT_CONFIRMATION_TRIGGER = false;
    }
    private void SetSpeechListener()
    {
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {

                if(results!=null && results.size()>0)
                {
                    if (TEXT_CONFIRMATION_TRIGGER) {
                        spokenText.setText(results.get(0));
                        if (results.get(0).equalsIgnoreCase("yes") || results.get(0).equalsIgnoreCase("yeah")
                                || results.get(0).equalsIgnoreCase("send")) {
                            microphone.setChecked(false);
                            textconfirmation(true);
                            return;
                        }
                        else if (results.get(0).equalsIgnoreCase("no") || results.get(0).equalsIgnoreCase("nah")
                                || results.get(0).equalsIgnoreCase("cancel")) {
                            microphone.setChecked(false);
                           textconfirmation(false);
                            return;
                        }
                        else {
                            return;
                        }
                    }
                    if (VOICE_TRIGGER) {
                        if (results.get(0).equals(KEYPHRASE)) {
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
                    if((Advanced.END && text.contains(Advanced.ENDCOMMAND)) || !Advanced.END)
                    {
                        if ((Advanced.END && text.contains(Advanced.ENDCOMMAND))) {
                            if (text.contains("finished")) {
                                text = text.replaceAll("finished", "");
                            }
                            else {
                                text = text.replaceAll("finish", "");
                            }
                        }
                        returnedText.setText(text);
                        mSpeechManager.destroy();
                        mSpeechManager = null;
                        microphone.setChecked(false);
                        execute();
                    }
                    else if (Advanced.CANCEL && text.contains(Advanced.NVMCOMMAND)) {
                        text = text.substring(0, text.length() - Advanced.NVMCOMMAND.length());
                        text = "Ok. I made sure to not process what you just said.";
                        beep.initiateCall(tts, "Ok. I made sure to not process what you just said.");
                        returnedText.setText(text);
                        text = "";
                        microphone.setChecked(false);
                        return;
                    }
                    else {
                        spokenText.setText("\"" + text + "\"");
                    }
                }
                else {

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
