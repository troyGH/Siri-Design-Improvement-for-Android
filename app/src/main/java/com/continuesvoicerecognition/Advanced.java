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
    public static int END = 0, CANCEL = 0, HEYSIRI = 0;
    private Switch end_command;
    private Switch nevermind_command;
    private Switch hello_siri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        end_command = (Switch) findViewById(R.id.End_Switch);
        nevermind_command = (Switch) findViewById(R.id.Nevermind_Switch);
        hello_siri = (Switch) findViewById(R.id.Allow_Switch);

        if (END == 0) {
            end_command.setChecked(false);
        }
        else {
            end_command.setChecked(true);
        }

        if (CANCEL == 0) {
            nevermind_command.setChecked(false);
        }
        else {
            nevermind_command.setChecked(true);
        }

        if (HEYSIRI == 0) {
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
                if (((Switch)v).isChecked()) {
                    END = 1;
                }
                else {
                    END = 0;
                }
            }
        });

        nevermind_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Switch) v).isChecked()) {
                    CANCEL = 1;
                } else {
                    CANCEL = 0;
                }
            }
        });

        hello_siri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Switch) v).isChecked()) {
                    HEYSIRI = 1;
                } else {
                    HEYSIRI = 0;
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
}
