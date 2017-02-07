package org.acrew.cmonk.theehentool_preview.common.tools;

import android.graphics.Bitmap;
import android.util.Pair;

import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

/**
 * Created by CMonk on 2/6/2017.
 */

public class Downloader {
    public ObservableSource<ArrayList<Bitmap>> PromiseToDownloadImages(ArrayList<String> strings) {
        //Create observable stream to start async downloading
        ArrayList<Pair<Integer, Observable<Bitmap>>> orderObList = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            Observable<Bitmap> obBitmap = Rx2AndroidNetworking.get(s)
                    .setOkHttpClient(client)
                    .getResponseOnlyFromNetwork()
                    .build()
                    .getBitmapObservable();

            orderObList.add(new Pair<>(i, obBitmap));
        }
        return Observable.fromIterable(orderObList)
        //Actual async downloading
        .flatMap((final Pair<Integer, Observable<Bitmap>> orderOb) ->
            Observable.defer(() ->
                Observable.just(
                    new Pair<>(orderOb.first, orderOb.second.subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).blockingSingle())
                )
            )
        )
        //Sort result
        .toSortedList((o1, o2) -> o1.first.compareTo(o2.first))
        //Get results and throw out order index
        .flatMap(pairs -> {
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            for (Pair<Integer, Bitmap> pair: pairs) {
                bitmaps.add(pair.second);
            }
            return Single.just(bitmaps);
        })
        .toObservable();
    }
}
