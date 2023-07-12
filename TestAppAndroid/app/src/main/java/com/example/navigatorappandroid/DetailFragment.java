package com.example.navigatorappandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    // обновление текстового поля
    public void setSelectedItem(String selectedItem) {
        TextView view = getView().findViewById(R.id.detailsText);
        view.setText(selectedItem);
    }
}
