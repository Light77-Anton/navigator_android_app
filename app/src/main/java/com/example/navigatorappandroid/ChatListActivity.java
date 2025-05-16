package com.example.navigatorappandroid;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.example.navigatorappandroid.dto.KeyValuePairDTO;
import com.example.navigatorappandroid.model.ChatMessage;
import com.example.navigatorappandroid.model.EmployeeData;
import com.example.navigatorappandroid.model.EmployerRequests;
import com.example.navigatorappandroid.model.User;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ChatListActivity extends BaseActivity {

    private LinearLayout insideLayout;
    private TreeMap<Integer, KeyValuePairDTO> map;
    private Button allChatsButton;
    private Button favoritesButton;
    private Button contactedPersonsButton;
    private Button blackListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setCurrentActivity(this);
        insideLayout = findViewById(R.id.chat_inside_layout);
        map = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        allChatsButton = findViewById(R.id.all_chats);
        favoritesButton = findViewById(R.id.favorites);
        contactedPersonsButton = findViewById(R.id.contacted_persons);
        blackListButton = findViewById(R.id.black_list);
    }

    private void changeButtonsColors(Button clickedButton) {
        if (clickedButton == allChatsButton) {
            allChatsButton.setTextColor(getResources().getColor(R.color.red));
            favoritesButton.setTextColor(getResources().getColor(R.color.white));
            contactedPersonsButton.setTextColor(getResources().getColor(R.color.white));
            blackListButton.setTextColor(getResources().getColor(R.color.white));
        } else if (clickedButton == favoritesButton) {
            favoritesButton.setTextColor(getResources().getColor(R.color.red));
            allChatsButton.setTextColor(getResources().getColor(R.color.white));
            contactedPersonsButton.setTextColor(getResources().getColor(R.color.white));
            blackListButton.setTextColor(getResources().getColor(R.color.white));
        } else if (clickedButton == contactedPersonsButton) {
            contactedPersonsButton.setTextColor(getResources().getColor(R.color.red));
            allChatsButton.setTextColor(getResources().getColor(R.color.white));
            favoritesButton.setTextColor(getResources().getColor(R.color.white));
            blackListButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            blackListButton.setTextColor(getResources().getColor(R.color.red));
            allChatsButton.setTextColor(getResources().getColor(R.color.white));
            favoritesButton.setTextColor(getResources().getColor(R.color.white));
            contactedPersonsButton.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void findAllChatsWithEmployers(List<EmployerRequests> employerList) {
        List<ChatMessage> receivedMessages = userInfoResponse.getReceivedMessages();
        for (EmployerRequests employerRequests : employerList) {
            int unreadMessagesCount = 0;
            for (ChatMessage chatMessage : receivedMessages) {
                if (chatMessage.getSender().getId().equals(employerRequests.getId())) {
                    if (chatMessage.getStatus().equals("Sent")) {
                        unreadMessagesCount++;
                    }
                }
            }
            KeyValuePairDTO idNamePair = new KeyValuePairDTO
                    (employerRequests.getId(), "   " + employerRequests.getEmployer().getName());
            map.put(unreadMessagesCount, idNamePair);
        }
        addChats(map);
    }

    private void findAllChatsWithEmployees(List<EmployeeData> employeeList) {
        List<ChatMessage> receivedMessages = userInfoResponse.getReceivedMessages();
        for (EmployeeData employeeData : employeeList) {
            int unreadMessagesCount = 0;
            for (ChatMessage chatMessage : receivedMessages) {
                if (chatMessage.getSender().getId().equals(employeeData.getId())) {
                    if (chatMessage.getStatus().equals("Sent")) {
                        unreadMessagesCount++;
                    }
                }
            }
            KeyValuePairDTO idNamePair = new KeyValuePairDTO
                    (employeeData.getId(), "   " + employeeData.getEmployee().getName());
            map.put(unreadMessagesCount, idNamePair);
        }
        addChats(map);
    }

    private void addChats(TreeMap<Integer, KeyValuePairDTO> map) {
        for (Map.Entry<Integer, KeyValuePairDTO> entry : map.entrySet()) {
            Button button = new Button(insideLayout.getContext());
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buttonParams.setMargins(0, 30, 0, 0);
            button.setLayoutParams(buttonParams);
            button.setPadding(5, 5, 5, 5);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.gray_blue_rectangle));
            button.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, R.drawable.message_sign),
                    null, null, null);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            if (entry.getKey().equals(0)) {
                button.setTextColor(getResources().getColor(R.color.black));
            } else {
                button.setTextColor(getResources().getColor(R.color.red));
            }
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            String text = entry.getKey() + entry.getValue().getO2().toString();
            button.setText(text);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addActivityToQueue(getCurrentActivity());
                    Intent intent = new Intent(getCurrentActivity(), ChatActivity.class);
                    intent.putExtra("user_id", (long) entry.getValue().getO1());
                    intent.putExtra("user_name", (String) entry.getValue().getO2());
                    finish();
                    startActivity(intent);
                }
            });
            insideLayout.addView(button);
        }
        map.clear();
    }

    public void onAllClick(View view) {
        if (userInfoResponse.getRole().equals("Employee")) {
            List<EmployerRequests> contactedEmployers =
                    userInfoResponse.getEmployeeData().getContactedEmployers();
            contactedEmployers.removeAll(userInfoResponse.getBlackList().stream()
                    .map(User::getEmployerRequests).collect(Collectors.toList()));
            findAllChatsWithEmployers(contactedEmployers);
        } else if (userInfoResponse.getRole().equals("Employer")) {
            List<EmployeeData> contactedEmployees =
                    userInfoResponse.getEmployerRequests().getContactedEmployees();
            contactedEmployees.removeAll(userInfoResponse.getBlackList().stream()
                    .map(User::getEmployeeData).collect(Collectors.toList()));
            findAllChatsWithEmployees(contactedEmployees);
        } else {}
        changeButtonsColors((Button) view);
    }

    public void onSavedPersonsClick(View view) {
        if (userInfoResponse.getRole().equals("Employee")) {
            List<EmployerRequests> favoriteEmployers = userInfoResponse.getFavorites().stream()
                            .map(User::getEmployerRequests).collect(Collectors.toList());
            findAllChatsWithEmployers(favoriteEmployers);
        } else if (userInfoResponse.getRole().equals("Employer")) {
            List<EmployeeData> favoriteEmployees = userInfoResponse.getFavorites().stream()
                    .map(User::getEmployeeData).collect(Collectors.toList());
            findAllChatsWithEmployees(favoriteEmployees);
        } else {}
        changeButtonsColors((Button) view);
    }

    public void onContactedPersonsClick(View view) {
        if (userInfoResponse.getRole().equals("Employee")) {
            List<EmployerRequests> contactedEmployers =
                    userInfoResponse.getEmployeeData().getContactedEmployers();
            contactedEmployers.removeAll(userInfoResponse.getBlackList().stream()
                    .map(User::getEmployerRequests).collect(Collectors.toList()));
            contactedEmployers.removeAll(userInfoResponse.getFavorites().stream()
                    .map(User::getEmployerRequests).collect(Collectors.toList()));
            findAllChatsWithEmployers(contactedEmployers);
        } else if (userInfoResponse.getRole().equals("Employer")) {
            List<EmployeeData> contactedEmployees =
                    userInfoResponse.getEmployerRequests().getContactedEmployees();
            contactedEmployees.removeAll(userInfoResponse.getBlackList().stream()
                    .map(User::getEmployeeData).collect(Collectors.toList()));
            contactedEmployees.removeAll(userInfoResponse.getFavorites().stream()
                    .map(User::getEmployeeData).collect(Collectors.toList()));
            findAllChatsWithEmployees(contactedEmployees);
        } else {}
        changeButtonsColors((Button) view);
    }

    public void onBlackListClick(View view) {
        if (userInfoResponse.getRole().equals("Employee")) {
            List<EmployerRequests> bannedEmployers = userInfoResponse.getBlackList().stream()
                    .map(User::getEmployerRequests).collect(Collectors.toList());
            findAllChatsWithEmployers(bannedEmployers);
        } else if (userInfoResponse.getRole().equals("Employer")) {
            List<EmployeeData> bannedEmployees = userInfoResponse.getBlackList().stream()
                    .map(User::getEmployeeData).collect(Collectors.toList());
            findAllChatsWithEmployees(bannedEmployees);
        } else {}
        changeButtonsColors((Button) view);
    }
}