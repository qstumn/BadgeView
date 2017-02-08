package q.rorbin.badgeviewdemo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


public class MainActivity extends AppCompatActivity {
    TextView textview, tv_offset, tv_padding, tv_numbersize, tv_dragstate;
    EditText et_badgenumber;
    ImageView imageview, iv_badgecolor, iv_numbercolor;
    Button button, btn_animation;
    RadioGroup rg_gravity;
    SeekBar seekBar_offset, seekBar_padding, seekBar_numbersize;
    Switch swicth_exact, swicth_draggable, swicth_shadow;

    List<Badge> badges;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initBadge();
        et_badgenumber.setText("5");
        seekBar_offset.setProgress(5);
        seekBar_padding.setProgress(5);
        seekBar_numbersize.setProgress(10);
        swicth_exact.setChecked(false);
        swicth_draggable.setChecked(true);
        swicth_shadow.setChecked(true);
    }

    private void initBadge() {
        badges = new ArrayList<>();
        badges.add(new QBadgeView(this).bindTarget(textview));
        badges.add(new QBadgeView(this).bindTarget(imageview));
        badges.add(new QBadgeView(this).bindTarget(button));
    }

    private void initView() {
        textview = (TextView) findViewById(R.id.textview);
        tv_offset = (TextView) findViewById(R.id.tv_offset);
        tv_padding = (TextView) findViewById(R.id.tv_padding);
        tv_numbersize = (TextView) findViewById(R.id.tv_numbersize);
        tv_dragstate = (TextView) findViewById(R.id.tv_dragstate);
        et_badgenumber = (EditText) findViewById(R.id.et_badgenumber);
        imageview = (ImageView) findViewById(R.id.imageview);
        iv_badgecolor = (ImageView) findViewById(R.id.iv_badgecolor);
        iv_numbercolor = (ImageView) findViewById(R.id.iv_numbercolor);
        iv_numbercolor = (ImageView) findViewById(R.id.iv_numbercolor);
        button = (Button) findViewById(R.id.button);
        btn_animation = (Button) findViewById(R.id.btn_animation);
        rg_gravity = (RadioGroup) findViewById(R.id.rg_gravity);
        seekBar_offset = (SeekBar) findViewById(R.id.seekBar_offset);
        seekBar_padding = (SeekBar) findViewById(R.id.seekBar_padding);
        seekBar_numbersize = (SeekBar) findViewById(R.id.seekBar_numbersize);
        swicth_exact = (Switch) findViewById(R.id.swicth_exact);
        swicth_draggable = (Switch) findViewById(R.id.swicth_draggable);
        swicth_shadow = (Switch) findViewById(R.id.swicth_shadow);
    }

    private void initListener() {
        rg_gravity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (Badge badge : badges) {
                    switch (checkedId) {
                        case R.id.rb_st:
                            badge.setBadgeGravity(Gravity.START | Gravity.TOP);
                            break;
                        case R.id.rb_sb:
                            badge.setBadgeGravity(Gravity.START | Gravity.BOTTOM);
                            break;
                        case R.id.rb_et:
                            badge.setBadgeGravity(Gravity.END | Gravity.TOP);
                            break;
                        case R.id.rb_eb:
                            badge.setBadgeGravity(Gravity.END | Gravity.BOTTOM);
                            break;
                        case R.id.rb_c:
                            badge.setBadgeGravity(Gravity.CENTER);
                            break;
                    }
                }
            }
        });
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (Badge badge : badges) {
                    if (seekBar == seekBar_offset) {
                        tv_offset.setText("GravityOffset : " + progress);
                        badge.setGravityOffset(progress, true);
                    } else if (seekBar == seekBar_padding) {
                        tv_padding.setText("BadgePadding : " + progress);
                        badge.setBadgePadding(progress, true);
                    } else if (seekBar == seekBar_numbersize) {
                        tv_numbersize.setText("NumberSize : " + progress);
                        badge.setBadgeNumberSize(progress, true);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekBar_offset.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar_padding.setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBar_numbersize.setOnSeekBarChangeListener(onSeekBarChangeListener);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == iv_badgecolor) {
                    selectorColor(new OnColorClickListener() {
                        @Override
                        public void onColorClick(int color) {
                            iv_badgecolor.setBackgroundColor(color);
                            for (Badge badge : badges) {
                                badge.setBadgeBackgroundColor(color);
                            }
                        }
                    });
                } else if (v == iv_numbercolor) {
                    selectorColor(new OnColorClickListener() {
                        @Override
                        public void onColorClick(int color) {
                            iv_numbercolor.setBackgroundColor(color);
                            for (Badge badge : badges) {
                                badge.setBadgeNumberColor(color);
                            }
                        }
                    });
                } else if (v == btn_animation) {
                    for (Badge badge : badges) {
                        badge.hide(true);
                    }
                }
            }
        };
        iv_badgecolor.setOnClickListener(onClickListener);
        iv_numbercolor.setOnClickListener(onClickListener);
        btn_animation.setOnClickListener(onClickListener);
        et_badgenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int num = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s.toString());
                    for (Badge badge : badges) {
                        badge.setBadgeNumber(num);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (Badge badge : badges) {
                    if (buttonView == swicth_exact) {
                        badge.setExactMode(isChecked);
                    } else if (buttonView == swicth_draggable) {
                        badge.setOnDragStateChangedListener(isChecked ?
                                new Badge.OnDragStateChangedListener() {
                                    @Override
                                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                                        switch (dragState) {
                                            case STATE_START:
                                                tv_dragstate.setText("STATE_START");
                                                break;
                                            case STATE_DRAGGING:
                                                tv_dragstate.setText("STATE_DRAGGING");
                                                break;
                                            case STATE_DRAGGING_OUT_OF_RANGE:
                                                tv_dragstate.setText("STATE_DRAGGING_OUT_OF_RANGE");
                                                break;
                                            case STATE_SUCCEED:
                                                tv_dragstate.setText("STATE_SUCCEED");
                                                break;
                                            case STATE_CANCELED:
                                                tv_dragstate.setText("STATE_CANCELED");
                                                break;
                                        }
                                    }
                                } : null);
                    } else if (buttonView == swicth_shadow) {
                        badge.setShowShadow(isChecked);
                    }
                }
            }
        };
        swicth_exact.setOnCheckedChangeListener(onCheckedChangeListener);
        swicth_draggable.setOnCheckedChangeListener(onCheckedChangeListener);
        swicth_shadow.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void selectorColor(final OnColorClickListener l) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        GridView gv = new GridView(this);
        gv.setNumColumns(4);
        gv.setAdapter(new BaseAdapter() {
            int[] colors = new int[]{0xffffffff, 0xff000000, 0xffe51c23, 0xffE84E40, 0xff9c27b0, 0xff673ab7,
                    0xff3f51b5, 0xff5677fc, 0xff03a9f4, 0xff00bcd4, 0xff009688, 0xff259b24, 0xff8bc34a, 0xffcddc39,
                    0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722, 0xff795548, 0xff9e9e9e};

            @Override
            public int getCount() {
                return colors.length;
            }

            @Override
            public Object getItem(int position) {
                return colors[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View v = new View(MainActivity.this);
                v.setBackgroundColor(colors[position]);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        l.onColorClick(colors[position]);
                        dialog.dismiss();
                    }
                });
                DisplayMetrics dm = new DisplayMetrics();
                WindowManager wm = (WindowManager) MainActivity.this
                        .getSystemService(Context.WINDOW_SERVICE);
                wm.getDefaultDisplay().getMetrics(dm);
                GridView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
                        (int) (dm.widthPixels / 5f));
                v.setLayoutParams(lp);
                return v;
            }
        });
        dialog.setView(gv);
        dialog.show();
    }

    interface OnColorClickListener {
        void onColorClick(int color);
    }
}
