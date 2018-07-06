package com.wheel.perfect.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wheel.perfect.R;
import com.wheel.perfect.api.downapi.DownApkInfo;
import com.wheel.perfect.api.downapi.DownState;
import com.wheel.perfect.api.downapi.HttpDownManager;
import com.wheel.perfect.api.downapi.HttpDownOnNextListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ClassName:	DownListAdapter
 * Function:	${TODO} 描述这个类的作用
 * Reason:	${TODO} ADD REASON(可选)
 * Date:	2018/6/27 11:25
 *
 * @author 孙科技
 * @version ${TODO}
 * @see
 * @since JDK 1.8
 */
public class DownListAdapter extends RecyclerView.Adapter {
    public List<DownApkInfo> list;

    public DownListAdapter(List<DownApkInfo> list) {
        this.list = list;
    }

    public void updataList(List<DownApkInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_down, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemViewHolder) {
            final DownApkInfo downApkInfo = list.get(i);

            ((ItemViewHolder) viewHolder).btnRxDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (downApkInfo.getState() != DownState.FINISH) {
                        HttpDownManager.getInstance().startDown(downApkInfo);
                    }
                }
            });
            ((ItemViewHolder) viewHolder).btnRxPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpDownManager.getInstance().pause(downApkInfo);
                }
            });

            ((ItemViewHolder) viewHolder).progressBar.setMax((int) downApkInfo.getApkTotalLength());
            ((ItemViewHolder) viewHolder).progressBar.setProgress((int) downApkInfo.getApkDownLength());
            /*第一次恢复 */
            switch (downApkInfo.getState()) {
                case START:
                    /*起始状态*/
                    break;
                case PAUSE:
                    ((ItemViewHolder) viewHolder).tvMsg.setText("暂停中");
                    break;
                case DOWN:
                    HttpDownManager.getInstance().startDown(downApkInfo);
                    break;
                case STOP:
                    ((ItemViewHolder) viewHolder).tvMsg.setText("下载停止");
                    break;
                case ERROR:
                    ((ItemViewHolder) viewHolder).tvMsg.setText("下載錯誤");
                    break;
                case FINISH:
                    ((ItemViewHolder) viewHolder).tvMsg.setText("下载完成");
                    break;
            }

            downApkInfo.setListener(new HttpDownOnNextListener<DownApkInfo>() {
                @Override
                public void onNext(DownApkInfo baseDownEntity) {
                    ((ItemViewHolder) viewHolder).tvMsg.setText("提示：下载完成/文件地址->" + baseDownEntity.getApkSavePath());
                }

                @Override
                public void onStart() {
                    ((ItemViewHolder) viewHolder).tvMsg.setText("提示:开始下载");
                }

                @Override
                public void onComplete() {
                    ((ItemViewHolder) viewHolder).tvMsg.setText("提示:下载完成");
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    ((ItemViewHolder) viewHolder).tvMsg.setText("失败:" + e.toString());
                }


                @Override
                public void onPuase() {
                    super.onPuase();
                    ((ItemViewHolder) viewHolder).tvMsg.setText("提示:暂停");
                }

                @Override
                public void onStop() {
                    super.onStop();
                }

                @Override
                public void updateProgress(long readLength, long countLength) {
                    ((ItemViewHolder) viewHolder).tvMsg.setText("提示:下载中");
                    ((ItemViewHolder) viewHolder).progressBar.setMax((int) countLength);
                    ((ItemViewHolder) viewHolder).progressBar.setProgress((int) readLength);
                }
            });
        }
    }

    /*下载回调*/

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_rx_down)
        Button btnRxDown;
        @BindView(R.id.btn_rx_pause)
        Button btnRxPause;
        @BindView(R.id.tv_msg)
        TextView tvMsg;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
//            R.layout.item_down
            ButterKnife.bind(this, itemView);
        }
    }
}
