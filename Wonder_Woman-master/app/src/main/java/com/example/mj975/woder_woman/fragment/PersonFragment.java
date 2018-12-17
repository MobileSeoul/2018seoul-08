package com.example.mj975.woder_woman.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.activity.SignInActivity;
import com.example.mj975.woder_woman.data.User;
import com.example.mj975.woder_woman.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PersonFragment extends Fragment {

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA}; //권한 설정 변수

    private Uri photoUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;

    private static final int MULTIPLE_PERMISSIONS = 101;
    private static final int LOGIN = 8;

    private ImageView profilPhoto;
    private Button loginButton;
    private LinearLayout linearLayout;
    private String uploadUrl;

    private FirebaseUser user;
    private FirebaseFirestore db;

    public static int reportNum = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        db = FirebaseFirestore.getInstance();

        profilPhoto = v.findViewById(R.id.profile_image);
        user = FirebaseAuth.getInstance().getCurrentUser();

        TextView photoChangeButton = v.findViewById(R.id.profile_image_change_button);
        photoChangeButton.setOnClickListener(view -> {
            if (user == null) {
                Snackbar.make(view, "로그인 해주세요", Snackbar.LENGTH_SHORT).show();
                return;
            }
            checkPermissions();
            setPopupMenu();
        });

        loginButton = v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), SignInActivity.class);
            startActivityForResult(i, LOGIN);
        });
        linearLayout = v.findViewById(R.id.profile);

        TextView profileName = v.findViewById(R.id.profile_name);
        TextView profileEmail = v.findViewById(R.id.profile_email);
        TextView profileReport = v.findViewById(R.id.profile_repoet_num);

        if (user != null) {
            loginButton.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            db.collection("User").document(user.getEmail())
                    .get().addOnSuccessListener(documentSnapshot -> {
                User u = documentSnapshot.toObject(User.class);
                profileName.setText(":  " + u.getName());
                profileEmail.setText(":  " + u.getEmail());
                profileReport.setText(":  " + reportNum + " 번");
                Glide.with(profilPhoto)
                        .load(u.getProfileImage())
                        .apply(new RequestOptions()
                                .centerInside()
                        )
                        .into(profilPhoto);
            });
        } else {
            loginButton.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        TextView logOutButton = v.findViewById(R.id.log_out);
        logOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            loginButton.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        });

        TextView signOutButton = v.findViewById(R.id.sign_out);
        signOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            loginButton.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        });

        TextView inquiryButton = v.findViewById(R.id.inquiry_button);
        inquiryButton.setOnClickListener(view -> Snackbar.make(view, "업데이트 예정입니다.", Snackbar.LENGTH_SHORT).show());
        TextView openSourceButton = v.findViewById(R.id.open_source_button);
        openSourceButton.setOnClickListener(view -> Snackbar.make(view, "업데이트 예정입니다.", Snackbar.LENGTH_SHORT).show());

        TextView expedition = v.findViewById(R.id.expedition_tab);
        expedition.setOnClickListener(view -> Snackbar.make(view, "업데이트 예정입니다.", Snackbar.LENGTH_SHORT).show());

        TextView myReportShow = v.findViewById(R.id.total_report);
        myReportShow.setOnClickListener(view -> Snackbar.make(view, "업데이트 예정입니다.", Snackbar.LENGTH_SHORT).show());

        return v;
    }

    private void uploadImages(Uri photoUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(Constants.STORAGE_URL).child("profile").child(generateTempFilename());
        storageReference.putFile(photoUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                Snackbar.make(getView(), "프로필 변경에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
            }
            return storageReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            uploadUrl = task.getResult().toString();

            db.collection("User").document(user.getEmail())
                    .update("profileImage", uploadUrl)
                    .addOnSuccessListener(aVoid -> {
                        profilPhoto.setImageURI(photoUri);
                        Snackbar.make(getView(), "프로필 변경에 성공하였습니다.", Snackbar.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                Snackbar.make(getView(), "프로필 변경에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
            });
        });
    }

    private String generateTempFilename() {
        return UUID.randomUUID().toString();
    }

    private void checkPermissions() {
        List<String> permissionList = new ArrayList<>();

        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED)  //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(permission);
        }

        if (!permissionList.isEmpty())
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);

    }

    private void setPopupMenu() {
        PopupMenu popup = new PopupMenu(getActivity(), profilPhoto);
        popup.getMenuInflater()
                .inflate(R.menu.camera_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popup_take_picture)
                    takePhoto();
                else if (item.getItemId() == R.id.popup_photo_select)
                    goToAlbum();
                return true;
            }
        });
        popup.show();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.
                    getUriForFile(getActivity(),
                            "com.example.mj975.woder_woman.provider",
                            photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/test/"); //test라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_FROM_ALBUM) {
                if (data == null)
                    return;

                photoUri = data.getData();
                cropImage();
            } else if (requestCode == PICK_FROM_CAMERA) {
                System.out.println("pick from camera");
                cropImage();
                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{photoUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else if (requestCode == CROP_FROM_CAMERA) {
                try {
                    uploadImages(photoUri);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            } else if (requestCode == LOGIN) {
                if (user != null) {
                    loginButton.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    loginButton.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                }
            }
        }
    }


    public void cropImage() {
        System.out.println("crop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getActivity().grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getActivity().getApplicationContext()
                    .grantUriPermission(list.get(1)
                                    .activityInfo
                                    .packageName,
                            photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);


        int size = list.size();
        if (size == 0) {
            Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/test/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(getActivity(),
                    "com.example.mj975.woder_woman.provider", tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                getActivity().grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }
}
