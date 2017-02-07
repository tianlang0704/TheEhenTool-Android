package org.acrew.cmonk.theehentool_preview.preview.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.acrew.cmonk.theehentool_preview.R;
import org.acrew.cmonk.theehentool_preview.common.tools.XPathParser;
import org.acrew.cmonk.theehentool_preview.preview.data.PreviewData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PreviewFragment extends Fragment implements PreviewDataFetcherDelegate {
    public RecyclerView previewRecView;

    private PreviewDataFetcher fetcher = PreviewDataFetcher.singleton;

    public PreviewFragment() {
        // Required empty public constructor
    }

    public static PreviewFragment newInstance() {
        return new PreviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.preview_fragment, container, false);
        this.InitializeRecView(view);
        this.fetcher.SetDelegate(this);
        this.fetcher.FetchData();

        return view;
    }

    private void InitializeRecView(View view) {
        this.previewRecView = (RecyclerView) view.findViewById(R.id.preview_rec_view);
        this.previewRecView.setAdapter(new PreviewRecViewAdapter(new ArrayList<>(), this.getContext()));
        this.previewRecView.setLayoutManager(new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL));
    }


    @Override
    public void FetchComplete(ArrayList<PreviewData> data) {
        this.getActivity().runOnUiThread(() -> previewRecView.setAdapter(new PreviewRecViewAdapter(data, getContext())));
        Log.wtf("wtf", data.toString());
    }
}


