package com.continuesvoicerecognition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;


public class MainMenu extends Activity {
    private TextView advanced_button;
    private Switch siriButton;
    private static boolean SIRION = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        advanced_button = (TextView) findViewById(R.id.Advanced_settings);
        siriButton = (Switch) findViewById(R.id.Siri_switch);

        if (SIRION) {
            siriButton.setChecked(true);
        }
        else {
            siriButton.setChecked(false);
        }

        advanced_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent advanced_intent = new Intent(MainMenu.this, Advanced.class);
                startActivity(advanced_intent);
            }
        });
        siriButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SIRION = true;
                }
                else {
                    SIRION = false;
                }
            }
        });
        ActionBar back = getActionBar();
        back.show();
        back.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
