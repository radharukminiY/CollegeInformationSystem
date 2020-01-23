package isep.fr.collegeinformationsystem.WebServiceUtil;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by lenovo on 11/27/2017.
 */

public class VolleySigleton {
    /*** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    private static VolleySigleton mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySigleton(Context context) {
        // Specify the application context
        mContext = context;
        // Get the request queue
        mRequestQueue = getRequestQueue();
    }

    public static VolleySigleton getInstance(Context context) {
        // If Instance is null then initialize new Instance
        if (mInstance == null) {
            mInstance = new VolleySigleton(context);
        }
        // Return MySingleton new Instance
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        // If RequestQueue is null the initialize new RequestQueue
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        // Return RequestQueue
        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request) {

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(request);
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
