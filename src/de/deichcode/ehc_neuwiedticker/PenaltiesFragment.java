package de.deichcode.ehc_neuwiedticker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PenaltiesFragment extends Fragment {
	private static View v;

	@Override
	public void onStart(){
		super.onStart();
		final ListView lv = (ListView) getView().findViewById(R.id.listViewPenalties);
		lv.setLongClickable(true);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
		    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
		    	Toast.makeText(getActivity(), "Text in Zwischenablage kopiert", Toast.LENGTH_SHORT).show();
		       	TextView textFirst = (TextView) arg1.findViewById(R.id.goals_penalties_first);
		      	String first = textFirst.getText().toString();
		       	TextView textSecond = (TextView) arg1.findViewById(R.id.goals_penalties_second);
		      	String second = textSecond.getText().toString();
		       	TextView textThird = (TextView) arg1.findViewById(R.id.goals_penalties_third);
		       	String third = textThird.getText().toString();
		       	String linebrak = "\n";
		        String copy = first + linebrak + second + linebrak + third;
		        copyStringToClipboard(copy);               	
		        return true;
		        }
	    });
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.penalties, container, false);
		
		ParsGoalsPenalties penalties = new ParsGoalsPenalties(v, 2);  //Pars Goals
		penalties.execute();
		
		return v;
	}

	
	protected void copyStringToClipboard(String copy) {
		ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("copy", copy);
		clipboard.setPrimaryClip(clip);
	}
	
	
	public static void refresh() {
		ParsGoalsPenalties penalties = new ParsGoalsPenalties(v, 2);  //Pars Goals
		penalties.execute();		
	}
}
