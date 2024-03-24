package com.example.navigatorappandroid;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.navigatorappandroid.model.ChatMessage;
import com.example.navigatorappandroid.model.User;
import com.example.navigatorappandroid.retrofit.ChatApi;
import com.example.navigatorappandroid.retrofit.GeneralApi;
import com.example.navigatorappandroid.retrofit.RetrofitService;
import com.example.navigatorappandroid.retrofit.SearchApi;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.response.ChatMessageResponse;
import com.example.navigatorappandroid.retrofit.response.UserInfoResponse;
import com.google.android.gms.common.util.IOUtils;
import java.io.InputStream;
import java.util.TreeSet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri selectedImageUri;
    private RetrofitService retrofitService;
    private ChatApi chatApi;
    private GeneralApi generalApi;
    private SearchApi searchApi;
    private UserInfoResponse userInfoResponse;
    private RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_chat, null);
    private ScrollView scrollView = relativeLayout.findViewById(R.id.chat_scroll_view);
    private LinearLayout linearLayout;
    private EditText enterMessage;
    private ChatRequest chatRequest;
    private User sender;
    private User recipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Wait until the layout is ready, then scroll to the bottom
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.fullScroll(View.FOCUS_DOWN);
                // Remove the listener to prevent multiple calls
                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        linearLayout = scrollView.findViewById(R.id.chat_inside_layout);
        retrofitService = new RetrofitService();
        chatApi = retrofitService.getRetrofit().create(ChatApi.class);
        generalApi = retrofitService.getRetrofit().create(GeneralApi.class);
        generalApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                userInfoResponse = response.body();
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        Bundle arguments = getIntent().getExtras();
        chatRequest = new ChatRequest();
        chatRequest.setSenderId(userInfoResponse.getId());
        chatRequest.setRecipientId(arguments.getLong("id"));
        chatApi.findChatMessages(chatRequest).enqueue(new Callback<ChatMessageResponse>() {
            @Override
            public void onResponse(Call<ChatMessageResponse> call, Response<ChatMessageResponse> response) {
                TreeSet<ChatMessage> set = response.body().getMessages();
                for (ChatMessage message : set) {
                    if (message.isImage()) {
                        ImageView imageView = new ImageView(linearLayout.getContext());
                        float density = getResources().getDisplayMetrics().density;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                                ((int) (120 * density), (int) (120 * density));
                        imageView.setLayoutParams(layoutParams);
                        imageView.setBackgroundResource(R.drawable.gray_blue_rectangle);
                        imageView.setImageURI();
                        if (message.getSender().getId() == userInfoResponse.getId()) {
                            layoutParams.setMargins(0, 20, 10, 0);
                            layoutParams.gravity = Gravity.RIGHT;
                        } else {
                            layoutParams.setMargins(10, 20, 0, 0);
                            layoutParams.gravity = Gravity.LEFT;
                        }
                        imageView.setLayoutParams(layoutParams);
                        linearLayout.addView(imageView);
                    } else {
                        TextView textView = new TextView(linearLayout.getContext());
                        textView.setText(message.getContent());
                        textView.setPadding(15, 15, 15, 15);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        if (message.getSender().getId() == userInfoResponse.getId()) {
                            layoutParams.setMargins(0, 20, 10, 0);
                            layoutParams.gravity = Gravity.RIGHT;
                            textView.setBackgroundResource(R.drawable.my_chat_message_background);
                            textView.setTextColor(getResources().getColor(R.color.black));
                        } else {
                            layoutParams.setMargins(10, 20, 0, 0);
                            layoutParams.gravity = Gravity.LEFT;
                            textView.setBackgroundResource(R.drawable.other_person_chat_message_background);
                            textView.setTextColor(getResources().getColor(R.color.white));
                        }
                        textView.setLayoutParams(layoutParams);
                        linearLayout.addView(textView);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatMessageResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
        TextView recipientName = relativeLayout.findViewById(R.id.recipient_name);
        recipientName.setText(arguments.getString("name"));
        Button attachFileButton = relativeLayout.findViewById(R.id.attach_file);
        attachFileButton.setOnClickListener(view -> openGallery());
        setContentView(R.layout.activity_chat);
        enterMessage = relativeLayout.findViewById(R.id.enter_message);
        enterMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
                && !enterMessage.getText().toString().equals("")) {
                    TextView textView = new TextView(linearLayout.getContext());
                    textView.setPadding(15, 15, 15, 15);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 20, 10, 0);
                    layoutParams.gravity = Gravity.RIGHT;
                    textView.setBackgroundResource(R.drawable.my_chat_message_background);
                    textView.setTextColor(getResources().getColor(R.color.black));
                    textView.setText(enterMessage.getText().toString());
                    textView.setLayoutParams(layoutParams);
                    linearLayout.addView(textView);
                    chatRequest.setImage(false);
                    chatApi.processMessage(chatRequest).enqueue(new Callback<ChatMessageResponse>() {
                        @Override
                        public void onResponse(Call<ChatMessageResponse> call, Response<ChatMessageResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<ChatMessageResponse> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            }
        });
    }

    public void onBack(View view) {
        Bundle arguments = getIntent().getExtras();
        Intent intent;
        if (arguments.getString("page_info_role").equals("employee")) {
            intent = new Intent(ChatActivity.this, EmployeeExtendedInfoActivity.class);
        } else {
            intent = new Intent(ChatActivity.this, EmployerExtendedInfoActivity.class);
        }
        intent.putExtra("id", arguments.getLong("id"));
        intent.putExtra("name", arguments.getString("name"));
        intent.putExtra("page_info_role", arguments.getString("page_info_role"));
        intent.putExtra("rating", arguments.getString("rating"));
        intent.putExtra("avatar", arguments.getString("avatar"));
        intent.putExtra("status", arguments.getString("status"));
        intent.putExtra("languages", arguments.getString("languages"));
        intent.putExtra("professions", arguments.getString("professions"));
        intent.putExtra("additional_info", arguments.getString("additional_info"));
        intent.putExtra("email", arguments.getString("email"));
        intent.putExtra("phone", arguments.getString("phone"));
        intent.putExtra("social_networks_links", arguments.getString("social_networks_links"));
        startActivity(intent);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                ImageView imageView = new ImageView(linearLayout.getContext());
                float density = getResources().getDisplayMetrics().density;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                        ((int) (120 * density), (int) (120 * density));
                imageView.setLayoutParams(layoutParams);
                imageView.setBackgroundResource(R.drawable.gray_blue_rectangle);
                imageView.setImageURI(selectedImageUri);
                layoutParams.setMargins(0, 20, 10, 0);
                layoutParams.gravity = Gravity.RIGHT;
                imageView.setLayoutParams(layoutParams);
                linearLayout.addView(imageView);
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
                    chatRequest.setContent(base64Image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                chatRequest.setImage(true);
                chatApi.processMessage(chatRequest).enqueue(new Callback<ChatMessageResponse>() {
                    @Override
                    public void onResponse(Call<ChatMessageResponse> call, Response<ChatMessageResponse> response) {
                    }

                    @Override
                    public void onFailure(Call<ChatMessageResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
