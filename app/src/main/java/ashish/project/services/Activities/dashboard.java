package ashish.project.services.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ashish.project.services.Adapters.sliderAdapter;
import ashish.project.services.R;
import ashish.project.services.models.sliderItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class dashboard extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Boolean alreadyHasDetails;
    private TextView userName;
    private TextView editProfileBtn;
    private LinearLayout electrician, carpenter, plumber, tailor, beautician, tuition, vehicle, tiffin, grocery, vegetables, medicine, jewellery, kids, womens;
    private LinearLayout RO, geyser, fridge, microwave, washing, AC, airCooler, TV;
    private LinearLayout customerCare;
    private Uri imageURI;
    private CircleImageView userImage;

    private final Handler sliderHandler = new Handler();
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final SharedPreferences userDetails = getSharedPreferences("user_details", MODE_PRIVATE);

        alreadyHasDetails = userDetails.getBoolean("hasDetails", false);

        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        viewPager2 = findViewById(R.id.imageSlider);

//        Home Services

        electrician = findViewById(R.id.electrician);
        carpenter = findViewById(R.id.carpenter);
        plumber = findViewById(R.id.plumber);
        tailor = findViewById(R.id.tailor);
        beautician = findViewById(R.id.beautician);
        tuition = findViewById(R.id.tuition);
        vehicle = findViewById(R.id.vehicle);
        tiffin = findViewById(R.id.tiffin);
        grocery = findViewById(R.id.grocery);
        vegetables = findViewById(R.id.vegetables);
        medicine = findViewById(R.id.medicine);
        jewellery = findViewById(R.id.initationJwellery);
        kids = findViewById(R.id.kidsWear);
        womens = findViewById(R.id.womensWear);

//        Appliance Services

        RO = findViewById(R.id.RO);
        geyser = findViewById(R.id.geyser);
        fridge = findViewById(R.id.fridge);
        microwave = findViewById(R.id.microwave);
        washing = findViewById(R.id.washing);
        AC = findViewById(R.id.AC);
        airCooler = findViewById(R.id.airCooler);
        TV = findViewById(R.id.TV);

        customerCare = findViewById(R.id.customerCare);

        List<sliderItem> sliderItem = new ArrayList<>();

        sliderItem.add(new sliderItem(R.drawable.intro1));
        sliderItem.add(new sliderItem(R.drawable.intro2));
        sliderItem.add(new sliderItem(R.drawable.intro4));         //             Images to be shown on page
        sliderItem.add(new sliderItem(R.drawable.intro5));
        sliderItem.add(new sliderItem(R.drawable.intro6));
        sliderItem.add(new sliderItem(R.drawable.intro7));

        viewPager2.setAdapter(new sliderAdapter(sliderItem, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(25));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);     //Slide Duration 2 seconds
            }
        });

        if(alreadyHasDetails){
            userName.setText(userDetails.getString("fName", ""));
        }

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(dashboard.this, ProfileForm.class);
                startActivity(profileIntent);
            }
        });

        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "electrician");
                startActivity(intent);

            }
        });

        carpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "carpenter");
                startActivity(intent);
            }
        });

        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "plumber");
                startActivity(intent);
            }
        });

        tailor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "tailor");
                startActivity(intent);
            }
        });

        beautician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "beautician");
                startActivity(intent);
            }
        });

        tuition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "tuition");
                startActivity(intent);
            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "vehicle");
                startActivity(intent);
            }
        });

        tiffin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "tiffin");
                startActivity(intent);
            }
        });

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "grocery");
                startActivity(intent);
            }
        });

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "vegetables");
                startActivity(intent);
            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "medicine");
                startActivity(intent);
            }
        });

        jewellery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "jewellery");
                startActivity(intent);
            }
        });

        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "kids");
                startActivity(intent);
            }
        });

        womens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "womens");
                startActivity(intent);
            }
        });

        RO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "RO");
                startActivity(intent);
            }
        });

        geyser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "geyser");
                startActivity(intent);
            }
        });

        fridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "fridge");
                startActivity(intent);
            }
        });

        microwave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "microwave");
                startActivity(intent);
            }
        });

        washing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "washing");
                startActivity(intent);
            }
        });

        AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "AC");
                startActivity(intent);
            }
        });

        airCooler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "airCooler");
                startActivity(intent);
            }
        });

        TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboard.this, serviceDetail.class);
                intent.putExtra("imageName", "TV");
                startActivity(intent);
            }
        });

        customerCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, customerCare.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final SharedPreferences userDetails = getSharedPreferences("user_details", MODE_PRIVATE);
        userName.setText(userDetails.getString("fName", ""));

//        imageURI = Uri.parse(userDetails.getString("image", ""));

//        userImage.setImageURI(imageURI);

    }
}