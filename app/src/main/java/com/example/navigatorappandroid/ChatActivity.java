package com.example.navigatorappandroid;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.navigatorappandroid.model.ChatMessage;
import com.example.navigatorappandroid.retrofit.request.ChatRequest;
import com.example.navigatorappandroid.retrofit.request.DecisionRequest;
import com.example.navigatorappandroid.retrofit.response.AnswerToOfferResponse;
import com.example.navigatorappandroid.retrofit.response.ChatMessageResponse;
import com.google.android.gms.common.util.IOUtils;
import java.io.InputStream;
import java.util.TreeSet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends BaseActivity {

    private Uri selectedImageUri;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private EditText enterMessage;
    private ChatRequest chatRequest;
    private String userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        scrollView = findViewById(R.id.chat_scroll_view);
        // Wait until the layout is ready, then scroll to the bottom
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.fullScroll(View.FOCUS_DOWN);
                // Remove the listener to prevent multiple calls
                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        linearLayout = findViewById(R.id.chat_inside_layout);
        userId = String.valueOf(arguments.getLong("user_id"));
        userName = String.valueOf(arguments.getString("user_name"));
        chatRequest = new ChatRequest();
        if (userInfoResponse.getRole().equals("Employee")) {
            chatRequest.setEmployeeId(userInfoResponse.getId());
            chatRequest.setEmployerId(arguments.getLong("user_id"));
        } else {
            chatRequest.setEmployeeId(arguments.getLong("user_id"));
            chatRequest.setEmployerId(userInfoResponse.getId());
        }
        chatApi.findChatMessages(chatRequest).enqueue(new Callback<ChatMessageResponse>() {
            @Override
            public void onResponse(Call<ChatMessageResponse> call, Response<ChatMessageResponse> response) {
                TreeSet<ChatMessage> set = response.body().getMessages();
                for (ChatMessage message : set) {
                    if (message.equals("Image")) {
                        ImageView imageView = new ImageView(linearLayout.getContext());
                        float density = getResources().getDisplayMetrics().density;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                                ((int) (120 * density), (int) (120 * density));
                        imageView.setLayoutParams(layoutParams);
                        imageView.setBackgroundResource(R.drawable.gray_blue_rectangle);
                        byte[] decodedBytes = Base64.decode(message.getContent(), Base64.DEFAULT);
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray
                                (decodedBytes, 0 , decodedBytes.length));
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
                            textView.setTextColor(getResources().getColor(R.color.black));
                            if (message.getType().equals("Offer")) {
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showPopupMenu(view, message.getVacancy().getId());
                                    }
                                });
                                textView.setBackgroundResource(R.drawable.my_chat_offer_background);
                            } else {
                                textView.setBackgroundResource(R.drawable.my_chat_message_background);
                            }
                        } else {
                            layoutParams.setMargins(10, 20, 0, 0);
                            layoutParams.gravity = Gravity.LEFT;
                            textView.setTextColor(getResources().getColor(R.color.white));
                            if (message.getType().equals("Offer")) {
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showPopupMenu(view, message.getVacancy().getId());
                                    }
                                });
                                textView.setBackgroundResource(R.drawable.other_person_chat_offer_background);
                            } else {
                                textView.setBackgroundResource(R.drawable.other_person_chat_message_background);
                            }
                        }
                        textView.setLayoutParams(layoutParams);
                        if (message.equals("Offer")) {
                        }
                        linearLayout.addView(textView);
                    }
                }
            }
            @Override
            public void onFailure(Call<ChatMessageResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "error: 'findChatMessages' " +
                        "method is failure", Toast.LENGTH_SHORT).show();
            }
        });
        TextView recipientName = findViewById(R.id.recipient_name);
        recipientName.setText(userName);
        Button attachFileButton = findViewById(R.id.attach_file);
        attachFileButton.setOnClickListener(view -> openGallery());
        enterMessage = findViewById(R.id.enter_message);
        enterMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
                && !enterMessage.getText().toString().isEmpty()) {
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
                    chatRequest.setImage(false);
                    chatRequest.setContent(enterMessage.getText().toString());
                    chatApi.processMessage(userId, chatRequest).enqueue(new Callback<ChatMessageResponse>() {
                        @Override
                        public void onResponse(Call<ChatMessageResponse> call, Response<ChatMessageResponse> response) {
                            linearLayout.addView(textView);
                            chatRequest.setContent(null);
                        }
                        @Override
                        public void onFailure(Call<ChatMessageResponse> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "error: 'processMessage' " +
                                    "method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            }
        });
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, ChatSettingsActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("user_name", userName);
        finish();
        startActivity(intent);
    }

    public void onBack(View view) {
        Intent intent = new Intent(this, ChatListActivity.class);
        finish();
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
                layoutParams.setMargins(0, 20, 10, 0);
                layoutParams.gravity = Gravity.RIGHT;
                imageView.setLayoutParams(layoutParams);
                imageView.setBackgroundResource(R.drawable.gray_blue_rectangle);
                imageView.setImageURI(selectedImageUri);
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
                if (chatRequest.getContent() != null) {
                    chatApi.processMessage(userId, chatRequest).enqueue(new Callback<ChatMessageResponse>() {
                        @Override
                        public void onResponse(Call<ChatMessageResponse> call, Response<ChatMessageResponse> response) {
                            linearLayout.addView(imageView);
                            chatRequest.setContent(null);
                        }
                        @Override
                        public void onFailure(Call<ChatMessageResponse> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "error: 'processMessage' " +
                                    "method is failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void showPopupMenu(View view, Long vacancyId) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.offer_decission_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DecisionRequest decision = new DecisionRequest();
                decision.setVacancyId(vacancyId.toString());
                decision.setRecipientId(userId);
                if (menuItem.getItemId() == R.id.accept) {
                    decision.setDecision("Accept");
                } else {
                    decision.setDecision("Decline");
                }
                chatApi.giveDecisionToOffer(decision).enqueue(new Callback<AnswerToOfferResponse>() {
                    @Override
                    public void onResponse(Call<AnswerToOfferResponse> call, Response<AnswerToOfferResponse> response) {
                        TextView textView = new TextView(linearLayout.getContext());
                        textView.setPadding(15, 15, 15, 15);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 20, 10, 0);
                        layoutParams.gravity = Gravity.RIGHT;
                        textView.setTextColor(getResources().getColor(R.color.black));
                        textView.setText(enterMessage.getText().toString());
                        textView.setLayoutParams(layoutParams);
                        if (response.body().isResult()) {
                            textView.setBackgroundResource(R.drawable.my_chat_offer_acceptance_background);
                        } else {
                            textView.setBackgroundResource(R.drawable.my_chat_offer_refusing_background);
                        }
                        linearLayout.addView(textView);
                    }
                    @Override
                    public void onFailure(Call<AnswerToOfferResponse> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "error: 'giveDecisionToOffer' " +
                                "method is failure", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });
        popupMenu.show();
    }
}
