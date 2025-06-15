package com.example.navigatorappandroid;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.navigatorappandroid.model.Comment;
import com.example.navigatorappandroid.retrofit.request.CommentRequest;
import com.example.navigatorappandroid.retrofit.response.CommentsListResponse;
import com.example.navigatorappandroid.retrofit.response.ResultErrorsResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
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
    private Button ownComment;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list);
        setCurrentActivity(this);
        if (userInfoResponse.getRole().equals("Employee")) {
            userId = arguments.getString("employee_id");
        } else {
            userId = arguments.getString("employer_id");
        }
        ownComment = findViewById(R.id.my_comment);
        commentsLayout = findViewById(R.id.comments_layout);
        sortTypesSpinner = findViewById(R.id.sort_spinner);
        ArrayList<String> sortTypes = new ArrayList<>();
        sortTypes.add(getResources().getString(R.string.all));
        sortTypes.add(getResources().getString(R.string.positive_first));
        sortTypes.add(getResources().getString(R.string.negative_first));
        ArrayAdapter<String> sortTypeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sortTypes);
        sortTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortTypesSpinner.setAdapter(sortTypeAdapter);
        sortTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                generalApi.getCommentsListByUserId(Long.parseLong(userId), (byte) i).enqueue(new Callback<CommentsListResponse>() {
                    @Override
                    public void onResponse(Call<CommentsListResponse> call, Response<CommentsListResponse> response) {
                        commentList = response.body().getList().stream().filter(Comment::isInitialComment)
                                .collect(Collectors.toList());
                        commentsMap = new TreeMap<>(getCommentsComparator((byte) i));
                        TreeSet<Comment> set = new TreeSet<>(getCommentsComparator(null));
                        for (Comment initialComment : commentList) {
                            if (initialComment.getReplies().isEmpty()) {
                                commentsMap.put(initialComment, null);
                            } else {
                                set.addAll(initialComment.getReplies());
                                commentsMap.put(initialComment, set);
                                set.clear();
                            }
                        }
                        if (!commentsMap.isEmpty()) {
                            showComments(commentsMap, null);
                        }
                    }
                    @Override
                    public void onFailure(Call<CommentsListResponse> call, Throwable t) {
                        Toast.makeText(CommentsListActivity.this, "error: 'getCommentsListByUserId' " +
                                "method is failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        generalApi.getCommentsListByUserId(Long.parseLong(userId), null).enqueue(new Callback<CommentsListResponse>() {
            @Override
            public void onResponse(Call<CommentsListResponse> call, Response<CommentsListResponse> response) {
                commentList = response.body().getList().stream().filter(Comment::isInitialComment)
                        .collect(Collectors.toList());
                commentsMap = new TreeMap<>(getCommentsComparator(null));
                TreeSet<Comment> set = new TreeSet<>(getCommentsComparator(null));
                for (Comment initialComment : commentList) {
                    if (initialComment.getReplies().isEmpty()) {
                        commentsMap.put(initialComment, null);
                    } else {
                        set.addAll(initialComment.getReplies());
                        commentsMap.put(initialComment, set);
                        set.clear();
                    }
                }
                if (!commentsMap.isEmpty()) {
                    showComments(commentsMap, null);
                }
            }
            @Override
            public void onFailure(Call<CommentsListResponse> call, Throwable t) {
                Toast.makeText(CommentsListActivity.this, "error: 'getCommentsListByUserId' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showComments(Map<Comment, TreeSet<Comment>> commentsMap, Long showRepliesId) {
        commentsLayout.removeAllViews();
        for (Map.Entry<Comment, TreeSet<Comment>> entrySet : commentsMap.entrySet()) {
            List<RelativeLayout> viewList = new ArrayList<>();
            RelativeLayout commentLayout = new RelativeLayout(commentsLayout.getContext());
            commentLayout.setContentDescription(entrySet.getKey().getId().toString());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0 , 20 , 0 , 0);
            commentLayout.setLayoutParams(params);
            TextView ratingNumberView = new TextView(commentLayout.getContext());
            ratingNumberView.setId(View.generateViewId());
            StringBuilder sb = new StringBuilder();
            sb.append(entrySet.getKey().getAverageVote());
            sb.append("/10");
            ratingNumberView.setText(sb);
            ratingNumberView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            ratingNumberView.setTextColor(getResources().getColor(R.color.dark_yellow));
            RelativeLayout.LayoutParams ratingNumberParams = new RelativeLayout.LayoutParams(110, 50);
            ratingNumberParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            ratingNumberParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            ratingNumberView.setLayoutParams(ratingNumberParams);
            commentLayout.addView(ratingNumberView);
            ImageView userAvatarView = new ImageView(commentLayout.getContext());
            userAvatarView.setId(View.generateViewId());
            if (entrySet.getKey().getSender().getAvatar() != null) {
                byte[] decodedBytes = Base64.decode(entrySet.getKey().getSender().getAvatar(), Base64.DEFAULT);
                userAvatarView.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0 , decodedBytes.length));
            } else {
                userAvatarView.setImageDrawable(getResources().getDrawable(R.drawable.default_user_avatar));
            }
            RelativeLayout.LayoutParams userAvatarParams = new RelativeLayout.LayoutParams(50, 50);
            userAvatarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            userAvatarParams.addRule(RelativeLayout.END_OF, ratingNumberView.getId());
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
            viewList.add(commentLayout);
            if (showRepliesId != null && entrySet.getValue() != null &&
                    Long.parseLong(commentLayout.getContentDescription().toString()) == showRepliesId ) {
                Comment earliestReply = entrySet.getValue().stream().max(getCommentsComparator(null)).get();
                for (Comment reply : entrySet.getValue()) {
                    RelativeLayout replyLayout = new RelativeLayout(commentsLayout.getContext());
                    replyLayout.setLayoutParams(params);
                    ImageView replyUserAvatarView = new ImageView(commentLayout.getContext());
                    replyUserAvatarView.setId(View.generateViewId());
                    if (reply.getSender().getAvatar() != null) {
                        byte[] decodedBytes = Base64.decode(entrySet.getKey().getSender().getAvatar(), Base64.DEFAULT);
                        replyUserAvatarView.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0 , decodedBytes.length));
                    } else {
                        replyUserAvatarView.setImageDrawable(getResources().getDrawable(R.drawable.default_user_avatar));
                    }
                    RelativeLayout.LayoutParams replyUserAvatarParams = new RelativeLayout.LayoutParams(50, 50);
                    replyUserAvatarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    replyUserAvatarParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                    replyUserAvatarParams.setMargins(10, 0, 10, 0);
                    replyUserAvatarView.setLayoutParams(replyUserAvatarParams);
                    replyLayout.addView(replyUserAvatarView);
                    TextView replyUsernameView = new TextView(commentLayout.getContext());
                    replyUsernameView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams replyUsernameParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, 50);
                    replyUsernameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    replyUsernameParams.addRule(RelativeLayout.END_OF, userAvatarView.getId());
                    replyUsernameView.setLayoutParams(usernameParams);
                    replyUsernameView.setTextColor(getResources().getColor(R.color.black));
                    replyUsernameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    replyUsernameView.setText(reply.getSender().getName());
                    replyLayout.addView(replyUsernameView);
                    TextView replyCommentView = new TextView(commentLayout.getContext());
                    replyCommentView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams replyCommentParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    replyCommentParams.addRule(RelativeLayout.BELOW, userAvatarView.getId());
                    replyCommentParams.setMargins(10,30, 10, 10);
                    replyCommentView.setLayoutParams(replyCommentParams);
                    replyCommentView.setPadding(3, 5, 3, 5);
                    replyCommentView.setBackgroundResource(R.drawable.comment_background);
                    replyCommentView.setTextColor(getResources().getColor(R.color.black));
                    replyCommentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    replyCommentView.setText(reply.getContent());
                    replyLayout.addView(replyCommentView);
                    TextView replyDateView = new TextView(commentLayout.getContext());
                    replyDateView.setId(View.generateViewId());
                    RelativeLayout.LayoutParams replyDateParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, 30);
                    replyDateParams.addRule(RelativeLayout.BELOW, commentView.getId());
                    replyDateView.setLayoutParams(replyDateParams);
                    replyDateView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    replyDateView.setTextColor(getResources().getColor(R.color.black));
                    replyDateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    replyDateView.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    replyLayout.addView(dateView);
                    if (Objects.equals(earliestReply.getId(), reply.getId())) {
                        Button hideReplyButton = new Button(commentLayout.getContext());
                        hideReplyButton.setId(View.generateViewId());
                        RelativeLayout.LayoutParams hideRepliesParams = new RelativeLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, 30);
                        hideRepliesParams.addRule(RelativeLayout.BELOW, dateView.getId());
                        hideReplyButton.setLayoutParams(hideRepliesParams);
                        hideReplyButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        hideReplyButton.setBackgroundResource(R.drawable.notification_background);
                        hideReplyButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner_arrow, 0);
                        hideReplyButton.setTextColor(getResources().getColor(R.color.black));
                        hideReplyButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        hideReplyButton.setText(getResources().getString(R.string.hide_replies));
                        hideReplyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showComments(commentsMap, null);
                            }
                        });
                        replyLayout.addView(hideReplyButton);
                    }
                    viewList.add(replyLayout);
                }
            } else if (entrySet.getValue() != null) {
                Button showReplyButton = new Button(commentLayout.getContext());
                showReplyButton.setId(View.generateViewId());
                RelativeLayout.LayoutParams showRepliesParams = new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, 30);
                showRepliesParams.addRule(RelativeLayout.BELOW, dateView.getId());
                showReplyButton.setLayoutParams(showRepliesParams);
                showReplyButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                showReplyButton.setBackgroundResource(R.drawable.notification_background);
                showReplyButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner_arrow, 0);
                showReplyButton.setTextColor(getResources().getColor(R.color.black));
                showReplyButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                String showRepliesString = getResources().getString(R.string.show_replies)
                        + "(" +  entrySet.getValue().size() + ")";
                showReplyButton.setText(showRepliesString);
                showReplyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showComments(commentsMap, entrySet.getKey().getId());
                    }
                });
                commentLayout.addView(showReplyButton);
            } else {}
            for (RelativeLayout layout : viewList) {
                commentsLayout.addView(layout);
            }
        }
    }

    private Comparator<Comment> getCommentsComparator(Byte sortType) {
        if (sortType == null || sortType == 0) {
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
        } else if (sortType == 2) {
            return new Comparator<Comment>() {
                @Override
                public int compare(Comment comment, Comment t1) {
                    if (comment.getAverageVote() < t1.getAverageVote()) {
                        return -1;
                    } else if (comment.getAverageVote() > t1.getAverageVote()) {
                        return 1;
                    }
                    return 0;
                }
            };
        } else {
            return new Comparator<Comment>() {
                @Override
                public int compare(Comment comment, Comment t1) {
                    if (comment.getAverageVote() > t1.getAverageVote()) {
                        return -1;
                    } else if (comment.getAverageVote() < t1.getAverageVote()) {
                        return 1;
                    }
                    return 0;
                }
            };
        }
    }

    public void onLeaveComment(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtras(arguments);
        finish();
        startActivity(intent);
    }

    public void onBack(View view) {
        Activity lastActivity = getLastActivity();
        if (lastActivity != null) {
            Intent intent = new Intent(this, lastActivity.getClass());
            intent.putExtras(arguments);
            removeActivityFromQueue();
            finish();
            startActivity(intent);
        }
    }
}
