package de.deichcode.ehc_neuwiedticker;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class ParsGoalsPenalties extends AsyncTask<Void, Void, Void> {
	private View v;
	private ListView tickerVerlauf;
	private Tick ticker_data[];
	private Queue<String> first_queue = new LinkedList<String>();
	private Queue<String> second_queue = new LinkedList<String>();
	private Queue<String> third_queue = new LinkedList<String>();
	private int index;
	GoalsPenaltiesAdapter adapterTicker;
	private static Tick[] EMPTY_TICK = new Tick[] { new Tick("Es sind noch keine Einträge Vorhanden,", "oder es gibt ein Problem mit Ihrer Internet-Verbindung", " ") };

	public ParsGoalsPenalties(View v, int index) {
		super();
		this.v = v;
		this.index = index;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Void doInBackground(Void... params) {
		Document doc;
		int i = 0;
		int out = 0;
		try {
			doc = Jsoup.connect("http://www.pro-eishockey-neuwied.de/ticker/tore.htm").get();
			Elements images = doc.select("td");
			for (Element image : images) {
				if (out == index && !(image.text().startsWith("\u00a0"))) {
					switch (i % 4) {
					case 0:
						break;
					case 1:
						first_queue.add(image.text());
						break;
					case 2:
						second_queue.add(image.text());
						if (image.text().isEmpty()) {
							third_queue.add("");
							i++;
							i++;
						}
						break;
					case 3:
						third_queue.add(image.text());
						if (image.text().isEmpty()) {
							i++;
						}
						break;
					}
				}
				if ((image.text().startsWith("\u00a0"))) {
					out++;
					i = 0;
				}
				i++;
			}
			i = 0;
			ticker_data = new Tick[Math.max(Math.max(third_queue.size(), second_queue.size()), first_queue.size())];
			while (!(third_queue.isEmpty()) || !(second_queue.isEmpty()) || !(first_queue.isEmpty())) {
				ticker_data[i] = new Tick(first_queue.poll(), second_queue.poll(), third_queue.poll());
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
		if (ticker_data != null) {
			adapterTicker = new GoalsPenaltiesAdapter(this.v.getContext(), R.layout.golas_penalties_item, ticker_data);
		}
		else {
			adapterTicker = new GoalsPenaltiesAdapter(this.v.getContext(), R.layout.golas_penalties_item, EMPTY_TICK);
		}
		if (index == 1) {
			tickerVerlauf = (ListView) this.v.findViewById(R.id.listViewGoals);
		}
		else {
			tickerVerlauf = (ListView) this.v.findViewById(R.id.listViewPenalties);
		}
		tickerVerlauf.setAdapter(adapterTicker);
	}

}
