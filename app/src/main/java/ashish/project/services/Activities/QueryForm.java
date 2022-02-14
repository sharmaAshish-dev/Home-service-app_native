package ashish.project.services.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ashish.project.services.R;

public class QueryForm extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST = 1122;
    private LinearLayout upiPaymentLayout;
    private RadioButton cod, upi;
    private TextView sendEnquiryBtn;
    Bitmap clickedPhoto;                    // captured Image by user
    String InitImageName;                   // Initial Image name = uID_ where uID is dynamic
    String finalImageName;                  // final Image name with convention uID_yyMMddHHmmss.jpg
    String imageString = "";               // for storing converted Image ( Base64 String )
    private CardView uploadImgCard;         // clickable card for uploading image
    private ImageView paymentSSImage;
    private TextView txtUploadImage;
    private EditText uFName, uLName, uPhone, uAddress, uPincode, uLandmark;
    private ImageView backBtn;
    private Boolean alreadyHasDetails;
    private String selectedCategory;
    private String selectedPaymentMode;
    private EditText customerQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_form);

        final SharedPreferences userDetails = getSharedPreferences("user_details", MODE_PRIVATE);

        alreadyHasDetails = userDetails.getBoolean("hasDetails", false);
        selectedPaymentMode = "Cash on delivery";
        selectedCategory = "Category";

        backBtn = findViewById(R.id.backBtn);

        uFName = findViewById(R.id.customerFirstName);
        uLName = findViewById(R.id.customerLastName);
        uPhone = findViewById(R.id.customerNumber);
        uAddress = findViewById(R.id.customerAddress);
        uPincode = findViewById(R.id.Pincode);
        uLandmark = findViewById(R.id.Landmark);

        customerQuery = findViewById(R.id.customerQuery);
        cod = findViewById(R.id.radioCOD);
        upi = findViewById(R.id.radioUPI);
        upiPaymentLayout = findViewById(R.id.upiPaymentLayout);
        uploadImgCard = findViewById(R.id.uploadImgLayout);
        paymentSSImage = findViewById(R.id.paymentSSImage);
        txtUploadImage = findViewById(R.id.txtUploadImage);
        sendEnquiryBtn = findViewById(R.id.sendEnquiryBtn);

        InitImageName = userDetails.getString("fName", "Invalid") + "_";
        finalImageName = "";

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        if (alreadyHasDetails) {

            uFName.setText(userDetails.getString("fName", ""));
            uLName.setText(userDetails.getString("lName", ""));
            uPhone.setText(userDetails.getString("Mobile", ""));
            uAddress.setText(userDetails.getString("Address", ""));
            uPincode.setText(userDetails.getString("Pincode", ""));
            uLandmark.setText(userDetails.getString("Landmark", ""));

        }

        if (getIntent().hasExtra("requestedCategory")) {
            selectedCategory = getIntent().getStringExtra("requestedCategory");
        }

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPaymentMode = "Cash on delivery";

                if (upiPaymentLayout.getVisibility() == View.VISIBLE) {
                    upiPaymentLayout.setVisibility(View.GONE);
                }

            }
        });

        upi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedPaymentMode = "UPI Payment";

                if (upiPaymentLayout.getVisibility() == View.GONE) {
                    upiPaymentLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        uploadImgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, IMAGE_PICK_REQUEST);
            }
        });

        sendEnquiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msgFooter = "payment Mode:  " + selectedPaymentMode + "\n\n"
                        + "From: \n\n" + uFName.getText().toString() + " " + uLName.getText().toString() + "\n"
                        + "Phone:  " + uPhone.getText().toString() + "\n"
                        + "Address:  " + uAddress.getText().toString() + "\n"
                        + "Pincode:  " +uPincode.getText().toString() + "\n"
                        + "LandMark:  " + uLandmark.getText().toString() + "\n";

                if (customerQuery.getText().toString().equals("")){
                    Toast.makeText(QueryForm.this, "Please enter your Query", Toast.LENGTH_SHORT).show();
                }else{

                    sendMail("Enquiry for " + selectedCategory, customerQuery.getText().toString() + "\n\n\n" + msgFooter);

                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMAGE_PICK_REQUEST) {

                Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                clickedPhoto = BitmapFactory.decodeFile(imagePath, options);

                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close();

                paymentSSImage.setImageURI(data.getData());

                txtUploadImage.setVisibility(View.GONE);

                SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
                Date date = new Date();

                // giving a name to captured file ( ImageName )
                if (finalImageName.equals("")) {
                    finalImageName = InitImageName + formatter.format(date);
                } else {
                    finalImageName = "";
                    finalImageName = InitImageName + formatter.format(date);
                }

                imageString = getStringImage(clickedPhoto);

            }

        } catch (Exception e) {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    //converting image to base64 string
    public String getStringImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    private void sendMail(String subject, String msgBody) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
//        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"Ashishking2014@gmail.com"});
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"influxinfotech19@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, msgBody);
        try {
            startActivity(Intent.createChooser(i, "Send mail via..."));
//            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendWhatsapp(String PhoneNo, String Text){

        boolean isWhatsappInstalled = appInstalled("com.whatsapp");

        if(isWhatsappInstalled){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + PhoneNo + "&text=" + Text));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Whatsapp not intalled on this device", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean appInstalled(String url){
        PackageManager packageManager = getPackageManager();
        boolean app_installed;

        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);

            app_installed = true;

        } catch (PackageManager.NameNotFoundException e) {

            app_installed = false;

            e.printStackTrace();
        }

        return app_installed;

    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();

    }
}