package com.systers.conference.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.systers.conference.R;
import com.systers.conference.util.AccountUtils;
import com.systers.conference.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static String LOG_TAG = LogUtils.makeLogTag(ProfileFragment.class);
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.subhead)
    TextView mSubHead;
    @BindView(R.id.connected)
    TextView mConnected;
    @BindView(R.id.google_plus_box)
    ImageView mGooglePlus;
    @BindView(R.id.facebook_box)
    ImageView mFacebook;
    private Unbinder mUnbinder;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        if (AccountUtils.getProfilePictureUrl(getActivity()) != null) {
            LogUtils.LOGE(LOG_TAG, AccountUtils.getProfilePictureUrl(getActivity()));
            Picasso.with(getActivity()).load(Uri.parse(AccountUtils.getProfilePictureUrl(getActivity())))
                    .resize(100, 100)
                    .placeholder(R.drawable.male_icon_9_glasses)
                    .error(R.drawable.male_icon_9_glasses)
                    .centerCrop()
                    .into(mAvatar);
        }
        mName.setText(AccountUtils.getFirstName(getActivity()) + " " + AccountUtils.getLastName(getActivity()));
        if (!TextUtils.isEmpty(AccountUtils.getCompanyRole(getActivity()))) {
            mSubHead.setText(AccountUtils.getCompanyRole(getActivity()));
        }
        if (!TextUtils.isEmpty(AccountUtils.getCompanyName(getActivity()))) {
            String text;
            if (!TextUtils.isEmpty(mSubHead.getText().toString())) {
                text = mSubHead.getText().toString() + ", " + AccountUtils.getCompanyName(getActivity());
            } else {
                text = AccountUtils.getCompanyName(getActivity());
            }
            mSubHead.setText(text);
        }
        if (!TextUtils.isEmpty(mSubHead.getText().toString())) {
            mSubHead.setVisibility(View.VISIBLE);
        }
        if (AccountUtils.hasActiveGoogleAccount(getActivity()) || AccountUtils.hasActiveFacebookAccount(getActivity())) {
            mConnected.setVisibility(View.VISIBLE);
        }
        if (AccountUtils.hasActiveGoogleAccount(getActivity())) {
            mGooglePlus.setVisibility(View.VISIBLE);
        }
        if (AccountUtils.hasActiveFacebookAccount(getActivity())) {
            mFacebook.setVisibility(View.VISIBLE);
            if (mGooglePlus.getVisibility() == View.GONE) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFacebook.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(0);
                }
                mFacebook.setLayoutParams(params);
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
