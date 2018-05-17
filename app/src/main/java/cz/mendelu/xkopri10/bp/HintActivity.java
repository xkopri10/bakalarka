package cz.mendelu.xkopri10.bp;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class HintActivity extends AppCompatActivity {

    private TextView firstTextView, secondTextView, thirdTextView, exampleText, moredetailText, aboutMethod;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_hint);

        firstTextView = (TextView) findViewById(R.id.firstTextView);
        secondTextView = (TextView) findViewById(R.id.secondTextView);
        thirdTextView = (TextView) findViewById(R.id.thirdTextView);
        exampleText = (TextView) findViewById(R.id.exampleText);
        moredetailText = (TextView) findViewById(R.id.moredetailText);
        aboutMethod = (TextView) findViewById(R.id.aboutMethod);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutMethod.setText(
                "Princip aplikace je založen na metodě, která se jmenuje Tři pozitivní věci. \n\nMetodu vynalezl v roce 2009 americký psycholog Martin Seligman a aplikoval ji na amerických vojácích, kteří trpěli nedostatkem pocitu štěstí, depresemi a někteří z nich si sáhli i na život. \n\nJiž po 3 týdnech aplikování metody začne mozek vnímat pouze pozitivní věci a negativní půjdou stranou. U vojáků to znamenalo zvýšení hladiny štěstí a vděčností vůči blízkým osobám a snížení počtu depresí a sebevražd.\nPo 6 měsících deprese takřka odezněly."
        );

        firstTextView.setText(
                "Každý den se snaž zapsat 3 radosti, které tě během dne potěšily. V případě, že ti radost nic nepřineslo, zapiš si alespoň vděčnost.");

        exampleText.setText("Příklad:\nRadost = 'Dnešní oběd s přáteli byl skvělý.'\nVděčnost = 'Rodina.'");

        moredetailText.setText("V poznámce u vděčnosti se snaž vystihnout svůj vděk minimálním počtem slov. Aplikace se tak stane přehlednější. Naopak u radosti se rozepiš co nejvíce.");

        secondTextView.setText(
                "V případě špatné nálady nebo těžkého období si nech zasílat notifikace s radostmi,"
                        +" které tě udělaly v minulosti šťastným/nou na základě tebou vybraných kategorií."
                        +" A nech se motivovat svými radostmi z minulosti. Zamysli se nad sebou zda je vůbec důvod být smutný.");

        thirdTextView.setText(
                "Své radosti i vděčnosti můžeš kontrolovat v jednotlivých statistikách, "
                        +"kde uvidíš jaké kategorie ti dělají největší radost, a které naopak méně. "
                        +"\nV případě těch málo obsazených kategorií si udělej sám/a pro sebe analýzu proč jsou zrovna tyto kategorie málo obsazené a nepřináší ti takovou radost jako ostatní. To stejné platí i pro průměrné ohodnocení jednotlivých kategorií.");
    }

    //metoda prro tlačítko zpět
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ukončí aktivitu
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
