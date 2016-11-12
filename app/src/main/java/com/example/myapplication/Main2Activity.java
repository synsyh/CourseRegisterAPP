package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    JSONObject result = null;
    String access_token;
    String clsList;

    JSONArray allClm;
    JSONObject cntClm;

    private TextView nameInfor;
    private TextView academyInfor;
    private TextView majorInfor;
    private TextView idInfor;
    private TextView gradeInfor;
    private Button clstButton;
    private GridView clmListView;
    private List<Map<String, String>> dataList;

    private String[] dayName = {};
    private String[] clsName = {};
    private SimpleAdapter adapter;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nameInfor = (TextView) findViewById(R.id.textView);
        academyInfor = (TextView) findViewById(R.id.textView2);
        majorInfor = (TextView) findViewById(R.id.textView3);
        idInfor = (TextView) findViewById(R.id.textView4);
        gradeInfor = (TextView) findViewById(R.id.textView5);

        clstButton = (Button) findViewById(R.id.clstbutton);

//        clmListView = (GridView) findViewById(R.id.clmView);


        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String accessToken = bundle.getString("accessToken");
//        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        Log.i("获取到的值为", accessToken);

        JSONObject json = null;
        try {
            json = new JSONObject(accessToken);
            result = new JSONObject(json.getString("userData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nameInfor.append(GetInfor.getName(result));
        academyInfor.append(GetInfor.getAcademy(result));
        majorInfor.append(GetInfor.getMajor(result));
        idInfor.append(GetInfor.getId(result));
        gradeInfor.append(GetInfor.getGrade(result));


        try {
            access_token = json.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("获取到的accesstoken为", access_token);

        access_token = "Bearer " + access_token;
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendGetRequest se = new sendGetRequest("http://202.203.209.96/v5api/api/Curriculum", "Authorization", access_token);
                clsList = se.getResponseContent();
                JSONObject clmJson = null;
                try {
                    clmJson = new JSONObject(clsList);
                    allClm = clmJson.getJSONArray("StudentCurriculumDetailInfos");
                    for (int i = 0; i < allClm.length(); i++) {
                        cntClm = allClm.getJSONObject(i);
                        if (cntClm.getString("SemesterId").equals("20161")) {
                            Log.i("本学期课程：", cntClm.getString("TeachClassName"));
                            System.out.print("hello");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        clstButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getClassList();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getClassList() {
//        String accessToken = bundle.getString("accessToken");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main2 Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
