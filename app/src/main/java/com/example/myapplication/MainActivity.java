package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements Runnable {
    private float dollarRate = 0.1406f;
    private float euroRate = 0.1276f;
    private float wonRate = 167.8472f;
    Handler handler;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
       /* dollarRate= sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate  = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate   = sharedPreferences.getFloat("won_rate",0.0f);
        */

        setContentView(R.layout.activity_main);
        String updateDate = sharedPreferences.getString("update_date","");

//获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);
       /* Thread t = new Thread(this);
        t.start();
        */
        Log.i("timeqqq", "onCreate: sp updateDate=" + updateDate);
        Log.i("timeqqq", "onCreate: todayStr=" + todayStr);

//判断时间
        if(!todayStr.equals(updateDate)){
            Log.i("timeqqq", "onCreate: 需要更新");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i("timeqqq", "onCreate: 不需要更新");
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");

                    Log.i("dooo", "handleMessage: dollarRate:" + dollarRate);
                    Log.i("euuu", "handleMessage: euroRate:" + euroRate);
                    Log.i("wooo", "handleMessage: wonRate:" + wonRate);
                    SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "汇率已更新", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };


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

       SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = sharedPreferences.getFloat("dollar_rate", 0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate", 0.0f);
        wonRate = sharedPreferences.getFloat("won_rate", 0.0f);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Drate) {
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

    /*private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
    */

    public void run() {
        Log.i("q", "run: run().......");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //用于保存获取的汇率
        Bundle bundle = new Bundle();


        //获取网络数据
/*        URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html ="+html);
            Document doc = Jsoup.parse(html);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);
            Log.i("wq", "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");

           /* for(Element table: tables){
                Log.i(TAG, "run: table["+i+"] = "+ table);
                i++;
            }
*/
            Element table1 = tables.get(0);
            Log.i("wqq", "run: table1 = "+table1);

            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i("wqqq", "run:  "+ td1.text()+ "==>"+td2.text());

                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        //bundle中保存所获取的汇率
        //获取msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        //msg.obj = "Hello for run()";
        msg.obj = bundle;
        handler.sendMessage(msg);

    }



  /*  private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();

    }
    */
}

