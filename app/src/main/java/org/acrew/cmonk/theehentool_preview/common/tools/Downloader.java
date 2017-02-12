package org.acrew.cmonk.theehentool_preview.common.tools;

import android.graphics.Bitmap;
import android.util.Pair;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import okhttp3.OkHttpClient;

/**
 * Created by CMonk on 2/6/2017.
 */

public class Downloader {
    public ObservableSource<ArrayList<Bitmap>> PromiseToDownloadImages(ArrayList<String> URLs) {
        //Create observable stream to start async downloading
        //TODO: Add wake lock in the near future
        ArrayList<Pair<Integer, ANRequest>> orderObList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
        for (int i = 0; i < URLs.size(); i++) {
            String s = URLs.get(i);
            ANRequest req = AndroidNetworking.get(s)
                    .setOkHttpClient(client)
                    .build();

            orderObList.add(new Pair<>(i, req));
        }
        return Observable.fromIterable(orderObList)
        //Do actual async downloading
        .flatMap((final Pair<Integer, ANRequest> orderReq) ->
            Observable.create((ObservableOnSubscribe<Pair<Integer, Bitmap>>) e -> {
                    orderReq.second.getAsBitmap(new BitmapRequestListener() {
                        @Override
                        public void onResponse(Bitmap response) {
                            e.onNext(new Pair<>(orderReq.first, response));
                            e.onComplete();
                        }
                        @Override
                        public void onError(ANError anError) {
                            e.onError(anError);
                        }
                    });
                }
            )
        )
        //Sort result
        .toSortedList((o1, o2) -> o1.first.compareTo(o2.first))
        //Get results and throw out order index
        .flatMap(pairs -> {
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            for (Pair<Integer, Bitmap> pair : pairs) {
                bitmaps.add(pair.second);
            }
            return Single.just(bitmaps);
        })
        .toObservable();
    }
}
