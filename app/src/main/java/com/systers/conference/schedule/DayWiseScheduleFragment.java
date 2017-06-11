package com.systers.conference.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.systers.conference.R;
import com.systers.conference.schedule.dummy.DummyContent;
import com.systers.conference.views.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a list of Items.
 */
public class DayWiseScheduleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Unbinder unbinder;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DayWiseScheduleFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DayWiseScheduleFragment newInstance(int columnCount) {
        DayWiseScheduleFragment fragment = new DayWiseScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Set the adapter
        if (mRecyclerView != null) {
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mColumnCount));
            }
            ConferenceScheduleRecyclerViewAdapter conferenceScheduleRecyclerViewAdapter = new ConferenceScheduleRecyclerViewAdapter(DummyContent.ITEMS);
            mRecyclerView.setAdapter(conferenceScheduleRecyclerViewAdapter);
            final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(conferenceScheduleRecyclerViewAdapter);
            mRecyclerView.addItemDecoration(headersDecoration);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
