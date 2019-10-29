package livrokotlin.com.br.calculadoradebitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.bloco_cotacao.*
import kotlinx.android.synthetic.main.bloco_cotacao.view.*
import kotlinx.android.synthetic.main.bloco_entrada.*
import kotlinx.android.synthetic.main.bloco_saida.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker"
    var cotacaoBitconis: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarContacao()

        btn_calcular.setOnClickListener{
            calcular()
        }
    }

    override fun onRestart() {
        super.onRestart()

        buscarContacao()
    }

    fun buscarContacao() {
        doAsync {
            //Acessa a API e busca o resultado
            val resposta = URL(API_URL).readText()

            cotacaoBitconis = JSONObject(resposta).getJSONObject("ticker").getDouble("last")
            val moeda = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = moeda.format(cotacaoBitconis)

            uiThread {
                txt_cotacao.setText("$cotacaoFormatada")
            }
        }
    }

    fun calcular(){
        if(txt_valor.text.isEmpty()){
            txt_valor.error = "Preencha um valor"
            return
        }

        val valor_digitado = txt_valor.text.toString().replace(",", ".").toDouble()

        val resultado = if(cotacaoBitconis > 0) valor_digitado / cotacaoBitconis else 0.0

        txt_qtd_bitcoins.text = "%.8f".format(resultado)


    }
}
