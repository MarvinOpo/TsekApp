package com.example.mvopo.tsekapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.mvopo.tsekapp.Helper.ConnectionChecker;
import com.example.mvopo.tsekapp.Helper.ListAdapter;
import com.example.mvopo.tsekapp.MainActivity;
import com.example.mvopo.tsekapp.Model.FamilyProfile;
import com.example.mvopo.tsekapp.Model.FeedBack;
import com.example.mvopo.tsekapp.R;

import java.util.ArrayList;

/**
 * Created by mvopo on 12/21/2017.
 */

public class FeedbackFragment extends Fragment{

    ListView lv;
    TextView tvNothing;
    ArrayList<FeedBack> feedbacks = new ArrayList<>();
    ListAdapter adapter;

    AlertDialog dialog;
    RelativeLayout searchholder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);

        lv = view.findViewById(R.id.lv);
        tvNothing = view.findViewById(R.id.list_nothing);
        searchholder = view.findViewById(R.id.search_holder);
        searchholder.setVisibility(View.GONE);

        feedbacks.clear();
        feedbacks = MainActivity.db.getFeedbacks();

        if(feedbacks.size() <= 0) tvNothing.setVisibility(View.VISIBLE);

        adapter = new ListAdapter(getContext(), R.layout.feedback_item, null, null, feedbacks);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        showTutorial();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.feedback, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_feedback:
                showDialog();
                break;
            case R.id.action_upload_feedback:
                int feedbackCount = MainActivity.db.getFeedbacksCount();

                if(feedbackCount > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(feedbackCount + " Feedbacks uploadable. Proceed upload?");
                    builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (new ConnectionChecker(getContext()).isConnectedToInternet()) {
                                String body = MainActivity.db.getFeedbacksForUpload();
                                Log.e("QWEQWE", body);
                                BackgroundMail.newBuilder(getContext())
                                        .withUsername("phacheckapp@gmail.com")
                                        .withPassword("phacheckapp123")
                                        .withMailto("hontoudesu123@gmail.com")
                                        .withSubject("PHA Check-App Feedback")
                                        .withBody(getSenderInfo() + body)
                                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                            @Override
                                            public void onSuccess() {
                                                MainActivity.db.deleteFeedback("");
                                                refreshList();
                                            }
                                        })
                                        .send();
                            } else
                                Toast.makeText(getContext(), "No internet conenction", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                }else  Toast.makeText(getContext(), "Nothing to upload.", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        View feedbackDilaog = LayoutInflater.from(getContext()).inflate(R.layout.feedback_dialog, null, false);

        final EditText edtxSubject = feedbackDilaog.findViewById(R.id.feedback_subject);
        final EditText edtxBody = feedbackDilaog.findViewById(R.id.feedback_body);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(feedbackDilaog);
        builder.setPositiveButton("Send", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String subject = edtxSubject.getText().toString().trim();
                final String body = edtxBody.getText().toString().trim();

                if(subject.isEmpty()){
                    edtxSubject.setError("Required.");
                    edtxSubject.requestFocus();
                }else if(body.isEmpty()){
                    edtxBody.setText("Required.");
                    edtxBody.requestFocus();
                }else{
                    if(new ConnectionChecker(getContext()).isConnectedToInternet()){
                        String user = MainActivity.user.fname + " " + MainActivity.user.lname;
                        BackgroundMail.newBuilder(getContext())
                                .withUsername("phacheckapp@gmail.com")
                                .withPassword("phacheckapp123")
                                .withMailto("hontoudesu123@gmail.com, games.jlomocso@gmail.com, " +
                                        "tsekap.dohro7@gmail.com")
                                .withSubject("PHA Check-App Feedback")
                                .withBody(getSenderInfo() + subject + " - " + body)
                                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                    @Override
                                    public void onFail() {
                                        MainActivity.db.addFeedback(new FeedBack("", subject, body));
                                        Toast.makeText(getContext(), "No internet conenction, feedback saved locally.", Toast.LENGTH_SHORT).show();
                                        refreshList();
                                    }
                                })
                                .send();
                    }else {
                        MainActivity.db.addFeedback(new FeedBack("", subject, body));
                        Toast.makeText(getContext(), "No internet conenction, feedback saved locally.", Toast.LENGTH_SHORT).show();
                        refreshList();
                    }

                    dialog.dismiss();
                }
            }
        });
    }

    public String getSenderInfo(){
        TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        String senderInfo =  "Name: " + MainActivity.user.fname + " " + MainActivity.user.lname + "\n" +
                "Registered Number: " + MainActivity.user.contact + "\n" +
                "Device Number:" + mPhoneNumber + "\n\n";

        return senderInfo;
    }

    public void refreshList(){
        feedbacks.clear();
        feedbacks.addAll(MainActivity.db.getFeedbacks());
        adapter.notifyDataSetChanged();

        if(feedbacks.size() <= 0) tvNothing.setVisibility(View.VISIBLE);
        else tvNothing.setVisibility(View.GONE);
    }

    public void showTutorial(){
//        new FancyShowCaseView.Builder(getActivity())
//                .focusOn(lv)
//                .title("This shows list of feedback that needs to be uploaded")
//                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                .titleGravity(Gravity.TOP)
//                .focusShape(FocusShape.ROUNDED_RECTANGLE)
//                .roundRectRadius(15)
//                .showOnce("feedback")
//                .dismissListener(new DismissListener() {
//                    @Override
//                    public void onDismiss(String id) {
//                        new FancyShowCaseView.Builder(getActivity())
//                                .focusOn(MainActivity.toolbar.getChildAt(2).findViewById(R.id.action_upload_feedback))
//                                .title("To upload feedbacks shown in list, tap this button")
//                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                .dismissListener(new DismissListener() {
//                                    @Override
//                                    public void onDismiss(String id) {
//                                        new FancyShowCaseView.Builder(getActivity())
//                                                .focusOn(MainActivity.toolbar.getChildAt(2).findViewById(R.id.action_add_feedback))
//                                                .titleSize(20, TypedValue.COMPLEX_UNIT_DIP)
//                                                .title("And to report problem/feedback, tap this button")
//                                                .build()
//                                                .show();
//                                    }
//
//                                    @Override
//                                    public void onSkipped(String id) {.
//
//                                    }
//                                })
//                                .build()
//                                .show();
//                    }
//
//                    @Override
//                    public void onSkipped(String id) {
//
//                    }
//                })
//                .build()
//                .show();

        MainActivity.queue.clear();
        MainActivity.queue.add(MainActivity.makeSpotlightView(lv,
                "Hi there!",
                "These are list of uploadable feedbacks",
                "FeedbackList"));

        MainActivity.queue.add(MainActivity.makeSpotlightView(MainActivity.toolbar.getChildAt(2),
                "Konnichiwa!",
                "Plus Button: If you want to give some feedback, just click me dudeee!\n\n" +
                        "Arrow Up Button: If you make feedback but doesnt have connection, it will appear in the list." +
                        "And you need to click me to upload those feedback.",
                "FeedbackMenus"));

        MainActivity.startSequence();

    }
}
