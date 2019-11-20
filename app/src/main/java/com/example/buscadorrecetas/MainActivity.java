package com.example.buscadorrecetas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    Button btnBuscar;
    EditText etBuscador;
    TextView tvIngredientes;
    TextView tvCalorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.etBuscador = (EditText) findViewById(R.id.etBuscador);
        this.btnBuscar = (Button) findViewById(R.id.btnBuscar);
        this.tvIngredientes = (TextView) findViewById(R.id.tvIngredientes);
        this.tvCalorias = (TextView) findViewById(R.id.tvCalorias);

        this.btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = etBuscador.getText().toString();
                buscar(keyword);
            }
        });
    }

    protected void buscar(String keywords) {
        String app_id = "57024004";
        String app_key = "ee5d2f5e0ffe4aa969345020d3d7b024";
        String baseUrl = "https://test-es.edamam.com/search?";
        String url = baseUrl + "app_id=" + app_id + "&app_key=" + app_key + "&q=" + keywords;

        StringRequest solicitud = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tenemos respuesta desde el servidor
                        try {
                            JSONObject respuestaJSON = new JSONObject(response);
                            JSONArray hits = respuestaJSON.getJSONArray("hits");
                            JSONObject primerResultado = hits.getJSONObject(0);
                            JSONObject receta = primerResultado.getJSONObject("recipe");
                            JSONArray ingredientesArray = receta.getJSONArray("ingredientLines");

                            String calorias = receta.getString("calories");
                            String ingredientes = "";

                            for (int i=0; i<ingredientesArray.length(); i++) {
                                ingredientes += "-" + ingredientesArray.getString(i) + "\n";
                            }

                            tvIngredientes.setText(ingredientes);
                            tvCalorias.setText(calorias);


                            Toast.makeText(getApplicationContext(), "Buscado", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Algo fallo
                    }
                }
        );

        RequestQueue listaEspera = Volley.newRequestQueue(getApplicationContext());
        listaEspera.add(solicitud);

    }
}
