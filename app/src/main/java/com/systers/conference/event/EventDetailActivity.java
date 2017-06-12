package com.systers.conference.event;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.systers.conference.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.event_title)
    TextView mEventTitle;

    @BindView(R.id.time)
    TextView mTime;

    @BindView(R.id.event_description)
    TextView mEventDescription;

    @BindView(R.id.fab_menu)
    FloatingActionsMenu mFloatingActionsMenu;

    @OnClick(R.id.calendar_fab)
    public void addToCalendar() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, mEventTitle.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, mEventDescription.getText().toString());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.calendar_not_found, Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.share_fab)
    public void share() {
        startActivity(getShareChooserIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Intent getShareChooserIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setSubject(String.format("%1$s (GHC)", mEventTitle.getText().toString()))
                .setType("text/plain")
                .setText(String.format("%1$s %2$s #GHC", mEventTitle.getText().toString(), mTime.getText().toString()))
                .setChooserTitle(R.string.share)
                .createChooserIntent();
    }

    @Override
    public void onBackPressed() {
        if (mFloatingActionsMenu.isExpanded()) {
            mFloatingActionsMenu.collapse();
        } else {
            super.onBackPressed();
        }
    }
}
