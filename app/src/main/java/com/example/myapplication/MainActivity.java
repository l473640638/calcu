package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    float dollarRate = 0;
    float euroRate = 0;
    float wonRate = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar", 0.1f);
            euroRate = bundle.getFloat("key_euro", 0.1f);
            wonRate = bundle.getFloat("key_won", 0.1f);
        }

    }

    public void bt1(View v) {
        show(dollarRate, "$");
    }

    public void bt2(View v) {
        show(euroRate, "€");
    }

    public void bt3(View v) {
        show(wonRate, "₩");
    }

    public void openOne(View v) {
        openone();
        //Log.e("tes","openOne: dollarRate="+dollarRate);
    }

    private void openone() {
        Intent config = new Intent(this, ConfigActivity.class);

        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);
        startActivityForResult(new Intent(MainActivity.this, ConfigActivity.class), 1);
        onActivityResult(1, RESULT_OK, config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            openone();
        }
        return super.onOptionsItemSelected(item);
    }

    public void show(float i, String s) {
        // onActivityResult(1,2,getIntent());


        TextView out = (TextView) findViewById(R.id.textView2);
        EditText inp = (EditText) findViewById(R.id.editText);
        String gain = inp.getText().toString();
        String out_n = String.valueOf(Float.parseFloat(gain) * i);
        out.setText(out_n + s);
    }
}
