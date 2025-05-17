package com.example.Aptech_Final.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.example.Aptech_Final.Security.VNPay.Config;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VNPAYService {

    public String createOrder(HttpServletRequest request, int amount, String orderInforUuid, String urlReturn){
    	// Khởi tạo các thông tin bắt buộc theo VNPay
    	// Phiên bản VNPay
        String vnp_Version = "2.1.0";
        // Lệnh thanh toán
        String vnp_Command = "pay";
        // Tạo mã ngẫu nhiên
        String vnp_TxnRef = Config.getRandomNumber(8);
        // Lấy IP (của khách hàng)
        String vnp_IpAddr = Config.getIpAddress(request);
        // Mã merchant do VNPay cung cấp (ở file Config)
        String vnp_TmnCode = Config.vnp_TmnCode;
        // Loại giao dịch
        String orderType = "order-type";
        
        // Gán các tham số vào Map để gửi cho VNPay
        Map<String, String> vnp_Params = new HashMap<>();       
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        
        // Phải *100 theo yêu cầu của VNPay
        vnp_Params.put("vnp_Amount", String.valueOf(amount*100)); 
        vnp_Params.put("vnp_CurrCode", "VND");
        
        // Mã giao dịch duy nhất
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        // Truyền UUID đã lưu qua orderCache
        vnp_Params.put("vnp_OrderInfo", orderInforUuid);
        // Loại giao dịch
        vnp_Params.put("vnp_OrderType", orderType);
        
        // Gán ngôn ngữ đại diện cho VNPay
        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);
        
        // Put url callback sau khi đã thanh toán thành công
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        // Put id của người dùng
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        
        // Tạo ngày giờ lịch 
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        
        // Thiết lập thời gian hết hạn (phút)
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        
        // Sắp xếp các key theo thứ tự a-z theo chuẩn VNPay
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        
        // Tạo chuỗi dữ liệu dùng để ký (hashData) và chuỗi query dùng để gọi API (query)
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Gắn field vào chuỗi hashData để mã hóa HMAC
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Gắn field vào chuỗi query string để redirect người dùng
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        
        // Tạo chữ ký HMAC SHA512 để xác minh data từ hashData và secretKey
        String queryUrl = query.toString();
        // Do VNPay cung cấp (lấy từ lớp Config)
        String salt = Config.secretKey; 
        String vnp_SecureHash = Config.hmacSHA512(salt, hashData.toString());
        // Gắn chữ ký vào cuối chuỗi query
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        
        // Tạo URL thanh toán đầy đủ để redirect
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }
    
    public int orderReturn(HttpServletRequest request){
    	// Lấy toàn bộ tham số trả về từ VNPay
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        
        // Tách riêng chữ ký trả về để kiểm tra xác thực
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        
        // Loại bỏ các trường không cần để tạo lại chữ ký
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        
        // Tạo lại chữ ký sau khi loại bỏ các trường k cần thiết để đối chiếu
        String signValue = Config.hashAllFields(fields);
        
        // If-else so sánh chữ ký để xác thực tính toàn vẹn dữ liệu
        if (signValue.equals(vnp_SecureHash)) {
            // Nếu là giao dịch thành công theo tài liệu VNPay
            return "00".equals(request.getParameter("vnp_TransactionStatus")) ? 1 : 0;
        } else {
            // Nếu là chữ ký không hợp lệ
            return -1;
        }
    }

}