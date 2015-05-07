package br.com.example.oficial1;

import java.util.ArrayList;

import br.com.example.java.Pessoa;
import br.com.example.java.PessoaAdapter;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Tela3 extends Activity {

	private ArrayList<Pessoa>  m_arrPessoa; 
	private int m_iPosicao;
	private ListView m_listView;
	private TextView m_titulo;
	private View m_ViewSelecionado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela3);
		    	
		Log.d("TiLL", "onCreate(Bundle savedInstanceState) tela3");
		
		m_titulo = (TextView) findViewById(R.id.t3_label1);
		m_titulo.setText("Carregando contatos.");

		m_arrPessoa = new ArrayList<Pessoa>();
        
		ContentResolver cr = getContentResolver();
		
		Cursor cursor = cr.query( ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		while (cursor.moveToNext()) {
			
			Pessoa pessoa = new Pessoa();
			pessoa.setNome(cursor.getString(cursor.getColumnIndex( Contacts.DISPLAY_NAME)));
			pessoa.setID(cursor.getString(cursor.getColumnIndex( Contacts._ID  )));
			
			Cursor cr_emails = cr.query( Email.CONTENT_URI, null, Email.CONTACT_ID+ " = "+ pessoa.getID(), null, null);
			
			while (cr_emails.moveToNext()) {
				pessoa.setEmail(cr_emails.getString(cr_emails.getColumnIndex(Email.ADDRESS)));
			} 
			
			Cursor cr_telefones = cr.query( Phone.CONTENT_URI, null, Phone.CONTACT_ID+ " = "+ pessoa.getID() , null, null);

			
			while (cr_telefones.moveToNext()) {
				int type = cr_telefones.getInt(cr_telefones.getColumnIndex(Phone.TYPE));
								
				if (type == Phone.TYPE_HOME || type == Phone.TYPE_WORK) {
					pessoa.setTelFixo(cr_telefones.getString(cr_telefones.getColumnIndex(Phone.NUMBER)));
				}
				else if (type == Phone.TYPE_MOBILE) {
					pessoa.setTelCelular(cr_telefones.getString(cr_telefones.getColumnIndex(Phone.NUMBER)));
				}
			}
			
			if(!pessoa.getID().isEmpty()) {
				m_titulo.setText(m_titulo.getText()+".");
				m_arrPessoa.add(pessoa);
			}
			m_titulo.setText(m_arrPessoa.size()+" contato encontrados");
		
		}
		cursor.close();
		
		m_listView = (ListView)findViewById( R.id.t3_lista);
		
		m_listView.setAdapter( new PessoaAdapter(getApplicationContext() , m_arrPessoa) );
      
		m_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (m_ViewSelecionado == null) {
					m_ViewSelecionado = arg1;
					m_ViewSelecionado.setBackgroundColor(Color.CYAN);
				} else if (m_ViewSelecionado != arg1) {
					m_ViewSelecionado.setBackgroundColor(Color.TRANSPARENT);
					m_ViewSelecionado = arg1;
					m_ViewSelecionado.setBackgroundColor(Color.CYAN);
				}
				m_iPosicao = arg2;
			}
        });
	}
	
	public void onConfirmar(View view) {
		Intent confirmar = new Intent();
		setResult(Tela1.S_OK, confirmar);
		
		confirmar.putExtra("objeto", m_arrPessoa.get(m_iPosicao));
		
		finish();
	}
	
	public void onCancelar(View v) {
		Intent cancelar = new Intent();
		setResult(Tela1.S_FAILED, cancelar);
		finish();
	}
		
}

