package com.example.farhaan.farhaan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    ImageView bgpView;
    ImageView camera;
    TextView recipeType, servingNumber, cookingTime;
    Button levelOne, levelTwo, levelThree;
    LinearLayout nextLayout;
    String feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgpView = (ImageView) findViewById(R.id.bgpView);
        camera = (ImageView) findViewById(R.id.camera);
        recipeType = (TextView) findViewById(R.id.recipeType);
        levelOne = (Button) findViewById(R.id.levelOne);
        levelTwo = (Button) findViewById(R.id.levelTwo);
        levelThree = (Button) findViewById(R.id.levelThree);
        servingNumber = (TextView) findViewById(R.id.number_serves);
        cookingTime = (TextView) findViewById(R.id.cookingTime);
        nextLayout = (LinearLayout) findViewById(R.id.next);

        Drawable myDrawable = getResources().getDrawable(R.drawable.bgp);
        Bitmap anImage      = ((BitmapDrawable) myDrawable).getBitmap();
        Bitmap blurredBitmap = BlurBuilder.blur(MainActivity.this, anImage);
        bgpView.setImageBitmap(blurredBitmap);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMethod(view);
            }
        });

        recipeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseRecipe(view);
            }
        });

        levelOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelOne.setBackgroundColor(Color.parseColor("#85BB38"));
                levelTwo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                levelThree.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        levelTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelOne.setBackgroundColor(Color.parseColor("#FFFFFF"));
                levelTwo.setBackgroundColor(Color.parseColor("#85BB38"));
                levelThree.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        levelThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                levelOne.setBackgroundColor(Color.parseColor("#FFFFFF"));
                levelTwo.setBackgroundColor(Color.parseColor("#FFFFFF"));
                levelThree.setBackgroundColor(Color.parseColor("#85BB38"));
            }
        });

        servingNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_number_picker);
                dialog.setTitle("Choose a number");
                final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.numberPicker);
                final Button numberPicked = (Button) dialog.findViewById(R.id.number_picking_done);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(10);
                numberPicked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        servingNumber.setText(String.valueOf(numberPicker.getValue()));
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        cookingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_time_picker);
                dialog.setTitle("Choose time");
                final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                final Button timePicked = (Button) dialog.findViewById(R.id.time_picking_done);
                timePicked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cookingTime.setText(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        nextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_feedback);
                dialog.setTitle("Please give feedback");
                final EditText feedbackText= (EditText) dialog.findViewById(R.id.feedbackText);
                final Button feedbackDone = (Button) dialog.findViewById(R.id.feedback_done);
                feedbackDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedback = feedbackText.getText().toString();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void selectMethod(View view) {
        System.out.println("Opening Dialog box");
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Choose a method");
        ImageView uploadgallery = (ImageView) dialog.findViewById(R.id.upload_gallery);
        ImageView uploadcamera = (ImageView) dialog.findViewById(R.id.upload_camera);
        uploadgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent choosePicture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choosePicture, 0);
                dialog.dismiss();
            }
        });
        uploadcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent capturePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(capturePicture, 1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent returned) {
        super.onActivityResult(requestCode, resultCode, returned);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK){
                    Uri selectedImage = returned.getData();
                    bgpView.setImageURI(selectedImage);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = returned.getData();
                    bgpView.setImageURI(selectedImage);
                }
                break;
        }
    }

    public void chooseRecipe(View view) {
        System.out.println("Choose recipe type");
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_choose_recipe);
        dialog.setTitle("Choose a recipe");
        ListView listView = (ListView) dialog.findViewById(R.id.recipe_list);
        final String[] types_recipe = getResources().getStringArray(R.array.recipe_types);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types_recipe));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recipeType.setText(types_recipe[i]);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
