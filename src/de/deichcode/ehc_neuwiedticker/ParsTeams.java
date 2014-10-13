package de.deichcode.ehc_neuwiedticker;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class ParsTeams extends AsyncTask<Void, Void, Void> {
	private MainActivity mainActivity;
	private String home = null;
	private String guest = null;

	public ParsTeams(MainActivity mainActivity) {
		super();
		this.mainActivity = mainActivity;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Document doc;

		try {
			doc = Jsoup.connect("http://www.pro-eishockey-neuwied.de/ticker/index.htm").timeout(10000).get();
			Elements images = doc.select("b");
			this.home = images.get(0).text();
			this.guest = images.get(1).text();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		this.mainActivity.setGuest(this.guest);
		this.mainActivity.setHome(this.home);
	}

}
