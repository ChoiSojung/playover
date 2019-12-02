package com.playover;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.playover.models.Comment;
import com.playover.models.Person;
import com.playover.viewmodels.AuthUserViewModel;
import com.playover.viewmodels.DiscountsViewModel;
import com.playover.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DiscountDetailFragment extends Fragment {

    private TextView displayBName;
    private TextView displayAddress;
    private TextView displayPhone;
    private TextView displayWebsite;
    private TextView displayDiscount;
    private TextView displayComments;
    private TextView lblComments;
    private TextView lastUpdate;
    private String city;
    private String state;
    private String uId;
    private Button addComment;
    private AuthUserViewModel authVm;
    private DiscountsViewModel discVm;
    private UserViewModel userVm;
    private Bundle fromRecycleViewer;
    private FragmentManager fragmentManager;

    public DiscountDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        authVm = new AuthUserViewModel();
        discVm = new DiscountsViewModel();
        userVm = new UserViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discount_detail, container, false);
        setHasOptionsMenu(true);
        DiscountsViewModel discVm = new DiscountsViewModel();
        fragmentManager = getFragmentManager();
        displayBName = rootView.findViewById(R.id.displayBName);
        displayAddress = rootView.findViewById(R.id.displayAddress);
        displayPhone = rootView.findViewById(R.id.displayPhone);
        displayWebsite = rootView.findViewById(R.id.displayWebsite);
        lblComments = rootView.findViewById(R.id.lblComments);
        displayDiscount = rootView.findViewById(R.id.displayDiscount);
        displayComments = rootView.findViewById(R.id.displayComments);
        lastUpdate = rootView.findViewById(R.id.lastUpdate);
        addComment = rootView.findViewById(R.id.addComment);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null) {
                    AlertDialog.Builder newComment = new AlertDialog.Builder(getContext());
                    //change alert box title color
                    SpannableString title = new SpannableString("Add A Comment");
                    title.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary)),
                            0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    newComment.setTitle(title);
                    final EditText inputComment = new EditText(getContext());
                    inputComment.requestFocus();
                    inputComment.setSingleLine(false);
                    inputComment.setLines(2);
                    inputComment.setHint("Comment...");
                    inputComment.setPadding(5, 0, 5, 0);
                    //set edittext border color
                    inputComment.setBackgroundResource(R.drawable.border_comment);
                    LinearLayout container = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(35, 15, 35, 10);
                    inputComment.setLayoutParams(params);
                    container.addView(inputComment);
                    newComment.setView(container);
                    newComment.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (inputComment.getText().toString().trim().length() != 0) {
                                userVm.getUser(authVm.getUser().getUid(),
                                        (Person user) -> {
                                            if (user != null) {
                                                String usersName = user.getFirstName().toUpperCase() + " "
                                                        + user.getLastName().toUpperCase().substring(0, 1) + ".";
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
                                                Date date = new Date();
                                                String today = dateFormat.format(date);
                                                Comment c = new Comment("", today, inputComment.getText().toString().trim().toUpperCase(), usersName, user.getuId());
                                                discVm.addComment(state, city, uId, c, new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            if(getActivity()!=null) {
                                                                Toast.makeText(getActivity(), "Comment added!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            if (task.getException() != null)
                                                                if (getActivity() != null) {
                                                                   // Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                );
                            } else {
                                if(getActivity()!=null) {
                                    Toast.makeText(getActivity(), "Comment text can not be empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    newComment.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //dont add comment
                        }
                    });
                    AlertDialog dialogComment = newComment.create();
                    if (dialogComment.getWindow() != null) {
                        dialogComment.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    }
                    dialogComment.show();
                }
            }
        });
        fromRecycleViewer = this.getArguments();
        StringBuilder sb = new StringBuilder();
        if (fromRecycleViewer != null) {
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_BUSINESSNAME))) {
                displayBName.setText(fromRecycleViewer.getString(Constants.KEY_BUSINESSNAME));
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_ADDRESS))) {
                sb.append(fromRecycleViewer.getString(Constants.KEY_ADDRESS));
            }
            if (!TextUtils.isEmpty((fromRecycleViewer.getString(Constants.KEY_CITY))) && !TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_STATE))) {
                sb.append("\n");
                sb.append(fromRecycleViewer.getString(Constants.KEY_CITY));
                city = fromRecycleViewer.getString(Constants.KEY_CITY);
                sb.append(", ");
                sb.append(fromRecycleViewer.getString(Constants.KEY_STATE).toUpperCase());
                state = fromRecycleViewer.getString(Constants.KEY_STATE);
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_PHONE))) {
                displayPhone.setText(fromRecycleViewer.getString(Constants.KEY_PHONE));
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_WEBSITE))) {
                displayWebsite.setText(fromRecycleViewer.getString(Constants.KEY_WEBSITE));
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_DETAILS))) {
                displayDiscount.setText(fromRecycleViewer.getString(Constants.KEY_DETAILS));
            }
            if (!TextUtils.isEmpty(sb.toString())) {
                displayAddress.setText(sb.toString());
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_UID))) {
                this.uId = fromRecycleViewer.getString(Constants.KEY_UID);
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_DISPLAY_NAME)) && !TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_LAST_UPDATE))) {
                //set lastUpdate with info from last person to post a comment and display
                StringBuilder lastComment = new StringBuilder();
                lastComment.append("Last Updated By: ");
                lastComment.append(fromRecycleViewer.getString(Constants.KEY_DISPLAY_NAME));
                lastComment.append(" on ");
                lastComment.append(fromRecycleViewer.getString(Constants.KEY_LAST_UPDATE));
                lastUpdate.setVisibility(View.VISIBLE);
                lastUpdate.setText(lastComment.toString());
            }
            if (!TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_CITY))
                    && !TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_STATE))
                    && !TextUtils.isEmpty(fromRecycleViewer.getString(Constants.KEY_UID))) {
                discVm.getDiscountComments(fromRecycleViewer.getString(Constants.KEY_STATE), fromRecycleViewer.getString(Constants.KEY_CITY),
                        fromRecycleViewer.getString(Constants.KEY_UID),
                        (ArrayList<Comment> comments) -> {
                            String numComments = "Comments(0)";
                            if (comments.size() > 0) {
                                //display number of comments if not 0
                                numComments = "Comments(" + comments.size() + ")";
                                StringBuilder commentsBuilder = new StringBuilder();
                                for (Comment c : comments) {
                                    commentsBuilder.append("\"");
                                    commentsBuilder.append(c.getCommentDetail());
                                    commentsBuilder.append("\"");
                                    commentsBuilder.append("\n");
                                    commentsBuilder.append("- ");
                                    commentsBuilder.append(c.getCommentPoster());
                                    commentsBuilder.append(" on ");
                                    commentsBuilder.append(c.getCommentDate());
                                    commentsBuilder.append("\n\n");
                                }
                                displayComments.setText(commentsBuilder.toString());
                            }
                            //change color of parenthesis and number of comments
                            if(getContext()!=null) {
                                Spannable parenthesis = new SpannableString(numComments);
                                parenthesis.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary)),
                                        numComments.indexOf("("), numComments.indexOf("(")+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                parenthesis.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary)),
                                        numComments.indexOf(")"), numComments.indexOf(")")+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                parenthesis.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.orangePalette)),
                                        numComments.indexOf("(") + 1, numComments.indexOf(")"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                lblComments.setText(parenthesis, TextView.BufferType.SPANNABLE);
                            }
                            else{
                                lblComments.setText(numComments);
                            }
                        }
                );

            }
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.clear();
        }
        inflater.inflate(R.menu.discount_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editDiscount:
                AddDiscount_Fragment editFragment = new AddDiscount_Fragment();
                fromRecycleViewer.putString(Constants.KEY_EDIT, "true");
                editFragment.setArguments(fromRecycleViewer);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.containerDiscounts, editFragment, "EditFragment");
                transaction.addToBackStack("EditFragment");
                transaction.commit();
                return super.onOptionsItemSelected(item);
            case R.id.deleteDiscount:
                if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(uId)) {
                    discVm.deleteDiscount(state, city, uId, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (getActivity() != null) {
                                    Toast.makeText(getActivity(), "Discount Deleted", Toast.LENGTH_SHORT).show();
                                    if (getActivity() != null) {
                                        //pop stack, update location, and rebuild recycleviewer
                                        getActivity().onBackPressed();
                                    }
                                }
                            } else {
                                if (getActivity() != null) {
                                    //Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    return super.onOptionsItemSelected(item);
                } else {
                    if(getActivity()!=null) {
                       // Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                    }
                    return super.onOptionsItemSelected(item);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DiscountsViewModel discVm = new DiscountsViewModel();
        discVm.clear();
    }

}
