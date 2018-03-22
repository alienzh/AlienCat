package com.zhangw.aliencat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String a = KEY_NULL.concat(type.value);
//        Toast.makeText(this, a, Toast.LENGTH_LONG).show();

        if (mManager == null) {
            mManager = new Manager();
        }
        mManager.doWork();
        System.out.println("count1:"+count1);
        System.out.println("count2:"+count2);
    }


    private Manager mManager = null;
    public int count1 = 0;
    public int count2 = 0;

    private class Manager {
        public Manager() {
        }

        void doWork() {
            int i = count1++;
            count2 = count2++;
        }
    }

    private String KEY_NULL = "null";
    private Type type = Type.THUMBNAIL;

    private enum Type {
        THUMBNAIL(""),
        BLUR("#BLUR"),
        ROUND("#ROUND");

        private String value;

        Type(String value) {
            this.value = value;
        }
    }
}
