package br.com.parcial2.gabrieltillmann;

import java.io.File;
import java.util.ArrayList;

import br.com.parcial2.gabrieltillmann.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Tela1 extends Activity {
	
	public static final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
	public static final int NOVO_ARQ = 1;
	public static final int EDITAR_ARQ = 2;
	
	//private Activity Tela1 = this;s
	private ListView listView;
	private ArrayList<String> arrArquivos;
	

	public void CarregaLista() {
		
		arrArquivos.removeAll(arrArquivos);
		arrArquivos.add("NOVO ARQUIVO");
		
		File[] lista = new File(PATH).listFiles();
		
		if (lista != null)
		{
			for (int i = 0; i < lista.length; ++i)
	    	{
				arrArquivos.add(lista[i].getName());
	    	}
		}
		
	    ArrayAdapter<String> Adapter;
	    Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrArquivos);
    	listView.setAdapter(Adapter);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);
      
		Log.d("TiLL", "onCreate(Bundle savedInstanceState)");
		
		SharedPreferences settings = getSharedPreferences("MinhaPeferencia", MODE_PRIVATE);
		String s = settings.getString("nomeArq", "");
		if (!s.isEmpty())
		{
			SharedPreferences.Editor editor = settings.edit();
			editor.remove("nomeArq");
			editor.commit();
			ChamaTela2(s, NOVO_ARQ);
		}
		
		listView = (ListView) findViewById(R.id.tela1_listView);
		arrArquivos = new ArrayList<String>();
		
		CarregaLista();
		
    	listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				if (position == 0) 
				{
					final Dialog dialog = new Dialog(arg1.getContext());
					dialog.setTitle("Novo arquivo");
					dialog.setContentView(R.layout.activity_dlg);
					dialog.show();
					
					final EditText editText  = (EditText) dialog.findViewById(R.id.dlg_text);
					Button btProsseguir      = (Button) dialog.findViewById(R.id.dlg_Prosseguir);
					Button btCancelar        = (Button) dialog.findViewById(R.id.dlg_Cancelar);
					
					
					btCancelar.setOnClickListener(new OnClickListener() 
					{
						public void onClick(View v) 
						{
							dialog.dismiss();
						}
					});
					
					btProsseguir.setOnClickListener(new OnClickListener() 
					{
						public void onClick(View v) 
						{
							ChamaTela2(editText.getText().toString(), NOVO_ARQ);
							dialog.dismiss();
						}
					});
					
				} else {
					ChamaTela2(arrArquivos.get(position), EDITAR_ARQ);
				}
			}
		});
    }
    
	protected void ChamaTela2(String nome, int tipo)
	{
		if (!nome.contains(".txt"))
			nome += ".txt";
			
		Intent i = new Intent(getApplicationContext(), Tela2.class);
		i.putExtra("nome", (nome));
		i.putExtra("tipo", tipo);
		startActivityForResult(i, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == -1) {
			finish();
		}

		CarregaLista();
		
	}
    
}
