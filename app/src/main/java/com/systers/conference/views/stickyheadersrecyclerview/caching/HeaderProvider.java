package com.systers.conference.views.stickyheadersrecyclerview.caching;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Aagam Shah on 6/3/2017.
 */

/**
 * Implemented by objects that provide header views for decoration
 */
public interface HeaderProvider {

    /**
     * Will provide a header view for a given position in the RecyclerView
     *
     * @param recyclerView that will display the header
     * @param position     that will be headed by the header
     * @return a header view for the given position and list
     */
    View getHeader(RecyclerView recyclerView, int position);

    void invalidate();
}
