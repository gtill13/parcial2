package br.com.parcial2.gabrieltillmann;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Tela2 extends Activity {

	public static final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
	public static final int NOVO_ARQ = 1;
	public static final int EDITAR_ARQ = 2;
	
	private TextView nomeArq;
	private EditText texto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela2);
		 
		Log.d("TiLL", "onCreate(Bundle savedInstanceState)");
		
		nomeArq = (TextView) findViewById(R.id.tela2_Nome);
		texto   = (EditText) findViewById(R.id.tela2_Conteudo);
		
		Intent i = getIntent();
		String s = i.getStringExtra("nome");
		if (s != null) {
			nomeArq.setText(s);
			Ler(s);
		}
		
		int tipo = i.getIntExtra("tipo", NOVO_ARQ);
		
		if (tipo == EDITAR_ARQ) {
			setTitle("Editar");
		} else {
			setTitle("Novo");
		}
	
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.menuSair)
		{
			salvaSharedPreferences();
			setResult(-1);
			finish();
		}

		return true;
	}
	
	public void salvaSharedPreferences(  ) {
		SharedPreferences settings 	= getSharedPreferences( "MinhaPeferencia", MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();
		editor.putString ("nomeArq" , nomeArq.getText().toString());
		editor.commit();
	}
	
	public void onSalvar(View v) {
		
		String nome = nomeArq.getText().toString();
		if (nome.equals(""))
			nome = "temporario.txt";
		
		File fileExt = new File(PATH, nome); 
    	fileExt.getParentFile().mkdirs();
    	
	    FileOutputStream fosExt = null ;
	    try {
			fosExt = new FileOutputStream(fileExt);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	    try {
			 fosExt.write( new String(texto.getText().toString()).getBytes() );
			 try {
				fosExt.close();
			 } 
		     catch (IOException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 finish();
		} 
	    catch (IOException e) {
	    	 e.printStackTrace();
		}
	}
	
	public void onCancelar(View v) {
		finish();
	}
	
	public void onExcluir(View v) {
		
		AlertDialog.Builder alert  = new AlertDialog.Builder(Tela2.this);
		alert.setTitle("Exclusão");
		alert.setMessage("Confirma a exclusão do arquivo?");
		alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() 
		{
            public void onClick(DialogInterface arg0, int arg1) {
        		new File(PATH, nomeArq.getText().toString()).delete();
        		finish();
            }
		});
		
		alert.setNegativeButton("Não", new DialogInterface.OnClickListener() 
		{
            public void onClick(DialogInterface arg0, int arg1) {		   
            }
		});
			
		alert.show();
		
		
	}
	
	public void Ler(String s)
	{
		File file = new File(PATH, s); 
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte arrBits[] = new byte[(int)file.length()]; 
            
			try {
				fis.read(arrBits);
			} 
            catch (IOException e) {
				e.printStackTrace();
			} 
            
            String sAux = new String(arrBits);
            try {
				fis.close();
			}
            catch (IOException e) {
				e.printStackTrace();
			}
            texto.setText(sAux);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
