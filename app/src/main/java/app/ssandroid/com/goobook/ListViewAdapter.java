package app.ssandroid.com.goobook;

/**
 * Copyright (c) 2019 <ClientName>. All rights reserved.
 * Created by deepak on 30/6/19.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ListViewAdapter extends ArrayAdapter<Book> {

    //the hero list that will be displayed
    private List<Book> bookList;

    //the context object
    private Context mCtx;

    // layout to inflate, i.e. R.layout.item_list
    private int layout;

    //here we are getting the bookList and context
    //so while creating the object of this adapter class we need to give bookList and context
    public ListViewAdapter(List<Book> bookList, int layout, Context mCtx) {
        super(mCtx, layout, bookList);
        this.bookList = bookList;
        this.mCtx = mCtx;
        this.layout = layout;
    }


    //this method will return the list item
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        //getting the layoutInflater
        final LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        final View listViewItem =  inflater.inflate(layout, null, true);

        // checkbox logic

        //getting text views
        TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);
        TextView textViewAuthor = listViewItem.findViewById(R.id.textViewAuthor);
        final CheckBox checkBox = listViewItem.findViewById(R.id.checkbox);

        //Getting the book for the specified position
        final Book book = bookList.get(position);

        // String array having details passing to details activity
        final String[] bookDetails = new String[] {
                book.getId(),
                book.getTitle(),
                book.getSubtitle(),
                book.getAuthor(),
                book.getImageUrl(),
                book.getPdf()
        };

        //setting book values to textViews
        textViewTitle.setText(bookDetails[1]);
        textViewAuthor.setText(bookDetails[3]);
        checkBox.setChecked(book.getSelected());

        // to show the title on long press
        listViewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mCtx, bookDetails[1], Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        // to pass intent to detail book activity
        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, BookDetails.class);
                intent.putExtra("book", bookDetails);
                mCtx.startActivity(intent);
            }

        });

        // to mark or un-mark the book as read or unread
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    book.setSelected(true);
                    Toast.makeText(mCtx, "Marked as read", Toast.LENGTH_SHORT).show();
                }
                else {
                    book.setSelected(false);
                    Toast.makeText(mCtx, "Marked as unread", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //returning the listItem
        return listViewItem;
    }


}