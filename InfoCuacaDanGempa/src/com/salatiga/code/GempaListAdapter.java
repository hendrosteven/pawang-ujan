package com.salatiga.code;

import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Class ini adalah Adapter untuk daftar gempa
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class GempaListAdapter extends ArrayAdapter<Gempa>{

	private final Context context;
	private final List<Gempa> listGempa;
	private Scanner scanner;
	
	public GempaListAdapter(Context context, List<Gempa> values) {
		super(context, R.layout.gempa_item, values);
		this.context = context;
		this.listGempa = values;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.gempa_item, parent, false);
		TextView txtWilayah = (TextView) rowView.findViewById(R.id.txtWilayah);
		TextView txtTanggalJam = (TextView)rowView.findViewById(R.id.txtTanggalJam);		
		TextView txtMagnitude = (TextView)rowView.findViewById(R.id.txtMagnitude);
		TextView txtSr = (TextView)rowView.findViewById(R.id.txtSr);
		
		Gempa gempa = listGempa.get(position);	
		
		txtWilayah.setText(gempa.getKeterangan());
		txtTanggalJam.setText(gempa.getTanggal());
		scanner = new Scanner(gempa.getMagnitude());
		double magnitude = scanner.nextDouble();
		
		txtMagnitude.setText(magnitude+"");
		if(magnitude<3.9){
			txtMagnitude.setTextColor(context.getResources().getColor(R.color.level1));
			txtSr.setTextColor(context.getResources().getColor(R.color.level1));
		}else if(magnitude>=4.0 && magnitude<=5.9){
			txtMagnitude.setTextColor(context.getResources().getColor(R.color.level2));
			txtSr.setTextColor(context.getResources().getColor(R.color.level2));
		}else if(magnitude>=6.0 && magnitude<=6.9){
			txtMagnitude.setTextColor(context.getResources().getColor(R.color.level3));
			txtSr.setTextColor(context.getResources().getColor(R.color.level3));
		}else if(magnitude>=6.9){
			txtMagnitude.setTextColor(context.getResources().getColor(R.color.level4));
			txtSr.setTextColor(context.getResources().getColor(R.color.level4));
		}
		
		return rowView;
	}

}
