package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {
    Intent intent;
    TextView t1;
    TextView t2;
    TextView t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);
         t1 = findViewById(R.id.Dollar1);
         t2 = findViewById(R.id.Euro1);
        t3 = findViewById(R.id.Won1);
        t1.setText(Float.toString(dollar2));
        t2.setText(Float.toString(euro2));
        t3.setText(Float.toString(won2));


    }
protected void btn5(View v)
{
    Bundle bdl = new Bundle();
    bdl.putFloat("key_dollar",Float.parseFloat(t1.getText().toString()));
    bdl.putFloat("key_euro",Float.parseFloat(t2.getText().toString()));
    bdl.putFloat("key_won",Float.parseFloat(t3.getText().toString()));
    //Log.i("aa",t1.getText().toString());
    intent.putExtras(bdl);
    setResult(RESULT_OK, intent);

    finish();
}

}
