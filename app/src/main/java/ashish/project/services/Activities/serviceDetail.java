package ashish.project.services.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ashish.project.services.R;

public class serviceDetail extends AppCompatActivity {

    private String serviceSelected;
    private ImageView headerImage;
    private ImageView backBtn;
    private TextView txtHeaderServiceName;
    private TextView bookNowBtn;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        String descriptionData = "";

        if (getIntent().hasExtra("imageName")) {
            serviceSelected = getIntent().getStringExtra("imageName");
        }

        backBtn = findViewById(R.id.backBtn);
        txtHeaderServiceName = findViewById(R.id.txtHeader);
        headerImage = findViewById(R.id.headerImage);
        description = findViewById(R.id.description);
        bookNowBtn = findViewById(R.id.bookNowBtn);


        switch (serviceSelected) {

            // Home Services
            case "electrician":
                headerImage.setImageResource(R.drawable.intro1);
                txtHeaderServiceName.setText("Electrician");
                descriptionData = getString(R.string.electrician);
                break;

            case "carpenter":
                headerImage.setImageResource(R.drawable.intro7);
                txtHeaderServiceName.setText("Carpenter");
                descriptionData = getString(R.string.carpenter);
                break;

            case "plumber":
                headerImage.setImageResource(R.drawable.plumber);
                txtHeaderServiceName.setText("Plumber");
                descriptionData = getString(R.string.plumber);
                break;

            case "tailor":
                headerImage.setImageResource(R.drawable.tailor);
                txtHeaderServiceName.setText("Tailor");
                descriptionData = getString(R.string.tailor);
                break;

            case "beautician":
                headerImage.setImageResource(R.drawable.beautician);
                txtHeaderServiceName.setText("Beautician");
                descriptionData = getString(R.string.beautician);
                break;

            case "tuition":
                headerImage.setImageResource(R.drawable.intro5);
                txtHeaderServiceName.setText("Tuition");
                descriptionData = getString(R.string.teacher);
                break;

            case "vehicle":
                headerImage.setImageResource(R.drawable.vehicle);
                txtHeaderServiceName.setText("Vehicle");
                descriptionData = getString(R.string.vehicle);
                break;

            case "tiffin":
                headerImage.setImageResource(R.drawable.tiffin);
                txtHeaderServiceName.setText("Tiffin");
                descriptionData = getString(R.string.tiffin);
                break;

            case "grocery":
                headerImage.setImageResource(R.drawable.grocery);
                txtHeaderServiceName.setText("Grocery");
                descriptionData = getString(R.string.grocery);
                break;

            case "vegetables":
                headerImage.setImageResource(R.drawable.vegetables);
                txtHeaderServiceName.setText("Vegetables");
                descriptionData = getString(R.string.grocery);
                break;

            case "medicine":
                headerImage.setImageResource(R.drawable.intro6);
                txtHeaderServiceName.setText("Medicine");
                descriptionData = getString(R.string.medicine);
                break;

            case "jewellery":
                headerImage.setImageResource(R.drawable.jewellery);
                txtHeaderServiceName.setText("jewellery");
                descriptionData = getString(R.string.jewellery);
                break;

                case "kids":
                headerImage.setImageResource(R.drawable.kids);
                txtHeaderServiceName.setText("kids wear");
                descriptionData = getString(R.string.kids);
                break;

                case "womens":
                headerImage.setImageResource(R.drawable.intro4);
                txtHeaderServiceName.setText("women's wear");
                descriptionData = getString(R.string.kids);
                break;

            // Appliance services

            case "RO":
                headerImage.setImageResource(R.drawable.intro2);
                txtHeaderServiceName.setText("RO Service");
                descriptionData = getString(R.string.appliance);
                break;

            case "geyser":
                headerImage.setImageResource(R.drawable.geyser);
                txtHeaderServiceName.setText("Geyser");
                descriptionData = getString(R.string.appliance);
                break;

            case "fridge":
                headerImage.setImageResource(R.drawable.fridge);
                txtHeaderServiceName.setText("Fridge");
                descriptionData = getString(R.string.appliance);
                break;

            case "microwave":
                headerImage.setImageResource(R.drawable.microwave);
                txtHeaderServiceName.setText("Microwave");
                descriptionData = getString(R.string.appliance);
                break;

            case "washing":
                headerImage.setImageResource(R.drawable.washing_machine);
                txtHeaderServiceName.setText("Washing Machine");
                descriptionData = getString(R.string.appliance);
                break;

            case "AC":
                headerImage.setImageResource(R.drawable.intro3);
                txtHeaderServiceName.setText("AC Service");
                descriptionData = getString(R.string.appliance);
                break;

            case "airCooler":
                headerImage.setImageResource(R.drawable.air_cooler);
                txtHeaderServiceName.setText("Air Cooler");
                descriptionData = getString(R.string.appliance);
                break;

            case "TV":
                headerImage.setImageResource(R.drawable.television);
                txtHeaderServiceName.setText("TV Service");
                descriptionData = getString(R.string.appliance);
                break;

        }

        description.setText(HtmlCompat.fromHtml(descriptionData, HtmlCompat.FROM_HTML_MODE_COMPACT));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(serviceDetail.this, QueryForm.class);
                intent.putExtra("requestedCategory", txtHeaderServiceName.getText().toString());
                startActivity(intent);
            }
        });
    }
}