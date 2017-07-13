package com.systers.conference.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.systers.conference.MainActivity;
import com.systers.conference.R;
import com.systers.conference.util.AccountUtils;
import com.systers.conference.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A screen that allows user to verify details and register for the event.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = LogUtils.makeLogTag(RegisterActivity.class);
    @BindView(R.id.register_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.register_form)
    ScrollView mRegisterForm;
    @BindView(R.id.first_name)
    EditText mFirstName;
    @BindView(R.id.last_name)
    EditText mLastName;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.company_name)
    EditText mCompanyName;
    @BindView(R.id.role)
    EditText mRole;
    @BindView(R.id.register_button)
    Button mRegister;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    @OnClick(R.id.register_button)
    public void register() {
        attemptLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AccountUtils.getRegisterVisited(this)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_register));
        mRole.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        if (AccountUtils.getFirstName(getApplicationContext()) != null) {
            mFirstName.setText(AccountUtils.getFirstName(getApplicationContext()));
        }
        if (AccountUtils.getLastName(getApplicationContext()) != null) {
            mLastName.setText(AccountUtils.getLastName(getApplicationContext()));
        }
        if (AccountUtils.getEmail(getApplicationContext()) != null) {
            mEmail.setText(AccountUtils.getEmail(getApplicationContext()));
        }
        if (AccountUtils.getCompanyName(getApplicationContext()) != null) {
            mCompanyName.setText(AccountUtils.getCompanyName(getApplicationContext()));
        }
        if (AccountUtils.getCompanyRole(getApplicationContext()) != null) {
            mRole.setText(AccountUtils.getCompanyRole(getApplicationContext()));
        }
        if (!TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setEnabled(false);
            mLastName.setNextFocusDownId(R.id.company_name);
        }
    }


    /**
     * Attempts to register the account specified by the form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mFirstName.setError(null);
        mLastName.setError(null);
        mCompanyName.setError(null);
        mRole.setError(null);
        mEmail.setError(null);

        // Store values at the time of the register attempt.
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check for empty names.
        if (TextUtils.isEmpty(firstName)) {
            mFirstName.setError(getString(R.string.error_field_required));
            focusView = mFirstName;
            cancel = true;
        } else if (TextUtils.isEmpty(lastName)) {
            mLastName.setError(getString(R.string.error_field_required));
            focusView = mLastName;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegisterForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private Activity activity;

        UserLoginTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                AccountUtils.setFirstName(activity, mFirstName.getText().toString());
                AccountUtils.setLastName(activity, mLastName.getText().toString());
                AccountUtils.setEmail(activity, mEmail.getText().toString());
                AccountUtils.setCompanyName(activity, mCompanyName.getText().toString());
                AccountUtils.setCompanyRole(activity, mRole.getText().toString());
                AccountUtils.setRegisterVisited(activity);
                startActivity(new Intent(activity, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                LogUtils.LOGE(LOG_TAG, "OnPostExecute()");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

