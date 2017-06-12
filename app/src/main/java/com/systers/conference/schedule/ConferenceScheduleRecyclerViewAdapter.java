package com.systers.conference.schedule;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.systers.conference.R;
import com.systers.conference.event.EventDetailActivity;
import com.systers.conference.schedule.dummy.DummyContent.DummyItem;
import com.systers.conference.views.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ConferenceScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ConferenceScheduleRecyclerViewAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter {

    private final List<DummyItem> mValues;


    public ConferenceScheduleRecyclerViewAdapter(List<DummyItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, holder.mIdView.getText().toString());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, holder.mContentView.getText().toString());
                try {
                    v.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(v.getContext(), R.string.calendar_not_found, Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), EventDetailActivity.class));
            }
        });
    }
    
    @Override
    public long getHeaderId(int position) {
        return Long.valueOf(mValues.get(position).id);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView.findViewById(R.id.recycler_view_header);
        textView.setText(mValues.get(position).id);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.subtitle);
            mImageView = (ImageView) view.findViewById(R.id.add_to_calendar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
