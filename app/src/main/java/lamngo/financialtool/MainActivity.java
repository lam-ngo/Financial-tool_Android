package lamngo.financialtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //eventListener for Dashboard
        TextView dashboard = (TextView)findViewById(R.id.dashboard_button);
        dashboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link Dashboard}
                Intent dashboardIntent = new Intent(MainActivity.this, Dashboard.class);
                //Start new activity
                startActivity(dashboardIntent);
            }
        });

        //eventListener for Transaction
        TextView transaction = (TextView)findViewById(R.id.transaction_button);
        transaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link Transaction}
                Intent transactionIntent = new Intent(MainActivity.this, Transaction.class);
                //Start new activity
                startActivity(transactionIntent);
            }
        });

        //eventListener for Property
        TextView property = (TextView)findViewById(R.id.property_button);
        property.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link Property}
                Intent propertyInten = new Intent(MainActivity.this, Property.class);
                //Start new activity
                startActivity(propertyInten);
            }
        });

        //eventListener for UserAccount
        TextView userAccount = (TextView)findViewById(R.id.userAccount_button);
        userAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link UserAccount}
                Intent userAccountIntent = new Intent(MainActivity.this, UserAccount.class);
                //Start new activity
                startActivity(userAccountIntent);
            }
        });
    }
}
