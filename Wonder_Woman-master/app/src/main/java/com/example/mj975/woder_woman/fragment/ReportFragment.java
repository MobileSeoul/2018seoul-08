package com.example.mj975.woder_woman.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Event;
import com.example.mj975.woder_woman.data.Report;
import com.example.mj975.woder_woman.util.Constants;
import com.example.mj975.woder_woman.util.GPSUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ReportFragment extends Fragment {

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA}; //권한 설정 변수

    private Uri photoUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;

    private static final int MULTIPLE_PERMISSIONS = 101;

    private String address;

    private ImageView photoButton;
    private EditText addressText;
    private FirebaseFirestore db;

    private double longitude;
    private double latitude;
    private String uploadUrl;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        addressText = v.findViewById(R.id.text_address);
        EditText detailAddressText = v.findViewById(R.id.text_detail_address);
        EditText infoText = v.findViewById(R.id.text_info);

        Bundle bundle = getArguments();
        if (bundle != null) {
            latitude = bundle.getDouble("LAT", 37.56);
            longitude = bundle.getDouble("LNG", 126.97);
        }

        Button getAddressButton = v.findViewById(R.id.get_address);
        getAddressButton.setOnClickListener(view -> {
            getAddress(getActivity(), latitude, longitude);
        });

        photoButton = v.findViewById(R.id.report_image_view);

        photoButton.setOnClickListener(view -> {
            checkPermissions();
            setPopupMenu();
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Button reportButton = v.findViewById(R.id.report_button);

        if (user == null) {
            reportButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
            reportButton.setEnabled(false);
            reportButton.setText("로그인 해주세요");
        }

        db = FirebaseFirestore.getInstance();

        reportButton.setOnClickListener(view -> {
            String addr = addressText.getText().toString();
            String detailAddr = detailAddressText.getText().toString();
            String content = infoText.getText().toString();
            if (addr.length() > 0
                    && detailAddr.length() > 0
                    && content.length() > 0) {

                dialog = new ProgressDialog(getContext());
                dialog.setMessage("신고 진행 중 입니다.");
                dialog.show();

                Report report = new Report(addr, detailAddr, content, "", user.getEmail());
                showReportDialog(report);

            } else
                Snackbar.make(view, "내용을 입력해 주세요", Snackbar.LENGTH_SHORT).show();
        });

        return v;
    }

    private void uploadImages(Uri photoUri, Report report) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(Constants.STORAGE_URL).child("report").child(generateTempFilename());
        storageReference.putFile(photoUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                dialog.dismiss();
                Snackbar.make(getView(), "신고에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
            }
            return storageReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            uploadUrl = task.getResult().toString();
            report.setUri(uploadUrl);

            db.collection("Report").document()
                    .set(report)
                    .addOnSuccessListener(aVoid -> {
                        dialog.dismiss();
                        Snackbar.make(getView(), "신고에 성공하였습니다.", Snackbar.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                dialog.dismiss();
                Snackbar.make(getView(), "신고에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
            });
        });
    }

    private String generateTempFilename() {
        return UUID.randomUUID().toString();
    }

    public String getAddress(Context context, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 5);

                if (address != null && address.size() > 0) {
                    nowAddress = address.get(0).getAddressLine(0);
                    showAddressDialog(address);
                }
            }

        } catch (IOException e) {
            Toast.makeText(context, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
    }


    private void showAddressDialog(List<Address> ListItems) {
        CharSequence[] items = new String[ListItems.size()];

        for (int i = 0; i < ListItems.size(); i++)
            items[i] = ListItems.get(i).getAddressLine(0);

        List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("주소 목록");
        builder.setSingleChoiceItems(items, defaultItem,
                (dialog, which) -> {
                    SelectedItems.clear();
                    SelectedItems.add(which);
                });
        builder.setPositiveButton("선택",
                (dialog, which) -> {
                    if (!SelectedItems.isEmpty()) {
                        int index = (int) SelectedItems.get(0);
                        address = ListItems.get(index).getAddressLine(0);
                        addressText.setText(address);
                    }
                });
        builder.setNegativeButton("취소",
                (dialog, which) -> {

                });
        builder.show();
    }

    private void showReportDialog(Report report) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("신고 하기");
        builder.setMessage("이 내용을 바탕으로 신고 하시겠습니까?");
        builder.setPositiveButton("예",
                (dialog, which) -> {
                    if (photoUri != null) {
                        uploadImages(photoUri, report);
                    } else {
                        db.collection("Report").document()
                                .set(report)
                                .addOnSuccessListener(aVoid -> {
                                    dialog.dismiss();
                                    Snackbar.make(getView(), "신고에 성공하였습니다.", Snackbar.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> {
                            dialog.dismiss();
                            Snackbar.make(getView(), "신고에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                        });
                    }
                });
        builder.setNegativeButton("아니오",
                (dialog, which) -> {
                });
        builder.show();
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
        PopupMenu popup = new PopupMenu(getActivity(), photoButton);
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
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/wonder_woman_pic/"); //test라는 경로에 이미지를 저장하기 위함
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
                    photoButton.setImageURI(photoUri);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
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

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getActivity().grantUriPermission(list.get(1).activityInfo.packageName, photoUri,
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}