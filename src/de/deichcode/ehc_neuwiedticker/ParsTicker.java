package de.deichcode.ehc_neuwiedticker;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class ParsTicker extends AsyncTask<Void, Void, Void> {
	private View v;
	private Tick ticker_data[];
	private Queue<String> head_queue = new LinkedList<String>();
	private Queue<String> time_queue = new LinkedList<String>();
	private Queue<String> text_queue = new LinkedList<String>();
	private TickerAdapter adapterTicker;
	private static Calendar calendar = Calendar.getInstance();
	private static Date date = calendar.getTime();
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
	private static String time = simpleDateFormat.format(date);
	private static Tick[] EMPTY_TICK = new Tick[] { new Tick("Achtung!", time + " Uhr", "Es sind noch keine Einträge Vorhanden, oder es gibt ein Problem mit Ihrer Internet-Verbindung") };

	public ParsTicker(View v) {
		super();
		this.v = v;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Document doc;
		int i = 0;
		try {
			doc = Jsoup.connect("http://www.pro-eishockey-neuwied.de/ticker/news.htm").timeout(10000).get();
			Elements images = doc.select("td");
			for (Element image : images) {
				switch (i % 4) {
				case 0:
					this.head_queue.add(image.text());
					break;
				case 1:
					this.time_queue.add(image.text());

					break;
				case 2:
					this.text_queue.add(image.text());
					break;
				case 3:

					break;
				}
				i++;
			}
			i = 0;
			this.ticker_data = new Tick[Math.max(Math.max(this.text_queue.size(), this.time_queue.size()), this.head_queue.size())];
			while (!(this.text_queue.isEmpty()) || !(this.time_queue.isEmpty()) || !(this.head_queue.isEmpty())) {
				this.ticker_data[i] = new Tick(this.head_queue.poll(), this.time_queue.poll(), this.text_queue.poll());
				i++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (this.ticker_data != null) {
			this.adapterTicker = new TickerAdapter(this.v.getContext(), R.layout.ticker_item, ticker_data);
		}
		else {
			this.adapterTicker = new TickerAdapter(this.v.getContext(), R.layout.ticker_item, EMPTY_TICK);
		}
		ListView tickerVerlauf = (ListView) this.v.findViewById(R.id.listViewTicker);
		// adapterTicker.clear();
		tickerVerlauf.setAdapter(this.adapterTicker);
	}

}
