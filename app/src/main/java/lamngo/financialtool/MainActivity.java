package lamngo.financialtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //eventListener for DashboardActivity
        TextView dashboard = (TextView)findViewById(R.id.dashboard_button);
        dashboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link DashboardActivity}
                Intent dashboardIntent = new Intent(MainActivity.this, DashboardActivity.class);
                //Start new activity
                startActivity(dashboardIntent);
            }
        });

        //eventListener for TransactionActivity
        TextView transaction = (TextView)findViewById(R.id.transaction_button);
        transaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link TransactionActivity}
                Intent transactionIntent = new Intent(MainActivity.this, TransactionActivity.class);
                //Start new activity
                startActivity(transactionIntent);
            }
        });

        //eventListener for PropertyActivity
        TextView property = (TextView)findViewById(R.id.property_button);
        property.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link PropertyActivity}
                Intent propertyInten = new Intent(MainActivity.this, PropertyActivity.class);
                //Start new activity
                startActivity(propertyInten);
            }
        });

        //eventListener for UserAccountActivity
        TextView userAccount = (TextView)findViewById(R.id.userAccount_button);
        userAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create new intent to open {@link UserAccountActivity}
                Intent userAccountIntent = new Intent(MainActivity.this, UserAccountActivity.class);
                //Start new activity
                startActivity(userAccountIntent);
            }
        });
    }
}
