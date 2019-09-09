package application.minseong.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriend extends AppCompatActivity {

    @BindView(R.id.friend1)
    EditText _friendName;

    @BindView(R.id.friend1phone)
    EditText _friendNum;

    @BindView(R.id.registerfriend)
    Button _friendBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        ButterKnife.bind(this);

        _friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friend_name = _friendName.getText().toString();
                String friend_num = _friendNum.getText().toString();
                Intent intent = new Intent();

                if(!friend_name.isEmpty() && !friend_num.isEmpty()){
                    intent.putExtra("friend1",friend_name);
                    intent.putExtra("friend2",Integer.parseInt(friend_num));
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
