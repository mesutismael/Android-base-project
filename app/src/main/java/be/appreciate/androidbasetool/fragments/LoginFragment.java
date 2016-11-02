package be.appreciate.androidbasetool.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.activities.MainActivity;
import be.appreciate.androidbasetool.api.ApiHelper;
import be.appreciate.androidbasetool.models.api.Technician;
import be.appreciate.androidbasetool.utils.DialogHelper;
import be.appreciate.androidbasetool.utils.Observer;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public class LoginFragment extends Fragment implements View.OnClickListener
{
    private EditText editTextUsername;
    private EditText editTextPassword;
    private MaterialDialog dialogProgress;

    public static LoginFragment newInstance()
    {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.editTextUsername = (EditText) view.findViewById(R.id.editText_username);
        this.editTextPassword = (EditText) view.findViewById(R.id.editText_password);
        Button buttonLogIn = (Button) view.findViewById(R.id.button_logIn);
        buttonLogIn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {

            case R.id.button_logIn:
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String username = this.editTextUsername.getText().toString();
                String password = this.editTextPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                {
                    this.showIncompleteDialog(view.getContext());
                } else
                {
                    this.showProgressDialog(view.getContext());
                    ApiHelper.logIn(view.getContext(), username, password).subscribe(this.loginObserver);
                }
                break;
        }
    }

    private void showIncompleteDialog(Context context)
    {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_error)
                .content(R.string.login_incomplete_error)
                .positiveText(R.string.dialog_positive)
                .show();
    }

    private void showLoginErrorDialog(Context context)
    {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_error)
                .content(R.string.login_login_error)
                .positiveText(R.string.dialog_positive)
                .show();
    }

    private void showProgressDialog(Context context)
    {
        this.dialogProgress = new MaterialDialog.Builder(context)
                .content(R.string.login_progress)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    private void startNextActivity()
    {
        if (this.getContext() != null)
        {
            Intent intent = MainActivity.getIntent(this.getContext());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(intent);
        }
    }

    private Observer<Technician> loginObserver = new Observer<Technician>()
    {
        @Override
        public void onCompleted()
        {
            if (LoginFragment.this.dialogProgress != null)
            {
                LoginFragment.this.dialogProgress.dismiss();
            }

            LoginFragment.this.startNextActivity();
        }

        @Override
        public void onError(Throwable e)
        {
            Log.d("LoginFragment", e.getMessage());
            if (LoginFragment.this.dialogProgress != null)
            {
                LoginFragment.this.dialogProgress.dismiss();
            }

            if (LoginFragment.this.getContext() != null && DialogHelper.canShowDialog(LoginFragment.this))
            {
                LoginFragment.this.showLoginErrorDialog(LoginFragment.this.getContext());
            }
        }
    };
}
