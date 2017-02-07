package org.acrew.cmonk.theehentool_preview.preview.controller;

import android.graphics.Bitmap;
import android.util.Log;

import org.acrew.cmonk.theehentool_preview.common.tools.Downloader;
import org.acrew.cmonk.theehentool_preview.common.tools.EHenConfigHelper;
import org.acrew.cmonk.theehentool_preview.common.tools.XPathParser;
import org.acrew.cmonk.theehentool_preview.preview.data.PreviewData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CMonk on 2/6/2017.
 */

interface PreviewDataFetcherDelegate {
    void FetchComplete(ArrayList<PreviewData> data);
}

public class PreviewDataFetcher {
    private PreviewDataFetcherDelegate delegate;
    private ArrayList<PreviewData> data = new ArrayList<>();
    private XPathParser parser = new XPathParser();
    private EHenConfigHelper ehenConfig = EHenConfigHelper.singleton;

    private int order = 0;

    public void FetchData() {
        parser.AddParseConfig("ImageBitmapURL", new XPathParser.ParseConfig(ehenConfig.previewThumbXPath, ""));
        parser.PromiseToParseListInURL("https://e-hentai.org/?inline_set=dm_t&f_non-h=1&f_apply=Apply+Filter", ehenConfig.previewListXPath)
        .flatMap((results) -> {
            for (HashMap<String, String> item: results) {
                data.add(new PreviewData(order, "", item.get("ImageBitmapURL"), null));
            }

            Downloader dl = new Downloader();
            ArrayList<String> imageURL = new ArrayList<>();
            for (Map<String, String> result: results) {
                imageURL.add(result.get("ImageBitmapURL"));
            }
            return dl.PromiseToDownloadImages(imageURL);
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(new Observer<List<Bitmap>>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onError(Throwable e) {
                Log.wtf("wtf", e.toString());
            }

            @Override
            public void onNext(List<Bitmap> bitmaps) {
                for (int i = 0; i < bitmaps.size(); i++) {
                    data.get(i).thumbBitmap = bitmaps.get(i);
                }
            }

            @Override
            public void onComplete() {
                if (delegate != null) {
                    delegate.FetchComplete(data);
                }
            }
        });
    }

    public void SetDelegate(PreviewDataFetcherDelegate delegate) {
        this.delegate = delegate;
    }

    public static final PreviewDataFetcher singleton = new PreviewDataFetcher();
}
