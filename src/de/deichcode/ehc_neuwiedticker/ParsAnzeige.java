package de.deichcode.ehc_neuwiedticker;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class ParsAnzeige extends AsyncTask<Void, Void, Void> {
	private View v;
	private String scoreHome;
	private String scoreGuest;
	private String firstThird;
	private String secondThird;
	private String thridThird;
	private String overtime;
	private TextView score;
	private TextView first;
	private TextView second;
	private TextView third;
	private TextView over;

	public ParsAnzeige(View v) {
		super();
		this.v = v;
	}

	@Override
	protected void onPreExecute() {
		this.score = (TextView) this.v.findViewById(R.id.textViewScore);
		this.first = (TextView) this.v.findViewById(R.id.textViewFirsthThirdScore);
		this.second = (TextView) this.v.findViewById(R.id.textViewSecondThirdScore);
		this.third = (TextView) this.v.findViewById(R.id.textViewThirdThirdScore);
		this.over = (TextView) this.v.findViewById(R.id.textViewOvertimeScore);
	}

	@Override
	protected Void doInBackground(Void... params) {
		Document doc;
		try {
			doc = Jsoup.connect("http://www.pro-eishockey-neuwied.de/ticker/anzeige.htm").timeout(10000).get();
			Elements images = doc.select("font");
			this.scoreHome = images.get(0).text();
			this.scoreGuest = images.get(5).text();
			this.firstThird = images.get(6).text();
			this.secondThird = images.get(7).text();
			this.thridThird = images.get(8).text();
			if (images.size() > 9) {
				this.overtime = images.get(9).text();
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		/*String oldScore = this.score.getText().toString();
		try {
			int oldScoreHome = Integer.parseInt(oldScore.substring(0,1));
			int oldScoreGuest = Integer.parseInt(oldScore.substring(oldScore.length()-1));
			Log.e(""+oldScoreHome, ""+oldScoreGuest);
		}
		catch(NumberFormatException e){
			
		}
		if (scoreHome()){
			Log.e("Tor", "für Heim");
			this.score.setText(this.scoreHome + ":" + this.scoreGuest);
		}
		else if (scoreGuest()){
			Log.e("Tor", "für Gast");
			this.score.setText(this.scoreHome + ":" + this.scoreGuest);
		}
		if (this.firstThird != null) {
			this.first.setText(this.firstThird);
		}
		if (this.secondThird != null) {
			this.second.setText(this.secondThird);
		}
		if (this.thridThird != null) {
			this.third.setText(this.thridThird);
		}
		if (this.overtime != null)
			this.over.setText(this.overtime);

        Log.e("test",""+this.scoreHome);*/
        if (this.scoreHome != null  && this.scoreGuest != null) {
            this.score.setText(this.scoreHome + ":" + this.scoreGuest);

        }
        if (this.firstThird != null) {
            this.first.setText(this.firstThird);
        }
        if (this.secondThird != null) {
            this.second.setText(this.secondThird);
        }
        if (this.thridThird != null) {
            this.third.setText(this.thridThird);
        }
        if (this.overtime != null)
            this.over.setText(this.overtime);

	}

	private boolean scoreGuest() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean scoreHome() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
