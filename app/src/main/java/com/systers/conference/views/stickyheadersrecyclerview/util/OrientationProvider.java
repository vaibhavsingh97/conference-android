package com.systers.conference.views.stickyheadersrecyclerview.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Aagam Shah on 6/3/2017.
 * <p>
 * Interface for getting the orientation of a RecyclerView from its LayoutManager
 */
public interface OrientationProvider {

    int getOrientation(RecyclerView recyclerView);

    boolean isReverseLayout(RecyclerView recyclerView);
}
