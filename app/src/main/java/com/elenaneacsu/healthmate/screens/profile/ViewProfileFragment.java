package com.elenaneacsu.healthmate.screens.profile;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elenaneacsu.healthmate.R;
import com.elenaneacsu.healthmate.screens.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static com.elenaneacsu.healthmate.utils.ToastUtil.showToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment implements View.OnClickListener {

    private CircularImageView mCircularImageView;
    private TextView mTextViewInfo;
    private TextView mTextViewCurrentWeight;
    private TextView mTextViewGoalWeight;
    private TextView mTextViewHeight;
    private Button mButtonEdit;

    private int GALLERY = 1, CAMERA = 2;
    private static final String IMAGE_DIRECTORY = "/healthmate";
    private byte[] photo;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        getFromFirestore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCircularImageView = view.findViewById(R.id.circularImageView);
        mTextViewInfo = view.findViewById(R.id.textview_summaryinfo);
        mTextViewCurrentWeight = view.findViewById(R.id.textview_currentweight);
        mTextViewGoalWeight = view.findViewById(R.id.textview_goalweight);
        mTextViewHeight = view.findViewById(R.id.textview_height);
        mButtonEdit = view.findViewById(R.id.btn_edit);

        mCircularImageView.setOnClickListener(this);
        mButtonEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.circularImageView:
                requestMultiplePermissions();
                Log.d("tag", "onClick: clicked");
                break;
            case R.id.btn_edit:
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
        }
    }

    public void choosePhotoDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(com.elenaneacsu.healthmate.R.string.choose_photo_from);
        String[] dialogItems = {
                "GALLERY",
                "CAMERA"
        };
        dialog.setItems(dialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallery();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });
        dialog.show();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            return;
        }
        if(requestCode == GALLERY) {
            if(data != null) {
                Uri contentUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
                    String path = saveImage(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    photo = stream.toByteArray();
                    saveToFirebaseStorage();
                    showToast(getContext(), "Image saved!");
                    mCircularImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast(getContext(), "Failed");
                }
            }
        } else if(requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mCircularImageView.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            saveToFirebaseStorage();
            showToast(getContext(), "Image saved!");
        }
    }

    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo = bytes.toByteArray();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory()
                            + IMAGE_DIRECTORY);
        if(!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        File file = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis()+".jpg");
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(), new String[]{file.getPath()},
                            new String[]{"image/jpeg"}, null);
            fileOutputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            choosePhotoDialog();
                            showToast(getContext(), "All permissions are granted by user!");
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        showToast(getContext(), "Some Error! ");
                    }
                })
                .onSameThread()
                .check();
    }

    private void saveToFirebaseStorage() {
        final StorageReference imgStorageReference = mFirebaseStorage.getReference().child("USERS")
                .child(mFirebaseAuth.getCurrentUser().getUid()).child("profilePic");

        UploadTask uploadTask = imgStorageReference.putBytes(photo);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, String> photoPath = new HashMap<>();
                        photoPath.put("photo", uri.toString());
                        DocumentReference userReference = mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid());
                        userReference.set(photoPath, SetOptions.merge());
                    }
                });
            }
        });
    }

    private void getFromFirestore() {
        DocumentReference documentReference = mFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                setFields(user);
            }
        });
    }

    private void setFields(User user) {
        mTextViewInfo.setText(user.getName()+" | "+user.getAge() + " y.o. " + user.getGender() + " | " + user.getActivityLevel());
        mTextViewGoalWeight.setText(getString(com.elenaneacsu.healthmate.R.string.goal_weight)+user.getDesiredWeight()+" kg");
        mTextViewCurrentWeight.setText(getString(com.elenaneacsu.healthmate.R.string.current_weight)+user.getCurrentWeight()+" kg");
        mTextViewHeight.setText(getString(com.elenaneacsu.healthmate.R.string.height)+user.getHeight()+" cm");
        Picasso.get().load(user.getPhoto()).into(mCircularImageView);
    }
}
