package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.TypedValueCompat;

import com.example.navigatorappandroid.model.Comment;
import com.example.navigatorappandroid.retrofit.response.CommentsListResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsListActivity extends BaseActivity {

    private LinearLayout commentsLayout;
    private Spinner sortTypesSpinner;
    private List<Comment> commentList;
    private Map<Comment, TreeSet<Comment>> commentsMap;
    private List<Comment> initialCommentsList = new ArrayList<>();
    private List<Comment> repliesList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);
        sortTypesSpinner = findViewById(R.id.sort_spinner);
        ArrayList<String> sortTypes = new ArrayList<>();
        sortTypes.add(getResources().getString(R.string.all));
        sortTypes.add(getResources().getString(R.string.positive_only));
        sortTypes.add(getResources().getString(R.string.negative_only));
        ArrayAdapter<String> sortTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sortTypes);
        sortTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortTypesSpinner.setAdapter(sortTypeAdapter);
        commentsLayout = findViewById(R.id.comments_layout);
        generalApi.getCommentsListByUserId(arguments.getString("user_id")).enqueue(new Callback<CommentsListResponse>() {
            @Override
            public void onResponse(Call<CommentsListResponse> call, Response<CommentsListResponse> response) {
                RelativeLayout commentLayout = new RelativeLayout(commentsLayout.getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                commentLayout.setLayoutParams(params);
                commentList = response.body().getList().stream().filter(Comment::isInitialComment)
                        .collect(Collectors.toList());
                commentsMap = new TreeMap<>(getCommentsComparator());
                TreeSet<Comment> set = new TreeSet<>(getCommentsComparator());
                for (Comment initialComment : commentList) {
                    if (initialComment.getReplies().isEmpty()) {
                        commentsMap.put(initialComment, null);
                    } else {
                        set.addAll(initialComment.getReplies());
                        commentsMap.put(initialComment, set);
                        set.clear();
                    }
                }
                for (Map.Entry<Comment, TreeSet<Comment>> entrySet : commentsMap.entrySet()) {
                    ImageView voteIconView = new ImageView(commentLayout.getContext());
                    voteIconView.setId(View.generateViewId());
                    if (entrySet.getKey().getVote().getValue() == 1) {
                        voteIconView.setImageResource(R.drawable.like);
                    } else {
                        voteIconView.setImageResource(R.drawable.dislike);
                    }
                    RelativeLayout.LayoutParams voteIconParams = new RelativeLayout.LayoutParams(50, 50);
                    voteIconParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    voteIconParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                    voteIconView.setLayoutParams(voteIconParams);
                    commentLayout.addView(voteIconView);
                    ImageView userAvatarView = new ImageView(commentLayout.getContext());
                    userAvatarView.setId(View.generateViewId());
                    userAvatarView.setImageBitmap();
                    RelativeLayout.LayoutParams userAvatarParams = new RelativeLayout.LayoutParams(50, 50);
                    userAvatarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    userAvatarParams.addRule(RelativeLayout.END_OF, voteIconView.getId());
                    userAvatarParams.setMargins(10, 0, 10, 0);
                    userAvatarView.setLayoutParams(userAvatarParams);
                    commentLayout.addView(userAvatarView);
                    TextView usernameView = new TextView(commentLayout.getContext());
                    usernameView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams usernameParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, 50);
                    usernameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    usernameParams.addRule(RelativeLayout.END_OF, userAvatarView.getId());
                    usernameView.setLayoutParams(usernameParams);
                    usernameView.setTextColor(getResources().getColor(R.color.black));
                    usernameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    usernameView.setText(entrySet.getKey().getSender().getName());
                    commentLayout.addView(usernameView);
                    TextView commentView = new TextView(commentLayout.getContext());
                    commentView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams commentParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    commentParams.addRule(RelativeLayout.BELOW, userAvatarView.getId());
                    commentParams.setMargins(10,30, 10, 10);
                    commentView.setLayoutParams(commentParams);
                    commentView.setPadding(3, 5, 3, 5);
                    commentView.setBackgroundResource(R.drawable.comment_background);
                    commentView.setTextColor(getResources().getColor(R.color.black));
                    commentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    commentView.setText(entrySet.getKey().getContent());
                    commentLayout.addView(commentView);
                    TextView dateView = new TextView(commentLayout.getContext());
                    dateView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, 30);
                    dateParams.addRule(RelativeLayout.BELOW, commentView.getId());
                    dateView.setLayoutParams(dateParams);
                    dateView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    dateView.setTextColor(getResources().getColor(R.color.black));
                    dateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    dateView.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    commentLayout.addView(dateView);
                    if (entrySet.getValue() != null) {
                        Button showReplyButton = new Button();
                    }
                    commentsLayout.addView(commentLayout);
                }
            }
            @Override
            public void onFailure(Call<CommentsListResponse> call, Throwable t) {
                Toast.makeText(CommentsListActivity.this, "error: 'getCommentsListByUserId' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Comparator<Comment> getCommentsComparator() {
        return new Comparator<Comment>() {
            @Override
            public int compare(Comment comment, Comment t1) {
                if (comment.getDateTime().isAfter(t1.getDateTime())) {
                    return -1;
                } else if (comment.getDateTime().isBefore(t1.getDateTime())) {
                    return 1;
                }
                return 0;
            }
        };
    }

    public void onBack(View view) {
        Activity lastActivity = getLastActivity();
        Intent intent;
        if (lastActivity != null) {
            intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        removeActivityFromQueue();
        finish();
        startActivity(intent);
    }
}
