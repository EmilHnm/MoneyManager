package com.example.moneymanager.methods;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SharedMethods extends AppCompatActivity {
    //Hàm sử dụng cho 4 activity trong menu
    public static void setNavigationMenu(BottomNavigationView bnv_menu, int selectedItemId){
        //Bỏ màu nền
        bnv_menu.setBackground(null);
        //Set item được chọn
        bnv_menu.setSelectedItemId(selectedItemId);
    }

    public static void setCategoryInformation(int categoryItemId, ImageView iv_category, TextView tv_category) {
        switch (categoryItemId) {
            case 1:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_cocktail)); tv_category.setText("Ăn uống");  break;
            case 2:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_transportation)); tv_category.setText("Di chuyển");  break;
            case 3:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_payment)); tv_category.setText("Hóa đơn");  break;
            case 4:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_house)); tv_category.setText("Trang trí, sửa nhà");  break;
            case 5:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_tools)); tv_category.setText("Bảo dưỡng xe");  break;
            case 6:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_stethoscope)); tv_category.setText("Sức khoẻ");  break;
            case 7:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_book)); tv_category.setText("Học tập");  break;
            case 8:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_kitchen_tools)); tv_category.setText("Đồ gia dụng");  break;
            case 9:  iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_shield)); tv_category.setText("Bảo hiểm");  break;
            case 10: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_phone_call)); tv_category.setText("Đồ dùng cá nhân");  break;
            case 11: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_dog)); tv_category.setText("Vật nuôi");  break;
            case 12: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_house_1)); tv_category.setText("Dịch vụ gia đình");  break;
            case 13: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_soccer)); tv_category.setText("Thể thao");  break;
            case 14: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_diamond)); tv_category.setText("Làm đẹp");  break;
            case 15: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_credit_card)); tv_category.setText("Dịch vụ trực tuyến");  break;
            case 16: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_game_console)); tv_category.setText("Giải trí");  break;
            case 17: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_calculator)); tv_category.setText("Trả lãi");  break;
            case 18: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_earning)); tv_category.setText("Chuyển tiền đi");  break;
            case 19: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_resource_package)); tv_category.setText("Các khoản chi khác");  break;
            case 20: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_calendar)); tv_category.setText("Lương");  break;
            case 21: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_percent__1_)); tv_category.setText("Thu lãi");  break;
            case 22: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_money)); tv_category.setText("Chuyển tiền đến");  break;
            case 23: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_resource_package)); tv_category.setText("Các khoản thu khác");  break;
            case 24: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_money_bag)); tv_category.setText("Thu nợ");  break;
            case 25: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_borrow)); tv_category.setText("Đi vay");  break;
            case 26: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_loan)); tv_category.setText("Cho vay");  break;
            case 27: iv_category.setImageDrawable(iv_category.getContext().getResources().getDrawable(R.drawable.ic_cash_payment)); tv_category.setText("Trả nợ");  break;
        }
    }

    public static String formatDate(int year, int month, int dayOfMonth){
        // Ngày, tháng < 10 thì thêm số 0 đằng trước
        String selectedDayOfMonth = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        String selectedMonth = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
        String formattedDate = String.format("%s/%s/%d", selectedDayOfMonth, selectedMonth, year);

        return formattedDate;
    }
}
