package com.continuesvoicerecognition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;


public class Advanced extends Activity {
    public static boolean END = false, CANCEL = false, HEYSIRI = false;
    public static String ENDCOMMAND = "finish", NVMCOMMAND = "never mind";
    private Switch end_command;
    private Switch nevermind_command;
    private Switch hello_siri;
    private final int endCode = 11, cancelCode = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        end_command = (Switch) findViewById(R.id.End_Switch);
        nevermind_command = (Switch) findViewById(R.id.Nevermind_Switch);
        hello_siri = (Switch) findViewById(R.id.Allow_Switch);

        if (END == false) {
            end_command.setChecked(false);
        }
        else {
            end_command.setChecked(true);
        }

        if (CANCEL == false) {
            nevermind_command.setChecked(false);
        }
        else {
            nevermind_command.setChecked(true);
        }

        if (HEYSIRI == false) {
            hello_siri.setChecked(false);
        }
        else {
            hello_siri.setChecked(true);
        }

        setListeners();
        ActionBar back = getActionBar();
        back.show();
        back.setDisplayHomeAsUpEnabled(true);
    }


    private void setListeners () {
        end_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Switch) v).isChecked()) {
                    END = true;
                    Intent customize_intent = new Intent(Advanced.this, VoiceCustomization.class);
                    customize_intent.putExtra("id", "finish");
                    startActivityForResult(customize_intent, endCode);
                } else {
                    END = false;
                }
            }
        });

        nevermind_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Switch) v).isChecked()) {
                    CANCEL = true;
                    Intent customize_intent = new Intent(Advanced.this, VoiceCustomization.class);
                    customize_intent.putExtra("id", "never mind");
                    startActivityForResult(customize_intent,cancelCode);
                } else {
                    CANCEL = false;
                }
            }
        });

        hello_siri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Switch) v).isChecked()) {
                    HEYSIRI = true;
                } else {
                    HEYSIRI = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_advanced, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent settings = new Intent(this, MainMenu.class);
                startActivity(settings);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == endCode){
            if(resultCode == Activity.RESULT_OK){
                ENDCOMMAND = data.getStringExtra("result");
            }
            else {
                end_command.setChecked(false);
            }
        }
        else if(requestCode == cancelCode){
            if(resultCode == Activity.RESULT_OK){
                NVMCOMMAND = data.getStringExtra("result");
            }
            else{
                nevermind_command.setChecked(false);
            }
        }
    }//onActivityResult

}
