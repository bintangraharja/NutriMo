package id.ac.umn.nutrimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageSlider artikelSlider = findViewById(R.id.artikelSlider);
        ArrayList<SlideModel> artikelModels = new ArrayList<>();
        artikelModels.add(new SlideModel(R.drawable.artikel_1, ScaleTypes.FIT));
        artikelModels.add(new SlideModel(R.drawable.artikel_2, ScaleTypes.FIT));
        artikelModels.add(new SlideModel(R.drawable.artikel_3, ScaleTypes.FIT));

        artikelSlider.setImageList(artikelModels,ScaleTypes.FIT);
    }
}