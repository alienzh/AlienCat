package com.zhangw.aliencat.ui.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.ui.test.volloy.BitmapCache;
import com.zhangw.aliencat.ui.test.volloy.GsonRequest;
import com.zhangw.aliencat.ui.test.volloy.Weather;
import com.zhangw.aliencat.ui.test.volloy.WeatherInfo;
import com.zhangw.aliencat.ui.test.volloy.XMLRequest;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zhangw
 * @date 2018/4/9.
 * 描述 volley测试
 */
public class TestVolleyActivity extends BaseActivity {
    private final static String TAG = "TestVolleyActivity";

    @BindView(R.id.rvVolley)
    RecyclerView rvVolley;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.networkImage)
    NetworkImageView networkImage;

    private String mUrl = "https://m.baidu.com/?from=844b&vit=fps";
    private String jsonUrl = "http://www.weather.com.cn/data/sk/101010100.html";
    private String imageUrl = "https://tse1-mm.cn.bing.net/th?id=OIP.D-L1Rd2isVSBkzTkmBusXAHaEo&p=0&o=5&pid=1.1";
    private String weatherUrl = "http://www.weather.com.cn/data/sk/101010100.html";
    private String xmlUrl = "http://flash.weather.com.cn/wmaps/xml/china.xml";

    private BaseQuickAdapter<String, BaseViewHolder> mQuickAdapter;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_volley;
    }

    @Override
    public void initEnv() {
        showHead(true, true);
        setHeadTitle("volley");
        List<String> data = new ArrayList<>();
        data.add("StringRequest");
        data.add("PostRequest");
        data.add("JsonObjectRequest");
        data.add("ImageRequest");
        data.add("ImageLoader");
        data.add("ImageLoaderCache");
        data.add("networkImage");
        data.add("gsonRequest");
        data.add("xmlRequest");

        rvVolley.setLayoutManager(new LinearLayoutManager(this));
        mQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.support_simple_spinner_dropdown_item, data) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
                helper.setTag(android.R.id.text1, item);
                helper.addOnClickListener(android.R.id.text1);
            }
        };
        rvVolley.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String str = (String) view.getTag();
                if (StringUtils.isEmpty(str)) {
                    return;
                }
                switch (str) {
                    case "StringRequest":
                        stringRequest();
                        break;
                    case "PostRequest":
                        postRequest();
                        break;
                    case "JsonObjectRequest":
                        jsonObjectRequest();
                        break;
                    case "ImageRequest":
                        imageRequest();
                        break;
                    case "ImageLoader":
                        imageLoaderRequest();
                        break;
                    case "ImageLoaderCache":
                        imageLoaderRequestCache();
                        break;
                    case "networkImage":
                        networkImageRequest();
                        break;
                    case "gsonRequest":
                        gsonRequest();
                        break;
                    case "xmlRequest":
                        xmlRequest();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void stringRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.dTag(TAG, response);
                ToastUtils.showLong("volleySuccess");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong("volleyError");
            }
        });
        queue.add(request);
    }

    private void postRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                LogUtils.dTag(TAG, response);
                ToastUtils.showLong("volleySuccess");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong("volleyError");
            }
        });
        queue.add(request);
    }

    private void jsonObjectRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(jsonUrl, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                LogUtils.dTag(TAG, response.toString());
                ToastUtils.showLong("volleySuccess");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong("volleyError");
            }
        });
        queue.add(request);
    }

    private void imageRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest request = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ToastUtils.showLong("volleySuccess");
                image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong("volleyError");
            }
        });
        queue.add(request);
    }

    private void imageLoaderRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        imageLoader.get(imageUrl, ImageLoader.getImageListener(image,
                R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground));
    }

    private void imageLoaderRequestCache() {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());
        imageLoader.get(imageUrl, ImageLoader.getImageListener(image,
                R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground));
    }

    private void networkImageRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageLoader imageLoader = new ImageLoader(queue, new BitmapCache());
        networkImage.setDefaultImageResId(R.drawable.ic_launcher_background);
        networkImage.setErrorImageResId(R.drawable.ic_launcher_foreground);
        networkImage.setImageUrl(imageUrl, imageLoader);
    }

    private void gsonRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        GsonRequest<Weather> request = new GsonRequest<Weather>(weatherUrl, Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather response) {
                ToastUtils.showLong("volleySuccess");
                LogUtils.dTag(TAG, response.getWeatherinfo().toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong("volleyError");

                LogUtils.eTag(TAG, error.getMessage(), error);
            }
        });
        queue.add(request);
    }

    private void xmlRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        XMLRequest request = new XMLRequest(xmlUrl, new Response.Listener<XmlPullParser>() {
            @Override
            public void onResponse(XmlPullParser response) {
                ToastUtils.showLong("volleySuccess");
                LogUtils.dTag(TAG, response.toString());
                try {
                    int eventType = response.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if ("city".equals(response.getName())) {

                                    LogUtils.dTag(TAG, response.getAttributeName(0));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showLong("volleyError");

                LogUtils.eTag(TAG, error.getMessage(), error);
            }
        });
        queue.add(request);
    }
}
