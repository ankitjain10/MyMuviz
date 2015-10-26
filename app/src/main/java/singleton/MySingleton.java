package singleton;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.dell.mymuviz.MyApplication;


public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private MySingleton() {
        mRequestQueue = getRequestQueue();
        imageLoader=new ImageLoader(mRequestQueue,new ImageLoader.ImageCache(){
            private final LruCache<String, Bitmap>
                    cache = new LruCache<>((int)Runtime.getRuntime().maxMemory()/(1024/8));

            @Override
            public Bitmap getBitmap(String url) {
                //Log.v("hello", String.valueOf(cache));
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                //Log.v("hello", String.valueOf(cache));
                cache.put(url, bitmap);
            }
        });

        }

    public static synchronized MySingleton getInstance() {
        if (mInstance == null) {
            mInstance = new MySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(MyApplication.getAppcontext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    public ImageLoader getImageLoader() {
        return imageLoader;
    }


}