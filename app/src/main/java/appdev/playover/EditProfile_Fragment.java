package appdev.playover;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import appdev.playover.R;

import appdev.playover.models.Person;
import appdev.playover.viewmodels.AuthUserViewModel;
import appdev.playover.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.ui.phone.SubmitConfirmationCodeFragment.TAG;

public class EditProfile_Fragment extends Fragment {


    private Spinner month, day, year, mRelationship;
    private TextView email, editImage, birthdayText;
    private EditText editFirstName, editLastName, editGroup, editPosition, editInterest;
    private Button submit;
    private ImageView image, pickedImage;
    private Uri selectedImage;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private AuthUserViewModel authVm;
    private UserViewModel userVm;
    private FirebaseUser currentFirebaseUser;
    private String imageUrl;
    private byte[] mUploadBytes;
    private Uri imageUri;
    private String profileImage;
    private Bitmap bitmap;
    private boolean dnd;
    private List<String> messageThreads;
    private String hotelCheckedInto;

    public EditProfile_Fragment() {
        //Required empty constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        image = rootView.findViewById(R.id.image);
        pickedImage = rootView.findViewById(R.id.pickedImage);
        email = rootView.findViewById(R.id.email);
        editImage = rootView.findViewById(R.id.editImageText);
        editFirstName = rootView.findViewById(R.id.editFirstName);
        editLastName = rootView.findViewById(R.id.editLastName);
        birthdayText = rootView.findViewById(R.id.birthdayText);
        day = rootView.findViewById(R.id.day);
        month = rootView.findViewById(R.id.month);
        editGroup = rootView.findViewById(R.id.editGroup);
        editPosition = rootView.findViewById(R.id.editPosition);
        editInterest = rootView.findViewById(R.id.editInterests);
        submit = rootView.findViewById(R.id.submitBtn);
        mRelationship = rootView.findViewById(R.id.relationship);

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        authVm = new AuthUserViewModel();
        userVm = new UserViewModel();

        //getting the year array populated by having all years since 1900 listed
        ArrayList<Integer> years = new ArrayList<>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayAdapter yearsAdapter;

        for (int i = 1900; i <= thisYear; i++) {
            years.add(i);
        }

        Collections.reverse(years);

        //adapting the arrayList years into the year spinner
        yearsAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item, years);
        year = rootView.findViewById(R.id.year);
        year.setAdapter(yearsAdapter);

        ArrayList<String> months = new ArrayList<>();
        ArrayAdapter monthsAdapter;

        spinnerIntToString(months, Constants.NUM_MONTHS);

        monthsAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, months);
        month.setAdapter(monthsAdapter);

        ArrayList<String> days = new ArrayList<>();
        ArrayAdapter daysAdapter;

        spinnerIntToString(days, Constants.NUM_DAYS);

        daysAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, days);
        day.setAdapter(daysAdapter);

        editImage.setOnClickListener(v -> {
            changeImageOnClick();
        });
        image.setOnClickListener(v -> {
            changeImageOnClick();
        });
        pickedImage.setOnClickListener(v -> {
            changeImageOnClick();
        });

        if (getArguments() != null) {
            String fName = getArguments().getString(Constants.KEY_FNAME);
            String lName = getArguments().getString(Constants.KEY_LNAME);
            String position = getArguments().getString(Constants.KEY_POSITION);
            String group = getArguments().getString(Constants.KEY_GROUP);
            String interests = getArguments().getString(Constants.KEY_INTERESTS);
            String relationship = getArguments().getString(Constants.KEY_RELATIONSHIP);
            String dob = getArguments().getString(Constants.KEY_DOB);
            profileImage = getArguments().getString(Constants.KEY_IMAGEURI);

            if (profileImage.isEmpty()) {
                image.setImageResource(R.drawable.profile_avatar_placeholder);
            } else {
                image.setVisibility(View.INVISIBLE);
                pickedImage.setVisibility(View.VISIBLE);
                Picasso mPicasso = Picasso.get();
                mPicasso.load(profileImage).resize(500, 500)
                        .centerCrop().into(pickedImage);
            }

            editFirstName.setText(fName);
            editLastName.setText(lName);
            editPosition.setText(position);
            editGroup.setText(group);
            editInterest.setText(interests);

            //sets relationship status spinner
            ArrayAdapter<CharSequence> rAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                    R.array.relationship, R.layout.spinner_item);
            rAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mRelationship.setAdapter(rAdapter);
            if (relationship != null) {
                int spinnerPosition = rAdapter.getPosition(relationship);
                mRelationship.setSelection(spinnerPosition);
            }

            if (dob != null && dob.length() == 10) {
                //sets spinner month
                String mCompare = dob.substring(0, 2);
                monthsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                int spinnerPositionMonth = monthsAdapter.getPosition(mCompare);
                month.setSelection(spinnerPositionMonth);

                //sets spinner day
                String dCompare = dob.substring(3, 5);
                daysAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                int spinnerPositionDay = daysAdapter.getPosition(dCompare);
                day.setSelection(spinnerPositionDay);

                //Sets spinner year
                String yCompare = dob.substring(6, 10);
                int intYCompare = Integer.parseInt(yCompare);
                int yearPos = thisYear - intYCompare;
                yearsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                year.setSelection(yearPos);
            }
        }

        submit.setOnClickListener(v -> {
            boolean flag = true;
            if (TextUtils.isEmpty(editFirstName.getText().toString().trim())) {
                editFirstName.setError("First name is required!");
                flag = false;
            }
            if (TextUtils.isEmpty(editLastName.getText().toString().trim())) {
                editLastName.setError("Last name is required!");
                flag = false;
            }
            if (TextUtils.isEmpty(editPosition.getText().toString().trim())) {
                editPosition.setError("Position is required!");
                flag = false;
            }
            String selectedDay = day.getSelectedItem().toString();
            String selectedYear = year.getSelectedItem().toString();
            String selectedMonth = month.getSelectedItem().toString();
            String dob = selectedMonth + "-" + selectedDay + "-" + selectedYear;

            if (imageUrl == null) {
                imageUrl = "no profile image";
            }

            if (authVm != null && userVm != null && authVm.getUser() != null) {
                userVm.getUser(authVm.getUser().getUid(),
                        (Person user) -> {
                            if (user != null) {
                                dnd = user.isDnd();
                                messageThreads = user.getMessageThreads();
                                hotelCheckedInto = user.getHotelCheckedInto();
                            }
                        });
            }

            if (authVm != null && userVm != null && flag) {
                Person updatedUser = new Person(editFirstName.getText().toString().trim(), editLastName.getText().toString().trim()
                        , editGroup.getText().toString().trim(), editPosition.getText().toString().trim(), dob, mRelationship.getSelectedItem().toString()
                        , authVm.getUser().getEmail(), authVm.getUser().getUid(), editInterest.getText().toString().trim(), profileImage, dnd, messageThreads, hotelCheckedInto, null);
                userVm.updateUser(authVm.getUser().getUid(), updatedUser, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            transaction = fragmentManager.beginTransaction();
                            Profile_Fragment newProfile = new Profile_Fragment();
                            transaction.replace(R.id.containerProfile, newProfile, "Profile");
                            transaction.addToBackStack("Profile");
                            transaction.commit();
                        } else {
                            if (task.getException() != null) {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("update error: ", task.getException().getMessage());
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), "Unable to connect to database!", Toast.LENGTH_SHORT).show();
            }
            uploadToStorage();
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST) {
                selectedImage = imageReturnedIntent.getData();
            }
        }

        if (selectedImage != null) {
            Picasso.get().load(selectedImage).resize(500, 500)
                    .centerCrop().into(pickedImage);
            pickedImage.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
            BackgroundImageResize resize = new BackgroundImageResize(null);
            resize.execute(selectedImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = Objects.requireNonNull(getContext()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadToStorage() {
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = Objects.requireNonNull(currentFirebaseUser).getUid();

        if (mUploadBytes != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + uid +
                    "/profile_picture." + getFileExtension(selectedImage));
            UploadTask uploadTask = storageReference.putBytes(mUploadBytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri;
                            if (authVm != null && userVm != null) {
                                userVm.updateUserImage(authVm.getUser().getUid(), imageUri.toString(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        } else {
                                            if (task.getException() != null)
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "could not upload image",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void spinnerIntToString(ArrayList<String> list, int num) {
        for (int i = 1; i <= num; i++) {
            String element;
            if (i < 10) {
                element = "0" + Integer.toString(i);
            } else {
                element = Integer.toString(i);
            }
            list.add(element);
        }
    }

    public void changeImageOnClick() {
        Intent pickPhoto = new Intent();
        pickPhoto.setType("image/*");
        pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pickPhoto, Constants.PICK_IMAGE_REQUEST);
    }

    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {
        Bitmap mBitmap;

        public BackgroundImageResize(Bitmap bitmap) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(Uri... params) {
            Log.d(TAG, "doInBackground: started.");
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext())
                        .getContentResolver(), params[0]);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: IOException" + e.getMessage());
            }
            byte[] bytes = null;

            bytes = getBytesFromBitmap(mBitmap, 50);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            mUploadBytes = bytes;
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
