package org.acrew.cmonk.theehentool_preview.preview.controller;

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
import org.acrew.cmonk.theehentool_preview.preview.data.PreviewData;

import java.util.ArrayList;

public class PreviewFragment extends Fragment implements PreviewDataFetcherDelegate {
    private PreviewDataFetcher m_Fetcher = PreviewDataFetcher.singleton;

    public RecyclerView m_PreviewRecView;
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
        this.m_Fetcher.SetDelegate(this);
        this.m_Fetcher.FetchData();

        return view;
    }

    private void InitializeRecView(View view) {
        this.m_PreviewRecView = (RecyclerView) view.findViewById(R.id.preview_rec_view);
        this.m_PreviewRecView.setAdapter(new PreviewRecViewAdapter(new ArrayList<>(), this.getContext()));
        this.m_PreviewRecView.setLayoutManager(new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL));
    }


    @Override
    public void FetchComplete(ArrayList<PreviewData> data) {
        this.getActivity().runOnUiThread(() -> m_PreviewRecView.setAdapter(new PreviewRecViewAdapter(data, getContext())));
        Log.wtf("wtf", data.toString());
    }
}


