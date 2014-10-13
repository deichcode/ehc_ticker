package de.deichcode.ehc_neuwiedticker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class DownloadImage extends AsyncTask<Void, Void, Void>{
	private ImageView iv;
	private View progress;
	private Integer id;
	private String filename;
	
	public DownloadImage(Integer id, ImageView iv, View progress){
		super();
		this.iv = iv;
		this.progress = progress;
		this.id = id;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
        Document doc;
        URL url;
        Bitmap logo;
        String fileSRC;
        int index;
        final String URL = "http://www.pro-eishockey-neuwied.de/ticker/";
        final String searchQuery = ".de/ticker/";
        Elements images;
        try {                                                                                       //Parse URL for Picture with Jsoup
            doc = Jsoup.connect(URL + "index.htm").timeout(10000).get();	                        //HTML in doc speichern mit JSoup
            images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");                                //Alle Bild Elemente in images Speichern
            fileSRC = images.get(this.id).attr("src");								                //scr Attribut in String fileSRC schreiben
            index = fileSRC.lastIndexOf("/");                                                       //search index of last "/"
            this.filename = fileSRC.substring(index+1);											    //extract filename from fileSRC
    		if(!(this.iv.getContext().getFileStreamPath(this.filename).exists())){                  //execute if file not locally stored
                index = fileSRC.lastIndexOf(searchQuery);                                           //lookup if image src is still original
                if (index != -1) {
                    fileSRC = fileSRC.substring(index + searchQuery.length());                      //repair file src
                }
                url = new URL(URL + fileSRC);		                                                //create url
    			logo = BitmapFactory.decodeStream(url.openConnection() .getInputStream());		    //load images from server
    			FileOutputStream fos = this.iv.getContext().openFileOutput(this.filename, Context.MODE_PRIVATE);
    			logo.compress(Bitmap.CompressFormat.PNG, 100, fos);
    			fos.close();
    		}
    		
        }
        catch (IOException e){
            e.printStackTrace();
        }
		return null;
	}
	
	@Override
	protected void onPostExecute(Void v){
		try {
			java.io.FileInputStream in = this.iv.getContext().openFileInput(this.filename);
			this.iv.setImageBitmap(BitmapFactory.decodeStream(in));
			this.progress.setVisibility(View.GONE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
			
}	
