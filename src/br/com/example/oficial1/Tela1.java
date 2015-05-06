package br.com.example.oficial1;

import java.util.ArrayList;

import br.com.example.java.Pessoa;
import br.com.example.java.PessoaAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class Tela1 extends Activity {

	private TextView m_texto;
	private ListView m_listView;
	private ArrayList<Pessoa> m_arrPessoas;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);
        
		Log.d("TiLL", "onCreate(Bundle savedInstanceState)");
        
		m_texto = (TextView) findViewById(R.id.t1_textView);
		m_listView = (ListView) findViewById(R.id.t1_listView);
		m_arrPessoas = new ArrayList<Pessoa>();
        
		m_arrPessoas.add(new Pessoa("", "Eduarda", "443.506.892-30", "mfey@ilang.com", "3330-0899", "9114-1699", 'f'));
		m_arrPessoas.add(new Pessoa("", "Marcelo Fey", "443.506.892-30", "mfey@ilang.com", "3330-0899", "9114-1699", 'm'));
		
		m_listView.setAdapter( new PessoaAdapter(getApplicationContext() , m_arrPessoas) );

		m_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicaoClicada, long arg3) {
				Log.d("TiLL", "Clicado na linha ("+posicaoClicada+")");

				Intent novaTela = new Intent(getApplicationContext(), Tela2.class);
					
				novaTela.putExtra("objeto", m_arrPessoas.get(posicaoClicada) );
				startActivityForResult(novaTela, 2);
			}
		});
		
		m_listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int posicaoClicada, long arg3) {
				Log.d("TiLL", "LongClick na linha ("+posicaoClicada+")");
				
				final int posicao = posicaoClicada;
	
				AlertDialog.Builder alert  = new AlertDialog.Builder(Tela1.this);
				alert.setTitle("Exclusão");
				alert.setMessage("Confirma a exclusão da linha seleciona?");
				alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() 
				{
		            public void onClick(DialogInterface arg0, int arg1) {
		            	m_arrPessoas.remove(posicao);
		            	AtualizaDados();
		            }
				});
				
				alert.setNegativeButton("Não", new DialogInterface.OnClickListener() 
				{
		            public void onClick(DialogInterface arg0, int arg1) {		   
		            }
				});
					
				alert.show();
				
				AtualizaDados();
				
				return false;
			}
	    });
			
		AtualizaDados();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	AtualizaDados();
    }
    
    public void OnButtonTela1(View v) {
    	
		Intent novaTela = new Intent(getApplicationContext(), Tela2.class);
		startActivityForResult(novaTela, 1);
    	
    }
    
	public void AtualizaDados()
	{
		m_listView.invalidateViews();
		
		if (m_arrPessoas.size() > 0)
			m_texto.setText("Contatos App, "+m_arrPessoas.size()+" encontrado(s)");
		else
			m_texto.setText("Contatos App, nenhum encontrado");
	}
}
