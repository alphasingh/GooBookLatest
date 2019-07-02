package app.ssandroid.com.goobook;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import android.widget.TextView;

public class BookDetails extends AppCompatActivity {

    TextView mTextViewTitle, mTextViewAuthor, mTextViewSubtitle;
    String[] bookDetails;
    Button mTextViewPdf;
    NetworkImageView mImageView;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // to go back manually and load data again
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the intent from the MainActivity and get every extra passed
        Intent intent = getIntent();
        // order: id [0], title [1], subtitle [2], author [3], image [4], pdf [5]
        bookDetails = intent.getStringArrayExtra("book");


        mTextViewTitle = findViewById(R.id.TextViewTitle);
        mTextViewAuthor = findViewById(R.id.TextViewAuthor);
        mTextViewPdf = findViewById(R.id.TextViewPdf);
        mTextViewSubtitle = findViewById(R.id.TextViewSubtitle);
        mImageView = findViewById(R.id.ImageView);


        //Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();

        mTextViewTitle.setText(bookDetails[1]);
        mTextViewSubtitle.setText(bookDetails[2]);
        mTextViewAuthor.setText(bookDetails[3]);
        // image setting on the imageView with the link given
        mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        loadImage(bookDetails[4]);
        // PDF download option
        mTextViewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bookDetails[5].equalsIgnoreCase("0"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bookDetails[5])));
                else
                    Toast.makeText(BookDetails.this, "No PDF available", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadImage(String url){
        if(url.equals("0")){
            mImageView.setDefaultImageResId(android.R.drawable.ic_dialog_alert);
            //Toast.makeText(this,"No Image found",Toast.LENGTH_LONG).show();
            return;
        }

        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext()).getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(mImageView, R.drawable.ic_launcher_foreground, android.R.drawable
                        .ic_dialog_alert));
        mImageView.setImageUrl(url, imageLoader);
    }
}
