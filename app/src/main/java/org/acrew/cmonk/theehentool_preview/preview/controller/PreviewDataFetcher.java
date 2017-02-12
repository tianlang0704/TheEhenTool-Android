package org.acrew.cmonk.theehentool_preview.preview.controller;

import android.graphics.Bitmap;
import android.util.Log;

import org.acrew.cmonk.theehentool_preview.common.tools.Downloader;
import org.acrew.cmonk.theehentool_preview.common.tools.EHenConfigHelper;
import org.acrew.cmonk.theehentool_preview.common.tools.SearchConfigHelper;
import org.acrew.cmonk.theehentool_preview.common.tools.XPathParser;
import org.acrew.cmonk.theehentool_preview.preview.data.PreviewData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by CMonk on 2/6/2017.
 */

interface PreviewDataFetcherDelegate {
    void FetchComplete(ArrayList<PreviewData> data);
}

public class PreviewDataFetcher {
    private PreviewDataFetcherDelegate m_Delegate;
    private ArrayList<PreviewData> m_Data = new ArrayList<>();
    private XPathParser m_Parser = new XPathParser();
    private EHenConfigHelper m_EhenConfig = EHenConfigHelper.singleton;
    private SearchConfigHelper m_SearchConfig = SearchConfigHelper.singleton;

    private int order = 0;

    public void FetchData() {
        m_SearchConfig.SetConfiguration(SearchConfigHelper.Options.NONH, true);
        String searchString = m_SearchConfig.GetSearchURL("");

        m_Parser.AddParseConfig("ImageBitmapURL", new XPathParser.ParseConfig(m_EhenConfig.m_PreviewThumbXPath, ""));
        m_Parser.PromiseToParseListInURL(searchString, m_EhenConfig.m_PreviewListXPath)
        .flatMap((results) -> {
            for (HashMap<String, String> item: results) {
                m_Data.add(new PreviewData(order, "", item.get("ImageBitmapURL"), null));
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
                    m_Data.get(i).m_ThumbBitmap = bitmaps.get(i);
                }
            }

            @Override
            public void onComplete() {
                if (m_Delegate != null) {
                    m_Delegate.FetchComplete(m_Data);
                }
            }
        });
    }

    public void SetDelegate(PreviewDataFetcherDelegate delegate) {
        this.m_Delegate = delegate;
    }

    public static final PreviewDataFetcher singleton = new PreviewDataFetcher();
}
