package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    public static final int SHOW_RESPONSE = 0;

    //    HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
//    String HTTPhead = null; // 请求头信息
    String access_tokenall = null; // 获得的token（许可证）
    String access_token = null; //
    public static String Guid = null; // 验证码信息
    public static String TempGuid = null;
    String Authorization = "Authorization"; // 身份标识
    String LoginUrl = "http://202.203.209.96/v5api/api/GetLoginCaptchaInfo/null";
    String PostUrl = "http://202.203.209.96/v5api/OAuth/Token";
//    public static String testurl = "http://202.203.209.96/v5api/api/Curriculum";

//    String xkurl = "http://202.203.209.96/v5api/api/xk/Captcha";// 选课IMGUID
//    String Postxk = "http://202.203.209.96/v5api/api/xk/addDirect"; // 提交选课信息

//    public static String Username = null;
//    public static String Password = null;
//    String str = null; // 验证码字符串
//    String xkyzm = null;
//    String Coursecode = null;

    byte[] byteCheckCodeNum;
//    public static String checkCodeNum;

    Button button;
    TextView textView;
    EditText textName;
    EditText textPswd;
    EditText textChck;
    ImageView imageView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    Bitmap codeBitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(codeBitmap);
                    break;
                default:
                    break;
            }
        }
    };

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case '1':
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("accessToken", access_tokenall);
                    //用Bundle携带数据
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case '2':
                    Toast.makeText(MainActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    break;
                case '3':
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView1);
        textName = (EditText) findViewById(R.id.editText);
        textPswd = (EditText) findViewById(R.id.editText2);
        textChck = (EditText) findViewById(R.id.editText3);
        imageView = (ImageView) findViewById(R.id.imageView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendGetRequest se = new sendGetRequest(LoginUrl, Authorization, access_token);
                Guid = se.getResponseContent();
                String imgGuid = Guid.substring(62, 98);
                TempGuid = Guid.substring(13, 49);
                String imgUrl = "http://202.203.209.96/vimgs/" + imgGuid + ".png";

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet ht = new HttpGet(imgUrl);
                HttpResponse response = null;
                try {
                    response = httpclient.execute(ht);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpEntity entity = response.getEntity();
                InputStream inStream = null;
                try {
                    inStream = entity.getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] data = new byte[0];
                try {
                    data = readInputStream(inStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bitmap codeBitmap = Bytes2Bimap(data);
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = codeBitmap;
                handler.sendMessage(message);
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userName = textName.getText().toString();
                String userPswd = textPswd.getText().toString();
                String checkCode = textChck.getText().toString();
                access_tokenall = sendPostRequest.getRes(PostUrl, userName, userPswd, checkCode);
                if (access_tokenall.indexOf("\"error\"") > 0) {
                    if (access_tokenall.indexOf("验证码错误") > 0) {
                        Message msg = msgHandler.obtainMessage();
                        msg.arg1 = '2';
                        msgHandler.sendMessage(msg);
//                            se = new sendGetRequest(LoginUrl, Authorization, access_token);
//                            Guid = se.getResponseContent();
//                            CheckCode CC = new CheckCode();
//                            byteCheckCodeNum = CC.getByte(Guid);
//                            jLabel6.setIcon(new javax.swing.ImageIcon(byteCheckCodeNum));
                    }
                    if (access_tokenall.indexOf("用户名或密码不正确") > 0) {
                        Message msg = msgHandler.obtainMessage();
                        msg.arg1 = '3';
                        msgHandler.sendMessage(msg);
//                            se = new sendGetRequest(LoginUrl, Authorization, access_token);
//                            Guid = se.getResponseContent();
//                            CheckCode CC = new CheckCode();
//                            byteCheckCodeNum = CC.getByte(Guid);
//                            jLabel6.setIcon(new javax.swing.ImageIcon(byteCheckCodeNum));
                    }
                } else { // 获取授权的头信息（此处可用于获取课程表等操作） 需修改TEST URL
                    Message msg = msgHandler.obtainMessage();
                    msg.arg1 = '1';
                    msgHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
