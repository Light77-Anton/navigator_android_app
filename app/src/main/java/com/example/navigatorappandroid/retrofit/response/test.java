package com.example.navigatorappandroid.retrofit.response;

public class YourActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView avatarImageView;
    private Button uploadButton;
    private Button setAvatarButton;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_activity_layout);

        avatarImageView = findViewById(R.id.avatar);
        uploadButton = findViewById(R.id.avatar_upload);
        setAvatarButton = findViewById(R.id.set_avatar);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        setAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    // Call your backend API to set the avatar
                    uploadAvatar(selectedImageUri);
                } else {
                    // Handle case where no image is selected
                    Toast.makeText(YourActivity.this, "Select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // User has successfully picked an image
            selectedImageUri = data.getData();

            // Display the selected image in the ImageView
            if (selectedImageUri != null) {
                avatarImageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void uploadAvatar(Uri imageUri) {
        // Implement your Retrofit call to upload the image to the backend
        // Use a library like Retrofit to simplify the API call
        // Example code assumes you have a Retrofit service interface named ApiService

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Get the file path from the image URI
        String filePath = getRealPathFromURI(imageUri);

        if (filePath != null) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

            Call<AvatarResponse> call = apiService.profileAvatar(body);
            call.enqueue(new Callback<AvatarResponse>() {
                @Override
                public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                    if (response.isSuccessful()) {
                        // Handle successful response
                        Toast.makeText(YourActivity.this, "Avatar set successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error response
                        Toast.makeText(YourActivity.this, "Failed to set avatar", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AvatarResponse> call, Throwable t) {
                    // Handle network failure
                    Toast.makeText(YourActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Helper method to get the file path from the content URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}
